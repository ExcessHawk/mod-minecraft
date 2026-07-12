package com.mythicalswords.core;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ExperienceDroppingBlock;
import net.minecraft.util.math.intprovider.UniformIntProvider;

/**
 * Tamahagane Ore block - Japanese mythology ore
 * Generates in the world and drops raw tamahagane when mined
 * Used for crafting legendary Japanese weapons
 */
public class TamahaganeOreBlock extends ExperienceDroppingBlock {
    
    public TamahaganeOreBlock(Settings settings) {
        super(settings, UniformIntProvider.create(3, 6));
    }
}
