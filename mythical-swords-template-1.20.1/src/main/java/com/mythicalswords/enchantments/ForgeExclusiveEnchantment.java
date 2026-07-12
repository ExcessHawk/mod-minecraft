package com.mythicalswords.enchantments;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;

/**
 * Base for mythology enchantments that can only be applied at the Mythical
 * Forge with a rune material. Hidden from the enchanting table, villager
 * trades and loot so the forge stays the sole source.
 */
public abstract class ForgeExclusiveEnchantment extends Enchantment {

    private final int maxLevel;

    protected ForgeExclusiveEnchantment(Rarity rarity, int maxLevel) {
        super(rarity, EnchantmentTarget.WEAPON, new EquipmentSlot[]{EquipmentSlot.MAINHAND});
        this.maxLevel = maxLevel;
    }

    @Override
    public int getMaxLevel() {
        return maxLevel;
    }

    @Override
    public boolean isAvailableForEnchantedBookOffer() {
        return false;
    }

    @Override
    public boolean isAvailableForRandomSelection() {
        return false;
    }
}
