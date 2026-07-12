package com.mythicalswords.structures;

import com.mythicalswords.blocks.BossAltarBlockEntity;
import com.mythicalswords.core.ModBlocks;
import com.mythicalswords.core.ModItems;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.BlockPos;

/**
 * Generator for Bamboo Temple structure (Japanese mythology — Susanoo + Izanagi)
 * A shrine/torii gate structure in jungle biomes.
 * ~30×20 footprint, 10 blocks tall.
 */
public class BambooTempleGenerator {

    public static void generate(StructureBuilder world, BlockPos pos) {
        clearArea(world, pos);
        buildFoundation(world, pos);
        buildToriiGate(world, pos);
        buildMainShrine(world, pos);
        placeSusanooAltar(world, pos);
        placeIzanagiAltar(world, pos);
        placeLootChests(world, pos);
        addDecorations(world, pos);
    }

    private static void clearArea(StructureBuilder world, BlockPos pos) {
        for (int x = -11; x <= 11; x++) {
            for (int z = -9; z <= 9; z++) {
                for (int y = 1; y <= 11; y++) {
                    BlockPos p = pos.add(x, y, z);
                    if (!world.getBlockState(p).isOf(Blocks.BEDROCK)) {
                        world.setBlockState(p, Blocks.AIR.getDefaultState(), 3);
                    }
                }
            }
        }
    }

    private static void buildFoundation(StructureBuilder world, BlockPos pos) {
        // Stone path leading to shrine
        for (int z = 5; z <= 11; z++) {
            for (int x = -2; x <= 2; x++) {
                world.setBlockState(pos.add(x, 0, z), Blocks.STONE_BRICKS.getDefaultState(), 3);
            }
        }
        // Main platform
        for (int x = -12; x <= 12; x++) {
            for (int z = -8; z <= 5; z++) {
                world.setBlockState(pos.add(x, -1, z), Blocks.STONE_BRICKS.getDefaultState(), 3);
                // Checkered floor
                if ((x + z) % 2 == 0) {
                    world.setBlockState(pos.add(x, 0, z), Blocks.CHERRY_PLANKS.getDefaultState(), 3);
                } else {
                    world.setBlockState(pos.add(x, 0, z), Blocks.JUNGLE_PLANKS.getDefaultState(), 3);
                }
            }
        }
    }

    private static void buildToriiGate(StructureBuilder world, BlockPos pos) {
        // Torii gate at entrance (south side)
        BlockPos gateCenter = pos.add(0, 0, 8);
        // Left pillar
        for (int y = 1; y <= 6; y++) {
            world.setBlockState(gateCenter.add(-3, y, 0), Blocks.CRIMSON_STEM.getDefaultState(), 3);
        }
        // Right pillar
        for (int y = 1; y <= 6; y++) {
            world.setBlockState(gateCenter.add(3, y, 0), Blocks.CRIMSON_STEM.getDefaultState(), 3);
        }
        // Top beam (kasagi)
        for (int x = -4; x <= 4; x++) {
            world.setBlockState(gateCenter.add(x, 7, 0), Blocks.CRIMSON_SLAB.getDefaultState(), 3);
        }
        // Lower beam (nuki)
        for (int x = -3; x <= 3; x++) {
            world.setBlockState(gateCenter.add(x, 5, 0), Blocks.CRIMSON_SLAB.getDefaultState(), 3);
        }
    }

    private static void buildMainShrine(StructureBuilder world, BlockPos pos) {
        // Walls — cherry wood frame with jungle planks
        for (int x = -10; x <= 10; x++) {
            for (int y = 1; y <= 7; y++) {
                world.setBlockState(pos.add(x, y, -8), Blocks.JUNGLE_PLANKS.getDefaultState(), 3);
                world.setBlockState(pos.add(x, y, 5), Blocks.JUNGLE_PLANKS.getDefaultState(), 3);
            }
        }
        for (int z = -8; z <= 5; z++) {
            for (int y = 1; y <= 7; y++) {
                world.setBlockState(pos.add(10, y, z), Blocks.JUNGLE_PLANKS.getDefaultState(), 3);
                world.setBlockState(pos.add(-10, y, z), Blocks.JUNGLE_PLANKS.getDefaultState(), 3);
            }
        }
        // Entrance opening (south wall)
        for (int x = -2; x <= 2; x++) {
            for (int y = 1; y <= 4; y++) {
                world.setBlockState(pos.add(x, y, 5), Blocks.AIR.getDefaultState(), 3);
            }
        }
        // Roof — tiered pagoda style
        for (int tier = 0; tier < 3; tier++) {
            int shrink = tier * 2;
            int roofY = 8 + tier;
            for (int x = -11 + shrink; x <= 11 - shrink; x++) {
                for (int z = -9 + shrink; z <= 6 - shrink; z++) {
                    world.setBlockState(pos.add(x, roofY, z), Blocks.CHERRY_PLANKS.getDefaultState(), 3);
                }
            }
        }
        // Windows
        for (int y = 3; y <= 5; y++) {
            world.setBlockState(pos.add(10, y, -2), Blocks.GLASS_PANE.getDefaultState(), 3);
            world.setBlockState(pos.add(-10, y, -2), Blocks.GLASS_PANE.getDefaultState(), 3);
        }
    }

