package com.mythicalswords.events;

import com.mythicalswords.core.ModItems;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.Item;
import net.minecraft.server.network.ServerPlayerEntity;

/**
 * Grants a set bonus (themed status effect) while a player wears a full
 * matching mythic armor set. Checked a few times per second and refreshed.
 */
public class ArmorSetHandler {

    public static void register() {
        ServerTickEvents.END_SERVER_TICK.register(server -> {
            if (server.getTicks() % 40 != 0) {
                return;
            }
            for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
                if (wears(player, ModItems.ORICHALCUM_HELMET, ModItems.ORICHALCUM_CHESTPLATE, ModItems.ORICHALCUM_LEGGINGS, ModItems.ORICHALCUM_BOOTS)) {
                    apply(player, StatusEffects.REGENERATION, 0);
                    apply(player, StatusEffects.RESISTANCE, 0);
                } else if (wears(player, ModItems.URU_HELMET, ModItems.URU_CHESTPLATE, ModItems.URU_LEGGINGS, ModItems.URU_BOOTS)) {
                    apply(player, StatusEffects.SPEED, 0);
                    apply(player, StatusEffects.HASTE, 0);
                } else if (wears(player, ModItems.VOIDSTEEL_HELMET, ModItems.VOIDSTEEL_CHESTPLATE, ModItems.VOIDSTEEL_LEGGINGS, ModItems.VOIDSTEEL_BOOTS)) {
                    apply(player, StatusEffects.NIGHT_VISION, 0);
                    apply(player, StatusEffects.STRENGTH, 0);
                } else if (wears(player, ModItems.FROSTSTEEL_HELMET, ModItems.FROSTSTEEL_CHESTPLATE, ModItems.FROSTSTEEL_LEGGINGS, ModItems.FROSTSTEEL_BOOTS)) {
                    apply(player, StatusEffects.RESISTANCE, 0);
                    apply(player, StatusEffects.FIRE_RESISTANCE, 0);
                }
            }
        });
    }

    private static boolean wears(ServerPlayerEntity player, Item helmet, Item chest, Item legs, Item boots) {
        return player.getInventory().getArmorStack(3).isOf(helmet)
                && player.getInventory().getArmorStack(2).isOf(chest)
                && player.getInventory().getArmorStack(1).isOf(legs)
                && player.getInventory().getArmorStack(0).isOf(boots);
    }

    private static void apply(ServerPlayerEntity player, net.minecraft.entity.effect.StatusEffect effect, int amplifier) {
        // 3s duration, refreshed every 2s; ambient, no particles, no icon spam
        player.addStatusEffect(new StatusEffectInstance(effect, 60, amplifier, true, false, true));
    }
}
