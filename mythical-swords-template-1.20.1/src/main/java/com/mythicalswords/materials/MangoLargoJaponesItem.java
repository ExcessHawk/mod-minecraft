package com.mythicalswords.materials;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.Item;

/**
 * Mango Largo Japonés (Long Japanese Handle) - Japanese mythology material
 * Traditional long wooden handle used for polearms
 * Used in crafting the Naginata de Bishamon
 * Craftable from bamboo and special wood
 */
public class MangoLargoJaponesItem extends Item {
    
    public MangoLargoJaponesItem() {
        super(new FabricItemSettings().maxCount(16));
    }
}
