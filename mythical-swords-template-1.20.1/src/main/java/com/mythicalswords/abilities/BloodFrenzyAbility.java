package com.mythicalswords.abilities;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.world.World;

/**
 * Blood Frenzy - Muramasa's special ability
 * Grants massive damage boost but drains health over time
 * High risk, high reward ability
 * Cooldown: 400 ticks (20 seconds)
 */
public class BloodFrenzyAbility implements WeaponAbility {
    
    private static final int COOLDOWN_TICKS = 400;
    private static final int EFFECT_DURATION = 200; // 10 seconds
    private static final int STRENGTH_AMPLIFIER = 2; // +150% damage (Strength III)
    private static final int WITHER_AMPLIFIER = 0; // 1 damage per 2 seconds
    
    @Override
    public boolean activate(World world, PlayerEntity player, ItemStack weapon) {
        if (world.isClient) {
            return false;
        }
        
        // Check if player has enough health (at least 4 hearts)
        if (player.getHealth() <= 8.0f) {
            player.sendMessage(
                net.minecraft.text.Text.literal("Not enough health to activate Blood Frenzy!")
                    .formatted(net.minecraft.util.Formatting.RED),
                true
            );
            return false;
        }
        
        // Apply strength effect (massive damage boost)
        player.addStatusEffect(new StatusEffectInstance(
            StatusEffects.STRENGTH,
            EFFECT_DURATION,
            STRENGTH_AMPLIFIER,
            false,
            true,
            true
        ));
        
        // Apply wither effect (health drain)
        player.addStatusEffect(new StatusEffectInstance(
            StatusEffects.WITHER,
            EFFECT_DURATION,
            WITHER_AMPLIFIER,
            false,
            true,
            true
        ));
        
        // Apply speed boost for berserker feel
        player.addStatusEffect(new StatusEffectInstance(
            StatusEffects.SPEED,
            EFFECT_DURATION,
            1, // Speed II
            false,
            true,
            true
        ));
        
        // Blood particles around player
        if (world instanceof ServerWorld serverWorld) {
            for (int i = 0; i < 30; i++) {
                double x = player.getX() + (world.random.nextDouble() - 0.5) * 3;
                double y = player.getY() + world.random.nextDouble() * 2;
                double z = player.getZ() + (world.random.nextDouble() - 0.5) * 3;
                
                serverWorld.spawnParticles(
                    ParticleTypes.DAMAGE_INDICATOR,
                    x, y, z,
                    1,
                    0, 0, 0,
                    0
                );
            }
            
            // Red dust particles for blood effect
            for (int i = 0; i < 20; i++) {
                double x = player.getX() + (world.random.nextDouble() - 0.5) * 2;
                double y = player.getY() + world.random.nextDouble() * 2;
                double z = player.getZ() + (world.random.nextDouble() - 0.5) * 2;
                
                serverWorld.spawnParticles(
                    ParticleTypes.CRIMSON_SPORE,
                    x, y, z,
                    1,
                    0, 0, 0,
                    0
                );
            }
        }
        
        // Play ominous sound
        world.playSound(
            null,
            player.getX(), player.getY(), player.getZ(),
            SoundEvents.ENTITY_WITHER_AMBIENT,
            SoundCategory.PLAYERS,
            0.8f,
            1.2f
        );
        
        // Success message
        player.sendMessage(
            net.minecraft.text.Text.literal("Blood Frenzy activated! Massive damage boost but health drains!")
                .formatted(net.minecraft.util.Formatting.DARK_RED),
            true
        );
        
        return true;
    }
    
    @Override
    public int getCooldownTicks() {
        return COOLDOWN_TICKS;
    }
    
    @Override
    public String getName() {
        return "Blood Frenzy";
    }
}
