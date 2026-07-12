package com.mythicalswords.materials;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

/**
 * Agni's Flame Core - Hindu special material
 * Obtained from Himalayan structures
 * Used for crafting Hindu weapons (Astra de Agni)
 * Contains the essence of the fire god Agni
 */
public class AgnisFlameCore extends Item {
    
    public AgnisFlameCore() {
        super(new FabricItemSettings());
    }
    
    /**
     * Makes the item have the enchantment glint effect
     * Divine materials are inherently magical
     */
    @Override
    public boolean hasGlint(ItemStack stack) {
        return true;
    }
}
