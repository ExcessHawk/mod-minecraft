package com.mythicalswords.abilities;

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
 * Fire Wave - Laevateinn's special ability
 * Creates a cone-shaped wave of fire that damages and ignites enemies
 * Cooldown: 350 ticks (17.5 seconds)
 */
public class FireWaveAbility implements WeaponAbility {
    
    private static final int COOLDOWN_TICKS = 350; // 17.5 seconds
    private static final double DAMAGE = 14.0;
    private static final double CONE_RANGE = 8.0;
    private static final double CONE_ANGLE = 60.0; // degrees
    private static final int FIRE_DURATION = 100; // 5 seconds
    
    @Override
    public boolean activate(World world, PlayerEntity player, ItemStack weapon) {
        if (world.isClient) {
            return false;
        }
        
        // Get player's look direction
        Vec3d lookVec = player.getRotationVec(1.0f);
        Vec3d playerPos = player.getEyePos();
        
        // Create fire wave particles
        if (world instanceof ServerWorld serverWorld) {
            // Create cone of fire particles
            for (int i = 0; i < 50; i++) {
                // Random angle within cone
                double angleOffset = (Math.random() - 0.5) * Math.toRadians(CONE_ANGLE);
                double distance = Math.random() * CONE_RANGE;
                
                // Rotate look vector by angle offset
                Vec3d particleDir = rotateAroundY(lookVec, angleOffset);
                Vec3d particlePos = playerPos.add(particleDir.multiply(distance));
                
                // Spawn fire particles
                serverWorld.spawnParticles(
                    ParticleTypes.FLAME,
                    particlePos.x, particlePos.y, particlePos.z,
                    3,
                    0.2, 0.2, 0.2,
                    0.05
                );
                
                serverWorld.spawnParticles(
                    ParticleTypes.LAVA,
                    particlePos.x, particlePos.y, particlePos.z,
                    1,
                    0.1, 0.1, 0.1,
                    0.02
                );
            }
            
            // Add smoke particles
            for (int i = 0; i < 30; i++) {
                double angleOffset = (Math.random() - 0.5) * Math.toRadians(CONE_ANGLE);
                double distance = Math.random() * CONE_RANGE;
                
                Vec3d particleDir = rotateAroundY(lookVec, angleOffset);
                Vec3d particlePos = playerPos.add(particleDir.multiply(distance));
                
                serverWorld.spawnParticles(
                    ParticleTypes.LARGE_SMOKE,
                    particlePos.x, particlePos.y, particlePos.z,
                    2,
                    0.3, 0.3, 0.3,
                    0.03
                );
            }
        }
        
        // Play sound
        world.playSound(
            null,
            player.getX(), player.getY(), player.getZ(),
            SoundEvents.ITEM_FIRECHARGE_USE,
            SoundCategory.PLAYERS,
            1.5f,
            0.8f
        );
        
        world.playSound(
            null,
            player.getX(), player.getY(), player.getZ(),
            SoundEvents.ENTITY_BLAZE_SHOOT,
            SoundCategory.PLAYERS,
            1.0f,
            1.0f
        );
        
        // Find and damage entities in cone
        Box searchBox = new Box(
            player.getX() - CONE_RANGE, player.getY() - 3, player.getZ() - CONE_RANGE,
            player.getX() + CONE_RANGE, player.getY() + 3, player.getZ() + CONE_RANGE
        );
        
        List<LivingEntity> entities = world.getEntitiesByClass(
            LivingEntity.class,
            searchBox,
            entity -> entity != player && !entity.isTeammate(player) && entity.isAlive()
        );
        
        int hitCount = 0;
        for (LivingEntity entity : entities) {
            // Check if entity is in cone
            Vec3d toEntity = entity.getPos().subtract(playerPos).normalize();
            double angle = Math.acos(lookVec.dotProduct(toEntity));
            
            if (angle <= Math.toRadians(CONE_ANGLE / 2)) {
                // Entity is in cone, damage it
                entity.damage(world.getDamageSources().playerAttack(player), (float) DAMAGE);
                
                // Set on fire
                entity.setOnFireFor(FIRE_DURATION / 20);
                
                // Spawn hit particles
                if (world instanceof ServerWorld serverWorld) {
                    serverWorld.spawnParticles(
                        ParticleTypes.FLAME,
                        entity.getX(), entity.getY() + entity.getHeight() / 2, entity.getZ(),
                        15,
                        0.3, 0.3, 0.3,
                        0.1
                    );
                }
                
                hitCount++;
            }
        }
        
        // Send feedback to player
        if (hitCount > 0) {
            player.sendMessage(
                net.minecraft.text.Text.literal("Fire Wave hit " + hitCount + " enemies!")
                    .formatted(net.minecraft.util.Formatting.GOLD),
                true
            );
        }
        
        return true;
    }
    
    /**
     * Rotate a vector around the Y axis
     */
    private Vec3d rotateAroundY(Vec3d vec, double angle) {
        double cos = Math.cos(angle);
        double sin = Math.sin(angle);
        
        double newX = vec.x * cos - vec.z * sin;
        double newZ = vec.x * sin + vec.z * cos;
        
        return new Vec3d(newX, vec.y, newZ);
    }
    
    @Override
    public int getCooldownTicks() {
        return COOLDOWN_TICKS;
    }
    
    @Override
    public String getName() {
        return "Fire Wave";
    }
}
