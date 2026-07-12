package com.mythicalswords.weapons;

import net.minecraft.item.ToolMaterials;

/**
 * Froststeel Saber - RARE ice weapon forged from Froststeel.
 */
public class FroststeelSaberItem extends MythicalWeaponItem {
    public FroststeelSaberItem() {
        super(ToolMaterials.DIAMOND, 10, -2.0f, new Settings().maxDamage(2200),
                WeaponTier.RARE, ElementalAffinity.ICE, "frost");
    }
}
