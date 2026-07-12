package com.mythicalswords.core;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ExperienceDroppingBlock;
import net.minecraft.util.math.intprovider.UniformIntProvider;

/**
 * Obsidiana Ritual Ore block - Mesoamerican mythology ore
 * Generates in the world and drops raw obsidiana ritual when mined
 * Used for crafting legendary Mesoamerican weapons
 * Features jade green and gold coloring
 */
public class ObsidianaRitualOreBlock extends ExperienceDroppingBlock {
    
    public ObsidianaRitualOreBlock(Settings settings) {
        super(settings, UniformIntProvider.create(3, 7));
    }
}
