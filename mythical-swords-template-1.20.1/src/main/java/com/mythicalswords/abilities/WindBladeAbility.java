package com.mythicalswords.abilities;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.List;

/**
 * Wind Blade - Kusanagi's special ability
 * Launches a cutting wind projectile that travels in a straight line
 * Damages and knocks back enemies in its path
 * Cooldown: 250 ticks (12.5 seconds)
 */
public class WindBladeAbility implements WeaponAbility {
    
    private static final int COOLDOWN_TICKS = 250;
    private static final float DAMAGE = 12.0f;
    private static final double RANGE = 16.0;
    private static final double KNOCKBACK_STRENGTH = 1.5;
    
    @Override
    public boolean activate(World world, PlayerEntity player, ItemStack weapon) {
        if (world.isClient) {
            return false;
        }
        
        // Get player's look direction
        Vec3d lookVec = player.getRotationVec(1.0f);
        Vec3d startPos = player.getEyePos();
        Vec3d endPos = startPos.add(lookVec.multiply(RANGE));
        
        // Create wind blade projectile effect
        if (world instanceof ServerWorld serverWorld) {
            // Spawn wind particles along the path
            for (double i = 0; i < RANGE; i += 0.5) {
                Vec3d particlePos = startPos.add(lookVec.multiply(i));
                
                // Main wind trail
                serverWorld.spawnParticles(
                    ParticleTypes.SWEEP_ATTACK,
                    particlePos.x, particlePos.y, particlePos.z,
                    2,
                    0.2, 0.2, 0.2,
                    0.05
                );
                
                // Cloud particles for wind effect
                serverWorld.spawnParticles(
                    ParticleTypes.CLOUD,
                    particlePos.x, particlePos.y, particlePos.z,
                    3,
                    0.3, 0.3, 0.3,
                    0.02
                );
            }
        }
        
        // Damage entities in the path
        List<Entity> hitEntities = world.getOtherEntities(
            player,
            new Box(startPos, endPos).expand(1.0),
            entity -> entity instanceof LivingEntity && entity.isAlive()
        );
        
        int hitCount = 0;
        for (Entity entity : hitEntities) {
            if (entity instanceof LivingEntity livingEntity) {
                // Check if entity is actually in the wind blade's path
                Vec3d toEntity = entity.getPos().subtract(startPos);
                double projection = toEntity.dotProduct(lookVec);
                
                if (projection > 0 && projection < RANGE) {
                    Vec3d closestPoint = startPos.add(lookVec.multiply(projection));
                    double distance = entity.getPos().distanceTo(closestPoint);
                    
                    if (distance < 1.5) {
                        // Deal damage
                        livingEntity.damage(
                            world.getDamageSources().playerAttack(player),
                            DAMAGE
                        );
                        
                        // Apply knockback
                        Vec3d knockback = lookVec.multiply(KNOCKBACK_STRENGTH);
                        livingEntity.setVelocity(
                            livingEntity.getVelocity().add(knockback.x, 0.3, knockback.z)
                        );
                        livingEntity.velocityModified = true;
                        
                        hitCount++;
                        
                        // Hit particles
                        if (world instanceof ServerWorld serverWorld) {
                            serverWorld.spawnParticles(
                                ParticleTypes.CRIT,
                                entity.getX(), entity.getY() + entity.getHeight() / 2, entity.getZ(),
                                10,
                                0.3, 0.3, 0.3,
                                0.1
                            );
                        }
                    }
                }
            }
        }
        
        // Play wind sound
        world.playSound(
            null,
            player.getX(), player.getY(), player.getZ(),
            SoundEvents.ITEM_TRIDENT_THROW,
            SoundCategory.PLAYERS,
            1.0f,
            1.5f
        );
        
        // Play additional whoosh sound
        world.playSound(
            null,
            player.getX(), player.getY(), player.getZ(),
            SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP,
            SoundCategory.PLAYERS,
            0.8f,
            0.8f
        );
        
        // Success message
        if (hitCount > 0) {
            player.sendMessage(
                net.minecraft.text.Text.literal("Wind Blade struck " + hitCount + " " + (hitCount == 1 ? "enemy" : "enemies") + "!")
                    .formatted(net.minecraft.util.Formatting.AQUA),
                true
            );
        } else {
            player.sendMessage(
                net.minecraft.text.Text.literal("Wind Blade unleashed!")
                    .formatted(net.minecraft.util.Formatting.AQUA),
                true
            );
        }
        
        return true;
    }
    
    @Override
    public int getCooldownTicks() {
        return COOLDOWN_TICKS;
    }
    
    @Override
    public String getName() {
        return "Wind Blade";
    }
}
