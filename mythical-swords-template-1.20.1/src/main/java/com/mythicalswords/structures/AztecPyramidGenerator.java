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
 * Generator for Aztec Pyramid structure (Mesoamerican mythology — Quetzalcoatl)
 * A step pyramid with 5 tiers descending to a center altar.
 * ~35×35 footprint, 15 blocks tall.
 */
public class AztecPyramidGenerator {

    private static final int TIERS = 5;
    private static final int BASE_HALF = 17; // half-width of base tier

    public static void generate(StructureBuilder world, BlockPos pos) {
        clearArea(world, pos);
        buildPyramid(world, pos);
        buildStaircase(world, pos);
        buildSummitTemple(world, pos);
        placeQuetzalcoatlAltar(world, pos);
        placeLootChests(world, pos);
        addDecorations(world, pos);
    }

    private static void clearArea(StructureBuilder world, BlockPos pos) {
        // Only clear the summit temple area — the pyramid is solid and overwrites terrain
        int summitY = TIERS * 3;
        int half = BASE_HALF - (TIERS - 1) * 3;
        for (int x = -half; x <= half; x++) {
            for (int z = -half; z <= half; z++) {
                for (int y = 0; y <= 6; y++) {
                    BlockPos p = pos.add(x, summitY + y, z);
                    if (!world.getBlockState(p).isOf(Blocks.BEDROCK)) {
                        world.setBlockState(p, Blocks.AIR.getDefaultState(), 3);
                    }
                }
            }
        }
    }

    private static void buildPyramid(StructureBuilder world, BlockPos pos) {
        for (int tier = 0; tier < TIERS; tier++) {
            int half = BASE_HALF - tier * 3;
            int y = tier * 3;
            for (int x = -half; x <= half; x++) {
                for (int z = -half; z <= half; z++) {
                    // Outer shell of each tier (3 blocks tall)
                    for (int dy = 0; dy < 3; dy++) {
                        boolean isEdge = Math.abs(x) == half || Math.abs(z) == half;
                        boolean isTop = dy == 2;
                        if (isEdge || isTop) {
                            BlockPos bp = pos.add(x, y + dy, z);
                            if (isTop && !isEdge) {
                                world.setBlockState(bp, Blocks.SMOOTH_SANDSTONE.getDefaultState(), 3);
                            } else {
                                world.setBlockState(bp, Blocks.SANDSTONE.getDefaultState(), 3);
                            }
                        }
                    }
                }
            }
            // Chiseled sandstone accents on corners
            int[][] corners = {{-half, -half}, {half, -half}, {-half, half}, {half, half}};
            for (int[] c : corners) {
                for (int dy = 0; dy < 3; dy++) {
                    world.setBlockState(pos.add(c[0], y + dy, c[1]), Blocks.CHISELED_SANDSTONE.getDefaultState(), 3);
                }
            }
        }
    }

    private static void buildStaircase(StructureBuilder world, BlockPos pos) {
        // Central staircase on south face
        for (int tier = 0; tier < TIERS; tier++) {
            int half = BASE_HALF - tier * 3;
            int baseY = tier * 3;
            for (int dy = 0; dy < 3; dy++) {
                for (int x = -2; x <= 2; x++) {
                    BlockPos step = pos.add(x, baseY + dy, half - dy);
                    world.setBlockState(step, Blocks.SANDSTONE_STAIRS.getDefaultState(), 3);
                }
            }
        }
    }

    private static void buildSummitTemple(StructureBuilder world, BlockPos pos) {
        int summitY = TIERS * 3;
        int half = BASE_HALF - (TIERS - 1) * 3; // top tier half-width
        // Small temple on top
        for (int x = -3; x <= 3; x++) {
            for (int y = 0; y <= 4; y++) {
                world.setBlockState(pos.add(x, summitY + y, -half + 1), Blocks.SANDSTONE.getDefaultState(), 3);
            }
        }
        // Side walls
        for (int z = -half + 1; z <= -half + 4; z++) {
            for (int y = 0; y <= 4; y++) {
                world.setBlockState(pos.add(-3, summitY + y, z), Blocks.SANDSTONE.getDefaultState(), 3);
                world.setBlockState(pos.add(3, summitY + y, z), Blocks.SANDSTONE.getDefaultState(), 3);
            }
        }
        // Roof
        for (int x = -3; x <= 3; x++) {
            for (int z = -half + 1; z <= -half + 4; z++) {
                world.setBlockState(pos.add(x, summitY + 5, z), Blocks.CHISELED_SANDSTONE.getDefaultState(), 3);
            }
        }
        // Entrance opening
        for (int x = -1; x <= 1; x++) {
            for (int y = 0; y <= 3; y++) {
                world.setBlockState(pos.add(x, summitY + y, -half + 4), Blocks.AIR.getDefaultState(), 3);
            }
        }
    }

