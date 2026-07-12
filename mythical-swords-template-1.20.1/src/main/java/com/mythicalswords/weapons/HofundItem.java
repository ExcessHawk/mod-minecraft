package com.mythicalswords.weapons;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.ToolMaterial;
import net.minecraft.recipe.Ingredient;

/**
 * Hofund - Legendary Norse sword of Heimdall, guardian of the Bifrost
 * RARE tier weapon with DIVINE affinity
 * Crafted with 2 Northsteel, 2 Oro Ritual, 1 Rainbow Bridge Fragment
 */
public class HofundItem extends MythicalWeaponItem {

    // Custom tool material for Hofund
    private static final ToolMaterial HOFUND_MATERIAL = new ToolMaterial() {
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
            return 7.0f; // Base damage (will be 9 total with modifier)
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
            // Can be repaired with Northsteel Ingot
            return Ingredient.EMPTY;
        }
    };

    public HofundItem() {
        super(
                HOFUND_MATERIAL,
                2, // Additional attack damage (7 base + 2 = 9 total)
                -2.4f, // Standard sword attack speed
                new FabricItemSettings().maxCount(1),
                WeaponTier.RARE,
                ElementalAffinity.DIVINE,
                "norse");
    }
}
