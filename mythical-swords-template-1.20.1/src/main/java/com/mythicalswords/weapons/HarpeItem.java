package com.mythicalswords.weapons;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.ToolMaterial;
import net.minecraft.recipe.Ingredient;

/**
 * Harpe - Perseus' sickle-sword used to slay Medusa
 * RARE tier weapon with DIVINE affinity
 */
public class HarpeItem extends MythicalWeaponItem {

    private static final ToolMaterial HARPE_MATERIAL = new ToolMaterial() {
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
            return 18; // Higher enchantability for rare weapons
        }

        @Override
        public Ingredient getRepairIngredient() {
            return Ingredient.EMPTY;
        }
    };

    public HarpeItem() {
        super(
                HARPE_MATERIAL,
                2, // Additional attack damage (6 base + 2 = 8 total)
                -2.4f, // Standard sword attack speed
                new FabricItemSettings().maxCount(1),
                WeaponTier.RARE,
                ElementalAffinity.DIVINE,
                "greek");
    }
}
