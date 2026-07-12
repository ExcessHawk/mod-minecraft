package com.mythicalswords.structures;

import com.mythicalswords.core.ModBlocks;
import com.mythicalswords.core.ModItems;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.BlockPos;

/**
 * Simple generator for Arthuran Castle structure
 * Creates a basic castle with throne room and altar
 */
public class ArthuranCastleGenerator {
    
    /**
     * Generate the castle at the given position
     * @param world The world to generate in
     * @param pos The center position for the castle
     */
    public static void generate(StructureBuilder world, BlockPos pos) {
        // Simple compact castle (the old 143-district CamelotBlueprint was huge
        // and unreliable to generate; use the lightweight build instead).
        clearArea(world, pos);
        buildFoundation(world, pos);
        buildWalls(world, pos);
        buildTowers(world, pos);
        buildThroneRoom(world, pos);
        addDecorations(world, pos);

        // Place the boss altar at the keep center to summon Rey Arturo
        placeBossAltar(world, pos);
    }
    
    /**
     * Clear only the interior area where the castle will be built (reduced for performance)
     */
    private static void clearArea(StructureBuilder world, BlockPos pos) {
        for (int x = -12; x <= 12; x++) {
            for (int z = -12; z <= 12; z++) {
                for (int y = 1; y <= 14; y++) {
                    BlockPos clearPos = pos.add(x, y, z);
                    if (!world.getBlockState(clearPos).isOf(Blocks.BEDROCK)) {
                        world.setBlockState(clearPos, Blocks.AIR.getDefaultState(), 3);
                    }
                }
            }
        }
    }
    
    /**
     * Build the stone foundation
     */
    private static void buildFoundation(StructureBuilder world, BlockPos pos) {
        // Main platform
        for (int x = -12; x <= 12; x++) {
            for (int z = -12; z <= 12; z++) {
                BlockPos foundationPos = pos.add(x, -1, z);
                world.setBlockState(foundationPos, Blocks.STONE_BRICKS.getDefaultState(), 3);
            }
        }
        
        // Floor
        for (int x = -11; x <= 11; x++) {
            for (int z = -11; z <= 11; z++) {
                BlockPos floorPos = pos.add(x, 0, z);
                // Checkered pattern
                if ((x + z) % 2 == 0) {
                    world.setBlockState(floorPos, Blocks.POLISHED_ANDESITE.getDefaultState(), 3);
                } else {
                    world.setBlockState(floorPos, Blocks.CHISELED_STONE_BRICKS.getDefaultState(), 3);
                }
            }
        }
    }
    
    /**
     * Build the outer walls
     */
    private static void buildWalls(StructureBuilder world, BlockPos pos) {
        // North and South walls
        for (int x = -11; x <= 11; x++) {
            for (int y = 1; y <= 8; y++) {
                // North wall
                BlockPos northPos = pos.add(x, y, -11);
                world.setBlockState(northPos, Blocks.STONE_BRICKS.getDefaultState(), 3);
                
                // South wall
                BlockPos southPos = pos.add(x, y, 11);
                world.setBlockState(southPos, Blocks.STONE_BRICKS.getDefaultState(), 3);
            }
        }
        
        // East and West walls
        for (int z = -11; z <= 11; z++) {
            for (int y = 1; y <= 8; y++) {
                // East wall
                BlockPos eastPos = pos.add(11, y, z);
                world.setBlockState(eastPos, Blocks.STONE_BRICKS.getDefaultState(), 3);
                
                // West wall
                BlockPos westPos = pos.add(-11, y, z);
                world.setBlockState(westPos, Blocks.STONE_BRICKS.getDefaultState(), 3);
            }
        }
        
        // Add entrance (South side)
        for (int x = -2; x <= 2; x++) {
            for (int y = 1; y <= 4; y++) {
                BlockPos entrancePos = pos.add(x, y, 11);
                world.setBlockState(entrancePos, Blocks.AIR.getDefaultState(), 3);
            }
        }
        
        // Add windows
        addWindows(world, pos);
    }
    
    /**
     * Add windows to the walls
     */
    private static void addWindows(StructureBuilder world, BlockPos pos) {
        // North wall windows
        placeWindow(world, pos.add(-6, 4, -11));
        placeWindow(world, pos.add(6, 4, -11));
        
        // East wall windows
        placeWindow(world, pos.add(11, 4, -6));
        placeWindow(world, pos.add(11, 4, 6));
        
        // West wall windows
        placeWindow(world, pos.add(-11, 4, -6));
        placeWindow(world, pos.add(-11, 4, 6));
    }
    
