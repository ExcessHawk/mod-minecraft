package com.mythicalswords.weapons;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.ToolMaterial;
import net.minecraft.recipe.Ingredient;

/**
 * Xiphos Sagrado - Sacred Greek short sword
 * COMMON tier weapon with DIVINE affinity
 */
public class XiphosSagradoItem extends MythicalWeaponItem {

    private static final ToolMaterial XIPHOS_MATERIAL = new ToolMaterial() {
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
            return 5.0f; // Base damage (will be 7 total with modifier)
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

    public XiphosSagradoItem() {
        super(
                XIPHOS_MATERIAL,
                2, // Additional attack damage (5 base + 2 = 7 total)
                -2.4f, // Standard sword attack speed
                new FabricItemSettings().maxCount(1),
                WeaponTier.COMMON,
                ElementalAffinity.DIVINE,
                "greek");
    }
}
