package com.mythicalswords.abilities;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;

import java.util.List;

/**
 * Divine Light Slash - Excalibur's special ability
 * Spawns light particles and deals AOE divine damage
 */
public class DivineLightSlashAbility implements WeaponAbility {
    
    private static final int COOLDOWN_TICKS = 300; // 15 seconds
    private static final double DAMAGE = 10.0;
    private static final double RADIUS = 5.0;
    
    @Override
    public boolean activate(World world, PlayerEntity player, ItemStack weapon) {
        if (world.isClient) {
            return false;
        }
        
        // Spawn light particles in a circle around the player
        if (world instanceof ServerWorld serverWorld) {
            for (int i = 0; i < 50; i++) {
                double angle = (i / 50.0) * Math.PI * 2;
                double x = player.getX() + Math.cos(angle) * RADIUS;
                double z = player.getZ() + Math.sin(angle) * RADIUS;
                double y = player.getY() + 1.0;
                
                serverWorld.spawnParticles(
                    ParticleTypes.END_ROD,
                    x, y, z,
                    3,
                    0.1, 0.1, 0.1,
                    0.05
                );
            }
            
            // Spawn additional particles at player position
            serverWorld.spawnParticles(
                ParticleTypes.FLASH,
                player.getX(), player.getY() + 1, player.getZ(),
                1,
                0, 0, 0,
                0
            );
        }
        
        // Play sound
        world.playSound(
            null,
            player.getX(), player.getY(), player.getZ(),
            SoundEvents.ENTITY_PLAYER_ATTACK_CRIT,
            SoundCategory.PLAYERS,
            1.0f,
            1.5f
        );
        
        // Deal AOE damage to nearby entities
        Box damageBox = new Box(
            player.getX() - RADIUS, player.getY() - 2, player.getZ() - RADIUS,
            player.getX() + RADIUS, player.getY() + 4, player.getZ() + RADIUS
        );
        
        List<LivingEntity> entities = world.getEntitiesByClass(
            LivingEntity.class,
            damageBox,
            entity -> entity != player && !entity.isTeammate(player)
        );
        
        for (LivingEntity entity : entities) {
            entity.damage(world.getDamageSources().playerAttack(player), (float) DAMAGE);
            
            // Spawn hit particles
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
        
        return true;
    }
    
    @Override
    public int getCooldownTicks() {
        return COOLDOWN_TICKS;
    }
    
    @Override
    public String getName() {
        return "Divine Light Slash";
    }
}
