package com.mythicalswords.weapons;

import com.mythicalswords.abilities.DivineLightSlashAbility;
import net.minecraft.item.ToolMaterials;

/**
 * Excalibur - The legendary sword of King Arthur
 * LEGENDARY tier weapon with DIVINE affinity. Drops from Rey Arturo.
 * Special Ability: Divine Light Slash. Rendered as a standard 2D handheld item.
 */
public class ExcaliburItem extends MythicalWeaponItem {

    public ExcaliburItem() {
        super(
            ToolMaterials.NETHERITE,
            15,
            -2.4f,
            new Settings().maxDamage(5000),
            WeaponTier.LEGENDARY,
            ElementalAffinity.DIVINE,
            "arthurian"
        );
        this.setAbility(new DivineLightSlashAbility());
    }
}
