package com.mythicalswords.weapons;

import net.minecraft.item.ToolMaterials;

/**
 * Orichalcum Blade - EPIC divine weapon forged from Orichalcum.
 */
public class OrichalcumBladeItem extends MythicalWeaponItem {
    public OrichalcumBladeItem() {
        super(ToolMaterials.NETHERITE, 11, -2.4f, new Settings().maxDamage(2500),
                WeaponTier.EPIC, ElementalAffinity.DIVINE, "atlantean");
    }
}
