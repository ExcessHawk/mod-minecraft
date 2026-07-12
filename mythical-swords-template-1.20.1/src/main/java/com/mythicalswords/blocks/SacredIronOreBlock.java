package com.mythicalswords.blocks;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ExperienceDroppingBlock;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.math.intprovider.UniformIntProvider;

/**
 * Sacred Iron Ore Block - Greek mythology ore
 * Generates in the world
 * Drops Raw Sacred Iron when mined
 * Drops 2-5 XP when mined
 */
public class SacredIronOreBlock extends ExperienceDroppingBlock {
    
    public SacredIronOreBlock() {
        super(
            FabricBlockSettings.create()
                .strength(3.0f, 3.0f) // Same as iron ore
                .requiresTool()
                .sounds(BlockSoundGroup.STONE),
            UniformIntProvider.create(2, 5) // XP drop: 2-5
        );
    }
}
