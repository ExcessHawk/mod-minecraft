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
 * Generator for Desert Tomb structure (Egyptian mythology — Anubis + Ra)
 * An Egyptian pyramid with inner chambers and two altars.
 * ~40×40 footprint, 20 blocks tall.
 * Lower chamber for Anubis, rooftop shrine for Ra.
 */
public class DesertTombGenerator {

    private static final int BASE_HALF = 20;
    private static final int HEIGHT = 20;

    public static void generate(StructureBuilder world, BlockPos pos) {
        clearArea(world, pos);
        buildPyramidShell(world, pos);
        buildLowerChamber(world, pos);
        buildUpperShrine(world, pos);
        placeAnubisAltar(world, pos);
        placeRaAltar(world, pos);
        placeLootChests(world, pos);
        addDecorations(world, pos);
    }

    private static void clearArea(StructureBuilder world, BlockPos pos) {
        // Only clear the lower chamber and upper shrine — the pyramid shell overwrites terrain
        // Lower chamber
        for (int x = -9; x <= 9; x++) {
            for (int z = -9; z <= 9; z++) {
                for (int y = -3; y <= 5; y++) {
                    BlockPos p = pos.add(x, y, z);
                    if (!world.getBlockState(p).isOf(Blocks.BEDROCK)) {
                        world.setBlockState(p, Blocks.AIR.getDefaultState(), 3);
                    }
                }
            }
        }
    }

    private static void buildPyramidShell(StructureBuilder world, BlockPos pos) {
        // Solid pyramid with smooth sandstone shell
        for (int y = 0; y < HEIGHT; y++) {
            int half = BASE_HALF - y;
            if (half < 0) break;
            for (int x = -half; x <= half; x++) {
                for (int z = -half; z <= half; z++) {
                    boolean isEdge = Math.abs(x) == half || Math.abs(z) == half;
                    boolean isFloor = y == 0;
                    if (isEdge || isFloor) {
                        world.setBlockState(pos.add(x, y, z), Blocks.SMOOTH_SANDSTONE.getDefaultState(), 3);
                    } else {
                        world.setBlockState(pos.add(x, y, z), Blocks.SANDSTONE.getDefaultState(), 3);
                    }
                }
            }
        }
        // Capstone
        world.setBlockState(pos.add(0, HEIGHT, 0), Blocks.GOLD_BLOCK.getDefaultState(), 3);
    }

    private static void buildLowerChamber(StructureBuilder world, BlockPos pos) {
        // Underground chamber for Anubis (below the pyramid)
        for (int x = -8; x <= 8; x++) {
            for (int z = -8; z <= 8; z++) {
                for (int y = -3; y <= 4; y++) {
                    boolean isWall = Math.abs(x) == 8 || Math.abs(z) == 8 || y == -3;
                    if (isWall) {
                        world.setBlockState(pos.add(x, y, z), Blocks.CUT_SANDSTONE.getDefaultState(), 3);
                    } else {
                        world.setBlockState(pos.add(x, y, z), Blocks.AIR.getDefaultState(), 3);
                    }
                }
            }
        }
        // Floor
        for (int x = -7; x <= 7; x++) {
            for (int z = -7; z <= 7; z++) {
                world.setBlockState(pos.add(x, -2, z), Blocks.SMOOTH_SANDSTONE.getDefaultState(), 3);
            }
        }
        // Entrance tunnel from south
        for (int z = 8; z <= BASE_HALF; z++) {
            for (int x = -2; x <= 2; x++) {
                for (int y = -1; y <= 2; y++) {
                    world.setBlockState(pos.add(x, y, z), Blocks.AIR.getDefaultState(), 3);
                }
                // Tunnel floor
                world.setBlockState(pos.add(x, -2, z), Blocks.SMOOTH_SANDSTONE.getDefaultState(), 3);
            }
        }
        // Staircase down from ground level
        for (int step = 0; step < 4; step++) {
            for (int x = -2; x <= 2; x++) {
                world.setBlockState(pos.add(x, -step, BASE_HALF + step), Blocks.SANDSTONE_STAIRS.getDefaultState(), 3);
            }
        }
        // Hieroglyph pillars inside chamber
        BlockPos[] pillars = {
            pos.add(-5, -1, -5), pos.add(5, -1, -5),
            pos.add(-5, -1, 5), pos.add(5, -1, 5)
        };
        for (BlockPos p : pillars) {
            for (int y = 0; y < 5; y++) {
                world.setBlockState(p.up(y), Blocks.CHISELED_SANDSTONE.getDefaultState(), 3);
            }
        }
    }

