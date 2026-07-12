package com.mythicalswords.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mythicalswords.structures.ArthuranCastleGenerator;
import com.mythicalswords.structures.DirectStructureBuilder;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;

/**
 * Custom commands for the mod
 */
public class ModCommands {
    
    /**
     * Register all custom commands
     */
    public static void register() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            registerCastleCommand(dispatcher, registryAccess);
        });
    }
    
    /**
     * Register the /summoncastle command
     */
    private static void registerCastleCommand(CommandDispatcher<ServerCommandSource> dispatcher, 
                                              CommandRegistryAccess registryAccess) {
        dispatcher.register(
            CommandManager.literal("summoncastle")
                .requires(source -> source.hasPermissionLevel(2)) // Requires OP
                .executes(ModCommands::executeSummonCastle)
        );
    }
    
    /**
     * Execute the summon castle command
     */
    private static int executeSummonCastle(CommandContext<ServerCommandSource> context) {
        ServerCommandSource source = context.getSource();
        
        try {
            // Get player position
            BlockPos playerPos = BlockPos.ofFloored(source.getPosition());
            
            // Generate castle at player's position
            ArthuranCastleGenerator.generate(new DirectStructureBuilder(source.getWorld()), playerPos);
            
            // Send success message
            source.sendFeedback(
                () -> Text.literal("Arthuran Castle generated at your location!"),
                true
            );
            
            return 1; // Success
        } catch (Exception e) {
            source.sendError(Text.literal("Failed to generate castle: " + e.getMessage()));
            return 0; // Failure
        }
    }
}
