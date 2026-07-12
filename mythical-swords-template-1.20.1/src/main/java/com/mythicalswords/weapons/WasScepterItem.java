package com.mythicalswords.weapons;

import com.mythicalswords.abilities.SummonScarabsAbility;
import net.minecraft.item.ToolMaterials;

/**
 * Was Scepter - Egyptian divine staff
 * Phase 4: Egyptian Mythology
 * LEGENDARY tier weapon with DIVINE affinity
 * Drops from Ra boss (5% chance)
 * Special Ability: Summon Scarabs
 */
public class WasScepterItem extends MythicalWeaponItem {

    public WasScepterItem() {
        super(
                ToolMaterials.NETHERITE, // Base material
                11, // Attack damage (medium-high)
                -2.5f, // Attack speed (slower staff)
                new Settings().maxDamage(4200), // LEGENDARY durability
                WeaponTier.LEGENDARY,
                ElementalAffinity.DIVINE,
                "egyptian" // Mythology
        );

        // Set the special ability
        this.setAbility(new SummonScarabsAbility());
    }
}
