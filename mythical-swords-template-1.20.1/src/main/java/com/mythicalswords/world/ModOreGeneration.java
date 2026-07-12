package com.mythicalswords.world;

import com.mythicalswords.MythicalSwords;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.feature.PlacedFeature;

/**
 * Handles ore generation in the world
 */
public class ModOreGeneration {
    
    public static final RegistryKey<PlacedFeature> MYTHRIL_ORE_PLACED = RegistryKey.of(
        RegistryKeys.PLACED_FEATURE,
        new Identifier(MythicalSwords.MOD_ID, "mythril_ore")
    );
    
    public static final RegistryKey<PlacedFeature> NORTHSTEEL_ORE_PLACED = RegistryKey.of(
        RegistryKeys.PLACED_FEATURE,
        new Identifier(MythicalSwords.MOD_ID, "northsteel_ore")
    );
    
    public static final RegistryKey<PlacedFeature> SACRED_IRON_ORE_PLACED = RegistryKey.of(
        RegistryKeys.PLACED_FEATURE,
        new Identifier(MythicalSwords.MOD_ID, "sacred_iron_ore")
    );
    
    public static final RegistryKey<PlacedFeature> TAMAHAGANE_ORE_PLACED = RegistryKey.of(
        RegistryKeys.PLACED_FEATURE,
        new Identifier(MythicalSwords.MOD_ID, "tamahagane_ore")
    );
    
    public static final RegistryKey<PlacedFeature> OBSIDIANA_RITUAL_ORE_PLACED = RegistryKey.of(
        RegistryKeys.PLACED_FEATURE,
        new Identifier(MythicalSwords.MOD_ID, "obsidiana_ritual_ore")
    );
    
    public static final RegistryKey<PlacedFeature> JADE_IMPERIAL_ORE_PLACED = RegistryKey.of(
        RegistryKeys.PLACED_FEATURE,
        new Identifier(MythicalSwords.MOD_ID, "jade_imperial_ore")
    );

    public static final RegistryKey<PlacedFeature> ORICHALCUM_ORE_PLACED = RegistryKey.of(
        RegistryKeys.PLACED_FEATURE, new Identifier(MythicalSwords.MOD_ID, "orichalcum_ore"));
    public static final RegistryKey<PlacedFeature> URU_ORE_PLACED = RegistryKey.of(
        RegistryKeys.PLACED_FEATURE, new Identifier(MythicalSwords.MOD_ID, "uru_ore"));
    public static final RegistryKey<PlacedFeature> VOIDSTEEL_ORE_PLACED = RegistryKey.of(
        RegistryKeys.PLACED_FEATURE, new Identifier(MythicalSwords.MOD_ID, "voidsteel_ore"));
    public static final RegistryKey<PlacedFeature> FROSTSTEEL_ORE_PLACED = RegistryKey.of(
        RegistryKeys.PLACED_FEATURE, new Identifier(MythicalSwords.MOD_ID, "froststeel_ore"));

    /**
     * Register ore generation features
     */
    public static void register() {
        // Add Mythril Ore to overworld generation
        BiomeModifications.addFeature(
            BiomeSelectors.foundInOverworld(),
            GenerationStep.Feature.UNDERGROUND_ORES,
            MYTHRIL_ORE_PLACED
        );
        
        // Add Northsteel Ore to cold biomes (Taiga, Snowy Plains, etc.)
        BiomeModifications.addFeature(
            BiomeSelectors.foundInOverworld(),
            GenerationStep.Feature.UNDERGROUND_ORES,
            NORTHSTEEL_ORE_PLACED
        );
        
        // Add Sacred Iron Ore to overworld generation
        BiomeModifications.addFeature(
            BiomeSelectors.foundInOverworld(),
            GenerationStep.Feature.UNDERGROUND_ORES,
            SACRED_IRON_ORE_PLACED
        );
        
        // Add Tamahagane Ore to overworld generation
        BiomeModifications.addFeature(
            BiomeSelectors.foundInOverworld(),
            GenerationStep.Feature.UNDERGROUND_ORES,
            TAMAHAGANE_ORE_PLACED
        );
        
        // Add Obsidiana Ritual Ore to jungle biomes (Mesoamerican)
        BiomeModifications.addFeature(
            BiomeSelectors.foundInOverworld(),
            GenerationStep.Feature.UNDERGROUND_ORES,
            OBSIDIANA_RITUAL_ORE_PLACED
        );
        
        // Add Jade Imperial Ore to overworld generation (Chinese)
        BiomeModifications.addFeature(
            BiomeSelectors.foundInOverworld(),
            GenerationStep.Feature.UNDERGROUND_ORES,
            JADE_IMPERIAL_ORE_PLACED
        );

        BiomeModifications.addFeature(BiomeSelectors.foundInOverworld(),
            GenerationStep.Feature.UNDERGROUND_ORES, ORICHALCUM_ORE_PLACED);
        BiomeModifications.addFeature(BiomeSelectors.foundInOverworld(),
            GenerationStep.Feature.UNDERGROUND_ORES, URU_ORE_PLACED);
        BiomeModifications.addFeature(BiomeSelectors.foundInOverworld(),
            GenerationStep.Feature.UNDERGROUND_ORES, VOIDSTEEL_ORE_PLACED);
        BiomeModifications.addFeature(BiomeSelectors.foundInOverworld(),
            GenerationStep.Feature.UNDERGROUND_ORES, FROSTSTEEL_ORE_PLACED);

        MythicalSwords.LOGGER.info("Registered ore generation for " + MythicalSwords.MOD_ID);
    }
}
