package com.mythicalswords;

import com.mythicalswords.client.model.ReyArturoModel;
import com.mythicalswords.client.model.OdinModel;
import com.mythicalswords.client.model.LokiModel;
import com.mythicalswords.client.model.AteneaModel;
import com.mythicalswords.client.model.SusanooModel;
import com.mythicalswords.client.model.OniOscuroModel;
import com.mythicalswords.client.model.IzanagiModel;
import com.mythicalswords.client.model.QuetzalcoatlModel;
import com.mythicalswords.client.model.AnubisModel;
import com.mythicalswords.client.model.RaModel;
import com.mythicalswords.client.model.SunWukongModel;
import com.mythicalswords.client.model.LegendaryBlacksmithModel;
import com.mythicalswords.client.renderer.ReyArturoRenderer;
import com.mythicalswords.client.renderer.OdinRenderer;
import com.mythicalswords.client.renderer.LokiRenderer;
import com.mythicalswords.client.renderer.AteneaRenderer;
import com.mythicalswords.client.renderer.SusanooRenderer;
import com.mythicalswords.client.renderer.OniOscuroRenderer;
import com.mythicalswords.client.renderer.IzanagiRenderer;
import com.mythicalswords.client.renderer.QuetzalcoatlRenderer;
import com.mythicalswords.client.renderer.SunWukongRenderer;
import com.mythicalswords.client.renderer.RaRenderer;
import com.mythicalswords.client.renderer.AnubisRenderer;
import com.mythicalswords.client.renderer.LegendaryBlacksmithRenderer;
import com.mythicalswords.client.WeaponAuraRenderer;
import com.mythicalswords.client.screen.MythicalForgeScreen;
import com.mythicalswords.core.ModEntities;
import com.mythicalswords.core.ModScreenHandlers;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.client.item.CompassAnglePredicateProvider;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.item.Item;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.GlobalPos;
import net.minecraft.world.World;
import com.mythicalswords.core.ModItems;

public class MythicalSwordsClient implements ClientModInitializer {

        public static final EntityModelLayer REY_ARTURO_LAYER = new EntityModelLayer(
                        new Identifier(MythicalSwords.MOD_ID, "rey_arturo"), "main");
        public static final EntityModelLayer ODIN_LAYER = new EntityModelLayer(
                        new Identifier(MythicalSwords.MOD_ID, "odin"), "main");
        public static final EntityModelLayer LOKI_LAYER = new EntityModelLayer(
                        new Identifier(MythicalSwords.MOD_ID, "loki"), "main");
        public static final EntityModelLayer ATENEA_LAYER = new EntityModelLayer(
                        new Identifier(MythicalSwords.MOD_ID, "atenea"), "main");

        // Japanese Model Layers
        public static final EntityModelLayer SUSANOO_LAYER = new EntityModelLayer(
                        new Identifier(MythicalSwords.MOD_ID, "susanoo"), "main");
        public static final EntityModelLayer ONI_OSCURO_LAYER = new EntityModelLayer(
                        new Identifier(MythicalSwords.MOD_ID, "oni_oscuro"), "main");
        public static final EntityModelLayer IZANAGI_LAYER = new EntityModelLayer(
                        new Identifier(MythicalSwords.MOD_ID, "izanagi"), "main");

        // Mesoamerican Model Layers
        public static final EntityModelLayer QUETZALCOATL_LAYER = new EntityModelLayer(
                        new Identifier(MythicalSwords.MOD_ID, "quetzalcoatl"), "main");

        // Mini-Boss Model Layers
        public static final EntityModelLayer LEGENDARY_BLACKSMITH_LAYER = new EntityModelLayer(
                        new Identifier(MythicalSwords.MOD_ID, "legendary_blacksmith"), "main");

        // Egyptian Model Layers
        public static final EntityModelLayer SUN_WUKONG_LAYER = new EntityModelLayer(
                        new Identifier(MythicalSwords.MOD_ID, "sun_wukong"), "main");
        public static final EntityModelLayer RA_LAYER = new EntityModelLayer(
                        new Identifier(MythicalSwords.MOD_ID, "ra"), "main");
        public static final EntityModelLayer ANUBIS_LAYER = new EntityModelLayer(
                        new Identifier(MythicalSwords.MOD_ID, "anubis"), "main");

