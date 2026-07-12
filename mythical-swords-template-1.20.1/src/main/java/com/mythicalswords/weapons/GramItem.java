package com.mythicalswords.weapons;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.ToolMaterial;
import net.minecraft.recipe.Ingredient;

/**
 * Gram - Legendary Norse sword wielded by Sigurd the dragon-slayer
 * RARE tier weapon with ICE affinity
 */
public class GramItem extends MythicalWeaponItem {

    // Custom tool material for Gram
    private static final ToolMaterial GRAM_MATERIAL = new ToolMaterial() {
        @Override
        public int getDurability() {
            return WeaponTier.RARE.getDurability(); // 1500
        }

        @Override
        public float getMiningSpeedMultiplier() {
            return 1.0f;
        }

        @Override
        public float getAttackDamage() {
            return 6.0f; // Base damage (will be 8 total with modifier)
        }

        @Override
        public int getMiningLevel() {
            return 3; // Diamond level
        }

        @Override
        public int getEnchantability() {
            return 15; // Higher enchantability for rare weapons
        }

        @Override
        public Ingredient getRepairIngredient() {
            // Can be repaired with Northsteel Ingot in future
            return Ingredient.EMPTY;
        }
    };

    public GramItem() {
        super(
                GRAM_MATERIAL,
                2, // Additional attack damage (6 base + 2 = 8 total)
                -2.4f, // Standard sword attack speed
                new FabricItemSettings().maxCount(1),
                WeaponTier.RARE,
                ElementalAffinity.ICE,
                "norse");
    }
}
