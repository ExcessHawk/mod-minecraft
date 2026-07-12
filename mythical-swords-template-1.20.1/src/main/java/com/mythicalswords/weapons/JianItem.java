package com.mythicalswords.weapons;

import com.mythicalswords.abilities.SwiftStrikesAbility;
import net.minecraft.item.ToolMaterials;

/**
 * Jian - Chinese straight sword
 * Phase 4: Chinese Mythology
 * EPIC tier weapon with LIGHTNING affinity
 * Craftable or rare drop
 * Special Ability: Swift Strikes (triple slash + movement boost)
 */
public class JianItem extends MythicalWeaponItem {

    public JianItem() {
        super(
                ToolMaterials.DIAMOND, // Base material
                10, // Attack damage (medium)
                -1.8f, // Attack speed (FASTEST)
                new Settings().maxDamage(2500), // EPIC durability
                WeaponTier.EPIC,
                ElementalAffinity.LIGHTNING,
                "chinese" // Mythology
        );

        // Set the special ability
        this.setAbility(new SwiftStrikesAbility());
    }
}
