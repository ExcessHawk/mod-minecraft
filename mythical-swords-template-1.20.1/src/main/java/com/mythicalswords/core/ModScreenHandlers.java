package com.mythicalswords.core;

import com.mythicalswords.MythicalSwords;
import com.mythicalswords.screen.MythicalForgeScreenHandler;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;

public class ModScreenHandlers {

    public static final ScreenHandlerType<MythicalForgeScreenHandler> MYTHICAL_FORGE =
        Registry.register(
            Registries.SCREEN_HANDLER,
            new Identifier(MythicalSwords.MOD_ID, "mythical_forge"),
            new ExtendedScreenHandlerType<>(MythicalForgeScreenHandler::new)
        );

    public static void register() {
        MythicalSwords.LOGGER.info("Registering ModScreenHandlers for " + MythicalSwords.MOD_ID);
    }
}
