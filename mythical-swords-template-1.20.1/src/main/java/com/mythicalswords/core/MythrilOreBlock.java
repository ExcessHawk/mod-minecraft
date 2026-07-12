package com.mythicalswords.core;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ExperienceDroppingBlock;
import net.minecraft.util.math.intprovider.UniformIntProvider;

/**
 * Mythril Ore block - generates in the world and drops raw mythril when mined
 */
public class MythrilOreBlock extends ExperienceDroppingBlock {
    
    public MythrilOreBlock(Settings settings) {
        super(settings, UniformIntProvider.create(2, 5));
    }
}
