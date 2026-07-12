package com.mythicalswords.structures;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.structure.StructureContext;
import net.minecraft.structure.StructurePiece;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.ChunkGenerator;

/**
 * A single piece covering the whole footprint of a mythical structure.
 * MC calls {@link #generate} once per chunk that intersects the bounding box;
 * each call places only the blocks inside that chunk via
 * {@link BoundedStructureBuilder}, so generation is spread across the async
 * chunk pipeline instead of one main-thread burst.
 */
public class MythicalStructurePiece extends StructurePiece {
    private final String variant;
    private final BlockPos center;

    public MythicalStructurePiece(String variant, BlockPos center) {
        super(ModStructureTypes.MYTHICAL_PIECE, 0, makeBox(variant, center));
        this.variant = variant;
        this.center = center;
    }

    public MythicalStructurePiece(StructureContext context, NbtCompound nbt) {
        super(ModStructureTypes.MYTHICAL_PIECE, nbt);
        this.variant = nbt.getString("Variant");
        this.center = new BlockPos(nbt.getInt("CX"), nbt.getInt("CY"), nbt.getInt("CZ"));
    }

    private static BlockBox makeBox(String variant, BlockPos c) {
        // All structures are compact now (Camelot was simplified to match).
        int half = 30;
        int down = 12;
        int up = 50;
        return new BlockBox(
            c.getX() - half, c.getY() - down, c.getZ() - half,
            c.getX() + half, c.getY() + up, c.getZ() + half);
    }

    @Override
    protected void writeNbt(StructureContext context, NbtCompound nbt) {
        nbt.putString("Variant", variant);
        nbt.putInt("CX", center.getX());
        nbt.putInt("CY", center.getY());
        nbt.putInt("CZ", center.getZ());
    }

    @Override
    public void generate(StructureWorldAccess world, StructureAccessor structureAccessor,
                         ChunkGenerator chunkGenerator, Random random, BlockBox chunkBox,
                         ChunkPos chunkPos, BlockPos pivot) {
        StructureBuilder builder = new BoundedStructureBuilder(world, chunkBox);
        StructureVariants.generate(variant, builder, center);
    }
}