    private static void placeQuetzalcoatlAltar(StructureBuilder world, BlockPos pos) {
        int summitY = TIERS * 3;
        int half = BASE_HALF - (TIERS - 1) * 3;
        BlockPos altarBase = pos.add(0, summitY, -half + 2);
        // Platform
        for (int x = -1; x <= 1; x++) {
            for (int z = -1; z <= 1; z++) {
                world.setBlockState(altarBase.add(x, 0, z), Blocks.CHISELED_SANDSTONE.getDefaultState(), 3);
            }
        }
        BlockPos altarPos = altarBase.add(0, 1, 0);
        world.setBlockState(altarPos, ModBlocks.BOSS_ALTAR.getDefaultState(), 3);
        if (world.getBlockEntity(altarPos) instanceof BossAltarBlockEntity altar) {
            altar.setBossId("quetzalcoatl");
        }
    }

    private static void placeLootChests(StructureBuilder world, BlockPos pos) {
        // Chest inside base tier (hidden chamber)
        BlockPos c1 = pos.add(-5, 1, -5);
        world.setBlockState(c1, Blocks.CHEST.getDefaultState(), 3);
        // Hollow out a small room around it
        for (int x = -6; x <= -4; x++) {
            for (int z = -6; z <= -4; z++) {
                for (int y = 1; y <= 2; y++) {
                    world.setBlockState(pos.add(x, y, z), Blocks.AIR.getDefaultState(), 3);
                }
            }
        }
        world.setBlockState(c1, Blocks.CHEST.getDefaultState(), 3);
        if (world.getBlockEntity(c1) instanceof ChestBlockEntity chest) {
            chest.setLootTable(new net.minecraft.util.Identifier("mythicalswords", "chests/aztec"), world.getSeed() ^ chest.getPos().asLong());
            var rng = world.getRandom();
            int s = 0;
            if (rng.nextFloat() < 0.50f) if (false) chest.setStack(s++, new ItemStack(ModItems.JADE_IMPERIAL_INGOT, 1));
            if (rng.nextFloat() < 0.50f) if (false) chest.setStack(s++, new ItemStack(ModItems.OBSIDIANA_RITUAL_SHARD, 1 + rng.nextInt(2)));
            if (rng.nextFloat() < 0.30f) if (false) chest.setStack(s++, new ItemStack(ModItems.AGNIS_FLAME_CORE, 1));
            if (false) chest.setStack(s++, new ItemStack(Items.GOLD_INGOT, 4 + rng.nextInt(6)));
            if (false) chest.setStack(s++, new ItemStack(Items.EMERALD, 2 + rng.nextInt(4)));
        }

        // Chest 2 — opposite side
        BlockPos c2 = pos.add(5, 1, 5);
        for (int x = 4; x <= 6; x++) {
            for (int z = 4; z <= 6; z++) {
                for (int y = 1; y <= 2; y++) {
                    world.setBlockState(pos.add(x, y, z), Blocks.AIR.getDefaultState(), 3);
                }
            }
        }
        world.setBlockState(c2, Blocks.CHEST.getDefaultState(), 3);
        if (world.getBlockEntity(c2) instanceof ChestBlockEntity chest) {
            chest.setLootTable(new net.minecraft.util.Identifier("mythicalswords", "chests/aztec"), world.getSeed() ^ chest.getPos().asLong());
            var rng = world.getRandom();
            int s = 0;
            if (rng.nextFloat() < 0.40f) if (false) chest.setStack(s++, new ItemStack(ModItems.FILO_DE_PLUMA_DE_QUETZAL, 1));
            if (rng.nextFloat() < 0.35f) if (false) chest.setStack(s++, new ItemStack(ModItems.PALO_RITUAL, 1));
            if (false) chest.setStack(s++, new ItemStack(Items.GOLD_INGOT, 3 + rng.nextInt(5)));
            if (false) chest.setStack(s++, new ItemStack(Items.COOKED_BEEF, 4 + rng.nextInt(4)));
        }
    }

    private static void addDecorations(StructureBuilder world, BlockPos pos) {
        // Terracotta accents on base tier
        for (int i = 0; i < 4; i++) {
            int x = (i % 2 == 0) ? BASE_HALF - 1 : -(BASE_HALF - 1);
            int z = (i < 2) ? BASE_HALF - 1 : -(BASE_HALF - 1);
            world.setBlockState(pos.add(x, 3, z), Blocks.ORANGE_TERRACOTTA.getDefaultState(), 3);
        }
        // Torches along staircase
        for (int tier = 0; tier < TIERS; tier++) {
            int half = BASE_HALF - tier * 3;
            int y = tier * 3 + 1;
            world.setBlockState(pos.add(-3, y, half), Blocks.TORCH.getDefaultState(), 3);
            world.setBlockState(pos.add(3, y, half), Blocks.TORCH.getDefaultState(), 3);
        }
        // Jungle vines on sides
        for (int z = -BASE_HALF; z <= BASE_HALF; z += 5) {
            world.setBlockState(pos.add(BASE_HALF, 2, z), Blocks.VINE.getDefaultState(), 3);
            world.setBlockState(pos.add(-BASE_HALF, 2, z), Blocks.VINE.getDefaultState(), 3);
        }
    }
}
