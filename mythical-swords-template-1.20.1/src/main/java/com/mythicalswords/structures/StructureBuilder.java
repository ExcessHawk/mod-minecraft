package com.mythicalswords.structures;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

/**
 * Minimal write surface used by structure generators. Implemented over the
 * worldgen {@code StructureWorldAccess} via {@link BoundedStructureBuilder},
 * which restricts writes to the chunk currently being generated. This lets the
 * generators run inside Minecraft's async chunk-generation pipeline instead of
 * dumping thousands of block updates on the server thread at chunk-load time.
 *
 * <p>Method signatures mirror {@code StructureWorldAccess} so the existing
 * generator code compiles unchanged after swapping the parameter type.
 */
public interface StructureBuilder {
    boolean setBlockState(BlockPos pos, BlockState state, int flags);

    BlockState getBlockState(BlockPos pos);

    BlockEntity getBlockEntity(BlockPos pos);

    Random getRandom();

    RegistryKey<World> getRegistryKey();

    int getTopY();

    int getBottomY();

    long getSeed();

    // Writable region bounds (inclusive). Generators clip their loops to these
    // so a 500-wide structure only iterates the slice in the current chunk.
    int writeMinX();

    int writeMaxX();

    int writeMinY();

    int writeMaxY();

    int writeMinZ();

    int writeMaxZ();
}
