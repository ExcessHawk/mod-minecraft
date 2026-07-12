package com.mythicalswords.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

public class MythicalForgeBlock extends BlockWithEntity {

    public MythicalForgeBlock(Settings settings) {
        super(settings);
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new MythicalForgeBlockEntity(pos, state);
    }

    @Override
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        // Embers rising from the crucible on top
        if (random.nextInt(3) != 0) return;
        double x = pos.getX() + 0.5 + (random.nextDouble() - 0.5) * 0.5;
        double y = pos.getY() + 1.05;
        double z = pos.getZ() + 0.5 + (random.nextDouble() - 0.5) * 0.5;
        world.addParticle(ParticleTypes.SMALL_FLAME, x, y, z, 0.0, 0.02, 0.0);
        if (random.nextInt(4) == 0) {
            world.addParticle(ParticleTypes.SMOKE, x, y, z, 0.0, 0.04, 0.0);
        }
        if (random.nextInt(8) == 0) {
            world.addParticle(ParticleTypes.LAVA, pos.getX() + 0.5, y, pos.getZ() + 0.5, 0.0, 0.0, 0.0);
        }
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos,
                              PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (!world.isClient) {
            NamedScreenHandlerFactory factory = state.createScreenHandlerFactory(world, pos);
            if (factory != null) {
                player.openHandledScreen(factory);
            }
        }
        return ActionResult.SUCCESS;
    }
}
