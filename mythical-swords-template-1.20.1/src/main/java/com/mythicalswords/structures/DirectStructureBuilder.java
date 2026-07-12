package com.mythicalswords.structures;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.registry.RegistryKey;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

/**
 * {@link StructureBuilder} backed directly by a live {@link ServerWorld},
 * placing blocks everywhere with no chunk-box restriction. Use only for
 * already-loaded regions (e.g. the {@code /summoncastle} debug command), NOT
 * during worldgen — that is what {@link BoundedStructureBuilder} is for.
 */
public class DirectStructureBuilder implements StructureBuilder {
    private final ServerWorld world;

    public DirectStructureBuilder(ServerWorld world) {
        this.world = world;
    }

    @Override
    public boolean setBlockState(BlockPos pos, BlockState state, int flags) {
        return world.setBlockState(pos, state, flags);
    }

    @Override
    public BlockState getBlockState(BlockPos pos) {
        return world.getBlockState(pos);
    }

    @Override
    public BlockEntity getBlockEntity(BlockPos pos) {
        return world.getBlockEntity(pos);
    }

    @Override
    public Random getRandom() {
        return world.getRandom();
    }

    @Override
    public RegistryKey<World> getRegistryKey() {
        return world.getRegistryKey();
    }

    @Override
    public int getTopY() {
        return world.getTopY();
    }

    @Override
    public int getBottomY() {
        return world.getBottomY();
    }

    @Override
    public long getSeed() {
        return world.getSeed();
    }

    // No chunk clipping for direct (already-loaded) placement.
    @Override public int writeMinX() { return -30000000; }
    @Override public int writeMaxX() { return 30000000; }
    @Override public int writeMinY() { return world.getBottomY(); }
    @Override public int writeMaxY() { return world.getTopY(); }
    @Override public int writeMinZ() { return -30000000; }
    @Override public int writeMaxZ() { return 30000000; }
}