    private static void buildUpperShrine(StructureBuilder world, BlockPos pos) {
        // Small open shrine at the top of the pyramid for Ra
        int shrineY = HEIGHT - 3;
        for (int x = -3; x <= 3; x++) {
            for (int z = -3; z <= 3; z++) {
                // Floor
                world.setBlockState(pos.add(x, shrineY, z), Blocks.GOLD_BLOCK.getDefaultState(), 3);
                // Clear space above
                for (int y = 1; y <= 3; y++) {
                    world.setBlockState(pos.add(x, shrineY + y, z), Blocks.AIR.getDefaultState(), 3);
                }
            }
        }
        // Corner pillars
        int[][] corners = {{-3, -3}, {3, -3}, {-3, 3}, {3, 3}};
        for (int[] c : corners) {
            for (int y = 1; y <= 3; y++) {
                world.setBlockState(pos.add(c[0], shrineY + y, c[1]), Blocks.CHISELED_SANDSTONE.getDefaultState(), 3);
            }
            world.setBlockState(pos.add(c[0], shrineY + 4, c[1]), Blocks.GLOWSTONE.getDefaultState(), 3);
        }
    }

    private static void placeAnubisAltar(StructureBuilder world, BlockPos pos) {
        // In the lower chamber center
        BlockPos altarBase = pos.add(0, -2, 0);
        for (int x = -1; x <= 1; x++) {
            for (int z = -1; z <= 1; z++) {
                world.setBlockState(altarBase.add(x, 0, z), Blocks.CUT_SANDSTONE.getDefaultState(), 3);
            }
        }
        BlockPos altarPos = altarBase.add(0, 1, 0);
        world.setBlockState(altarPos, ModBlocks.BOSS_ALTAR.getDefaultState(), 3);
        if (world.getBlockEntity(altarPos) instanceof BossAltarBlockEntity altar) {
            altar.setBossId("anubis");
        }
        // Soul lanterns
        world.setBlockState(altarBase.add(-2, 1, 0), Blocks.SOUL_LANTERN.getDefaultState(), 3);
        world.setBlockState(altarBase.add(2, 1, 0), Blocks.SOUL_LANTERN.getDefaultState(), 3);
    }

    private static void placeRaAltar(StructureBuilder world, BlockPos pos) {
        // On the upper shrine
        int shrineY = HEIGHT - 3;
        BlockPos altarPos = pos.add(0, shrineY + 1, 0);
        world.setBlockState(altarPos, ModBlocks.BOSS_ALTAR.getDefaultState(), 3);
        if (world.getBlockEntity(altarPos) instanceof BossAltarBlockEntity altar) {
            altar.setBossId("ra");
        }
        // Glowstone around Ra's altar (sun theme)
        world.setBlockState(pos.add(-1, shrineY + 1, 0), Blocks.GLOWSTONE.getDefaultState(), 3);
        world.setBlockState(pos.add(1, shrineY + 1, 0), Blocks.GLOWSTONE.getDefaultState(), 3);
        world.setBlockState(pos.add(0, shrineY + 1, -1), Blocks.GLOWSTONE.getDefaultState(), 3);
        world.setBlockState(pos.add(0, shrineY + 1, 1), Blocks.GLOWSTONE.getDefaultState(), 3);
    }

