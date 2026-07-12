package com.mythicalswords.core;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ExperienceDroppingBlock;
import net.minecraft.util.math.intprovider.UniformIntProvider;

/**
 * Jade Imperial Ore block - Chinese mythology ore
 * Generates in the world and drops raw jade imperial when mined
 */
public class JadeImperialOreBlock extends ExperienceDroppingBlock {
    
    public JadeImperialOreBlock(Settings settings) {
        super(settings, UniformIntProvider.create(2, 5));
    }
}
