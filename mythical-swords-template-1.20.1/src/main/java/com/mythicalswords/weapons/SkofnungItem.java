package com.mythicalswords.weapons;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.ToolMaterial;
import net.minecraft.recipe.Ingredient;

/**
 * Skofnung - Legendary Norse sword of King Hrólf Kraki
 * RARE tier weapon with ICE affinity
 * Crafted with 2 Northsteel, 1 Spiritbound Leather, 1 Frozen Soul Crystal
 */
public class SkofnungItem extends MythicalWeaponItem {

    // Custom tool material for Skofnung
    private static final ToolMaterial SKOFNUNG_MATERIAL = new ToolMaterial() {
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

    public SkofnungItem() {
        super(
                SKOFNUNG_MATERIAL,
                2, // Additional attack damage (7 base + 2 = 9 total)
                -2.4f, // Standard sword attack speed
                new FabricItemSettings().maxCount(1),
                WeaponTier.RARE,
                ElementalAffinity.ICE,
                "norse");
    }
}
