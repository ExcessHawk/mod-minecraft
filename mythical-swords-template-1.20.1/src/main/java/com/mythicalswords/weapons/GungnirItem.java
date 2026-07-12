package com.mythicalswords.weapons;

import com.mythicalswords.abilities.NeverMissStrikeAbility;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.ToolMaterial;
import net.minecraft.recipe.Ingredient;

/**
 * Gungnir - Odin's legendary spear
 * LEGENDARY tier weapon with LIGHTNING affinity
 * Drops from Odín boss
 * 
 * Special ability: Never Miss Strike - Homing lightning projectile
 * Requirements: 1.10, 15.3
 */
public class GungnirItem extends MythicalWeaponItem {

    private static final ToolMaterial GUNGNIR_MATERIAL = new ToolMaterial() {
        @Override
        public int getDurability() {
            return WeaponTier.LEGENDARY.getDurability(); // 5000
        }

        @Override
        public float getMiningSpeedMultiplier() {
            return 1.0f;
        }

        @Override
        public float getAttackDamage() {
            return 13.0f; // Base damage (will be 16 total with modifier)
        }

        @Override
        public int getMiningLevel() {
            return 4; // Netherite level
        }

        @Override
        public int getEnchantability() {
            return 22; // Highest enchantability for legendary
        }

        @Override
        public Ingredient getRepairIngredient() {
            return Ingredient.EMPTY;
        }
    };

    public GungnirItem() {
        super(
                GUNGNIR_MATERIAL,
                3, // Additional attack damage (13 base + 3 = 16 total)
                -2.4f, // Standard sword attack speed
                new FabricItemSettings().maxCount(1).fireproof(),
                WeaponTier.LEGENDARY,
                ElementalAffinity.LIGHTNING,
                "norse");
        
        // Set the Never Miss Strike ability
        this.setAbility(new NeverMissStrikeAbility());
    }
}
