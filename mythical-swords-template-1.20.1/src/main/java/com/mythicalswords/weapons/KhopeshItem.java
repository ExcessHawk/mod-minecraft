package com.mythicalswords.weapons;

import com.mythicalswords.abilities.LifeStealAbility;
import net.minecraft.item.ToolMaterials;

/**
 * Khopesh - Egyptian sickle sword
 * Phase 4: Egyptian Mythology
 * LEGENDARY tier weapon with DARK affinity
 * Drops from Anubis boss (5% chance)
 * Special Ability: Life Steal
 */
public class KhopeshItem extends MythicalWeaponItem {

    public KhopeshItem() {
        super(
                ToolMaterials.NETHERITE, // Base material
                12, // Attack damage (high)
                -2.3f, // Attack speed (medium)
                new Settings().maxDamage(4500), // LEGENDARY durability
                WeaponTier.LEGENDARY,
                ElementalAffinity.DARK,
                "egyptian" // Mythology
        );

        // Set the special ability
        this.setAbility(new LifeStealAbility());
    }
}
