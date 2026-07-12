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
 * Generator for Celestial Palace structure (Chinese mythology — Sun Wukong)
 * A pagoda-style tiered palace on an elevated platform.
 * ~25×25 footprint, 18 blocks tall.
 */
public class CelestialPalaceGenerator {

    public static void generate(StructureBuilder world, BlockPos pos) {
        clearArea(world, pos);
        buildElevatedPlatform(world, pos);
        buildPagoda(world, pos);
        buildStaircase(world, pos);
        placeSunWukongAltar(world, pos);
        placeLootChests(world, pos);
        addDecorations(world, pos);
    }

    private static void clearArea(StructureBuilder world, BlockPos pos) {
        // Only clear the pagoda interior — the platform and walls overwrite terrain
        for (int x = -9; x <= 9; x++) {
            for (int z = -9; z <= 9; z++) {
                for (int y = 4; y <= 20; y++) {
                    BlockPos p = pos.add(x, y, z);
                    if (!world.getBlockState(p).isOf(Blocks.BEDROCK)) {
                        world.setBlockState(p, Blocks.AIR.getDefaultState(), 3);
                    }
                }
            }
        }
    }

    private static void buildElevatedPlatform(StructureBuilder world, BlockPos pos) {
        // Raised platform (3 blocks high)
        for (int x = -13; x <= 13; x++) {
            for (int z = -13; z <= 13; z++) {
                for (int y = -1; y <= 2; y++) {
                    world.setBlockState(pos.add(x, y, z), Blocks.QUARTZ_BLOCK.getDefaultState(), 3);
                }
            }
        }
        // Floor on top of platform
        for (int x = -12; x <= 12; x++) {
            for (int z = -12; z <= 12; z++) {
                if ((x + z) % 2 == 0) {
                    world.setBlockState(pos.add(x, 3, z), Blocks.QUARTZ_BLOCK.getDefaultState(), 3);
                } else {
                    world.setBlockState(pos.add(x, 3, z), Blocks.PURPUR_BLOCK.getDefaultState(), 3);
                }
            }
        }
    }

    private static void buildPagoda(StructureBuilder world, BlockPos pos) {
        int baseY = 4; // above the platform
        // 3 tiers of pagoda
        int[][] tiers = {
            {-8, 8, 6},   // tier 1: half-width 8, height 6
            {-5, 5, 5},   // tier 2: half-width 5, height 5
            {-3, 3, 4},   // tier 3: half-width 3, height 4
        };

        int currentY = baseY;
        for (int[] tier : tiers) {
            int halfMin = tier[0];
            int halfMax = tier[1];
            int tierHeight = tier[2];

            // Walls
            for (int y = 0; y < tierHeight; y++) {
                for (int x = halfMin; x <= halfMax; x++) {
                    world.setBlockState(pos.add(x, currentY + y, halfMin), Blocks.END_STONE_BRICKS.getDefaultState(), 3);
                    world.setBlockState(pos.add(x, currentY + y, halfMax), Blocks.END_STONE_BRICKS.getDefaultState(), 3);
                }
                for (int z = halfMin; z <= halfMax; z++) {
                    world.setBlockState(pos.add(halfMin, currentY + y, z), Blocks.END_STONE_BRICKS.getDefaultState(), 3);
                    world.setBlockState(pos.add(halfMax, currentY + y, z), Blocks.END_STONE_BRICKS.getDefaultState(), 3);
                }
            }

            // Entrance on south wall
            for (int x = -1; x <= 1; x++) {
                for (int y = 0; y <= Math.min(3, tierHeight - 1); y++) {
                    world.setBlockState(pos.add(x, currentY + y, halfMax), Blocks.AIR.getDefaultState(), 3);
                }
            }

            // Roof overhang
            int roofHalf = halfMax + 1;
            for (int x = -roofHalf; x <= roofHalf; x++) {
                for (int z = -roofHalf; z <= roofHalf; z++) {
                    world.setBlockState(pos.add(x, currentY + tierHeight, z), Blocks.PURPUR_SLAB.getDefaultState(), 3);
                }
            }

            currentY += tierHeight + 1;
        }

        // Spire on top
        world.setBlockState(pos.add(0, currentY, 0), Blocks.GOLD_BLOCK.getDefaultState(), 3);
        world.setBlockState(pos.add(0, currentY + 1, 0), Blocks.GOLD_BLOCK.getDefaultState(), 3);
        world.setBlockState(pos.add(0, currentY + 2, 0), Blocks.LIGHTNING_ROD.getDefaultState(), 3);
    }

    private static void buildStaircase(StructureBuilder world, BlockPos pos) {
        // Grand staircase from ground to platform (south side)
        for (int step = 0; step < 4; step++) {
            for (int x = -3; x <= 3; x++) {
                world.setBlockState(pos.add(x, step, 13 + (3 - step)), Blocks.QUARTZ_STAIRS.getDefaultState(), 3);
            }
        }
    }

