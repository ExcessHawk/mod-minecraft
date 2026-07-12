package com.mythicalswords.structures;

import com.mythicalswords.core.ModBlocks;
import com.mythicalswords.core.ModItems;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.BlockPos;

/**
 * Generator for Valhalla Hall structure (Norse mythology)
 * Creates a grand hall with 4 Eternal Flame spawn points and Odín spawn trigger
 */
public class ValhallaHallGenerator {
    
    /**
     * Generate the Valhalla Hall at the given position
     * @param world The world to generate in
     * @param pos The center position for the hall
     */
    public static void generate(StructureBuilder world, BlockPos pos) {
        // Clear area first
        clearArea(world, pos);
        
        // Build foundation
        buildFoundation(world, pos);
        
        // Build walls
        buildWalls(world, pos);
        
        // Build roof
        buildRoof(world, pos);
        
        // Build pillars
        buildPillars(world, pos);
        
        // Place eternal flame spawn points
        placeEternalFlames(world, pos);
        
        // Place Odín spawn trigger (boss altar)
        placeOdinAltar(world, pos);
        
        // Add decorations
        addDecorations(world, pos);
    }
    
    /**
     * Clear only the interior area (reduced for performance)
     */
    private static void clearArea(StructureBuilder world, BlockPos pos) {
        for (int x = -17; x <= 17; x++) {
            for (int z = -11; z <= 11; z++) {
                for (int y = 1; y <= 10; y++) {
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
        for (int x = -17; x <= 17; x++) {
            for (int z = -11; z <= 11; z++) {
                BlockPos foundationPos = pos.add(x, -1, z);
                world.setBlockState(foundationPos, Blocks.COBBLESTONE.getDefaultState(), 3);
            }
        }
        
        // Floor (dark wood planks)
        for (int x = -16; x <= 16; x++) {
            for (int z = -10; z <= 10; z++) {
                BlockPos floorPos = pos.add(x, 0, z);
                world.setBlockState(floorPos, Blocks.DARK_OAK_PLANKS.getDefaultState(), 3);
            }
        }
    }
    
    /**
     * Build the outer walls
     */
    private static void buildWalls(StructureBuilder world, BlockPos pos) {
        // North and South walls (shorter sides)
        for (int x = -16; x <= 16; x++) {
            for (int y = 1; y <= 8; y++) {
                // North wall
                BlockPos northPos = pos.add(x, y, -10);
                world.setBlockState(northPos, Blocks.STONE_BRICKS.getDefaultState(), 3);
                
                // South wall
                BlockPos southPos = pos.add(x, y, 10);
                world.setBlockState(southPos, Blocks.STONE_BRICKS.getDefaultState(), 3);
            }
        }
        
        // East and West walls (longer sides)
        for (int z = -10; z <= 10; z++) {
            for (int y = 1; y <= 8; y++) {
                // East wall
                BlockPos eastPos = pos.add(16, y, z);
                world.setBlockState(eastPos, Blocks.STONE_BRICKS.getDefaultState(), 3);
                
                // West wall
                BlockPos westPos = pos.add(-16, y, z);
                world.setBlockState(westPos, Blocks.STONE_BRICKS.getDefaultState(), 3);
            }
        }
        
        // Add entrance (South side)
        for (int x = -3; x <= 3; x++) {
            for (int y = 1; y <= 5; y++) {
                BlockPos entrancePos = pos.add(x, y, 10);
                world.setBlockState(entrancePos, Blocks.AIR.getDefaultState(), 3);
            }
        }
        
        // Add windows with iron bars (Norse style)
        addWindows(world, pos);
    }
    
    /**
     * Add windows to the walls
     */
    private static void addWindows(StructureBuilder world, BlockPos pos) {
        // North wall windows
        placeWindow(world, pos.add(-10, 4, -10));
        placeWindow(world, pos.add(0, 4, -10));
        placeWindow(world, pos.add(10, 4, -10));
        
        // East wall windows
        placeWindow(world, pos.add(16, 4, -6));
        placeWindow(world, pos.add(16, 4, 0));
        placeWindow(world, pos.add(16, 4, 6));
        
        // West wall windows
        placeWindow(world, pos.add(-16, 4, -6));
        placeWindow(world, pos.add(-16, 4, 0));
        placeWindow(world, pos.add(-16, 4, 6));
    }
    
    /**
     * Place a single window (Norse style with iron bars)
     */
    private static void placeWindow(StructureBuilder world, BlockPos pos) {
        world.setBlockState(pos, Blocks.IRON_BARS.getDefaultState(), 3);
        world.setBlockState(pos.up(), Blocks.IRON_BARS.getDefaultState(), 3);
    }
    
    /**
     * Build the roof
     */
    private static void buildRoof(StructureBuilder world, BlockPos pos) {
        // Peaked roof with dark oak
        for (int x = -17; x <= 17; x++) {
            for (int z = -11; z <= 11; z++) {
                int distanceFromCenter = Math.abs(z);
                int roofHeight = 9 + (11 - distanceFromCenter) / 2;
                
                if (roofHeight > 9) {
                    BlockPos roofPos = pos.add(x, roofHeight, z);
                    world.setBlockState(roofPos, Blocks.DARK_OAK_PLANKS.getDefaultState(), 3);
                }
            }
        }
        
        // Ridge beam
        for (int x = -17; x <= 17; x++) {
            BlockPos ridgePos = pos.add(x, 14, 0);
            world.setBlockState(ridgePos, Blocks.DARK_OAK_LOG.getDefaultState(), 3);
        }
    }
    
    /**
     * Build interior pillars
     */
    private static void buildPillars(StructureBuilder world, BlockPos pos) {
        // Two rows of pillars along the hall
        int[] pillarXPositions = {-12, -6, 0, 6, 12};
        int[] pillarZPositions = {-6, 6};
        
        for (int x : pillarXPositions) {
            for (int z : pillarZPositions) {
                buildPillar(world, pos.add(x, 0, z));
            }
        }
    }
    
    /**
     * Build a single pillar
     */
    private static void buildPillar(StructureBuilder world, BlockPos base) {
        // Pillar base
        world.setBlockState(base.add(0, 1, 0), Blocks.CHISELED_STONE_BRICKS.getDefaultState(), 3);
        
        // Pillar body
        for (int y = 2; y <= 7; y++) {
            world.setBlockState(base.add(0, y, 0), Blocks.STONE_BRICK_WALL.getDefaultState(), 3);
        }
        
        // Pillar capital
        world.setBlockState(base.add(0, 8, 0), Blocks.CHISELED_STONE_BRICKS.getDefaultState(), 3);
    }
    
    /**
     * Place the 4 Eternal Flame spawn points
     * These are special braziers that must be lit to summon Odín
     */
    private static void placeEternalFlames(StructureBuilder world, BlockPos pos) {
        // Four corners of the hall interior
        BlockPos[] flamePositions = {
            pos.add(-14, 1, -8),  // Northwest
            pos.add(14, 1, -8),   // Northeast
            pos.add(-14, 1, 8),   // Southwest
            pos.add(14, 1, 8)     // Southeast
        };
        
        for (BlockPos flamePos : flamePositions) {
            // Brazier base (iron block)
            world.setBlockState(flamePos, Blocks.IRON_BLOCK.getDefaultState(), 3);
            
            // Brazier bowl (cauldron)
            world.setBlockState(flamePos.up(), Blocks.CAULDRON.getDefaultState(), 3);
            
            // Decorative blocks around brazier
            world.setBlockState(flamePos.add(1, 0, 0), Blocks.STONE_BRICKS.getDefaultState(), 3);
            world.setBlockState(flamePos.add(-1, 0, 0), Blocks.STONE_BRICKS.getDefaultState(), 3);
            world.setBlockState(flamePos.add(0, 0, 1), Blocks.STONE_BRICKS.getDefaultState(), 3);
            world.setBlockState(flamePos.add(0, 0, -1), Blocks.STONE_BRICKS.getDefaultState(), 3);
        }
    }
    
    /**
     * Place the Odín spawn trigger (boss altar)
     */
    private static void placeOdinAltar(StructureBuilder world, BlockPos pos) {
        // Altar at the north end of the hall
        BlockPos altarBase = pos.add(0, 0, -7);
        
        // Raised platform
        for (int x = -3; x <= 3; x++) {
            for (int z = -2; z <= 2; z++) {
                BlockPos platformPos = altarBase.add(x, 1, z);
                world.setBlockState(platformPos, Blocks.CHISELED_QUARTZ_BLOCK.getDefaultState(), 3);
            }
        }
        
        // The actual altar block (center)
        BlockPos altarPos = altarBase.add(0, 2, 0);
        world.setBlockState(altarPos, ModBlocks.BOSS_ALTAR.getDefaultState(), 3);

        // Tag this altar to summon Odín
        if (world.getBlockEntity(altarPos) instanceof com.mythicalswords.blocks.BossAltarBlockEntity altar) {
            altar.setBossId("odin");
        }
        
        // Throne behind altar
        BlockPos thronePos = altarBase.add(0, 2, -2);
        world.setBlockState(thronePos, Blocks.QUARTZ_STAIRS.getDefaultState(), 3);
        world.setBlockState(thronePos.add(0, 0, -1), Blocks.QUARTZ_BLOCK.getDefaultState(), 3);
        world.setBlockState(thronePos.add(0, 1, -1), Blocks.QUARTZ_PILLAR.getDefaultState(), 3);
        world.setBlockState(thronePos.add(0, 2, -1), Blocks.QUARTZ_PILLAR.getDefaultState(), 3);
        
        // Decorative pillars around altar
        for (int i = 0; i < 4; i++) {
            int x = (i % 2 == 0) ? 3 : -3;
            int z = (i < 2) ? 2 : -2;
            BlockPos pillarPos = altarBase.add(x, 2, z);
            world.setBlockState(pillarPos, Blocks.STONE_BRICK_WALL.getDefaultState(), 3);
            world.setBlockState(pillarPos.up(), Blocks.STONE_BRICK_WALL.getDefaultState(), 3);
            world.setBlockState(pillarPos.up(2), Blocks.LANTERN.getDefaultState(), 3);
        }
    }
    
    /**
     * Add decorative elements
     */
    private static void addDecorations(StructureBuilder world, BlockPos pos) {
        // Long tables along the sides (feast hall style)
        buildTable(world, pos.add(-10, 1, -3), 8, true);  // West table
        buildTable(world, pos.add(10, 1, -3), 8, true);   // East table
        
        // Torches along walls
        for (int x = -14; x <= 14; x += 4) {
            world.setBlockState(pos.add(x, 3, -9), Blocks.WALL_TORCH.getDefaultState(), 3);
            world.setBlockState(pos.add(x, 3, 9), Blocks.WALL_TORCH.getDefaultState(), 3);
        }
        
        // Banners (Norse style - gray and white)
        world.setBlockState(pos.add(-8, 3, -9), Blocks.GRAY_BANNER.getDefaultState(), 3);
        world.setBlockState(pos.add(8, 3, -9), Blocks.GRAY_BANNER.getDefaultState(), 3);
        world.setBlockState(pos.add(-4, 3, 9), Blocks.WHITE_BANNER.getDefaultState(), 3);
        world.setBlockState(pos.add(4, 3, 9), Blocks.WHITE_BANNER.getDefaultState(), 3);
        
        // Weapon racks (using item frames on walls)
        // Note: Armor stands are entities, not blocks, so we use decorative blocks instead
        world.setBlockState(pos.add(-15, 1, -8), Blocks.CHISELED_STONE_BRICKS.getDefaultState(), 3);
        world.setBlockState(pos.add(15, 1, -8), Blocks.CHISELED_STONE_BRICKS.getDefaultState(), 3);
        world.setBlockState(pos.add(-15, 1, 8), Blocks.CHISELED_STONE_BRICKS.getDefaultState(), 3);
        world.setBlockState(pos.add(15, 1, 8), Blocks.CHISELED_STONE_BRICKS.getDefaultState(), 3);
        
        // Loot chests
        BlockPos chestPos1 = pos.add(-13, 1, 9);
        world.setBlockState(chestPos1, Blocks.CHEST.getDefaultState(), 3);
        if (world.getBlockEntity(chestPos1) instanceof ChestBlockEntity chest) {
            chest.setLootTable(new net.minecraft.util.Identifier("mythicalswords", "chests/valhalla"), world.getSeed() ^ chest.getPos().asLong());
            var rng = world.getRandom();
            int slot = 0;
            if (false) chest.setStack(slot++, new ItemStack(ModItems.NORTHSTEEL_INGOT, 2 + rng.nextInt(3)));
            if (rng.nextFloat() < 0.40f) {
                if (false) chest.setStack(slot++, new ItemStack(ModItems.SPIRITBOUND_LEATHER, 1));
            }
            if (rng.nextFloat() < 0.25f) {
                if (false) chest.setStack(slot++, new ItemStack(ModItems.RAINBOW_BRIDGE_FRAGMENT, 1));
            }
            if (false) chest.setStack(slot++, new ItemStack(Items.IRON_INGOT, 3 + rng.nextInt(4)));
            if (rng.nextFloat() < 0.20f) {
                if (false) chest.setStack(slot++, new ItemStack(Items.ENCHANTED_BOOK, 1));
            }
        }

        BlockPos chestPos2 = pos.add(13, 1, 9);
        world.setBlockState(chestPos2, Blocks.CHEST.getDefaultState(), 3);
        if (world.getBlockEntity(chestPos2) instanceof ChestBlockEntity chest) {
            chest.setLootTable(new net.minecraft.util.Identifier("mythicalswords", "chests/valhalla"), world.getSeed() ^ chest.getPos().asLong());
            var rng = world.getRandom();
            int slot = 0;
            if (false) chest.setStack(slot++, new ItemStack(Items.GOLD_INGOT, 3 + rng.nextInt(5)));
            if (false) chest.setStack(slot++, new ItemStack(Items.COOKED_BEEF, 4 + rng.nextInt(4)));
            if (rng.nextFloat() < 0.15f) {
                if (false) chest.setStack(slot++, new ItemStack(ModItems.MYTHRIL_INGOT, 1));
            }
            if (false) chest.setStack(slot++, new ItemStack(ModItems.RAW_NORTHSTEEL, 2 + rng.nextInt(3)));
        }
    }
    
    /**
     * Build a long table
     */
    private static void buildTable(StructureBuilder world, BlockPos start, int length, boolean alongZ) {
        for (int i = 0; i < length; i++) {
            BlockPos tablePos = alongZ ? start.add(0, 0, i) : start.add(i, 0, 0);
            world.setBlockState(tablePos, Blocks.DARK_OAK_SLAB.getDefaultState(), 3);
            
            // Benches on both sides
            if (alongZ) {
                world.setBlockState(tablePos.add(-1, 0, 0), Blocks.DARK_OAK_STAIRS.getDefaultState(), 3);
                world.setBlockState(tablePos.add(1, 0, 0), Blocks.DARK_OAK_STAIRS.getDefaultState(), 3);
            } else {
                world.setBlockState(tablePos.add(0, 0, -1), Blocks.DARK_OAK_STAIRS.getDefaultState(), 3);
                world.setBlockState(tablePos.add(0, 0, 1), Blocks.DARK_OAK_STAIRS.getDefaultState(), 3);
            }
        }
    }
}
