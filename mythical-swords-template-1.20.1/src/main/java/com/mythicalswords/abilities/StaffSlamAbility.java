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
 * Staff Slam Ability - Ruyi Jingu Bang special ability
 * AoE ground slam with massive knockback in 5 block radius
 */
public class StaffSlamAbility implements WeaponAbility {

    private static final int COOLDOWN_TICKS = 300; // 15 seconds
    private static final double RADIUS = 5.0;
    private static final float DAMAGE = 6.0f;
    private static final double KNOCKBACK_STRENGTH = 2.5;

    @Override
    public boolean activate(World world, PlayerEntity player, ItemStack weapon) {
        if (world.isClient) {
            return false;
        }

        if (!(world instanceof ServerWorld serverWorld)) {
            return false;
        }

        // Create earthquake particles in a circle
        for (int i = 0; i < 60; i++) {
            double angle = (i / 60.0) * Math.PI * 2;
            double x = player.getX() + Math.cos(angle) * RADIUS;
            double z = player.getZ() + Math.sin(angle) * RADIUS;
            double y = player.getY();

            serverWorld.spawnParticles(
                    ParticleTypes.EXPLOSION,
                    x, y + 0.1, z,
                    1,
                    0, 0, 0,
                    0);

            serverWorld.spawnParticles(
                    ParticleTypes.POOF,
                    x, y, z,
                    3,
                    0.2, 0.1, 0.2,
                    0.05);
        }

        // Central impact particles
        serverWorld.spawnParticles(
                ParticleTypes.EXPLOSION_EMITTER,
                player.getX(), player.getY(), player.getZ(),
                1,
                0, 0, 0,
                0);

        // Sound effects
        world.playSound(
                null,
                player.getX(), player.getY(), player.getZ(),
                SoundEvents.ENTITY_WARDEN_SONIC_BOOM,
                SoundCategory.PLAYERS,
                2.0f,
                0.8f);

        world.playSound(
                null,
                player.getX(), player.getY(), player.getZ(),
                SoundEvents.ENTITY_GENERIC_EXPLODE,
                SoundCategory.PLAYERS,
                1.5f,
                0.5f);

        // Deal damage and knockback to all entities in radius
        Box damageBox = new Box(
                player.getX() - RADIUS, player.getY() - 2, player.getZ() - RADIUS,
                player.getX() + RADIUS, player.getY() + 4, player.getZ() + RADIUS);

        List<LivingEntity> entities = world.getEntitiesByClass(
                LivingEntity.class,
                damageBox,
                entity -> entity != player && !entity.isTeammate(player));

        for (LivingEntity entity : entities) {
            // Damage
            entity.damage(world.getDamageSources().playerAttack(player), DAMAGE);

            // Massive knockback
            Vec3d knockbackDirection = entity.getPos().subtract(player.getPos()).normalize();
            Vec3d knockback = knockbackDirection.multiply(KNOCKBACK_STRENGTH, 0.8, KNOCKBACK_STRENGTH);
            entity.addVelocity(knockback.x, knockback.y, knockback.z);
            entity.velocityModified = true;

            // Hit particles
            serverWorld.spawnParticles(
                    ParticleTypes.CRIT,
                    entity.getX(), entity.getY() + entity.getHeight() / 2, entity.getZ(),
                    10,
                    0.3, 0.3, 0.3,
                    0.1);
        }

        return true;
    }

    @Override
    public int getCooldownTicks() {
        return COOLDOWN_TICKS;
    }

    @Override
    public String getName() {
        return "Staff Slam";
    }
}
