package com.mythicalswords.core;

import com.mythicalswords.MythicalSwords;
import com.mythicalswords.entity.ReyArturoEntity;
import com.mythicalswords.entity.OdinEntity;
import com.mythicalswords.entity.LokiEntity;
import com.mythicalswords.entity.AteneaEntity;
import com.mythicalswords.entity.SusanooEntity;
import com.mythicalswords.entity.OniOscuroEntity;
import com.mythicalswords.entity.IzanagiEntity;
import com.mythicalswords.entity.QuetzalcoatlEntity;
import com.mythicalswords.entity.LegendaryBlacksmithEntity;
import com.mythicalswords.entity.AnubisEntity;
import com.mythicalswords.entity.RaEntity;
import com.mythicalswords.entity.SunWukongEntity;
import com.mythicalswords.entity.DraugrEntity;
import com.mythicalswords.entity.OniMenorEntity;
import com.mythicalswords.entity.MomiaSirvienteEntity;
import com.mythicalswords.entity.GuerreroJaguarEntity;
import com.mythicalswords.entity.HoplitaEspectralEntity;
import com.mythicalswords.entity.SoldadoTerracotaEntity;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.item.Item;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModEntities {

        // Rideable Dragon mount
        public static final EntityType<com.mythicalswords.entity.RideableDragonEntity> RIDEABLE_DRAGON = Registry.register(
                        Registries.ENTITY_TYPE,
                        new Identifier(MythicalSwords.MOD_ID, "rideable_dragon"),
                        FabricEntityTypeBuilder.<com.mythicalswords.entity.RideableDragonEntity>create(
                                        SpawnGroup.CREATURE, com.mythicalswords.entity.RideableDragonEntity::new)
                                        .dimensions(EntityDimensions.fixed(2.2f, 2.2f))
                                        .build());

        // Rey Arturo Boss Entity
        public static final EntityType<ReyArturoEntity> REY_ARTURO = Registry.register(
                        Registries.ENTITY_TYPE,
                        new Identifier(MythicalSwords.MOD_ID, "rey_arturo"),
                        FabricEntityTypeBuilder.<ReyArturoEntity>create(SpawnGroup.MONSTER, ReyArturoEntity::new)
                                        .dimensions(EntityDimensions.fixed(0.6f, 1.95f))
                                        .build());

        // Odín Boss Entity
        public static final EntityType<OdinEntity> ODIN = Registry.register(
                        Registries.ENTITY_TYPE,
                        new Identifier(MythicalSwords.MOD_ID, "odin"),
                        FabricEntityTypeBuilder.<OdinEntity>create(SpawnGroup.MONSTER, OdinEntity::new)
                                        .dimensions(EntityDimensions.fixed(0.6f, 1.95f))
                                        .build());

        // Loki Boss Entity
        public static final EntityType<LokiEntity> LOKI = Registry.register(
                        Registries.ENTITY_TYPE,
                        new Identifier(MythicalSwords.MOD_ID, "loki"),
                        FabricEntityTypeBuilder.<LokiEntity>create(SpawnGroup.MONSTER, LokiEntity::new)
                                        .dimensions(EntityDimensions.fixed(0.6f, 1.95f))
                                        .build());

        // Atenea Boss Entity
        public static final EntityType<AteneaEntity> ATENEA = Registry.register(
                        Registries.ENTITY_TYPE,
                        new Identifier(MythicalSwords.MOD_ID, "atenea"),
                        FabricEntityTypeBuilder.<AteneaEntity>create(SpawnGroup.MONSTER, AteneaEntity::new)
                                        .dimensions(EntityDimensions.fixed(0.6f, 1.95f))
                                        .build());

        // Japanese Bosses
        public static final EntityType<SusanooEntity> SUSANOO = Registry.register(
                        Registries.ENTITY_TYPE,
                        new Identifier(MythicalSwords.MOD_ID, "susanoo"),
                        FabricEntityTypeBuilder.<SusanooEntity>create(SpawnGroup.MONSTER, SusanooEntity::new)
                                        .dimensions(EntityDimensions.fixed(0.6f, 1.95f))
                                        .build());

        public static final EntityType<OniOscuroEntity> ONI_OSCURO = Registry.register(
                        Registries.ENTITY_TYPE,
                        new Identifier(MythicalSwords.MOD_ID, "oni_oscuro"),
                        FabricEntityTypeBuilder.<OniOscuroEntity>create(SpawnGroup.MONSTER, OniOscuroEntity::new)
                                        .dimensions(EntityDimensions.fixed(0.8f, 2.2f))
                                        .build());

        public static final EntityType<IzanagiEntity> IZANAGI = Registry.register(
                        Registries.ENTITY_TYPE,
                        new Identifier(MythicalSwords.MOD_ID, "izanagi"),
                        FabricEntityTypeBuilder.<IzanagiEntity>create(SpawnGroup.MONSTER, IzanagiEntity::new)
                                        .dimensions(EntityDimensions.fixed(0.6f, 1.95f))
                                        .build());

        // Mesoamerican Boss
        public static final EntityType<QuetzalcoatlEntity> QUETZALCOATL = Registry.register(
                        Registries.ENTITY_TYPE,
                        new Identifier(MythicalSwords.MOD_ID, "quetzalcoatl"),
                        FabricEntityTypeBuilder.<QuetzalcoatlEntity>create(SpawnGroup.MONSTER, QuetzalcoatlEntity::new)
                                        .dimensions(EntityDimensions.fixed(1.0f, 1.0f)) // Flying serpent dimensions
                                        .build());

        // Phase 4: Egyptian Bosses
        public static final EntityType<AnubisEntity> ANUBIS = Registry.register(
                        Registries.ENTITY_TYPE,
                        new Identifier(MythicalSwords.MOD_ID, "anubis"),
                        FabricEntityTypeBuilder.<AnubisEntity>create(SpawnGroup.MONSTER, AnubisEntity::new)
                                        .dimensions(EntityDimensions.fixed(0.6f, 1.95f))
                                        .build());

        public static final EntityType<RaEntity> RA = Registry.register(
                        Registries.ENTITY_TYPE,
                        new Identifier(MythicalSwords.MOD_ID, "ra"),
                        FabricEntityTypeBuilder.<RaEntity>create(SpawnGroup.MONSTER, RaEntity::new)
                                        .dimensions(EntityDimensions.fixed(0.6f, 1.95f))
                                        .build());

        // Phase 4: Chinese Boss
        public static final EntityType<SunWukongEntity> SUN_WUKONG = Registry.register(
                        Registries.ENTITY_TYPE,
                        new Identifier(MythicalSwords.MOD_ID, "sun_wukong"),
                        FabricEntityTypeBuilder.<SunWukongEntity>create(SpawnGroup.MONSTER, SunWukongEntity::new)
                                        .dimensions(EntityDimensions.fixed(0.6f, 1.95f))
                                        .build());

        // Mini-Bosses
        public static final EntityType<LegendaryBlacksmithEntity> LEGENDARY_BLACKSMITH = Registry.register(
                        Registries.ENTITY_TYPE,
                        new Identifier(MythicalSwords.MOD_ID, "legendary_blacksmith"),
                        FabricEntityTypeBuilder
                                        .<LegendaryBlacksmithEntity>create(SpawnGroup.MONSTER,
                                                        LegendaryBlacksmithEntity::new)
                                        .dimensions(EntityDimensions.fixed(0.6f, 1.95f))
                                        .build());

        // ===== Mythology minions (v0.6 F3) =====
        public static final EntityType<DraugrEntity> DRAUGR = registerMinion("draugr", DraugrEntity::new);
        public static final EntityType<OniMenorEntity> ONI_MENOR = registerMinion("oni_menor", OniMenorEntity::new);
        public static final EntityType<MomiaSirvienteEntity> MOMIA_SIRVIENTE = registerMinion("momia_sirviente", MomiaSirvienteEntity::new);
        public static final EntityType<GuerreroJaguarEntity> GUERRERO_JAGUAR = registerMinion("guerrero_jaguar", GuerreroJaguarEntity::new);
        public static final EntityType<HoplitaEspectralEntity> HOPLITA_ESPECTRAL = registerMinion("hoplita_espectral", HoplitaEspectralEntity::new);
        public static final EntityType<SoldadoTerracotaEntity> SOLDADO_TERRACOTA = registerMinion("soldado_terracota", SoldadoTerracotaEntity::new);

        private static <T extends net.minecraft.entity.mob.HostileEntity> EntityType<T> registerMinion(
                        String name, net.minecraft.entity.EntityType.EntityFactory<T> factory) {
                return Registry.register(Registries.ENTITY_TYPE,
                                new Identifier(MythicalSwords.MOD_ID, name),
                                FabricEntityTypeBuilder.create(SpawnGroup.MONSTER, factory)
                                                .dimensions(EntityDimensions.fixed(0.6f, 1.9f))
                                                .build());
        }

        /**
         * Register entity attributes
         */
        public static void register() {
                MythicalSwords.LOGGER.info("Registering entities for " + MythicalSwords.MOD_ID);

                // Register entity attributes
                FabricDefaultAttributeRegistry.register(RIDEABLE_DRAGON,
                                com.mythicalswords.entity.RideableDragonEntity.createDragonAttributes());
                FabricDefaultAttributeRegistry.register(REY_ARTURO, ReyArturoEntity.createReyArturoAttributes());
                FabricDefaultAttributeRegistry.register(ODIN, OdinEntity.createOdinAttributes());
                FabricDefaultAttributeRegistry.register(LOKI, LokiEntity.createLokiAttributes());
                FabricDefaultAttributeRegistry.register(ATENEA, AteneaEntity.createAteneaAttributes());
                FabricDefaultAttributeRegistry.register(SUSANOO, SusanooEntity.createSusanooAttributes());
                FabricDefaultAttributeRegistry.register(ONI_OSCURO, OniOscuroEntity.createOniOscuroAttributes());
                FabricDefaultAttributeRegistry.register(IZANAGI, IzanagiEntity.createIzanagiAttributes());
                FabricDefaultAttributeRegistry.register(QUETZALCOATL,
                                QuetzalcoatlEntity.createQuetzalcoatlAttributes());
                FabricDefaultAttributeRegistry.register(LEGENDARY_BLACKSMITH,
                                LegendaryBlacksmithEntity.createLegendaryBlacksmithAttributes());

                // Phase 4 bosses
                FabricDefaultAttributeRegistry.register(ANUBIS, AnubisEntity.createAnubisAttributes());
                FabricDefaultAttributeRegistry.register(RA, RaEntity.createRaAttributes());
                FabricDefaultAttributeRegistry.register(SUN_WUKONG, SunWukongEntity.createSunWukongAttributes());

                // Mythology minions
                FabricDefaultAttributeRegistry.register(DRAUGR, DraugrEntity.createDraugrAttributes());
                FabricDefaultAttributeRegistry.register(ONI_MENOR, OniMenorEntity.createOniMenorAttributes());
                FabricDefaultAttributeRegistry.register(MOMIA_SIRVIENTE, MomiaSirvienteEntity.createMomiaAttributes());
                FabricDefaultAttributeRegistry.register(GUERRERO_JAGUAR, GuerreroJaguarEntity.createGuerreroJaguarAttributes());
                FabricDefaultAttributeRegistry.register(HOPLITA_ESPECTRAL, HoplitaEspectralEntity.createHoplitaAttributes());
                FabricDefaultAttributeRegistry.register(SOLDADO_TERRACOTA, SoldadoTerracotaEntity.createSoldadoTerracotaAttributes());
        }
}
