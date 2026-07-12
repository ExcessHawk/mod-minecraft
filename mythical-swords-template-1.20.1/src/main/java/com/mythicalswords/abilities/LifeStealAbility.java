package com.mythicalswords.abilities;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.world.World;

/**
 * Life Steal Ability - Khopesh special ability
 * Heals the attacker for 25% of damage dealt
 */
public class LifeStealAbility implements WeaponAbility {

    private static final int COOLDOWN_TICKS = 0; // Passive ability (no cooldown)
    private static final float LIFESTEAL_PERCENT = 0.25f; // 25% heal

    /**
     * This is called in postHit to apply life steal
     */
    public void applyLifeSteal(PlayerEntity player, LivingEntity target, float damageDealt, World world) {
        float healthToRestore = damageDealt * LIFESTEAL_PERCENT;
        player.heal(healthToRestore);

        // Visual feedback
        if (world instanceof ServerWorld serverWorld) {
            serverWorld.spawnParticles(
                    ParticleTypes.HEART,
                    player.getX(), player.getY() + 1.5, player.getZ(),
                    3,
                    0.3, 0.3, 0.3,
                    0.1);

            // Dark particles from target
            serverWorld.spawnParticles(
                    ParticleTypes.SOUL,
                    target.getX(), target.getY() + 1, target.getZ(),
                    5,
                    0.3, 0.3, 0.3,
                    0.05);
        }

        // Sound effect
        world.playSound(
                null,
                player.getX(), player.getY(), player.getZ(),
                SoundEvents.ENTITY_PLAYER_LEVELUP,
                SoundCategory.PLAYERS,
                0.3f,
                2.0f);
    }

    @Override
    public boolean activate(World world, PlayerEntity player, ItemStack weapon) {
        // Passive ability - activated in postHit, not via right-click
        return false;
    }

    @Override
    public int getCooldownTicks() {
        return COOLDOWN_TICKS;
    }

    @Override
    public String getName() {
        return "Life Steal";
    }
}