    /**
     * Place a single window
     */
    private static void placeWindow(StructureBuilder world, BlockPos pos) {
        world.setBlockState(pos, Blocks.GLASS_PANE.getDefaultState(), 3);
        world.setBlockState(pos.up(), Blocks.GLASS_PANE.getDefaultState(), 3);
    }
    
    /**
     * Build corner towers
     */
    private static void buildTowers(StructureBuilder world, BlockPos pos) {
        // Four corner towers
        buildTower(world, pos.add(-11, 0, -11)); // Northwest
        buildTower(world, pos.add(11, 0, -11));  // Northeast
        buildTower(world, pos.add(-11, 0, 11));  // Southwest
        buildTower(world, pos.add(11, 0, 11));   // Southeast
    }
    
    /**
     * Build a single tower
     */
    private static void buildTower(StructureBuilder world, BlockPos base) {
        // Tower body (3x3)
        for (int x = -1; x <= 1; x++) {
            for (int z = -1; z <= 1; z++) {
                for (int y = 0; y <= 12; y++) {
                    BlockPos towerPos = base.add(x, y, z);
                    // Hollow interior
                    if (x == 0 && z == 0 && y > 0 && y < 12) {
                        world.setBlockState(towerPos, Blocks.AIR.getDefaultState(), 3);
                    } else {
                        world.setBlockState(towerPos, Blocks.STONE_BRICKS.getDefaultState(), 3);
                    }
                }
            }
        }
        
        // Battlements on top
        for (int x = -1; x <= 1; x++) {
            for (int z = -1; z <= 1; z++) {
                if ((x + z) % 2 == 0) {
                    BlockPos battlement = base.add(x, 13, z);
                    world.setBlockState(battlement, Blocks.STONE_BRICK_WALL.getDefaultState(), 3);
                }
            }
        }
        
        // Torch on top
        world.setBlockState(base.add(0, 13, 0), Blocks.TORCH.getDefaultState(), 3);
    }
    
    /**
     * Build the throne room in the center
     */
    private static void buildThroneRoom(StructureBuilder world, BlockPos pos) {
        // Throne platform (North side)
        for (int x = -3; x <= 3; x++) {
            for (int z = -9; z <= -7; z++) {
                BlockPos platformPos = pos.add(x, 1, z);
                world.setBlockState(platformPos, Blocks.QUARTZ_BLOCK.getDefaultState(), 3);
            }
        }
        
        // Throne (simple chair)
        BlockPos thronePos = pos.add(0, 2, -8);
        world.setBlockState(thronePos, Blocks.QUARTZ_STAIRS.getDefaultState(), 3);
        world.setBlockState(thronePos.add(0, 0, -1), Blocks.QUARTZ_BLOCK.getDefaultState(), 3);
        
        // Throne back
        world.setBlockState(thronePos.add(0, 1, -1), Blocks.QUARTZ_PILLAR.getDefaultState(), 3);
        world.setBlockState(thronePos.add(0, 2, -1), Blocks.QUARTZ_PILLAR.getDefaultState(), 3);
        
        // Armrests
        world.setBlockState(thronePos.add(1, 1, 0), Blocks.QUARTZ_SLAB.getDefaultState(), 3);
        world.setBlockState(thronePos.add(-1, 1, 0), Blocks.QUARTZ_SLAB.getDefaultState(), 3);
    }
    
    /**
     * Place the boss summoning altar
     */
    private static void placeBossAltar(StructureBuilder world, BlockPos pos) {
        // Altar platform in center
        BlockPos altarBase = pos.add(0, 0, 0);
        
        // Platform
        for (int x = -2; x <= 2; x++) {
            for (int z = -2; z <= 2; z++) {
                if (Math.abs(x) + Math.abs(z) <= 2) {
                    BlockPos platformPos = altarBase.add(x, 1, z);
                    world.setBlockState(platformPos, Blocks.CHISELED_QUARTZ_BLOCK.getDefaultState(), 3);
                }
            }
        }
        
        // The actual altar block
        BlockPos altarPos = altarBase.add(0, 2, 0);
        world.setBlockState(altarPos, ModBlocks.BOSS_ALTAR.getDefaultState(), 3);

        // Tag this altar to summon Rey Arturo
        if (world.getBlockEntity(altarPos) instanceof com.mythicalswords.blocks.BossAltarBlockEntity altar) {
            altar.setBossId("rey_arturo");
        }
        
        // Decorative pillars around altar
        for (int i = 0; i < 4; i++) {
            int x = (i % 2 == 0) ? 2 : -2;
            int z = (i < 2) ? 2 : -2;
            BlockPos pillarPos = altarBase.add(x, 2, z);
            world.setBlockState(pillarPos, Blocks.QUARTZ_PILLAR.getDefaultState(), 3);
            world.setBlockState(pillarPos.up(), Blocks.QUARTZ_PILLAR.getDefaultState(), 3);
            world.setBlockState(pillarPos.up(2), Blocks.LANTERN.getDefaultState(), 3);
        }
    }
    
