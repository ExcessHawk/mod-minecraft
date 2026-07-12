package com.mythicalswords.world;

import com.mythicalswords.MythicalSwords;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectionContext;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.BiomeTags;
import net.minecraft.util.Identifier;
import net.minecraft.world.biome.BiomeKeys;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.feature.PlacedFeature;

import java.util.List;
import java.util.function.Predicate;

/**
 * Ore generation, one declarative entry per ore. Each mineral spawns only in
 * biomes matching its mythology (heights/vein sizes live in the
 * worldgen/placed_feature JSONs).
 */
public class ModOreGeneration {

    private record OreEntry(String name, Predicate<BiomeSelectionContext> biomes) {
        RegistryKey<PlacedFeature> feature() {
            return RegistryKey.of(RegistryKeys.PLACED_FEATURE,
                    new Identifier(MythicalSwords.MOD_ID, name));
        }
    }

    private static final List<OreEntry> ORES = List.of(
        // arthurian — mountains
        new OreEntry("mythril_ore", BiomeSelectors.tag(BiomeTags.IS_MOUNTAIN)),
        // norse — cold taiga and snowy lands
        new OreEntry("northsteel_ore", BiomeSelectors.tag(BiomeTags.IS_TAIGA)
                .or(BiomeSelectors.includeByKey(BiomeKeys.SNOWY_PLAINS, BiomeKeys.SNOWY_TAIGA, BiomeKeys.ICE_SPIKES))),
        // greek — open plains and meadows
        new OreEntry("sacred_iron_ore", BiomeSelectors.includeByKey(
                BiomeKeys.PLAINS, BiomeKeys.SUNFLOWER_PLAINS, BiomeKeys.MEADOW, BiomeKeys.FLOWER_FOREST)),
        // japanese — cherry groves and dark forests
        new OreEntry("tamahagane_ore", BiomeSelectors.includeByKey(
                BiomeKeys.CHERRY_GROVE, BiomeKeys.DARK_FOREST)),
        // mesoamerican — badlands and sparse jungle
        new OreEntry("obsidiana_ritual_ore", BiomeSelectors.tag(BiomeTags.IS_BADLANDS)
                .or(BiomeSelectors.includeByKey(BiomeKeys.SPARSE_JUNGLE))),
        // chinese — jungle and bamboo
        new OreEntry("jade_imperial_ore", BiomeSelectors.includeByKey(
                BiomeKeys.JUNGLE, BiomeKeys.BAMBOO_JUNGLE)),
        // atlantean — deep ocean floors
        new OreEntry("orichalcum_ore", BiomeSelectors.tag(BiomeTags.IS_DEEP_OCEAN)),
        // uru — high frozen/jagged peaks
        new OreEntry("uru_ore", BiomeSelectors.includeByKey(
                BiomeKeys.JAGGED_PEAKS, BiomeKeys.FROZEN_PEAKS, BiomeKeys.STONY_PEAKS)),
        // voidsteel — deep cave biomes
        new OreEntry("voidsteel_ore", BiomeSelectors.includeByKey(
                BiomeKeys.DEEP_DARK, BiomeKeys.DRIPSTONE_CAVES)),
        // froststeel — ice biomes
        new OreEntry("froststeel_ore", BiomeSelectors.includeByKey(
                BiomeKeys.FROZEN_PEAKS, BiomeKeys.ICE_SPIKES, BiomeKeys.SNOWY_SLOPES, BiomeKeys.FROZEN_OCEAN))
    );

    /**
     * Register ore generation features
     */
    public static void register() {
        for (OreEntry ore : ORES) {
            BiomeModifications.addFeature(ore.biomes(),
                    GenerationStep.Feature.UNDERGROUND_ORES, ore.feature());
        }
        MythicalSwords.LOGGER.info("Registered biome-specific ore generation for {} ores", ORES.size());
    }
}
