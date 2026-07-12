package com.mythicalswords.abilities;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.SilverfishEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.world.World;

/**
 * Summon Scarabs Ability - Was Scepter special ability
 * Right-click to summon 3 friendly silverfish for 30 seconds
 */
public class SummonScarabsAbility implements WeaponAbility {

    private static final int COOLDOWN_TICKS = 600; // 30 seconds
    private static final int SCARAB_COUNT = 3;
    private static final int SCARAB_DURATION = 600; // 30 seconds lifespan

    @Override
    public boolean activate(World world, PlayerEntity player, ItemStack weapon) {
        if (world.isClient) {
            return false;
        }

        if (!(world instanceof ServerWorld serverWorld)) {
            return false;
        }

        // Summon scarabs around player
        for (int i = 0; i < SCARAB_COUNT; i++) {
            double angle = (i / (double) SCARAB_COUNT) * Math.PI * 2;
            double offsetX = Math.cos(angle) * 2;
            double offsetZ = Math.sin(angle) * 2;

            SilverfishEntity scarab = EntityType.SILVERFISH.create(serverWorld);
            if (scarab != null) {
                scarab.refreshPositionAndAngles(
                        player.getX() + offsetX,
                        player.getY(),
                        player.getZ() + offsetZ,
                        player.getYaw(),
                        0.0F);

                // Make scarabs friendly to player (they won't attack player)
                scarab.setTarget(null);

                // Buff scarabs
                scarab.addStatusEffect(new StatusEffectInstance(StatusEffects.STRENGTH, SCARAB_DURATION, 1));
                scarab.addStatusEffect(new StatusEffectInstance(StatusEffects.SPEED, SCARAB_DURATION, 1));
                scarab.addStatusEffect(new StatusEffectInstance(StatusEffects.GLOWING, SCARAB_DURATION, 0));

                serverWorld.spawnEntity(scarab);

                // Particles at summon location
                serverWorld.spawnParticles(
                        ParticleTypes.END_ROD,
                        scarab.getX(), scarab.getY() + 0.5, scarab.getZ(),
                        10,
                        0.3, 0.3, 0.3,
                        0.1);
            }
        }

        // Central particles
        serverWorld.spawnParticles(
                ParticleTypes.ENCHANT,
                player.getX(), player.getY() + 1, player.getZ(),
                30,
                0.5, 0.5, 0.5,
                1.0);

        // Sound effect
        world.playSound(
                null,
                player.getX(), player.getY(), player.getZ(),
                SoundEvents.ENTITY_EVOKER_PREPARE_SUMMON,
                SoundCategory.PLAYERS,
                1.0f,
                1.2f);

        return true;
    }

    @Override
    public int getCooldownTicks() {
        return COOLDOWN_TICKS;
    }

    @Override
    public String getName() {
        return "Summon Scarabs";
    }
}
