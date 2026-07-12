package com.mythicalswords.weapons;

import com.mythicalswords.abilities.FireWaveAbility;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.ToolMaterial;
import net.minecraft.recipe.Ingredient;

/**
 * Laevateinn - Loki's legendary sword
 * LEGENDARY tier weapon with FIRE affinity
 * Drops from Loki boss
 * 
 * Special ability: Fire Wave - Cone-shaped fire damage
 * Requirements: 1.10, 15.3
 */
public class LaevateinnItem extends MythicalWeaponItem {

    private static final ToolMaterial LAEVATEINN_MATERIAL = new ToolMaterial() {
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
            return 14.0f; // Base damage (will be 17 total with modifier)
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

    public LaevateinnItem() {
        super(
                LAEVATEINN_MATERIAL,
                3, // Additional attack damage (14 base + 3 = 17 total)
                -2.4f, // Standard sword attack speed
                new FabricItemSettings().maxCount(1).fireproof(),
                WeaponTier.LEGENDARY,
                ElementalAffinity.FIRE,
                "norse");
        
        // Set the Fire Wave ability
        this.setAbility(new FireWaveAbility());
    }
}
