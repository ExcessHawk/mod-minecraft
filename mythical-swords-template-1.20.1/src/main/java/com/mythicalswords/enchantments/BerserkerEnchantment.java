package com.mythicalswords.enchantments;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;

/**
 * Berserker — the lower the wielder's health, the more bonus damage dealt.
 */
public class BerserkerEnchantment extends Enchantment {

    public BerserkerEnchantment() {
        super(Rarity.RARE, EnchantmentTarget.WEAPON, new EquipmentSlot[]{EquipmentSlot.MAINHAND});
    }

    @Override
    public int getMinPower(int level) {
        return 15 + (level - 1) * 9;
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
        if (!(target instanceof LivingEntity living)) return;

        float missing = 1.0f - (user.getHealth() / user.getMaxHealth());
        if (missing > 0.5f) {
            // Up to level*4 bonus magic damage when near death.
            float bonus = level * 4.0f * missing;
            living.damage(user.getDamageSources().magic(), bonus);
        }
    }
}
