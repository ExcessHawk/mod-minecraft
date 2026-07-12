package com.mythicalswords.structures;

import net.minecraft.util.math.BlockPos;

/**
 * Dispatches a structure variant id to its block-placement generator.
 * The variant string comes from the {@code variant} field of the structure
 * JSON ({@code data/mythicalswords/worldgen/structure/<variant>.json}).
 */
public final class StructureVariants {
    private StructureVariants() {}

    public static void generate(String variant, StructureBuilder world, BlockPos pos) {
        switch (variant) {
            case ModStructures.ARTHURIAN -> ArthuranCastleGenerator.generate(world, pos);
            case ModStructures.VALHALLA  -> ValhallaHallGenerator.generate(world, pos);
            case ModStructures.TRICKSTER -> TrickstersCaveGenerator.generate(world, pos);
            case ModStructures.GREEK     -> GreekTempleGenerator.generate(world, pos);
            case ModStructures.BAMBOO    -> BambooTempleGenerator.generate(world, pos);
            case ModStructures.ONI       -> OniFortressGenerator.generate(world, pos);
            case ModStructures.AZTEC     -> AztecPyramidGenerator.generate(world, pos);
            case ModStructures.DESERT    -> DesertTombGenerator.generate(world, pos);
            case ModStructures.CELESTIAL -> CelestialPalaceGenerator.generate(world, pos);
            default -> {}
        }
    }
}
