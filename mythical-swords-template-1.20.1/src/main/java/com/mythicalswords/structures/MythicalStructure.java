package com.mythicalswords.structures;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.Heightmap;
import net.minecraft.world.gen.structure.Structure;
import net.minecraft.world.gen.structure.StructureType;

import java.util.Optional;

/**
 * Code-defined structure integrated into the worldgen pipeline. Placement
 * (spacing/separation/biomes) is data-driven via the structure_set JSON; this
 * class only decides the surface position and emits a {@link MythicalStructurePiece}
 * carrying the {@code variant} that selects the block generator.
 */
public class MythicalStructure extends Structure {
    public static final Codec<MythicalStructure> CODEC = RecordCodecBuilder.create(instance ->
        instance.group(
            MythicalStructure.configCodecBuilder(instance),
            Codec.STRING.fieldOf("variant").forGetter(s -> s.variant)
        ).apply(instance, MythicalStructure::new));

    private final String variant;

    public MythicalStructure(Config config, String variant) {
        super(config);
        this.variant = variant;
    }

    @Override
    public Optional<StructurePosition> getStructurePosition(Context context) {
        ChunkPos chunkPos = context.chunkPos();
        int x = chunkPos.getCenterX();
        int z = chunkPos.getCenterZ();
        int y = context.chunkGenerator().getHeightOnGround(
            x, z, Heightmap.Type.WORLD_SURFACE_WG, context.world(), context.noiseConfig());
        BlockPos pos = new BlockPos(x, y, z);
        return Optional.of(new StructurePosition(pos, collector ->
            collector.addPiece(new MythicalStructurePiece(variant, pos))));
    }

    @Override
    public StructureType<?> getType() {
        return ModStructureTypes.MYTHICAL;
    }
}
