package com.mythicalswords.enchantments;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;

/**
 * Soul Reaper — chance on hit to inflict Wither, draining the target's soul.
 */
public class SoulReaperEnchantment extends Enchantment {

    private static final float WITHER_CHANCE = 0.20f; // per level

    public SoulReaperEnchantment() {
        super(Rarity.VERY_RARE, EnchantmentTarget.WEAPON, new EquipmentSlot[]{EquipmentSlot.MAINHAND});
    }

    @Override
    public int getMinPower(int level) {
        return 20 + (level - 1) * 10;
    }

    @Override
    public int getMaxPower(int level) {
        return getMinPower(level) + 25;
    }

    @Override
    public int getMaxLevel() {
        return 2;
    }

    @Override
    public boolean isAvailableForEnchantedBookOffer() {
        return true;
    }

    @Override
    public void onTargetDamaged(LivingEntity user, Entity target, int level) {
        if (user.getWorld().isClient) return;
        if (target instanceof LivingEntity living
                && user.getRandom().nextFloat() < WITHER_CHANCE * level) {
            living.addStatusEffect(new StatusEffectInstance(
                StatusEffects.WITHER, 60 + level * 20, level - 1));
        }
    }
}
