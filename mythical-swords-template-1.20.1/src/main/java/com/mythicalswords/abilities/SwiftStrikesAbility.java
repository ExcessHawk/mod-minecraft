package com.mythicalswords.abilities;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
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
 * Swift Strikes Ability - Jian special ability
 * Passive: +20% movement speed when held
 * Active: Triple slash attack on nearby enemies
 */
public class SwiftStrikesAbility implements WeaponAbility {

    private static final int COOLDOWN_TICKS = 200; // 10 seconds
    private static final float SLASH_DAMAGE = 5.0f;
    private static final double SLASH_RADIUS = 4.0;

    /**
     * Applies passive movement speed buff
     * Should be called in inventoryTick
     */
    public void applyPassiveSpeed(PlayerEntity player) {
        // Add Speed I effect if not already present
        if (!player.hasStatusEffect(StatusEffects.SPEED)) {
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.SPEED, 20, 0, true, false));
        }
    }

    @Override
    public boolean activate(World world, PlayerEntity player, ItemStack weapon) {
        if (world.isClient) {
            return false;
        }

        if (!(world instanceof ServerWorld serverWorld)) {
            return false;
        }

        // Triple slash: first immediately, the rest staggered over real ticks
        performSlash(serverWorld, player);
        com.mythicalswords.util.ServerScheduler.schedule(6, () -> {
            if (player.isAlive()) performSlash(serverWorld, player);
        });
        com.mythicalswords.util.ServerScheduler.schedule(12, () -> {
            if (player.isAlive()) performSlash(serverWorld, player);
        });

        return true;
    }

    private void performSlash(ServerWorld world, PlayerEntity player) {
        // Lightning particle trail in arc
        double playerYaw = Math.toRadians(-player.getYaw() + 90);

        for (int i = 0; i < 15; i++) {
            double angle = playerYaw + (i / 15.0 - 0.5) * Math.PI; // arc in front
            double distance = 2.0 + (i / 15.0);
            double x = player.getX() + Math.cos(angle) * distance;
            double z = player.getZ() + Math.sin(angle) * distance;
            double y = player.getY() + 1.0;

            world.spawnParticles(
                    ParticleTypes.ELECTRIC_SPARK,
                    x, y, z,
                    2,
                    0.1, 0.1, 0.1,
                    0.2);

            world.spawnParticles(
                    ParticleTypes.SWEEP_ATTACK,
                    x, y, z,
                    1,
                    0, 0, 0,
                    0);
        }

        // Sound
        world.playSound(
                null,
                player.getX(), player.getY(), player.getZ(),
                SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP,
                SoundCategory.PLAYERS,
                0.8f,
                1.5f);

        // Deal damage to nearby entities
        Box damageBox = new Box(
                player.getX() - SLASH_RADIUS, player.getY() - 1, player.getZ() - SLASH_RADIUS,
                player.getX() + SLASH_RADIUS, player.getY() + 3, player.getZ() + SLASH_RADIUS);

        List<LivingEntity> entities = world.getEntitiesByClass(
                LivingEntity.class,
                damageBox,
                entity -> entity != player && !entity.isTeammate(player));

        for (LivingEntity entity : entities) {
            // Bypass invulnerability frames so each slash in the combo lands
            entity.timeUntilRegen = 0;
            entity.damage(world.getDamageSources().playerAttack(player), SLASH_DAMAGE);

            // Hit particles
            world.spawnParticles(
                    ParticleTypes.CRIT,
                    entity.getX(), entity.getY() + entity.getHeight() / 2, entity.getZ(),
                    5,
                    0.3, 0.3, 0.3,
                    0.1);
        }
    }

    @Override
    public int getCooldownTicks() {
        return COOLDOWN_TICKS;
    }

    @Override
    public String getName() {
        return "Swift Strikes";
    }
}
