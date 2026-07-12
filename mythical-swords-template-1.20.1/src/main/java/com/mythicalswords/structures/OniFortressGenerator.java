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
 * Generator for Oni Fortress structure (Japanese mythology — Oni Oscuro)
 * A demon fortress built with nether bricks and obsidian.
 * ~20×20 footprint, 12 blocks tall.
 */
public class OniFortressGenerator {

    public static void generate(StructureBuilder world, BlockPos pos) {
        clearArea(world, pos);
        buildFoundation(world, pos);
        buildWalls(world, pos);
        buildTowers(world, pos);
        buildInterior(world, pos);
        placeOniAltar(world, pos);
        placeLootChest(world, pos);
        addDecorations(world, pos);
    }

    private static void clearArea(StructureBuilder world, BlockPos pos) {
        for (int x = -10; x <= 10; x++) {
            for (int z = -10; z <= 10; z++) {
                for (int y = 1; y <= 13; y++) {
                    BlockPos p = pos.add(x, y, z);
                    if (!world.getBlockState(p).isOf(Blocks.BEDROCK)) {
                        world.setBlockState(p, Blocks.AIR.getDefaultState(), 3);
                    }
                }
            }
        }
    }

    private static void buildFoundation(StructureBuilder world, BlockPos pos) {
        for (int x = -10; x <= 10; x++) {
            for (int z = -10; z <= 10; z++) {
                world.setBlockState(pos.add(x, -1, z), Blocks.OBSIDIAN.getDefaultState(), 3);
                // Floor — polished blackstone
                world.setBlockState(pos.add(x, 0, z), Blocks.POLISHED_BLACKSTONE.getDefaultState(), 3);
            }
        }
    }

    private static void buildWalls(StructureBuilder world, BlockPos pos) {
        for (int x = -10; x <= 10; x++) {
            for (int y = 1; y <= 8; y++) {
                world.setBlockState(pos.add(x, y, -10), Blocks.NETHER_BRICKS.getDefaultState(), 3);
                world.setBlockState(pos.add(x, y, 10), Blocks.NETHER_BRICKS.getDefaultState(), 3);
            }
        }
        for (int z = -10; z <= 10; z++) {
            for (int y = 1; y <= 8; y++) {
                world.setBlockState(pos.add(10, y, z), Blocks.NETHER_BRICKS.getDefaultState(), 3);
                world.setBlockState(pos.add(-10, y, z), Blocks.NETHER_BRICKS.getDefaultState(), 3);
            }
        }
        // Entrance (south)
        for (int x = -2; x <= 2; x++) {
            for (int y = 1; y <= 4; y++) {
                world.setBlockState(pos.add(x, y, 10), Blocks.AIR.getDefaultState(), 3);
            }
        }
        // Battlements
        for (int x = -10; x <= 10; x++) {
            if (x % 2 == 0) {
                world.setBlockState(pos.add(x, 9, -10), Blocks.NETHER_BRICK_WALL.getDefaultState(), 3);
                world.setBlockState(pos.add(x, 9, 10), Blocks.NETHER_BRICK_WALL.getDefaultState(), 3);
            }
        }
        for (int z = -10; z <= 10; z++) {
            if (z % 2 == 0) {
                world.setBlockState(pos.add(10, 9, z), Blocks.NETHER_BRICK_WALL.getDefaultState(), 3);
                world.setBlockState(pos.add(-10, 9, z), Blocks.NETHER_BRICK_WALL.getDefaultState(), 3);
            }
        }
    }

    private static void buildTowers(StructureBuilder world, BlockPos pos) {
        BlockPos[] corners = {
            pos.add(-10, 0, -10), pos.add(10, 0, -10),
            pos.add(-10, 0, 10), pos.add(10, 0, 10)
        };
        for (BlockPos corner : corners) {
            for (int x = -1; x <= 1; x++) {
                for (int z = -1; z <= 1; z++) {
                    for (int y = 1; y <= 12; y++) {
                        if (x == 0 && z == 0 && y > 1 && y < 12) {
                            world.setBlockState(corner.add(x, y, z), Blocks.AIR.getDefaultState(), 3);
                        } else {
                            world.setBlockState(corner.add(x, y, z), Blocks.NETHER_BRICKS.getDefaultState(), 3);
                        }
                    }
                }
            }
            // Magma on top
            world.setBlockState(corner.add(0, 12, 0), Blocks.MAGMA_BLOCK.getDefaultState(), 3);
        }
    }

