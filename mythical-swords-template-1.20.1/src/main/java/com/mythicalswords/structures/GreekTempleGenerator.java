package com.mythicalswords.structures;

import com.mythicalswords.core.ModBlocks;
import com.mythicalswords.core.ModItems;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.BlockPos;

/**
 * Generator for Greek Temple structure
 * Creates a marble temple with columns, chest with Shard of Divinity, and statue for Atenea spawn trigger
 */
public class GreekTempleGenerator {
    
    /**
     * Generate the Greek Temple at the given position
     * @param world The world to generate in
     * @param pos The center position for the temple
     */
    public static void generate(StructureBuilder world, BlockPos pos) {
        // Clear area first
        clearArea(world, pos);
        
        // Build foundation
        buildFoundation(world, pos);
        
        // Build columns
        buildColumns(world, pos);
        
        // Build roof
        buildRoof(world, pos);
        
        // Build inner sanctum
        buildInnerSanctum(world, pos);
        
        // Place Atenea statue (spawn trigger)
        placeAteneaStatue(world, pos);
        
        // Place boss altar
        placeBossAltar(world, pos);
        
        // Place loot chest with Shard of Divinity
        placeLootChest(world, pos);
        
        // Add decorations
        addDecorations(world, pos);
    }
    
    /**
     * Clear only the interior area (reduced for performance)
     */
    private static void clearArea(StructureBuilder world, BlockPos pos) {
        for (int x = -13; x <= 13; x++) {
            for (int z = -13; z <= 13; z++) {
                for (int y = 1; y <= 13; y++) {
                    BlockPos clearPos = pos.add(x, y, z);
                    if (!world.getBlockState(clearPos).isOf(Blocks.BEDROCK)) {
                        world.setBlockState(clearPos, Blocks.AIR.getDefaultState(), 3);
                    }
                }
            }
        }
    }
    
    /**
     * Build the marble foundation
     */
    private static void buildFoundation(StructureBuilder world, BlockPos pos) {
        // Main platform (marble)
        for (int x = -14; x <= 14; x++) {
            for (int z = -14; z <= 14; z++) {
                BlockPos foundationPos = pos.add(x, -1, z);
                world.setBlockState(foundationPos, Blocks.QUARTZ_BLOCK.getDefaultState(), 3);
            }
        }
        
        // Floor (polished marble pattern)
        for (int x = -13; x <= 13; x++) {
            for (int z = -13; z <= 13; z++) {
                BlockPos floorPos = pos.add(x, 0, z);
                // Checkered pattern with quartz
                if ((x + z) % 2 == 0) {
                    world.setBlockState(floorPos, Blocks.QUARTZ_BLOCK.getDefaultState(), 3);
                } else {
                    world.setBlockState(floorPos, Blocks.CHISELED_QUARTZ_BLOCK.getDefaultState(), 3);
                }
            }
        }
        
        // Steps leading up (South side)
        for (int x = -6; x <= 6; x++) {
            for (int step = 0; step < 3; step++) {
                BlockPos stepPos = pos.add(x, -1 + step, 14 + step);
                world.setBlockState(stepPos, Blocks.QUARTZ_STAIRS.getDefaultState(), 3);
            }
        }
    }
    
    /**
     * Build the iconic Greek columns
     */
    private static void buildColumns(StructureBuilder world, BlockPos pos) {
        // Front columns (South side)
        buildColumn(world, pos.add(-10, 0, 12));
        buildColumn(world, pos.add(-5, 0, 12));
        buildColumn(world, pos.add(0, 0, 12));
        buildColumn(world, pos.add(5, 0, 12));
        buildColumn(world, pos.add(10, 0, 12));
        
        // Back columns (North side)
        buildColumn(world, pos.add(-10, 0, -12));
        buildColumn(world, pos.add(-5, 0, -12));
        buildColumn(world, pos.add(0, 0, -12));
        buildColumn(world, pos.add(5, 0, -12));
        buildColumn(world, pos.add(10, 0, -12));
        
        // Side columns (East)
        buildColumn(world, pos.add(12, 0, -8));
        buildColumn(world, pos.add(12, 0, -4));
        buildColumn(world, pos.add(12, 0, 0));
        buildColumn(world, pos.add(12, 0, 4));
        buildColumn(world, pos.add(12, 0, 8));
        
        // Side columns (West)
        buildColumn(world, pos.add(-12, 0, -8));
        buildColumn(world, pos.add(-12, 0, -4));
        buildColumn(world, pos.add(-12, 0, 0));
        buildColumn(world, pos.add(-12, 0, 4));
        buildColumn(world, pos.add(-12, 0, 8));
    }
    
