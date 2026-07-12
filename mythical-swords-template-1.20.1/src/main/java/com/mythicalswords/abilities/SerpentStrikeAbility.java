package com.mythicalswords.abilities;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.List;

/**
 * Serpent Strike - Xiuhcoatl's special ability
 * Summons a fire serpent that seeks out and damages nearby enemies
 * The serpent persists for 5 seconds, dealing fire damage in an area
 * Cooldown: 300 ticks (15 seconds)
 */
public class SerpentStrikeAbility implements WeaponAbility {
    
    private static final int COOLDOWN_TICKS = 300;
    private static final int DURATION_TICKS = 100; // 5 seconds
    private static final float DAMAGE_PER_TICK = 0.5f;
    private static final double SEARCH_RANGE = 12.0;
    private static final double DAMAGE_RANGE = 3.0;
    
    @Override
    public boolean activate(World world, PlayerEntity player, ItemStack weapon) {
        if (world.isClient) {
            return false;
        }
        
        // Start the serpent effect
        world.playSound(
            null,
            player.getX(), player.getY(), player.getZ(),
            SoundEvents.ENTITY_BLAZE_SHOOT,
            SoundCategory.PLAYERS,
            1.0f,
            0.8f
        );
        
        player.sendMessage(
            net.minecraft.text.Text.literal("Fire Serpent summoned!")
                .formatted(net.minecraft.util.Formatting.GOLD),
            true
        );
        
        // Schedule serpent behavior over time
        scheduleSerpentTicks(world, player, 0);
        
        return true;
    }
    
    private void scheduleSerpentTicks(World world, PlayerEntity player, int currentTick) {
        if (currentTick >= DURATION_TICKS) {
            return; // Serpent duration expired
        }
        
        if (world.isClient || !(world instanceof ServerWorld serverWorld)) {
            return;
        }
        
        // Find nearest enemy
        Box searchBox = new Box(
            player.getX() - SEARCH_RANGE,
            player.getY() - SEARCH_RANGE,
            player.getZ() - SEARCH_RANGE,
            player.getX() + SEARCH_RANGE,
            player.getY() + SEARCH_RANGE,
            player.getZ() + SEARCH_RANGE
        );
        
        List<Entity> nearbyEntities = world.getOtherEntities(
            player,
            searchBox,
            entity -> entity instanceof LivingEntity && entity.isAlive() && !(entity instanceof PlayerEntity)
        );
        
        // Serpent position (starts at player, moves toward enemies)
        Vec3d serpentPos;
        
        if (!nearbyEntities.isEmpty()) {
            // Find closest enemy
            Entity closestEnemy = null;
            double closestDistance = SEARCH_RANGE;
            
            for (Entity entity : nearbyEntities) {
                double distance = player.distanceTo(entity);
                if (distance < closestDistance) {
                    closestEnemy = entity;
                    closestDistance = distance;
                }
            }
            
            if (closestEnemy != null) {
                // Serpent moves toward enemy
                Vec3d direction = closestEnemy.getPos().subtract(player.getPos()).normalize();
                double progress = Math.min(1.0, currentTick / 20.0); // Move over first second
                serpentPos = player.getPos().add(direction.multiply(progress * 8));
            } else {
                serpentPos = player.getPos().add(0, 1, 0);
            }
        } else {
            // No enemies, serpent circles player
            double angle = (currentTick / 20.0) * Math.PI * 2;
            double radius = 3.0;
            serpentPos = player.getPos().add(
                Math.cos(angle) * radius,
                1.0 + Math.sin(currentTick / 10.0),
                Math.sin(angle) * radius
            );
        }
        
        // Spawn serpent particles
        for (int i = 0; i < 5; i++) {
            double offsetX = (world.random.nextDouble() - 0.5) * 0.5;
            double offsetY = (world.random.nextDouble() - 0.5) * 0.5;
            double offsetZ = (world.random.nextDouble() - 0.5) * 0.5;
            
            serverWorld.spawnParticles(
                ParticleTypes.FLAME,
                serpentPos.x + offsetX,
                serpentPos.y + offsetY,
                serpentPos.z + offsetZ,
                1,
                0, 0, 0,
                0.02
            );
            
            serverWorld.spawnParticles(
                ParticleTypes.SOUL_FIRE_FLAME,
                serpentPos.x + offsetX,
                serpentPos.y + offsetY,
                serpentPos.z + offsetZ,
                1,
                0, 0, 0,
                0.01
            );
        }
        
        // Damage nearby enemies
        Box damageBox = new Box(
            serpentPos.x - DAMAGE_RANGE,
            serpentPos.y - DAMAGE_RANGE,
            serpentPos.z - DAMAGE_RANGE,
            serpentPos.x + DAMAGE_RANGE,
            serpentPos.y + DAMAGE_RANGE,
            serpentPos.z + DAMAGE_RANGE
        );
        
        List<Entity> damagedEntities = world.getOtherEntities(
            player,
            damageBox,
            entity -> entity instanceof LivingEntity && entity.isAlive()
        );
        
        for (Entity entity : damagedEntities) {
            if (entity instanceof LivingEntity livingEntity) {
                // Bypass i-frames so the per-tick fire damage actually applies
                livingEntity.timeUntilRegen = 0;
                // Deal fire damage
                livingEntity.damage(
                    world.getDamageSources().onFire(),
                    DAMAGE_PER_TICK
                );
                
                // Set on fire
                if (currentTick % 20 == 0) { // Every second
                    livingEntity.setOnFireFor(3);
                }
                
                // Burn particles
                serverWorld.spawnParticles(
                    ParticleTypes.LAVA,
                    entity.getX(),
                    entity.getY() + entity.getHeight() / 2,
                    entity.getZ(),
                    3,
                    0.3, 0.3, 0.3,
                    0
                );
            }
        }
        
        // Play hissing sound occasionally
        if (currentTick % 20 == 0) {
            world.playSound(
                null,
                serpentPos.x, serpentPos.y, serpentPos.z,
                SoundEvents.ENTITY_BLAZE_AMBIENT,
                SoundCategory.PLAYERS,
                0.5f,
                1.5f
            );
        }
        
        // Schedule next tick after one real server tick (not the same tick)
        final int nextTick = currentTick + 1;
        com.mythicalswords.util.ServerScheduler.schedule(1, () -> {
            if (player.isAlive()) {
                scheduleSerpentTicks(world, player, nextTick);
            }
        });
    }
    
    @Override
    public int getCooldownTicks() {
        return COOLDOWN_TICKS;
    }
    
    @Override
    public String getName() {
        return "Serpent Strike";
    }
}
