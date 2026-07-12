package com.mythicalswords.abilities;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.TridentEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;

import java.util.List;

/**
 * Never Miss Strike - Gungnir's special ability
 * Creates a homing lightning projectile that seeks the nearest enemy
 * Cooldown: 400 ticks (20 seconds)
 */
public class NeverMissStrikeAbility implements WeaponAbility {
    
    private static final int COOLDOWN_TICKS = 400; // 20 seconds
    private static final double DAMAGE = 16.0;
    private static final double SEARCH_RADIUS = 32.0;
    
    @Override
    public boolean activate(World world, PlayerEntity player, ItemStack weapon) {
        if (world.isClient) {
            return false;
        }
        
        // Find the nearest hostile entity
        LivingEntity target = findNearestTarget(world, player);
        
        if (target == null) {
            player.sendMessage(
                net.minecraft.text.Text.literal("No target found!")
                    .formatted(net.minecraft.util.Formatting.RED),
                true
            );
            return false;
        }
        
        // Create lightning strike effect at target
        if (world instanceof ServerWorld serverWorld) {
            // Spawn lightning particles from player to target
            Vec3d start = player.getEyePos();
            Vec3d end = target.getPos().add(0, target.getHeight() / 2, 0);
            
            // Create particle trail
            int particleCount = 20;
            for (int i = 0; i <= particleCount; i++) {
                double t = i / (double) particleCount;
                Vec3d pos = start.lerp(end, t);
                
                serverWorld.spawnParticles(
                    ParticleTypes.ELECTRIC_SPARK,
                    pos.x, pos.y, pos.z,
                    2,
                    0.1, 0.1, 0.1,
                    0.05
                );
                
                serverWorld.spawnParticles(
                    ParticleTypes.END_ROD,
                    pos.x, pos.y, pos.z,
                    1,
                    0.05, 0.05, 0.05,
                    0.02
                );
            }
            
            // Spawn impact particles
            serverWorld.spawnParticles(
                ParticleTypes.FLASH,
                target.getX(), target.getY() + target.getHeight() / 2, target.getZ(),
                1,
                0, 0, 0,
                0
            );
            
            serverWorld.spawnParticles(
                ParticleTypes.ELECTRIC_SPARK,
                target.getX(), target.getY() + target.getHeight() / 2, target.getZ(),
                30,
                0.5, 0.5, 0.5,
                0.2
            );
        }
        
        // Play sound at player
        world.playSound(
            null,
            player.getX(), player.getY(), player.getZ(),
            SoundEvents.ITEM_TRIDENT_THROW,
            SoundCategory.PLAYERS,
            1.0f,
            1.2f
        );
        
        // Play sound at target
        world.playSound(
            null,
            target.getX(), target.getY(), target.getZ(),
            SoundEvents.ENTITY_LIGHTNING_BOLT_IMPACT,
            SoundCategory.HOSTILE,
            1.0f,
            1.0f
        );
        
        // Deal damage to target
        target.damage(world.getDamageSources().playerAttack(player), (float) DAMAGE);
        
        // Apply knockback
        Vec3d knockback = target.getPos().subtract(player.getPos()).normalize().multiply(0.5);
        target.setVelocity(knockback.x, 0.3, knockback.z);
        target.velocityModified = true;
        
        return true;
    }
    
    /**
     * Find the nearest hostile entity within range
     */
    private LivingEntity findNearestTarget(World world, PlayerEntity player) {
        Box searchBox = new Box(
            player.getX() - SEARCH_RADIUS, player.getY() - SEARCH_RADIUS, player.getZ() - SEARCH_RADIUS,
            player.getX() + SEARCH_RADIUS, player.getY() + SEARCH_RADIUS, player.getZ() + SEARCH_RADIUS
        );
        
        List<LivingEntity> entities = world.getEntitiesByClass(
            LivingEntity.class,
            searchBox,
            entity -> entity != player 
                && !entity.isTeammate(player) 
                && entity.isAlive()
                && !entity.isSpectator()
        );
        
        if (entities.isEmpty()) {
            return null;
        }
        
        // Find the closest entity
        LivingEntity closest = null;
        double closestDistance = Double.MAX_VALUE;
        
        for (LivingEntity entity : entities) {
            double distance = player.squaredDistanceTo(entity);
            if (distance < closestDistance) {
                closestDistance = distance;
                closest = entity;
            }
        }
        
        return closest;
    }
    
    @Override
    public int getCooldownTicks() {
        return COOLDOWN_TICKS;
    }
    
    @Override
    public String getName() {
        return "Never Miss Strike";
    }
}
