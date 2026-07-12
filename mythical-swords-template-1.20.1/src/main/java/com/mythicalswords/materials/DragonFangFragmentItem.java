package com.mythicalswords.materials;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;

/**
 * Dragon Fang Fragment - Legendary weapon component
 * Can be used as guard, pommel, or edge enhancement component
 * Has a magical glint effect due to its draconic nature
 */
public class DragonFangFragmentItem extends Item {

    public DragonFangFragmentItem() {
        super(new FabricItemSettings());
    }
    
    /**
     * Makes the item have the enchantment glint effect
     * Dragon materials are inherently magical
     */
    @Override
    public boolean hasGlint(ItemStack stack) {
        return true;
    }
}
