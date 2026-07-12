package com.mythicalswords.core;

import com.mythicalswords.MythicalSwords;
import com.mythicalswords.blocks.BossAltarBlock;
import com.mythicalswords.blocks.MythicalForgeBlock;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;

public class ModBlocks {

    // Mythril Ore block
    public static final Block MYTHRIL_ORE = registerBlock("mythril_ore",
            new MythrilOreBlock(FabricBlockSettings.create()
                    .strength(45.0f, 30.0f) // Hardness: 45 (slightly less than obsidian's 50), Resistance: 30
                    .requiresTool()
                    .sounds(BlockSoundGroup.STONE)));

    // Northsteel Ore block - found in cold biomes
    public static final Block NORTHSTEEL_ORE = registerBlock("northsteel_ore",
            new NorthsteelOreBlock(FabricBlockSettings.create()
                    .strength(3.5f, 3.5f) // Slightly harder than iron ore
                    .requiresTool()
                    .sounds(BlockSoundGroup.STONE)));
    
    // Sacred Iron Ore block - Greek mythology ore
    public static final Block SACRED_IRON_ORE = registerBlock("sacred_iron_ore",
            new com.mythicalswords.blocks.SacredIronOreBlock());
    
    // Tamahagane Ore block - Japanese mythology ore
    public static final Block TAMAHAGANE_ORE = registerBlock("tamahagane_ore",
            new TamahaganeOreBlock(FabricBlockSettings.create()
                    .strength(3.5f, 3.5f) // Similar to iron ore
                    .requiresTool()
                    .sounds(BlockSoundGroup.STONE)));
    
    // Obsidiana Ritual Ore block - Mesoamerican mythology ore
    public static final Block OBSIDIANA_RITUAL_ORE = registerBlock("obsidiana_ritual_ore",
            new ObsidianaRitualOreBlock(FabricBlockSettings.create()
                    .strength(4.0f, 4.0f) // Slightly harder than iron ore
                    .requiresTool()
                    .sounds(BlockSoundGroup.STONE)));
    
    // Jade Imperial Ore block - Chinese mythology ore
    public static final Block JADE_IMPERIAL_ORE = registerBlock("jade_imperial_ore",
            new JadeImperialOreBlock(FabricBlockSettings.create()
                    .strength(3.5f, 3.5f) // Similar to iron ore
                    .requiresTool()
                    .sounds(BlockSoundGroup.STONE)));
    
    // ===== New minerals (v0.5) =====
    public static final Block ORICHALCUM_ORE = registerBlock("orichalcum_ore",
            new net.minecraft.block.ExperienceDroppingBlock(FabricBlockSettings.create()
                    .strength(4.0f, 4.0f).requiresTool().sounds(BlockSoundGroup.STONE),
                    net.minecraft.util.math.intprovider.UniformIntProvider.create(3, 7)));

    public static final Block URU_ORE = registerBlock("uru_ore",
            new net.minecraft.block.ExperienceDroppingBlock(FabricBlockSettings.create()
                    .strength(4.5f, 4.5f).requiresTool().sounds(BlockSoundGroup.STONE),
                    net.minecraft.util.math.intprovider.UniformIntProvider.create(3, 7)));

    public static final Block VOIDSTEEL_ORE = registerBlock("voidsteel_ore",
            new net.minecraft.block.ExperienceDroppingBlock(FabricBlockSettings.create()
                    .strength(5.0f, 6.0f).requiresTool().sounds(BlockSoundGroup.STONE),
                    net.minecraft.util.math.intprovider.UniformIntProvider.create(4, 9)));

    public static final Block FROSTSTEEL_ORE = registerBlock("froststeel_ore",
            new net.minecraft.block.ExperienceDroppingBlock(FabricBlockSettings.create()
                    .strength(3.5f, 3.5f).requiresTool().sounds(BlockSoundGroup.STONE),
                    net.minecraft.util.math.intprovider.UniformIntProvider.create(2, 5)));

    // Mythical Forge - Phase 4 crafting station
    public static final Block MYTHICAL_FORGE = registerBlock("mythical_forge",
            new MythicalForgeBlock(FabricBlockSettings.create()
                    .strength(8.0f, 1200.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.ANVIL)
                    .nonOpaque() // 3D model is not a full cube; avoids neighbor face culling holes
                    .luminance(state -> 7)));

    // Boss Altar - NBT-driven boss summoning (each structure sets its own boss ID)
    public static final Block BOSS_ALTAR = registerBlock("boss_altar",
            new BossAltarBlock(FabricBlockSettings.create()
                    .strength(50.0f, 1200.0f) // Very hard, explosion resistant
                    .requiresTool()
                    .sounds(BlockSoundGroup.STONE)
                    .nonOpaque() // 3D pedestal model is not a full cube
                    .luminance(state -> 7))); // Emits light

    /**
     * Helper method to register a block and its corresponding item
     */
    private static Block registerBlock(String name, Block block) {
        registerBlockItem(name, block);
        return Registry.register(Registries.BLOCK, new Identifier(MythicalSwords.MOD_ID, name), block);
    }

    /**
     * Helper method to register a block item
     */
    private static Item registerBlockItem(String name, Block block) {
        return Registry.register(Registries.ITEM, new Identifier(MythicalSwords.MOD_ID, name),
                new BlockItem(block, new FabricItemSettings()));
    }

    /**
     * Initialize and register all blocks
     */
    public static void register() {
        MythicalSwords.LOGGER.info("Registering blocks for " + MythicalSwords.MOD_ID);
    }
}
