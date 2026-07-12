package com.mythicalswords.structures;

import com.mythicalswords.MythicalSwords;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.structure.StructurePieceType;
import net.minecraft.util.Identifier;
import net.minecraft.world.gen.structure.StructureType;

/**
 * Registers the custom {@link StructureType} and {@link StructurePieceType}.
 * Must run during mod init, before any datapack worldgen entry referencing
 * {@code mythicalswords:mythical} is loaded.
 */
public class ModStructureTypes {
    public static StructureType<MythicalStructure> MYTHICAL;
    public static StructurePieceType MYTHICAL_PIECE;

    public static void register() {
        MYTHICAL_PIECE = Registry.register(
            Registries.STRUCTURE_PIECE,
            new Identifier(MythicalSwords.MOD_ID, "mythical_piece"),
            MythicalStructurePiece::new);

        MYTHICAL = Registry.register(
            Registries.STRUCTURE_TYPE,
            new Identifier(MythicalSwords.MOD_ID, "mythical"),
            () -> MythicalStructure.CODEC);

        MythicalSwords.LOGGER.info("Registered structure types for " + MythicalSwords.MOD_ID);
    }
}