    private static void buildInterior(StructureBuilder world, BlockPos pos) {
        // Central pillar ring
        for (int i = 0; i < 4; i++) {
            int x = (i % 2 == 0) ? 5 : -5;
            int z = (i < 2) ? 5 : -5;
            for (int y = 1; y <= 6; y++) {
                world.setBlockState(pos.add(x, y, z), Blocks.CRYING_OBSIDIAN.getDefaultState(), 3);
            }
        }
    }

    private static void placeOniAltar(StructureBuilder world, BlockPos pos) {
        BlockPos altarBase = pos;
        for (int x = -2; x <= 2; x++) {
            for (int z = -2; z <= 2; z++) {
                if (Math.abs(x) + Math.abs(z) <= 2) {
                    world.setBlockState(altarBase.add(x, 1, z), Blocks.POLISHED_BLACKSTONE_BRICKS.getDefaultState(), 3);
                }
            }
        }
        BlockPos altarPos = altarBase.add(0, 2, 0);
        world.setBlockState(altarPos, ModBlocks.BOSS_ALTAR.getDefaultState(), 3);
        if (world.getBlockEntity(altarPos) instanceof BossAltarBlockEntity altar) {
            altar.setBossId("oni_oscuro");
        }
        // Fire around altar
        world.setBlockState(altarBase.add(2, 2, 0), Blocks.NETHERRACK.getDefaultState(), 3);
        world.setBlockState(altarBase.add(2, 3, 0), Blocks.FIRE.getDefaultState(), 3);
        world.setBlockState(altarBase.add(-2, 2, 0), Blocks.NETHERRACK.getDefaultState(), 3);
        world.setBlockState(altarBase.add(-2, 3, 0), Blocks.FIRE.getDefaultState(), 3);
        world.setBlockState(altarBase.add(0, 2, 2), Blocks.NETHERRACK.getDefaultState(), 3);
        world.setBlockState(altarBase.add(0, 3, 2), Blocks.FIRE.getDefaultState(), 3);
        world.setBlockState(altarBase.add(0, 2, -2), Blocks.NETHERRACK.getDefaultState(), 3);
        world.setBlockState(altarBase.add(0, 3, -2), Blocks.FIRE.getDefaultState(), 3);
    }

    private static void placeLootChest(StructureBuilder world, BlockPos pos) {
        BlockPos c = pos.add(7, 1, -7);
        world.setBlockState(c, Blocks.CHEST.getDefaultState(), 3);
        if (world.getBlockEntity(c) instanceof ChestBlockEntity chest) {
            chest.setLootTable(new net.minecraft.util.Identifier("mythicalswords", "chests/oni"), world.getSeed() ^ chest.getPos().asLong());
            var rng = world.getRandom();
            int s = 0;
            if (rng.nextFloat() < 0.40f) if (false) chest.setStack(s++, new ItemStack(ModItems.SOUL_SWORDSMITH, 1));
            if (false) chest.setStack(s++, new ItemStack(ModItems.TAMAHAGANE_INGOT, 1 + rng.nextInt(2)));
            if (false) chest.setStack(s++, new ItemStack(Items.GOLD_INGOT, 2 + rng.nextInt(4)));
            if (false) chest.setStack(s++, new ItemStack(Items.BLAZE_ROD, 1 + rng.nextInt(3)));
            if (rng.nextFloat() < 0.25f) if (false) chest.setStack(s++, new ItemStack(ModItems.MANGO_LARGO_JAPONES, 1));
        }
    }

    private static void addDecorations(StructureBuilder world, BlockPos pos) {
        // Soul torches
        world.setBlockState(pos.add(-8, 2, -8), Blocks.SOUL_TORCH.getDefaultState(), 3);
        world.setBlockState(pos.add(8, 2, -8), Blocks.SOUL_TORCH.getDefaultState(), 3);
        world.setBlockState(pos.add(-8, 2, 8), Blocks.SOUL_TORCH.getDefaultState(), 3);
        world.setBlockState(pos.add(8, 2, 8), Blocks.SOUL_TORCH.getDefaultState(), 3);
        // Magma blocks for atmosphere
        world.setBlockState(pos.add(-3, 0, -3), Blocks.MAGMA_BLOCK.getDefaultState(), 3);
        world.setBlockState(pos.add(3, 0, -3), Blocks.MAGMA_BLOCK.getDefaultState(), 3);
        world.setBlockState(pos.add(-3, 0, 3), Blocks.MAGMA_BLOCK.getDefaultState(), 3);
        world.setBlockState(pos.add(3, 0, 3), Blocks.MAGMA_BLOCK.getDefaultState(), 3);
        // Banners
        world.setBlockState(pos.add(-2, 3, -9), Blocks.RED_BANNER.getDefaultState(), 3);
        world.setBlockState(pos.add(2, 3, -9), Blocks.RED_BANNER.getDefaultState(), 3);
    }
}
