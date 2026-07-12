package com.mythicalswords.materials;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.Item;

/**
 * Sacred Water of Amaterasu - Japanese mythology material
 * Holy water blessed by the sun goddess Amaterasu
 * Used in crafting legendary Japanese weapons like Masamune
 * Obtainable from Shinto shrine structures
 */
public class SacredWaterOfAmaterasuItem extends Item {
    
    public SacredWaterOfAmaterasuItem() {
        super(new FabricItemSettings().maxCount(16));
    }
}
