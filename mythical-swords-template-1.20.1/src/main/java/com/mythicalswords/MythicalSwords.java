package com.mythicalswords;

import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MythicalSwords implements ModInitializer {
	public static final String MOD_ID = "mythicalswords";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		LOGGER.info("Mythical Swords Mod initialized!");

		// Load config first so every later registration can read it
		com.mythicalswords.config.ModConfig.load();

		// Tick scheduler for time-based abilities (Serpent Strike, Swift Strikes)
		com.mythicalswords.util.ServerScheduler.init();

		// Register sounds (before blocks/items)
		com.mythicalswords.core.ModSounds.register();

		// Register blocks
		com.mythicalswords.core.ModBlocks.register();

		// Register items
		com.mythicalswords.core.ModItems.register();

		// Register ore generation
		com.mythicalswords.world.ModOreGeneration.register();

		// Register entities
		com.mythicalswords.core.ModEntities.register();

		// Register block entities
		com.mythicalswords.core.ModBlockEntities.register();

		// Register screen handlers
		com.mythicalswords.core.ModScreenHandlers.register();

		// Register networking (forge button packet)
		com.mythicalswords.core.ModNetworking.register();

		// Register enchantments
		com.mythicalswords.core.ModEnchantments.register();

		// Register weapon events (XP gain)
		com.mythicalswords.events.WeaponEvents.register();
		
		// Register affinity event handler (safe, no loops)
		com.mythicalswords.events.AffinityEventHandler.register();

		// Register relic event handler (Phoenix Feather revive)
		com.mythicalswords.events.RelicEventHandler.register();

		// Register armor set bonus handler
		com.mythicalswords.events.ArmorSetHandler.register();

		// Register loot table modifications (mob drops)
		com.mythicalswords.core.ModLootTableModifiers.modifyLootTables();

		// Register structures
		com.mythicalswords.structures.ModStructures.register();

		// Register commands
		com.mythicalswords.commands.ModCommands.register();

		// Validation
		com.mythicalswords.validation.RegistryValidator.validate();
		com.mythicalswords.validation.AffinityVisualsValidator.validate();

		LOGGER.info("Mythical Swords Mod registration complete!");
	}
}