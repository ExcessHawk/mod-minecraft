package com.mythicalswords.structures;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.World;

/**
 * {@link StructureBuilder} backed by the worldgen {@code StructureWorldAccess}.
 * Writes outside {@code box} (the chunk currently being generated) are dropped,
 * so a generator can describe the whole structure each call while MC places
 * only the slice belonging to the active chunk. Reads/writes stay within the
 * worldgen region — no cascading chunk loads, no main-thread spike.
 */
public class BoundedStructureBuilder implements StructureBuilder {
    private final StructureWorldAccess world;
    private final BlockBox box;

    public BoundedStructureBuilder(StructureWorldAccess world, BlockBox box) {
        this.world = world;
        this.box = box;
    }

    @Override
    public boolean setBlockState(BlockPos pos, BlockState state, int flags) {
        if (!box.contains(pos)) return false;
        return world.setBlockState(pos, state, flags);
    }

    @Override
    public BlockState getBlockState(BlockPos pos) {
        return world.getBlockState(pos);
    }

    @Override
    public BlockEntity getBlockEntity(BlockPos pos) {
        return box.contains(pos) ? world.getBlockEntity(pos) : null;
    }

    @Override
    public Random getRandom() {
        return world.getRandom();
    }

    @Override
    public RegistryKey<World> getRegistryKey() {
        return world.toServerWorld().getRegistryKey();
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

    @Override public int writeMinX() { return box.getMinX(); }
    @Override public int writeMaxX() { return box.getMaxX(); }
    @Override public int writeMinY() { return box.getMinY(); }
    @Override public int writeMaxY() { return box.getMaxY(); }
    @Override public int writeMinZ() { return box.getMinZ(); }
    @Override public int writeMaxZ() { return box.getMaxZ(); }
}