    /**
     * Build a single Greek column
     */
    private static void buildColumn(StructureBuilder world, BlockPos base) {
        // Column base
        world.setBlockState(base.add(0, 1, 0), Blocks.CHISELED_QUARTZ_BLOCK.getDefaultState(), 3);
        
        // Column shaft (fluted)
        for (int y = 2; y <= 9; y++) {
            world.setBlockState(base.add(0, y, 0), Blocks.QUARTZ_PILLAR.getDefaultState(), 3);
        }
        
        // Column capital
        world.setBlockState(base.add(0, 10, 0), Blocks.CHISELED_QUARTZ_BLOCK.getDefaultState(), 3);
        
        // Capital extension (wider)
        world.setBlockState(base.add(1, 11, 0), Blocks.QUARTZ_SLAB.getDefaultState(), 3);
        world.setBlockState(base.add(-1, 11, 0), Blocks.QUARTZ_SLAB.getDefaultState(), 3);
        world.setBlockState(base.add(0, 11, 1), Blocks.QUARTZ_SLAB.getDefaultState(), 3);
        world.setBlockState(base.add(0, 11, -1), Blocks.QUARTZ_SLAB.getDefaultState(), 3);
    }
    
    /**
     * Build the roof
     */
    private static void buildRoof(StructureBuilder world, BlockPos pos) {
        // Flat roof with slight overhang
        for (int x = -14; x <= 14; x++) {
            for (int z = -14; z <= 14; z++) {
                BlockPos roofPos = pos.add(x, 12, z);
                world.setBlockState(roofPos, Blocks.QUARTZ_BLOCK.getDefaultState(), 3);
            }
        }
        
        // Decorative frieze around roof edge
        for (int x = -14; x <= 14; x++) {
            world.setBlockState(pos.add(x, 11, -14), Blocks.CHISELED_QUARTZ_BLOCK.getDefaultState(), 3);
            world.setBlockState(pos.add(x, 11, 14), Blocks.CHISELED_QUARTZ_BLOCK.getDefaultState(), 3);
        }
        for (int z = -14; z <= 14; z++) {
            world.setBlockState(pos.add(-14, 11, z), Blocks.CHISELED_QUARTZ_BLOCK.getDefaultState(), 3);
            world.setBlockState(pos.add(14, 11, z), Blocks.CHISELED_QUARTZ_BLOCK.getDefaultState(), 3);
        }
        
        // Pediment (triangular front)
        buildPediment(world, pos.add(0, 12, 12));
    }
    
    /**
     * Build the triangular pediment
     */
    private static void buildPediment(StructureBuilder world, BlockPos base) {
        // Triangular shape
        for (int y = 0; y <= 4; y++) {
            int width = 10 - (y * 2);
            for (int x = -width; x <= width; x++) {
                world.setBlockState(base.add(x, y, 0), Blocks.QUARTZ_BLOCK.getDefaultState(), 3);
            }
        }
        
        // Peak
        world.setBlockState(base.add(0, 5, 0), Blocks.CHISELED_QUARTZ_BLOCK.getDefaultState(), 3);
    }
    
    /**
     * Build the inner sanctum (enclosed area)
     */
    private static void buildInnerSanctum(StructureBuilder world, BlockPos pos) {
        // Inner walls (smaller enclosed area)
        for (int x = -8; x <= 8; x++) {
            for (int y = 1; y <= 10; y++) {
                // North wall
                world.setBlockState(pos.add(x, y, -8), Blocks.QUARTZ_BLOCK.getDefaultState(), 3);
                // South wall (with entrance)
                if (Math.abs(x) > 3) {
                    world.setBlockState(pos.add(x, y, 8), Blocks.QUARTZ_BLOCK.getDefaultState(), 3);
                }
            }
        }
        
        for (int z = -8; z <= 8; z++) {
            for (int y = 1; y <= 10; y++) {
                // East wall
                world.setBlockState(pos.add(8, y, z), Blocks.QUARTZ_BLOCK.getDefaultState(), 3);
                // West wall
                world.setBlockState(pos.add(-8, y, z), Blocks.QUARTZ_BLOCK.getDefaultState(), 3);
            }
        }
        
        // Inner sanctum entrance (South side)
        for (int x = -3; x <= 3; x++) {
            for (int y = 1; y <= 6; y++) {
                world.setBlockState(pos.add(x, y, 8), Blocks.AIR.getDefaultState(), 3);
            }
        }
    }
    