    private static void placeLootChests(StructureBuilder world, BlockPos pos) {
        // Chest 1 — lower chamber east
        BlockPos c1 = pos.add(6, -1, -6);
        world.setBlockState(c1, Blocks.CHEST.getDefaultState(), 3);
        if (world.getBlockEntity(c1) instanceof ChestBlockEntity chest) {
            chest.setLootTable(new net.minecraft.util.Identifier("mythicalswords", "chests/desert"), world.getSeed() ^ chest.getPos().asLong());
            var rng = world.getRandom();
            int s = 0;
            if (false) chest.setStack(s++, new ItemStack(Items.GOLD_INGOT, 5 + rng.nextInt(8)));
            if (false) chest.setStack(s++, new ItemStack(Items.LAPIS_LAZULI, 4 + rng.nextInt(8)));
            if (rng.nextFloat() < 0.40f) if (false) chest.setStack(s++, new ItemStack(ModItems.SUN_BLESSED_ALLOY, 1));
            if (rng.nextFloat() < 0.30f) if (false) chest.setStack(s++, new ItemStack(Items.ENCHANTED_BOOK, 1));
        }

        // Chest 2 — lower chamber west
        BlockPos c2 = pos.add(-6, -1, 6);
        world.setBlockState(c2, Blocks.CHEST.getDefaultState(), 3);
        if (world.getBlockEntity(c2) instanceof ChestBlockEntity chest) {
            chest.setLootTable(new net.minecraft.util.Identifier("mythicalswords", "chests/desert"), world.getSeed() ^ chest.getPos().asLong());
            var rng = world.getRandom();
            int s = 0;
            if (false) chest.setStack(s++, new ItemStack(Items.GOLD_BLOCK, 1 + rng.nextInt(2)));
            if (false) chest.setStack(s++, new ItemStack(Items.LAPIS_LAZULI, 6 + rng.nextInt(10)));
            if (rng.nextFloat() < 0.35f) if (false) chest.setStack(s++, new ItemStack(ModItems.DRAGON_FANG_FRAGMENT, 1));
        }

        // Chest 3 — near entrance
        BlockPos c3 = pos.add(3, -1, 6);
        world.setBlockState(c3, Blocks.CHEST.getDefaultState(), 3);
        if (world.getBlockEntity(c3) instanceof ChestBlockEntity chest) {
            chest.setLootTable(new net.minecraft.util.Identifier("mythicalswords", "chests/desert"), world.getSeed() ^ chest.getPos().asLong());
            var rng = world.getRandom();
            int s = 0;
            if (false) chest.setStack(s++, new ItemStack(Items.GOLDEN_APPLE, 1));
            if (false) chest.setStack(s++, new ItemStack(Items.IRON_INGOT, 4 + rng.nextInt(5)));
            if (false) chest.setStack(s++, new ItemStack(Items.EXPERIENCE_BOTTLE, 3 + rng.nextInt(5)));
        }
    }

    private static void addDecorations(StructureBuilder world, BlockPos pos) {
        // Torches in lower chamber
        world.setBlockState(pos.add(-7, 0, -7), Blocks.TORCH.getDefaultState(), 3);
        world.setBlockState(pos.add(7, 0, -7), Blocks.TORCH.getDefaultState(), 3);
        world.setBlockState(pos.add(-7, 0, 7), Blocks.TORCH.getDefaultState(), 3);
        world.setBlockState(pos.add(7, 0, 7), Blocks.TORCH.getDefaultState(), 3);
        // Cobwebs in corners (ancient tomb feel)
        world.setBlockState(pos.add(-7, 2, -7), Blocks.COBWEB.getDefaultState(), 3);
        world.setBlockState(pos.add(7, 2, 7), Blocks.COBWEB.getDefaultState(), 3);
        // Suspicious sand near entrance
        world.setBlockState(pos.add(-1, -1, 7), Blocks.SUSPICIOUS_SAND.getDefaultState(), 3);
        world.setBlockState(pos.add(1, -1, 7), Blocks.SUSPICIOUS_SAND.getDefaultState(), 3);
    }
}