    /**
     * Add decorative elements
     */
    private static void addDecorations(StructureBuilder world, BlockPos pos) {
        // Torches along walls
        placeTorch(world, pos.add(-9, 2, -9));
        placeTorch(world, pos.add(9, 2, -9));
        placeTorch(world, pos.add(-9, 2, 9));
        placeTorch(world, pos.add(9, 2, 9));
        placeTorch(world, pos.add(0, 2, -9));
        placeTorch(world, pos.add(-9, 2, 0));
        placeTorch(world, pos.add(9, 2, 0));
        
        // Banners near throne
        world.setBlockState(pos.add(-2, 2, -7), Blocks.BLUE_BANNER.getDefaultState(), 3);
        world.setBlockState(pos.add(2, 2, -7), Blocks.BLUE_BANNER.getDefaultState(), 3);
        
        // Carpet leading to throne
        for (int z = 3; z <= 9; z++) {
            world.setBlockState(pos.add(0, 1, -z), Blocks.RED_CARPET.getDefaultState(), 3);
        }
        
        // Chest with loot near entrance
        BlockPos chestPos1 = pos.add(3, 1, 8);
        world.setBlockState(chestPos1, Blocks.CHEST.getDefaultState(), 3);
        if (world.getBlockEntity(chestPos1) instanceof ChestBlockEntity chest) {
            chest.setLootTable(new net.minecraft.util.Identifier("mythicalswords", "chests/arthurian"), world.getSeed() ^ chest.getPos().asLong());
            var rng = world.getRandom();
            int slot = 0;
            if (false) chest.setStack(slot++, new ItemStack(Items.GOLD_INGOT, 3 + rng.nextInt(4)));
            if (false) chest.setStack(slot++, new ItemStack(Items.IRON_INGOT, 2 + rng.nextInt(3)));
            if (false) chest.setStack(slot++, new ItemStack(Items.BREAD, 3 + rng.nextInt(3)));
            if (rng.nextFloat() < 0.30f) {
                if (false) chest.setStack(slot++, new ItemStack(ModItems.MYTHRIL_INGOT, 1));
            }
            if (rng.nextFloat() < 0.40f) {
                if (false) chest.setStack(slot++, new ItemStack(ModItems.SACRED_IRON_INGOT, 1 + rng.nextInt(2)));
            }
            if (rng.nextFloat() < 0.20f) {
                if (false) chest.setStack(slot++, new ItemStack(Items.ENCHANTED_BOOK, 1));
            }
        }

        BlockPos chestPos2 = pos.add(-3, 1, 8);
        world.setBlockState(chestPos2, Blocks.CHEST.getDefaultState(), 3);
        if (world.getBlockEntity(chestPos2) instanceof ChestBlockEntity chest) {
            chest.setLootTable(new net.minecraft.util.Identifier("mythicalswords", "chests/arthurian"), world.getSeed() ^ chest.getPos().asLong());
            var rng = world.getRandom();
            int slot = 0;
            if (false) chest.setStack(slot++, new ItemStack(Items.GOLD_INGOT, 2 + rng.nextInt(5)));
            if (false) chest.setStack(slot++, new ItemStack(Items.NAME_TAG, 1));
            if (rng.nextFloat() < 0.10f) {
                if (false) chest.setStack(slot++, new ItemStack(ModItems.MYTHRIL_INGOT, 1));
            }
            if (false) chest.setStack(slot++, new ItemStack(Items.IRON_INGOT, 3 + rng.nextInt(3)));
            if (rng.nextFloat() < 0.25f) {
                if (false) chest.setStack(slot++, new ItemStack(Items.GOLDEN_APPLE, 1));
            }
        }

        // Chest 3 — beside the throne (treasure of the king)
        BlockPos chestPos3 = pos.add(4, 1, -8);
        world.setBlockState(chestPos3, Blocks.CHEST.getDefaultState(), 3);
        if (world.getBlockEntity(chestPos3) instanceof ChestBlockEntity chest) {
            chest.setLootTable(new net.minecraft.util.Identifier("mythicalswords", "chests/arthurian"), world.getSeed() ^ chest.getPos().asLong());
        }
    }
    
    /**
     * Place a torch on a wall
     */
    private static void placeTorch(StructureBuilder world, BlockPos pos) {
        world.setBlockState(pos, Blocks.WALL_TORCH.getDefaultState(), 3);
    }
}