    /**
     * Place the Atenea statue (spawn trigger)
     */
    private static void placeAteneaStatue(StructureBuilder world, BlockPos pos) {
        // Statue at the north end of the sanctum
        BlockPos statueBase = pos.add(0, 0, -6);
        
        // Pedestal
        for (int x = -1; x <= 1; x++) {
            for (int z = -1; z <= 1; z++) {
                world.setBlockState(statueBase.add(x, 1, z), Blocks.QUARTZ_BLOCK.getDefaultState(), 3);
            }
        }
        world.setBlockState(statueBase.add(0, 2, 0), Blocks.CHISELED_QUARTZ_BLOCK.getDefaultState(), 3);
        
        // Statue body (simplified representation using blocks)
        // Base
        world.setBlockState(statueBase.add(0, 3, 0), Blocks.GOLD_BLOCK.getDefaultState(), 3);
        
        // Torso
        world.setBlockState(statueBase.add(0, 4, 0), Blocks.GOLD_BLOCK.getDefaultState(), 3);
        world.setBlockState(statueBase.add(0, 5, 0), Blocks.GOLD_BLOCK.getDefaultState(), 3);
        
        // Arms
        world.setBlockState(statueBase.add(-1, 4, 0), Blocks.GOLD_BLOCK.getDefaultState(), 3);
        world.setBlockState(statueBase.add(1, 4, 0), Blocks.GOLD_BLOCK.getDefaultState(), 3);
        
        // Head
        world.setBlockState(statueBase.add(0, 6, 0), Blocks.GOLD_BLOCK.getDefaultState(), 3);
        
        // Helmet/Crown
        world.setBlockState(statueBase.add(0, 7, 0), Blocks.CHISELED_QUARTZ_BLOCK.getDefaultState(), 3);
        
        // Shield (left side)
        world.setBlockState(statueBase.add(-1, 3, 0), Blocks.IRON_BLOCK.getDefaultState(), 3);
        
        // Spear (right side)
        world.setBlockState(statueBase.add(1, 5, 0), Blocks.IRON_BARS.getDefaultState(), 3);
        world.setBlockState(statueBase.add(1, 6, 0), Blocks.IRON_BARS.getDefaultState(), 3);
        world.setBlockState(statueBase.add(1, 7, 0), Blocks.IRON_BARS.getDefaultState(), 3);
        
        // Offering altar in front of statue
        world.setBlockState(statueBase.add(0, 1, 2), Blocks.CHISELED_QUARTZ_BLOCK.getDefaultState(), 3);
        world.setBlockState(statueBase.add(0, 2, 2), Blocks.CHISELED_QUARTZ_BLOCK.getDefaultState(), 3);
    }
    
    /**
     * Place the boss summoning altar
     */
    private static void placeBossAltar(StructureBuilder world, BlockPos pos) {
        // Altar in center of sanctum
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

        // Tag this altar to summon Atenea
        if (world.getBlockEntity(altarPos) instanceof com.mythicalswords.blocks.BossAltarBlockEntity altar) {
            altar.setBossId("atenea");
        }
        
        // Decorative braziers around altar
        for (int i = 0; i < 4; i++) {
            int x = (i % 2 == 0) ? 2 : -2;
            int z = (i < 2) ? 2 : -2;
            BlockPos brazierPos = altarBase.add(x, 2, z);
            world.setBlockState(brazierPos, Blocks.GOLD_BLOCK.getDefaultState(), 3);
            world.setBlockState(brazierPos.up(), Blocks.LANTERN.getDefaultState(), 3);
        }
    }
    
