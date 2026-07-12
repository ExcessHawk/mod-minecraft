package com.mythicalswords.weapons;

import net.minecraft.item.ToolMaterials;

/**
 * Voidsteel Edge - EPIC dark/void weapon forged from Voidsteel.
 */
public class VoidsteelEdgeItem extends MythicalWeaponItem {
    public VoidsteelEdgeItem() {
        super(ToolMaterials.NETHERITE, 12, -2.2f, new Settings().maxDamage(2800),
                WeaponTier.EPIC, ElementalAffinity.DARK, "void");
    }
}
