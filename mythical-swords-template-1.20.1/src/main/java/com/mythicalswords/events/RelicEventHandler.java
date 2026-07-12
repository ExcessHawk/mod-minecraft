package com.mythicalswords.events;

import com.mythicalswords.core.ModItems;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;

/**
 * Handles passive relic effects. Currently: Phoenix Feather cheats death once,
 * consuming a feather from the player's inventory and reviving them in flame.
 */
public class RelicEventHandler {

    public static void register() {
        ServerLivingEntityEvents.ALLOW_DEATH.register((entity, source, amount) -> {
            if (!(entity instanceof PlayerEntity player)) {
                return true; // allow death
            }
            if (!consumeFeather(player)) {
                return true; // no feather, allow death
            }

            // Revive
            player.setHealth(player.getMaxHealth());
            player.clearStatusEffects();
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.FIRE_RESISTANCE, 600, 0));
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 200, 2));
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.ABSORPTION, 600, 2));
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, 200, 1));
            player.setFireTicks(0);

            if (player.getWorld() instanceof ServerWorld serverWorld) {
                serverWorld.spawnParticles(ParticleTypes.FLAME,
                        player.getX(), player.getY() + 1.0, player.getZ(),
                        80, 0.5, 1.0, 0.5, 0.2);
                serverWorld.spawnParticles(ParticleTypes.FLASH,
                        player.getX(), player.getY() + 1.0, player.getZ(), 1, 0, 0, 0, 0);
            }
            player.getWorld().playSound(null, player.getX(), player.getY(), player.getZ(),
                    SoundEvents.ITEM_FIRECHARGE_USE, SoundCategory.PLAYERS, 1.5f, 1.0f);

            return false; // cancel death
        });
    }

    private static boolean consumeFeather(PlayerEntity player) {
        var inv = player.getInventory();
        for (int i = 0; i < inv.size(); i++) {
            ItemStack stack = inv.getStack(i);
            if (stack.isOf(ModItems.PHOENIX_FEATHER)) {
                stack.decrement(1);
                return true;
            }
        }
        return false;
    }
}
