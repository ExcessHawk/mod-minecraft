package com.mythicalswords.weapons;

import com.mythicalswords.abilities.StaffSlamAbility;
import net.minecraft.item.ToolMaterials;

/**
 * Ruyi Jingu Bang - The Monkey King's legendary staff
 * Phase 4: Chinese Mythology
 * LEGENDARY tier weapon with NATURE affinity
 * Drops from Sun Wukong boss (3% chance - RAREST DROP)
 * Special Ability: Staff Slam (AoE knockback)
 */
public class RuyiJinguBangItem extends MythicalWeaponItem {

    public RuyiJinguBangItem() {
        super(
                ToolMaterials.NETHERITE, // Base material
                15, // Attack damage (HIGHEST IN MOD)
                -2.8f, // Attack speed (slow but powerful)
                new Settings().maxDamage(5000), // Maximum durability
                WeaponTier.LEGENDARY,
                ElementalAffinity.NATURE,
                "chinese" // Mythology
        );

        // Set the special ability
        this.setAbility(new StaffSlamAbility());
    }
}
