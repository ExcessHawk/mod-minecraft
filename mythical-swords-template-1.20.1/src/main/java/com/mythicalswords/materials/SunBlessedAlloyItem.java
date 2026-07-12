package com.mythicalswords.materials;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;

/**
 * Sun-Blessed Alloy - Enchanted reinforcement material
 * Can be used as blade reinforcement or magical binding component
 * Has a magical glint effect like enchanted items
 */
public class SunBlessedAlloyItem extends Item {

    public SunBlessedAlloyItem() {
        super(new FabricItemSettings());
    }
    
    /**
     * Makes the item have the enchantment glint effect
     * This gives it that magical shimmering appearance
     */
    @Override
    public boolean hasGlint(ItemStack stack) {
        return true;
    }
}
