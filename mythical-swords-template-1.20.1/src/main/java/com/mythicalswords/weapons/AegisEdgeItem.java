package com.mythicalswords.weapons;

import com.mythicalswords.abilities.ShieldReflectionAbility;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.ToolMaterial;
import net.minecraft.recipe.Ingredient;

/**
 * Aegis Edge - Athena's legendary sword
 * LEGENDARY tier weapon with DIVINE affinity
 * Drops from Atenea boss
 * Special Ability: Shield Reflection
 */
public class AegisEdgeItem extends MythicalWeaponItem {

    private static final ToolMaterial AEGIS_MATERIAL = new ToolMaterial() {
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
            return 12.0f; // Base damage (will be 14 total with modifier)
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

    public AegisEdgeItem() {
        super(
                AEGIS_MATERIAL,
                2, // Additional attack damage (12 base + 2 = 14 total)
                -2.4f, // Standard sword attack speed
                new FabricItemSettings().maxCount(1).fireproof(),
                WeaponTier.LEGENDARY,
                ElementalAffinity.DIVINE,
                "greek");
        
        // Set the special ability
        this.setAbility(new ShieldReflectionAbility());
    }
}