    private static void placeSunWukongAltar(StructureBuilder world, BlockPos pos) {
        // Altar on the ground floor of the pagoda
        BlockPos altarBase = pos.add(0, 3, 0);
        for (int x = -1; x <= 1; x++) {
            for (int z = -1; z <= 1; z++) {
                world.setBlockState(altarBase.add(x, 1, z), Blocks.GOLD_BLOCK.getDefaultState(), 3);
            }
        }
        BlockPos altarPos = altarBase.add(0, 2, 0);
        world.setBlockState(altarPos, ModBlocks.BOSS_ALTAR.getDefaultState(), 3);
        if (world.getBlockEntity(altarPos) instanceof BossAltarBlockEntity altar) {
            altar.setBossId("sun_wukong");
        }
        // Lanterns
        world.setBlockState(altarBase.add(-2, 2, 0), Blocks.LANTERN.getDefaultState(), 3);
        world.setBlockState(altarBase.add(2, 2, 0), Blocks.LANTERN.getDefaultState(), 3);
        world.setBlockState(altarBase.add(0, 2, -2), Blocks.LANTERN.getDefaultState(), 3);
        world.setBlockState(altarBase.add(0, 2, 2), Blocks.LANTERN.getDefaultState(), 3);
    }

    private static void placeLootChests(StructureBuilder world, BlockPos pos) {
        // Chest 1 — ground floor
        BlockPos c1 = pos.add(-6, 4, -6);
        world.setBlockState(c1, Blocks.CHEST.getDefaultState(), 3);
        if (world.getBlockEntity(c1) instanceof ChestBlockEntity chest) {
            chest.setLootTable(new net.minecraft.util.Identifier("mythicalswords", "chests/celestial"), world.getSeed() ^ chest.getPos().asLong());
            var rng = world.getRandom();
            int s = 0;
            if (rng.nextFloat() < 0.40f) if (false) chest.setStack(s++, new ItemStack(ModItems.DUST_OF_LONGEVITY, 1));
            if (rng.nextFloat() < 0.35f) if (false) chest.setStack(s++, new ItemStack(ModItems.ESSENCE_OF_RIGHTEOUSNESS, 1));
            if (false) chest.setStack(s++, new ItemStack(Items.GOLD_BLOCK, 1 + rng.nextInt(2)));
            if (false) chest.setStack(s++, new ItemStack(Items.EXPERIENCE_BOTTLE, 5 + rng.nextInt(8)));
        }

        // Chest 2 — ground floor opposite
        BlockPos c2 = pos.add(6, 4, -6);
        world.setBlockState(c2, Blocks.CHEST.getDefaultState(), 3);
        if (world.getBlockEntity(c2) instanceof ChestBlockEntity chest) {
            chest.setLootTable(new net.minecraft.util.Identifier("mythicalswords", "chests/celestial"), world.getSeed() ^ chest.getPos().asLong());
            var rng = world.getRandom();
            int s = 0;
            if (rng.nextFloat() < 0.40f) if (false) chest.setStack(s++, new ItemStack(ModItems.DRAGON_FANG_FRAGMENT, 1));
            if (rng.nextFloat() < 0.30f) if (false) chest.setStack(s++, new ItemStack(ModItems.JADE_IMPERIAL_INGOT, 1));
            if (false) chest.setStack(s++, new ItemStack(Items.GOLD_INGOT, 4 + rng.nextInt(6)));
            if (false) chest.setStack(s++, new ItemStack(Items.GOLDEN_APPLE, 1 + rng.nextInt(2)));
        }
    }

    private static void addDecorations(StructureBuilder world, BlockPos pos) {
        // Cloud blocks around the platform edges (white wool)
        for (int x = -13; x <= 13; x += 4) {
            world.setBlockState(pos.add(x, 3, -13), Blocks.WHITE_WOOL.getDefaultState(), 3);
            world.setBlockState(pos.add(x, 3, 13), Blocks.WHITE_WOOL.getDefaultState(), 3);
        }
        for (int z = -13; z <= 13; z += 4) {
            world.setBlockState(pos.add(-13, 3, z), Blocks.WHITE_WOOL.getDefaultState(), 3);
            world.setBlockState(pos.add(13, 3, z), Blocks.WHITE_WOOL.getDefaultState(), 3);
        }
        // Gold accents at entrance
        world.setBlockState(pos.add(-2, 4, 8), Blocks.GOLD_BLOCK.getDefaultState(), 3);
        world.setBlockState(pos.add(2, 4, 8), Blocks.GOLD_BLOCK.getDefaultState(), 3);
        // Banners
        world.setBlockState(pos.add(-3, 5, 8), Blocks.RED_BANNER.getDefaultState(), 3);
        world.setBlockState(pos.add(3, 5, 8), Blocks.YELLOW_BANNER.getDefaultState(), 3);
        // Interior lanterns
        world.setBlockState(pos.add(-6, 7, 0), Blocks.LANTERN.getDefaultState(), 3);
        world.setBlockState(pos.add(6, 7, 0), Blocks.LANTERN.getDefaultState(), 3);
        world.setBlockState(pos.add(0, 7, -6), Blocks.LANTERN.getDefaultState(), 3);
    }
}
