package com.mythicalswords.structures;

import com.mythicalswords.core.ModBlocks;
import com.mythicalswords.core.ModItems;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.BlockPos;

/**
 * Generator for Trickster's Cave structure (Norse mythology - Loki)
 * Creates a mysterious cave with 3 rune puzzles and Loki spawn trigger
 */
public class TrickstersCaveGenerator {
    
    /**
     * Generate the Trickster's Cave at the given position
     * @param world The world to generate in
     * @param pos The center position for the cave
     */
    public static void generate(StructureBuilder world, BlockPos pos) {
        // Clear area first
        clearArea(world, pos);
        
        // Build cave structure
        buildCaveWalls(world, pos);
        
        // Build cave floor
        buildCaveFloor(world, pos);
        
        // Build cave ceiling
        buildCaveCeiling(world, pos);
        
        // Place rune puzzles
        placeRunePuzzles(world, pos);
        
        // Place Loki spawn trigger (boss altar)
        placeLokiAltar(world, pos);
        
        // Add decorations
        addDecorations(world, pos);
    }
    
    /**
     * Clear only the interior area (reduced for performance)
     */
    private static void clearArea(StructureBuilder world, BlockPos pos) {
        for (int x = -13; x <= 13; x++) {
            for (int z = -13; z <= 13; z++) {
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
     * Build the cave walls (organic, irregular shape)
     */
    private static void buildCaveWalls(StructureBuilder world, BlockPos pos) {
        // Create an organic cave shape using distance from center
        for (int x = -14; x <= 14; x++) {
            for (int z = -14; z <= 14; z++) {
                for (int y = 1; y <= 10; y++) {
                    BlockPos wallPos = pos.add(x, y, z);
                    
                    // Calculate distance from center (elliptical)
                    double distX = x / 14.0;
                    double distZ = z / 14.0;
                    double distance = Math.sqrt(distX * distX + distZ * distZ);
                    
                    // Add some variation to make it organic
                    double variation = Math.sin(x * 0.5) * 0.1 + Math.cos(z * 0.5) * 0.1;
                    
                    // Place walls at the edge
                    if (distance + variation > 0.85) {
                        world.setBlockState(wallPos, Blocks.STONE.getDefaultState(), 3);
                    }
                }
            }
        }
        
        // Add entrance tunnel (South side)
        for (int z = 10; z <= 15; z++) {
            for (int x = -2; x <= 2; x++) {
                for (int y = 1; y <= 4; y++) {
                    BlockPos entrancePos = pos.add(x, y, z);
                    world.setBlockState(entrancePos, Blocks.AIR.getDefaultState(), 3);
                }
            }
        }
        
        // Entrance tunnel walls
        for (int z = 10; z <= 15; z++) {
            for (int y = 1; y <= 4; y++) {
                world.setBlockState(pos.add(-3, y, z), Blocks.STONE.getDefaultState(), 3);
                world.setBlockState(pos.add(3, y, z), Blocks.STONE.getDefaultState(), 3);
            }
        }
    }
    
    /**
     * Build the cave floor
     */
    private static void buildCaveFloor(StructureBuilder world, BlockPos pos) {
        // Main floor
        for (int x = -13; x <= 13; x++) {
            for (int z = -13; z <= 13; z++) {
                BlockPos floorPos = pos.add(x, 0, z);
                
                // Calculate distance from center
                double distX = x / 13.0;
                double distZ = z / 13.0;
                double distance = Math.sqrt(distX * distX + distZ * distZ);
                
                if (distance <= 1.0) {
                    // Mix of stone and mossy cobblestone
                    if ((x + z) % 3 == 0) {
                        world.setBlockState(floorPos, Blocks.MOSSY_COBBLESTONE.getDefaultState(), 3);
                    } else {
                        world.setBlockState(floorPos, Blocks.COBBLESTONE.getDefaultState(), 3);
                    }
                }
            }
        }
        
        // Entrance tunnel floor
        for (int z = 10; z <= 15; z++) {
            for (int x = -2; x <= 2; x++) {
                BlockPos floorPos = pos.add(x, 0, z);
                world.setBlockState(floorPos, Blocks.COBBLESTONE.getDefaultState(), 3);
            }
        }
    }
    
    /**
     * Build the cave ceiling
     */
    private static void buildCaveCeiling(StructureBuilder world, BlockPos pos) {
        // Domed ceiling
        for (int x = -14; x <= 14; x++) {
            for (int z = -14; z <= 14; z++) {
                // Calculate distance from center
                double distX = x / 14.0;
                double distZ = z / 14.0;
                double distance = Math.sqrt(distX * distX + distZ * distZ);
                
                if (distance <= 1.0) {
                    // Height based on distance from center (dome shape)
                    int ceilingHeight = 11 - (int)(distance * 3);
                    BlockPos ceilingPos = pos.add(x, ceilingHeight, z);
                    world.setBlockState(ceilingPos, Blocks.STONE.getDefaultState(), 3);
                    
                    // Add stalactites randomly
                    if ((x * 7 + z * 11) % 13 == 0 && distance < 0.8) {
                        world.setBlockState(ceilingPos.down(), Blocks.STONE.getDefaultState(), 3);
                        if ((x + z) % 5 == 0) {
                            world.setBlockState(ceilingPos.down(2), Blocks.STONE.getDefaultState(), 3);
                        }
                    }
                }
            }
        }
        
        // Entrance tunnel ceiling
        for (int z = 10; z <= 15; z++) {
            for (int x = -2; x <= 2; x++) {
                BlockPos ceilingPos = pos.add(x, 5, z);
                world.setBlockState(ceilingPos, Blocks.STONE.getDefaultState(), 3);
            }
        }
    }
    
    /**
     * Place the 3 rune puzzles
     * These are pressure plate puzzles that must be solved to summon Loki
     */
    private static void placeRunePuzzles(StructureBuilder world, BlockPos pos) {
        // Puzzle 1: West side
        placeRunePuzzle(world, pos.add(-10, 1, -5), 1);
        
        // Puzzle 2: East side
        placeRunePuzzle(world, pos.add(10, 1, -5), 2);
        
        // Puzzle 3: North side
        placeRunePuzzle(world, pos.add(0, 1, -10), 3);
    }
    
    /**
     * Place a single rune puzzle
     */
    private static void placeRunePuzzle(StructureBuilder world, BlockPos center, int puzzleNumber) {
        // Platform base
        for (int x = -2; x <= 2; x++) {
            for (int z = -2; z <= 2; z++) {
                if (Math.abs(x) + Math.abs(z) <= 3) {
                    BlockPos platformPos = center.add(x, -1, z);
                    world.setBlockState(platformPos, Blocks.CHISELED_STONE_BRICKS.getDefaultState(), 3);
                }
            }
        }
        
        // Rune stone (center)
        world.setBlockState(center, Blocks.CHISELED_STONE_BRICKS.getDefaultState(), 3);
        
        // Pressure plates around the rune stone (puzzle elements)
        BlockPos[] platePositions = {
            center.add(2, 0, 0),
            center.add(-2, 0, 0),
            center.add(0, 0, 2),
            center.add(0, 0, -2)
        };
        
        for (BlockPos platePos : platePositions) {
            world.setBlockState(platePos, Blocks.STONE_PRESSURE_PLATE.getDefaultState(), 3);
        }
        
        // Decorative pillars
        for (int i = 0; i < 4; i++) {
            int x = (i % 2 == 0) ? 2 : -2;
            int z = (i < 2) ? 2 : -2;
            BlockPos pillarPos = center.add(x, 0, z);
            world.setBlockState(pillarPos, Blocks.STONE_BRICK_WALL.getDefaultState(), 3);
            world.setBlockState(pillarPos.up(), Blocks.STONE_BRICK_WALL.getDefaultState(), 3);
            
            // Different colored lanterns for each puzzle
            if (puzzleNumber == 1) {
                world.setBlockState(pillarPos.up(2), Blocks.LANTERN.getDefaultState(), 3);
            } else if (puzzleNumber == 2) {
                world.setBlockState(pillarPos.up(2), Blocks.SOUL_LANTERN.getDefaultState(), 3);
            } else {
                world.setBlockState(pillarPos.up(2), Blocks.LANTERN.getDefaultState(), 3);
            }
        }
        
        // Glowstone for ambient light
        world.setBlockState(center.up(3), Blocks.GLOWSTONE.getDefaultState(), 3);
    }
    
    /**
     * Place the Loki spawn trigger (boss altar)
     */
    private static void placeLokiAltar(StructureBuilder world, BlockPos pos) {
        // Altar in the center of the cave
        BlockPos altarBase = pos.add(0, 0, 0);
        
        // Raised platform
        for (int x = -3; x <= 3; x++) {
            for (int z = -3; z <= 3; z++) {
                if (Math.abs(x) + Math.abs(z) <= 4) {
                    BlockPos platformPos = altarBase.add(x, 1, z);
                    world.setBlockState(platformPos, Blocks.BLACKSTONE.getDefaultState(), 3);
                }
            }
        }
        
        // Inner circle
        for (int x = -1; x <= 1; x++) {
            for (int z = -1; z <= 1; z++) {
                if (Math.abs(x) + Math.abs(z) <= 1) {
                    BlockPos innerPos = altarBase.add(x, 2, z);
                    world.setBlockState(innerPos, Blocks.POLISHED_BLACKSTONE.getDefaultState(), 3);
                }
            }
        }
        
        // The actual altar block (center)
        BlockPos altarPos = altarBase.add(0, 3, 0);
        world.setBlockState(altarPos, ModBlocks.BOSS_ALTAR.getDefaultState(), 3);

        // Tag this altar to summon Loki
        if (world.getBlockEntity(altarPos) instanceof com.mythicalswords.blocks.BossAltarBlockEntity altar) {
            altar.setBossId("loki");
        }
        
        // Decorative fire bowls around altar
        BlockPos[] firePositions = {
            altarBase.add(3, 2, 0),
            altarBase.add(-3, 2, 0),
            altarBase.add(0, 2, 3),
            altarBase.add(0, 2, -3)
        };
        
        for (BlockPos firePos : firePositions) {
            world.setBlockState(firePos, Blocks.NETHERRACK.getDefaultState(), 3);
            world.setBlockState(firePos.up(), Blocks.FIRE.getDefaultState(), 3);
        }
        
        // Pillars around the altar
        for (int i = 0; i < 4; i++) {
            int x = (i % 2 == 0) ? 4 : -4;
            int z = (i < 2) ? 4 : -4;
            BlockPos pillarPos = altarBase.add(x, 1, z);
            
            for (int y = 0; y < 4; y++) {
                world.setBlockState(pillarPos.up(y), Blocks.BLACKSTONE_WALL.getDefaultState(), 3);
            }
            world.setBlockState(pillarPos.up(4), Blocks.SOUL_LANTERN.getDefaultState(), 3);
        }
    }
    
    /**
     * Add decorative elements
     */
    private static void addDecorations(StructureBuilder world, BlockPos pos) {
        // Glowstone clusters for ambient lighting
        world.setBlockState(pos.add(-8, 6, -8), Blocks.GLOWSTONE.getDefaultState(), 3);
        world.setBlockState(pos.add(8, 6, -8), Blocks.GLOWSTONE.getDefaultState(), 3);
        world.setBlockState(pos.add(-8, 6, 8), Blocks.GLOWSTONE.getDefaultState(), 3);
        world.setBlockState(pos.add(8, 6, 8), Blocks.GLOWSTONE.getDefaultState(), 3);
        
        // Mushrooms on the floor (cave atmosphere)
        world.setBlockState(pos.add(-7, 1, -3), Blocks.RED_MUSHROOM.getDefaultState(), 3);
        world.setBlockState(pos.add(7, 1, -3), Blocks.BROWN_MUSHROOM.getDefaultState(), 3);
        world.setBlockState(pos.add(-5, 1, 7), Blocks.RED_MUSHROOM.getDefaultState(), 3);
        world.setBlockState(pos.add(5, 1, 7), Blocks.BROWN_MUSHROOM.getDefaultState(), 3);
        
        // Cobwebs in corners
        world.setBlockState(pos.add(-10, 3, -10), Blocks.COBWEB.getDefaultState(), 3);
        world.setBlockState(pos.add(10, 3, -10), Blocks.COBWEB.getDefaultState(), 3);
        world.setBlockState(pos.add(-10, 3, 10), Blocks.COBWEB.getDefaultState(), 3);
        world.setBlockState(pos.add(10, 3, 10), Blocks.COBWEB.getDefaultState(), 3);
        
        // Loot chests hidden in alcoves
        BlockPos chestPos1 = pos.add(-11, 1, 0);
        world.setBlockState(chestPos1, Blocks.CHEST.getDefaultState(), 3);
        if (world.getBlockEntity(chestPos1) instanceof ChestBlockEntity chest) {
            chest.setLootTable(new net.minecraft.util.Identifier("mythicalswords", "chests/trickster"), world.getSeed() ^ chest.getPos().asLong());
            var rng = world.getRandom();
            int slot = 0;
            if (rng.nextFloat() < 0.50f) {
                if (false) chest.setStack(slot++, new ItemStack(ModItems.FROZEN_SOUL_CRYSTAL, 1));
            }
            if (false) chest.setStack(slot++, new ItemStack(Items.GOLD_INGOT, 2 + rng.nextInt(4)));
            if (rng.nextFloat() < 0.20f) {
                if (false) chest.setStack(slot++, new ItemStack(Items.ENCHANTED_BOOK, 1));
            }
            if (rng.nextFloat() < 0.15f) {
                if (false) chest.setStack(slot++, new ItemStack(ModItems.MYTHRIL_INGOT, 1));
            }
            if (false) chest.setStack(slot++, new ItemStack(Items.EMERALD, 1 + rng.nextInt(3)));
        }

        BlockPos chestPos2 = pos.add(11, 1, 0);
        world.setBlockState(chestPos2, Blocks.CHEST.getDefaultState(), 3);
        if (world.getBlockEntity(chestPos2) instanceof ChestBlockEntity chest) {
            chest.setLootTable(new net.minecraft.util.Identifier("mythicalswords", "chests/trickster"), world.getSeed() ^ chest.getPos().asLong());
            var rng = world.getRandom();
            int slot = 0;
            if (rng.nextFloat() < 0.50f) {
                if (false) chest.setStack(slot++, new ItemStack(ModItems.FROZEN_SOUL_CRYSTAL, 1));
            }
            if (false) chest.setStack(slot++, new ItemStack(Items.IRON_INGOT, 3 + rng.nextInt(3)));
            if (false) chest.setStack(slot++, new ItemStack(Items.SPIDER_EYE, 2 + rng.nextInt(3)));
            if (rng.nextFloat() < 0.30f) {
                if (false) chest.setStack(slot++, new ItemStack(ModItems.SPIRITBOUND_LEATHER, 1));
            }
        }
        
        // Suspicious blocks (could contain loot)
        world.setBlockState(pos.add(-6, 1, -8), Blocks.SUSPICIOUS_GRAVEL.getDefaultState(), 3);
        world.setBlockState(pos.add(6, 1, -8), Blocks.SUSPICIOUS_GRAVEL.getDefaultState(), 3);
        
        // Vines hanging from ceiling
        for (int i = 0; i < 8; i++) {
            int x = -8 + i * 2;
            int z = -6 + (i % 3) * 2;
            BlockPos vinePos = pos.add(x, 8, z);
            world.setBlockState(vinePos, Blocks.VINE.getDefaultState(), 3);
            if (i % 2 == 0) {
                world.setBlockState(vinePos.down(), Blocks.VINE.getDefaultState(), 3);
            }
        }
        
        // Cauldrons with water (mysterious atmosphere)
        world.setBlockState(pos.add(-8, 1, -6), Blocks.WATER_CAULDRON.getDefaultState(), 3);
        world.setBlockState(pos.add(8, 1, -6), Blocks.WATER_CAULDRON.getDefaultState(), 3);
        
        // Brewing stands (Loki's trickery)
        world.setBlockState(pos.add(-9, 1, 2), Blocks.BREWING_STAND.getDefaultState(), 3);
        world.setBlockState(pos.add(9, 1, 2), Blocks.BREWING_STAND.getDefaultState(), 3);
    }
}