    private static void placeSusanooAltar(StructureBuilder world, BlockPos pos) {
        // Primary altar — west side
        BlockPos altarBase = pos.add(-5, 0, -4);
        for (int x = -1; x <= 1; x++) {
            for (int z = -1; z <= 1; z++) {
                world.setBlockState(altarBase.add(x, 1, z), Blocks.CHISELED_STONE_BRICKS.getDefaultState(), 3);
            }
        }
        BlockPos altarPos = altarBase.add(0, 2, 0);
        world.setBlockState(altarPos, ModBlocks.BOSS_ALTAR.getDefaultState(), 3);
        if (world.getBlockEntity(altarPos) instanceof BossAltarBlockEntity altar) {
            altar.setBossId("susanoo");
        }
        // Lanterns
        world.setBlockState(altarBase.add(-2, 2, 0), Blocks.LANTERN.getDefaultState(), 3);
        world.setBlockState(altarBase.add(2, 2, 0), Blocks.LANTERN.getDefaultState(), 3);
    }

    private static void placeIzanagiAltar(StructureBuilder world, BlockPos pos) {
        // Secondary altar — east side
        BlockPos altarBase = pos.add(5, 0, -4);
        for (int x = -1; x <= 1; x++) {
            for (int z = -1; z <= 1; z++) {
                world.setBlockState(altarBase.add(x, 1, z), Blocks.CHISELED_STONE_BRICKS.getDefaultState(), 3);
            }
        }
        BlockPos altarPos = altarBase.add(0, 2, 0);
        world.setBlockState(altarPos, ModBlocks.BOSS_ALTAR.getDefaultState(), 3);
        if (world.getBlockEntity(altarPos) instanceof BossAltarBlockEntity altar) {
            altar.setBossId("izanagi");
        }
        world.setBlockState(altarBase.add(-2, 2, 0), Blocks.SOUL_LANTERN.getDefaultState(), 3);
        world.setBlockState(altarBase.add(2, 2, 0), Blocks.SOUL_LANTERN.getDefaultState(), 3);
    }

    private static void placeLootChests(StructureBuilder world, BlockPos pos) {
        // Chest 1 — near Susanoo altar
        BlockPos c1 = pos.add(-8, 1, -6);
        world.setBlockState(c1, Blocks.CHEST.getDefaultState(), 3);
        if (world.getBlockEntity(c1) instanceof ChestBlockEntity chest) {
            chest.setLootTable(new net.minecraft.util.Identifier("mythicalswords", "chests/bamboo"), world.getSeed() ^ chest.getPos().asLong());
            var rng = world.getRandom();
            int s = 0;
            if (false) chest.setStack(s++, new ItemStack(ModItems.TAMAHAGANE_INGOT, 1 + rng.nextInt(2)));
            if (rng.nextFloat() < 0.50f) if (false) chest.setStack(s++, new ItemStack(ModItems.GEM_OF_BISHAMON, 1));
            if (false) chest.setStack(s++, new ItemStack(Items.ARROW, 8 + rng.nextInt(16)));
            if (false) chest.setStack(s++, new ItemStack(Items.COOKED_BEEF, 3 + rng.nextInt(4)));
            if (rng.nextFloat() < 0.25f) if (false) chest.setStack(s++, new ItemStack(ModItems.MANGO_LARGO_JAPONES, 1));
        }
        // Chest 2 — near Izanagi altar
        BlockPos c2 = pos.add(8, 1, -6);
        world.setBlockState(c2, Blocks.CHEST.getDefaultState(), 3);
        if (world.getBlockEntity(c2) instanceof ChestBlockEntity chest) {
            chest.setLootTable(new net.minecraft.util.Identifier("mythicalswords", "chests/bamboo"), world.getSeed() ^ chest.getPos().asLong());
            var rng = world.getRandom();
            int s = 0;
            if (false) chest.setStack(s++, new ItemStack(ModItems.TAMAHAGANE_INGOT, 1 + rng.nextInt(2)));
            if (rng.nextFloat() < 0.30f) if (false) chest.setStack(s++, new ItemStack(ModItems.SACRED_WATER_OF_AMATERASU, 1));
            if (false) chest.setStack(s++, new ItemStack(Items.GOLD_INGOT, 3 + rng.nextInt(4)));
            if (rng.nextFloat() < 0.20f) if (false) chest.setStack(s++, new ItemStack(Items.ENCHANTED_BOOK, 1));
        }
    }

    private static void addDecorations(StructureBuilder world, BlockPos pos) {
        // Bamboo around the exterior
        BlockPos[] bambooSpots = {
            pos.add(-12, 1, -6), pos.add(-12, 1, 0), pos.add(-12, 1, 4),
            pos.add(12, 1, -6), pos.add(12, 1, 0), pos.add(12, 1, 4),
        };
        for (BlockPos bp : bambooSpots) {
            for (int y = 0; y < 3 + world.getRandom().nextInt(3); y++) {
                world.setBlockState(bp.up(y), Blocks.BAMBOO.getDefaultState(), 3);
            }
        }
        // Lanterns inside
        world.setBlockState(pos.add(-8, 4, -2), Blocks.LANTERN.getDefaultState(), 3);
        world.setBlockState(pos.add(8, 4, -2), Blocks.LANTERN.getDefaultState(), 3);
        world.setBlockState(pos.add(0, 4, -6), Blocks.LANTERN.getDefaultState(), 3);
        // Carpet path
        for (int z = 0; z <= 4; z++) {
            world.setBlockState(pos.add(0, 1, z), Blocks.RED_CARPET.getDefaultState(), 3);
        }
    }
}
