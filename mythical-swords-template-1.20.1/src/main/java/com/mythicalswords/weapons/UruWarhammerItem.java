package com.mythicalswords.weapons;

import net.minecraft.item.ToolMaterials;

/**
 * Uru Warhammer - EPIC lightning weapon forged from Uru, heavy and slow.
 */
public class UruWarhammerItem extends MythicalWeaponItem {
    public UruWarhammerItem() {
        super(ToolMaterials.NETHERITE, 13, -3.0f, new Settings().maxDamage(3000),
                WeaponTier.EPIC, ElementalAffinity.LIGHTNING, "norse");
    }
}
