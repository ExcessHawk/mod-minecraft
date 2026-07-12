package com.mythicalswords.item.custom;

import com.mythicalswords.structures.ModStructures;
import net.minecraft.util.Formatting;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;

/**
 * Brújula de Camelot — Points to Arthurian Castle (Rey Arturo's structure).
 * Now just a thin wrapper around MythicalCompassItem.
 */
public class CamelotCompassItem extends MythicalCompassItem {

    public CamelotCompassItem(Settings settings) {
        super(settings, ModStructures.ARTHURIAN, "Camelot", Formatting.GOLD);
    }
}