        @Override
        public void onInitializeClient() {
                // Register Entity Renderers
                EntityRendererRegistry.register(ModEntities.REY_ARTURO, ReyArturoRenderer::new);
                EntityRendererRegistry.register(ModEntities.ODIN, OdinRenderer::new);
                EntityRendererRegistry.register(ModEntities.LOKI, LokiRenderer::new);
                EntityRendererRegistry.register(ModEntities.ATENEA, AteneaRenderer::new);

                EntityRendererRegistry.register(ModEntities.SUSANOO, SusanooRenderer::new);
                EntityRendererRegistry.register(ModEntities.ONI_OSCURO, OniOscuroRenderer::new);
                EntityRendererRegistry.register(ModEntities.IZANAGI, IzanagiRenderer::new);
                EntityRendererRegistry.register(ModEntities.QUETZALCOATL, QuetzalcoatlRenderer::new);

                EntityRendererRegistry.register(ModEntities.LEGENDARY_BLACKSMITH, LegendaryBlacksmithRenderer::new);
                EntityRendererRegistry.register(ModEntities.DRAUGR, com.mythicalswords.client.renderer.MinionRenderer::new);
                EntityRendererRegistry.register(ModEntities.ONI_MENOR, com.mythicalswords.client.renderer.MinionRenderer::new);
                EntityRendererRegistry.register(ModEntities.MOMIA_SIRVIENTE, com.mythicalswords.client.renderer.MinionRenderer::new);
                EntityRendererRegistry.register(ModEntities.GUERRERO_JAGUAR, com.mythicalswords.client.renderer.MinionRenderer::new);
                EntityRendererRegistry.register(ModEntities.HOPLITA_ESPECTRAL, com.mythicalswords.client.renderer.MinionRenderer::new);
                EntityRendererRegistry.register(ModEntities.SOLDADO_TERRACOTA, com.mythicalswords.client.renderer.MinionRenderer::new);

                // Register Entity Model Layers
                // Rey Arturo now uses a GeckoLib model (no vanilla model layer)
                // Odin now uses a GeckoLib model
                // Loki now uses a GeckoLib model
                // Atenea now uses a GeckoLib model

                // Susanoo now uses a GeckoLib model (no vanilla model layer)
                // Oni Oscuro now uses a GeckoLib model
                // Izanagi now uses a GeckoLib model
                // Quetzalcoatl now uses a GeckoLib model

                // Legendary Blacksmith now uses a GeckoLib model
                        
                // Sun Wukong now uses a GeckoLib model
                // Ra now uses a GeckoLib model (no vanilla model layer)
                // Anubis now uses a GeckoLib model (no vanilla model layer)

                // Egyptian Model Layers
                EntityRendererRegistry.register(ModEntities.SUN_WUKONG, SunWukongRenderer::new);
                EntityRendererRegistry.register(ModEntities.RA, RaRenderer::new);
                EntityRendererRegistry.register(ModEntities.ANUBIS, AnubisRenderer::new);
                EntityRendererRegistry.register(ModEntities.RIDEABLE_DRAGON,
                                com.mythicalswords.client.renderer.RideableDragonRenderer::new);

                // Register Mythical Forge screen
                HandledScreens.register(ModScreenHandlers.MYTHICAL_FORGE, MythicalForgeScreen::new);

                // Register weapon aura particle renderer
                WeaponAuraRenderer.register();

                // Vanilla-compass needle for mythical compasses (points to structure
                // via the LodestonePos NBT the compass writes each tick).
                registerCompassAngle(ModItems.CAMELOT_COMPASS);
                registerCompassAngle(ModItems.VALHALLA_COMPASS);
                registerCompassAngle(ModItems.TRICKSTER_COMPASS);
                registerCompassAngle(ModItems.GREEK_COMPASS);
                registerCompassAngle(ModItems.BAMBOO_COMPASS);
                registerCompassAngle(ModItems.ONI_COMPASS);
                registerCompassAngle(ModItems.AZTEC_COMPASS);
                registerCompassAngle(ModItems.DESERT_COMPASS);
                registerCompassAngle(ModItems.CELESTIAL_COMPASS);

                MythicalSwords.LOGGER.info("Mythical Swords Client initialized!");
        }

        private static void registerCompassAngle(Item compass) {
                ModelPredicateProviderRegistry.register(
                        compass,
                        new Identifier("angle"),
                        new CompassAnglePredicateProvider((world, stack, entity) -> {
                                NbtCompound nbt = stack.getNbt();
                                if (nbt != null && nbt.contains("LodestonePos")) {
                                        BlockPos pos = NbtHelper.toBlockPos(nbt.getCompound("LodestonePos"));
                                        return GlobalPos.create(World.OVERWORLD, pos);
                                }
                                return null;
                        }));
        }
}