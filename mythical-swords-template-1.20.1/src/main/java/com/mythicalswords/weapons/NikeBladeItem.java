package com.mythicalswords.weapons;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.ToolMaterial;
import net.minecraft.recipe.Ingredient;

/**
 * Niké Blade - Sword of Victory
 * COMMON tier weapon with LIGHTNING affinity
 */
public class NikeBladeItem extends MythicalWeaponItem {

    private static final ToolMaterial NIKE_MATERIAL = new ToolMaterial() {
        @Override
        public int getDurability() {
            return WeaponTier.COMMON.getDurability(); // 1000
        }

        @Override
        public float getMiningSpeedMultiplier() {
            return 1.0f;
        }

        @Override
        public float getAttackDamage() {
            return 4.0f; // Base damage (will be 6 total with modifier)
        }

        @Override
        public int getMiningLevel() {
            return 2; // Iron level
        }

        @Override
        public int getEnchantability() {
            return 15; // Standard enchantability
        }

        @Override
        public Ingredient getRepairIngredient() {
            return Ingredient.EMPTY;
        }
    };

    public NikeBladeItem() {
        super(
                NIKE_MATERIAL,
                2, // Additional attack damage (4 base + 2 = 6 total)
                -2.4f, // Standard sword attack speed
                new FabricItemSettings().maxCount(1),
                WeaponTier.COMMON,
                ElementalAffinity.LIGHTNING,
                "greek");
    }
}
