package com.mythicalswords.materials;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.Item;

/**
 * Gem of Bishamon - Japanese mythology material
 * Rare gem associated with the god of warriors and fortune
 * Used in crafting the Naginata de Bishamon
 * Obtainable from Japanese temple chests
 */
public class GemOfBishamonItem extends Item {
    
    public GemOfBishamonItem() {
        super(new FabricItemSettings().maxCount(16));
    }
}
