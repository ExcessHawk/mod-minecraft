package com.mythicalswords.materials;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

/**
 * Vajra Crystal - Hindu special material
 * Obtained from Himalayan mountain peak structures
 * Used for crafting Hindu weapons (Vajra)
 * Contains the power of Indra's thunderbolt
 */
public class VajraCrystal extends Item {
    
    public VajraCrystal() {
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
