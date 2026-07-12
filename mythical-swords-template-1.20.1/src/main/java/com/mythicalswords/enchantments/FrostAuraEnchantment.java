package com.mythicalswords.enchantments;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;

/**
 * Frost Aura — chills the target on hit, applying Slowness and frozen ticks.
 */
public class FrostAuraEnchantment extends Enchantment {

    public FrostAuraEnchantment() {
        super(Rarity.UNCOMMON, EnchantmentTarget.WEAPON, new EquipmentSlot[]{EquipmentSlot.MAINHAND});
    }

    @Override
    public int getMinPower(int level) {
        return 10 + (level - 1) * 8;
    }

    @Override
    public int getMaxPower(int level) {
        return getMinPower(level) + 20;
    }

    @Override
    public int getMaxLevel() {
        return 3;
    }

    @Override
    public boolean isAvailableForEnchantedBookOffer() {
        return true;
    }

    @Override
    public void onTargetDamaged(LivingEntity user, Entity target, int level) {
        if (user.getWorld().isClient) return;
        if (target instanceof LivingEntity living) {
            living.addStatusEffect(new StatusEffectInstance(
                StatusEffects.SLOWNESS, 40 + level * 20, level - 1));
            living.setFrozenTicks(living.getFrozenTicks() + level * 40);
        }
    }
}
