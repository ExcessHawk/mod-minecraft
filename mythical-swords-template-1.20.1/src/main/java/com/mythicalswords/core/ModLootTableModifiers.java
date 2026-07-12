package com.mythicalswords.core;

import com.mythicalswords.MythicalSwords;
import net.fabricmc.fabric.api.loot.v2.LootTableEvents;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.condition.RandomChanceLootCondition;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.function.SetCountLootFunction;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.loot.provider.number.UniformLootNumberProvider;
import net.minecraft.util.Identifier;

/**
 * Handles custom loot table modifications for mob drops
 */
public class ModLootTableModifiers {

    private static final Identifier BLAZE_LOOT_TABLE = new Identifier("minecraft", "entities/blaze");
    private static final Identifier ENDER_DRAGON_LOOT_TABLE = new Identifier("minecraft", "entities/ender_dragon");
    private static final Identifier ENDERMAN_LOOT_TABLE = new Identifier("minecraft", "entities/enderman");
    
    // Norse material drops
    private static final Identifier STRAY_LOOT_TABLE = new Identifier("minecraft", "entities/stray");
    private static final Identifier PHANTOM_LOOT_TABLE = new Identifier("minecraft", "entities/phantom");
    private static final Identifier WITHER_SKELETON_LOOT_TABLE = new Identifier("minecraft", "entities/wither_skeleton");

    /**
     * Register all loot table modifications
     */
    public static void modifyLootTables() {
        LootTableEvents.MODIFY.register((resourceManager, lootManager, id, tableBuilder, source) -> {

            // Blaze drops Sun-Blessed Alloy (10% chance, 1 drop)
            if (BLAZE_LOOT_TABLE.equals(id)) {
                LootPool.Builder poolBuilder = LootPool.builder()
                        .rolls(ConstantLootNumberProvider.create(1))
                        .conditionally(RandomChanceLootCondition.builder(0.10f)) // 10% chance
                        .with(ItemEntry.builder(ModItems.SUN_BLESSED_ALLOY));

                tableBuilder.pool(poolBuilder.build());
                MythicalSwords.LOGGER.info("Added Sun-Blessed Alloy drop to Blazes (10%)");
            }

            // Ender Dragon drops Dragon Fang Fragment (100% chance, 2-3 drops)
            if (ENDER_DRAGON_LOOT_TABLE.equals(id)) {
                LootPool.Builder poolBuilder = LootPool.builder()
                        .rolls(ConstantLootNumberProvider.create(1))
                        .with(ItemEntry.builder(ModItems.DRAGON_FANG_FRAGMENT)
                                .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(2.0f, 3.0f))));

                tableBuilder.pool(poolBuilder.build());
                MythicalSwords.LOGGER.info("Added Dragon Fang Fragment drop to Ender Dragon (100%, 2-3 drops)");
            }

            // Enderman drops Dragon Fang Fragment (1% chance, 1 drop)
            if (ENDERMAN_LOOT_TABLE.equals(id)) {
                LootPool.Builder poolBuilder = LootPool.builder()
                        .rolls(ConstantLootNumberProvider.create(1))
                        .conditionally(RandomChanceLootCondition.builder(0.01f)) // 1% chance
                        .with(ItemEntry.builder(ModItems.DRAGON_FANG_FRAGMENT));

                tableBuilder.pool(poolBuilder.build());
                MythicalSwords.LOGGER.info("Added Dragon Fang Fragment drop to Endermen (1%)");
            }
            
            // Stray drops Frozen Soul Crystal (5% chance, 1 drop)
            if (STRAY_LOOT_TABLE.equals(id)) {
                LootPool.Builder poolBuilder = LootPool.builder()
                        .rolls(ConstantLootNumberProvider.create(1))
                        .conditionally(RandomChanceLootCondition.builder(0.05f)) // 5% chance
                        .with(ItemEntry.builder(ModItems.FROZEN_SOUL_CRYSTAL));

                tableBuilder.pool(poolBuilder.build());
                MythicalSwords.LOGGER.info("Added Frozen Soul Crystal drop to Strays (5%)");
            }
            
            // Phantom drops Spiritbound Leather (8% chance, 1 drop)
            if (PHANTOM_LOOT_TABLE.equals(id)) {
                LootPool.Builder poolBuilder = LootPool.builder()
                        .rolls(ConstantLootNumberProvider.create(1))
                        .conditionally(RandomChanceLootCondition.builder(0.08f)) // 8% chance
                        .with(ItemEntry.builder(ModItems.SPIRITBOUND_LEATHER));

                tableBuilder.pool(poolBuilder.build());
                MythicalSwords.LOGGER.info("Added Spiritbound Leather drop to Phantoms (8%)");
            }
            
            // Wither Skeleton drops Rainbow Bridge Fragment (2% chance, 1 drop) - rare Norse material
            if (WITHER_SKELETON_LOOT_TABLE.equals(id)) {
                LootPool.Builder poolBuilder = LootPool.builder()
                        .rolls(ConstantLootNumberProvider.create(1))
                        .conditionally(RandomChanceLootCondition.builder(0.02f)) // 2% chance
                        .with(ItemEntry.builder(ModItems.RAINBOW_BRIDGE_FRAGMENT));

                tableBuilder.pool(poolBuilder.build());
                MythicalSwords.LOGGER.info("Added Rainbow Bridge Fragment drop to Wither Skeletons (2%)");
            }
        });
    }
}
