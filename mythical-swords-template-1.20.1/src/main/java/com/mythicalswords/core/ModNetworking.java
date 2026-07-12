package com.mythicalswords.core;

import com.mythicalswords.MythicalSwords;
import com.mythicalswords.screen.MythicalForgeScreenHandler;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.util.Identifier;

/**
 * Server-side packet receivers. The Mythical Forge button is client-side, so it
 * sends this packet to ask the server to run the forge operation on the real
 * (server) container inventory and sync the result back.
 */
public class ModNetworking {
    public static final Identifier FORGE_CRAFT = new Identifier(MythicalSwords.MOD_ID, "forge_craft");

    public static void register() {
        ServerPlayNetworking.registerGlobalReceiver(FORGE_CRAFT, (server, player, handler, buf, sender) ->
            server.execute(() -> {
                if (player.currentScreenHandler instanceof MythicalForgeScreenHandler forge) {
                    forge.craft(player);
                    forge.sendContentUpdates();
                }
            }));
        MythicalSwords.LOGGER.info("Registering networking for " + MythicalSwords.MOD_ID);
    }
}