    /**
     * Place loot chest with Shard of Divinity
     */
    private static void placeLootChest(StructureBuilder world, BlockPos pos) {
        // Chest near the statue
        BlockPos chestPos = pos.add(3, 1, -6);
        world.setBlockState(chestPos, Blocks.CHEST.getDefaultState(), 3);
        
        // Add loot to chest
        BlockEntity blockEntity = world.getBlockEntity(chestPos);
        if (blockEntity instanceof ChestBlockEntity chest) {
            chest.setLootTable(new net.minecraft.util.Identifier("mythicalswords", "chests/greek"), world.getSeed() ^ chest.getPos().asLong());
            // Add Shard of Divinity (60% chance as per requirements)
            if (world.getRandom().nextFloat() < 0.6f) {
                if (false) chest.setStack(0, new ItemStack(ModItems.SHARD_OF_DIVINITY, 1 + world.getRandom().nextInt(3)));
            }
            
            // Add other Greek-themed loot
            if (false) chest.setStack(1, new ItemStack(Items.GOLD_INGOT, 3 + world.getRandom().nextInt(5)));
            if (false) chest.setStack(2, new ItemStack(Items.LAPIS_LAZULI, 5 + world.getRandom().nextInt(10)));
            
            // Chance for additional materials
            if (world.getRandom().nextFloat() < 0.4f) {
                if (false) chest.setStack(3, new ItemStack(ModItems.FEATHER_OF_VICTORY, 1));
            }
            if (world.getRandom().nextFloat() < 0.3f) {
                if (false) chest.setStack(4, new ItemStack(ModItems.BRONCE_BENDITO, 1 + world.getRandom().nextInt(2)));
            }
            
            // Some experience bottles
            if (false) chest.setStack(5, new ItemStack(Items.EXPERIENCE_BOTTLE, 3 + world.getRandom().nextInt(5)));
        }
        
        // Second chest on the other side
        BlockPos chestPos2 = pos.add(-3, 1, -6);
        world.setBlockState(chestPos2, Blocks.CHEST.getDefaultState(), 3);
        
        BlockEntity blockEntity2 = world.getBlockEntity(chestPos2);
        if (blockEntity2 instanceof ChestBlockEntity chest) {
            chest.setLootTable(new net.minecraft.util.Identifier("mythicalswords", "chests/greek"), world.getSeed() ^ chest.getPos().asLong());
            // More general loot
            if (false) chest.setStack(0, new ItemStack(Items.DIAMOND, 1 + world.getRandom().nextInt(3)));
            if (false) chest.setStack(1, new ItemStack(Items.EMERALD, 2 + world.getRandom().nextInt(4)));
            if (false) chest.setStack(2, new ItemStack(Items.GOLDEN_APPLE, 1 + world.getRandom().nextInt(2)));
        }
    }
    
    /**
     * Add decorative elements
     */
    private static void addDecorations(StructureBuilder world, BlockPos pos) {
        // Torches/lanterns along columns
        world.setBlockState(pos.add(-10, 6, 12), Blocks.LANTERN.getDefaultState(), 3);
        world.setBlockState(pos.add(10, 6, 12), Blocks.LANTERN.getDefaultState(), 3);
        world.setBlockState(pos.add(-10, 6, -12), Blocks.LANTERN.getDefaultState(), 3);
        world.setBlockState(pos.add(10, 6, -12), Blocks.LANTERN.getDefaultState(), 3);
        
        // Blue and white banners (Greek colors)
        world.setBlockState(pos.add(-6, 2, 8), Blocks.BLUE_BANNER.getDefaultState(), 3);
        world.setBlockState(pos.add(6, 2, 8), Blocks.BLUE_BANNER.getDefaultState(), 3);
        world.setBlockState(pos.add(-6, 2, -8), Blocks.WHITE_BANNER.getDefaultState(), 3);
        world.setBlockState(pos.add(6, 2, -8), Blocks.WHITE_BANNER.getDefaultState(), 3);
        
        // Decorative gold blocks (Greek wealth)
        world.setBlockState(pos.add(-7, 1, -7), Blocks.GOLD_BLOCK.getDefaultState(), 3);
        world.setBlockState(pos.add(7, 1, -7), Blocks.GOLD_BLOCK.getDefaultState(), 3);
        world.setBlockState(pos.add(-7, 1, 7), Blocks.GOLD_BLOCK.getDefaultState(), 3);
        world.setBlockState(pos.add(7, 1, 7), Blocks.GOLD_BLOCK.getDefaultState(), 3);
        
        // Lapis lazuli accents (Greek blue)
        world.setBlockState(pos.add(0, 2, 8), Blocks.LAPIS_BLOCK.getDefaultState(), 3);
        world.setBlockState(pos.add(0, 2, -8), Blocks.LAPIS_BLOCK.getDefaultState(), 3);
        
        // Flower pots with plants (Greek gardens)
        world.setBlockState(pos.add(-5, 1, 10), Blocks.POTTED_AZURE_BLUET.getDefaultState(), 3);
        world.setBlockState(pos.add(5, 1, 10), Blocks.POTTED_AZURE_BLUET.getDefaultState(), 3);
        
        // Carpet leading to altar (ceremonial path)
        for (int z = 2; z <= 7; z++) {
            world.setBlockState(pos.add(0, 1, z), Blocks.BLUE_CARPET.getDefaultState(), 3);
        }
    }
}
