package com.mythicalswords.materials;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.Item;

/**
 * Soul of the Swordsmith - Rare crafting material
 * Drops from Legendary Blacksmith mini-boss
 * Used in crafting legendary Japanese weapons like Masamune
 */
public class SoulSwordsmithItem extends Item {
    public SoulSwordsmithItem() {
        super(new FabricItemSettings().maxCount(16));
    }
}
