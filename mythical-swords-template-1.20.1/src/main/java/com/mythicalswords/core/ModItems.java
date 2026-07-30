package com.mythicalswords.core;

import com.mythicalswords.MythicalSwords;
import com.mythicalswords.materials.*;
import com.mythicalswords.materials.SoulSwordsmithItem;
import com.mythicalswords.materials.TamahaganeIngotItem;
import com.mythicalswords.materials.GemOfBishamonItem;
import com.mythicalswords.materials.SacredWaterOfAmaterasuItem;
import com.mythicalswords.materials.MangoLargoJaponesItem;
import com.mythicalswords.materials.JadeImperialIngotItem;
import com.mythicalswords.materials.DustOfLongevityItem;
import com.mythicalswords.materials.EssenceOfRighteousnessItem;
import com.mythicalswords.materials.SoulFragmentItem;
import com.mythicalswords.materials.LoversBondTokenItem;
import com.mythicalswords.materials.MoonstoneShardItem;
import com.mythicalswords.materials.AgnisFlameCore;
import com.mythicalswords.materials.VajraCrystal;
import com.mythicalswords.materials.BambooReinforcedShaft;
import com.mythicalswords.materials.BindingClothOfTheMonks;
import com.mythicalswords.materials.ObsidianaRitualShard;
import com.mythicalswords.weapons.ExcaliburItem;
import com.mythicalswords.weapons.GramItem;
import com.mythicalswords.weapons.SkofnungItem;
import com.mythicalswords.weapons.HofundItem;
import com.mythicalswords.weapons.HarpeItem;
import com.mythicalswords.weapons.XiphosSagradoItem;
import com.mythicalswords.weapons.NikeBladeItem;
import com.mythicalswords.weapons.GungnirItem;
import com.mythicalswords.weapons.LaevateinnItem;
import com.mythicalswords.weapons.AegisEdgeItem;
import com.mythicalswords.weapons.KusanagiItem;
import com.mythicalswords.weapons.MuramasaItem;
import com.mythicalswords.weapons.TotsukaItem;
import com.mythicalswords.weapons.XiuhcoatlItem;
import com.mythicalswords.weapons.KhopeshItem;
import com.mythicalswords.weapons.WasScepterItem;
import com.mythicalswords.weapons.RuyiJinguBangItem;
import com.mythicalswords.weapons.JianItem;
import com.mythicalswords.weapons.OrichalcumBladeItem;
import com.mythicalswords.weapons.UruWarhammerItem;
import com.mythicalswords.weapons.VoidsteelEdgeItem;
import com.mythicalswords.weapons.FroststeelSaberItem;
import com.mythicalswords.item.custom.MythicalCompassItem;
import com.mythicalswords.structures.ModStructures;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.util.Formatting;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import com.mythicalswords.relics.ConsumableRelicItem;
import com.mythicalswords.relics.PhoenixFeatherItem;
import com.mythicalswords.relics.VoidPearlItem;
import com.mythicalswords.relics.MedusaEyeItem;
import com.mythicalswords.relics.StormVialItem;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ModItems {

        // Mythical Materials
        public static final Item MYTHRIL_INGOT = registerItem("mythril_ingot",
                        new MythrilIngotItem());

        public static final Item RAW_MYTHRIL = registerItem("raw_mythril",
                        new RawMythrilItem());

        public static final Item RAW_NORTHSTEEL = registerItem("raw_northsteel",
                        new RawNorthsteelItem());

        // Crafting Materials (Weapon Components)
        public static final Item NORTHSTEEL_INGOT = registerItem("northsteel_ingot",
                        new NorthsteelIngotItem());

        public static final Item SUN_BLESSED_ALLOY = registerItem("sun_blessed_alloy",
                        new SunBlessedAlloyItem());

        public static final Item DRAGON_FANG_FRAGMENT = registerItem("dragon_fang_fragment",
                        new DragonFangFragmentItem());

        // Norse Special Materials
        public static final Item SPIRITBOUND_LEATHER = registerItem("spiritbound_leather",
                        new SpiritboundLeatherItem());

        public static final Item FROZEN_SOUL_CRYSTAL = registerItem("frozen_soul_crystal",
                        new FrozenSoulCrystalItem());

        // ===== New minerals (v0.5): raw + ingot =====
        public static final Item RAW_ORICHALCUM = registerItem("raw_orichalcum", new Item(new FabricItemSettings()));
        public static final Item ORICHALCUM_INGOT = registerItem("orichalcum_ingot", new Item(new FabricItemSettings()));
        public static final Item RAW_URU = registerItem("raw_uru", new Item(new FabricItemSettings()));
        public static final Item URU_INGOT = registerItem("uru_ingot", new Item(new FabricItemSettings()));
        public static final Item RAW_VOIDSTEEL = registerItem("raw_voidsteel", new Item(new FabricItemSettings()));
        public static final Item VOIDSTEEL_INGOT = registerItem("voidsteel_ingot", new Item(new FabricItemSettings()));
        public static final Item RAW_FROSTSTEEL = registerItem("raw_froststeel", new Item(new FabricItemSettings()));
        public static final Item FROSTSTEEL_INGOT = registerItem("froststeel_ingot", new Item(new FabricItemSettings()));

        // ===== Celestial endgame tier (forged from multi-boss materials) =====
        public static final Item CELESTIAL_INGOT = registerItem("celestial_ingot",
                        new Item(new FabricItemSettings()));

        /** Unique drop from the Celestial Guardian — the key to the dragon (F6). */
        public static final Item CELESTIAL_HEART = registerItem("celestial_heart",
                        new Item(new FabricItemSettings().maxCount(1).rarity(net.minecraft.util.Rarity.EPIC)));

        public static final Item CELESTIAL_HELMET = registerItem("celestial_helmet",
                        new net.minecraft.item.ArmorItem(com.mythicalswords.armor.ModArmorMaterials.CELESTIAL, net.minecraft.item.ArmorItem.Type.HELMET, new FabricItemSettings()));
        public static final Item CELESTIAL_CHESTPLATE = registerItem("celestial_chestplate",
                        new net.minecraft.item.ArmorItem(com.mythicalswords.armor.ModArmorMaterials.CELESTIAL, net.minecraft.item.ArmorItem.Type.CHESTPLATE, new FabricItemSettings()));
        public static final Item CELESTIAL_LEGGINGS = registerItem("celestial_leggings",
                        new net.minecraft.item.ArmorItem(com.mythicalswords.armor.ModArmorMaterials.CELESTIAL, net.minecraft.item.ArmorItem.Type.LEGGINGS, new FabricItemSettings()));
        public static final Item CELESTIAL_BOOTS = registerItem("celestial_boots",
                        new net.minecraft.item.ArmorItem(com.mythicalswords.armor.ModArmorMaterials.CELESTIAL, net.minecraft.item.ArmorItem.Type.BOOTS, new FabricItemSettings()));

        // ===== Mythic Armor Sets =====
        public static final Item ORICHALCUM_HELMET = registerItem("orichalcum_helmet",
                        new net.minecraft.item.ArmorItem(com.mythicalswords.armor.ModArmorMaterials.ORICHALCUM, net.minecraft.item.ArmorItem.Type.HELMET, new FabricItemSettings()));
        public static final Item ORICHALCUM_CHESTPLATE = registerItem("orichalcum_chestplate",
                        new net.minecraft.item.ArmorItem(com.mythicalswords.armor.ModArmorMaterials.ORICHALCUM, net.minecraft.item.ArmorItem.Type.CHESTPLATE, new FabricItemSettings()));
        public static final Item ORICHALCUM_LEGGINGS = registerItem("orichalcum_leggings",
                        new net.minecraft.item.ArmorItem(com.mythicalswords.armor.ModArmorMaterials.ORICHALCUM, net.minecraft.item.ArmorItem.Type.LEGGINGS, new FabricItemSettings()));
        public static final Item ORICHALCUM_BOOTS = registerItem("orichalcum_boots",
                        new net.minecraft.item.ArmorItem(com.mythicalswords.armor.ModArmorMaterials.ORICHALCUM, net.minecraft.item.ArmorItem.Type.BOOTS, new FabricItemSettings()));

        public static final Item URU_HELMET = registerItem("uru_helmet",
                        new net.minecraft.item.ArmorItem(com.mythicalswords.armor.ModArmorMaterials.URU, net.minecraft.item.ArmorItem.Type.HELMET, new FabricItemSettings()));
        public static final Item URU_CHESTPLATE = registerItem("uru_chestplate",
                        new net.minecraft.item.ArmorItem(com.mythicalswords.armor.ModArmorMaterials.URU, net.minecraft.item.ArmorItem.Type.CHESTPLATE, new FabricItemSettings()));
        public static final Item URU_LEGGINGS = registerItem("uru_leggings",
                        new net.minecraft.item.ArmorItem(com.mythicalswords.armor.ModArmorMaterials.URU, net.minecraft.item.ArmorItem.Type.LEGGINGS, new FabricItemSettings()));
        public static final Item URU_BOOTS = registerItem("uru_boots",
                        new net.minecraft.item.ArmorItem(com.mythicalswords.armor.ModArmorMaterials.URU, net.minecraft.item.ArmorItem.Type.BOOTS, new FabricItemSettings()));

        public static final Item VOIDSTEEL_HELMET = registerItem("voidsteel_helmet",
                        new net.minecraft.item.ArmorItem(com.mythicalswords.armor.ModArmorMaterials.VOIDSTEEL, net.minecraft.item.ArmorItem.Type.HELMET, new FabricItemSettings()));
        public static final Item VOIDSTEEL_CHESTPLATE = registerItem("voidsteel_chestplate",
                        new net.minecraft.item.ArmorItem(com.mythicalswords.armor.ModArmorMaterials.VOIDSTEEL, net.minecraft.item.ArmorItem.Type.CHESTPLATE, new FabricItemSettings()));
        public static final Item VOIDSTEEL_LEGGINGS = registerItem("voidsteel_leggings",
                        new net.minecraft.item.ArmorItem(com.mythicalswords.armor.ModArmorMaterials.VOIDSTEEL, net.minecraft.item.ArmorItem.Type.LEGGINGS, new FabricItemSettings()));
        public static final Item VOIDSTEEL_BOOTS = registerItem("voidsteel_boots",
                        new net.minecraft.item.ArmorItem(com.mythicalswords.armor.ModArmorMaterials.VOIDSTEEL, net.minecraft.item.ArmorItem.Type.BOOTS, new FabricItemSettings()));

        public static final Item FROSTSTEEL_HELMET = registerItem("froststeel_helmet",
                        new net.minecraft.item.ArmorItem(com.mythicalswords.armor.ModArmorMaterials.FROSTSTEEL, net.minecraft.item.ArmorItem.Type.HELMET, new FabricItemSettings()));
        public static final Item FROSTSTEEL_CHESTPLATE = registerItem("froststeel_chestplate",
                        new net.minecraft.item.ArmorItem(com.mythicalswords.armor.ModArmorMaterials.FROSTSTEEL, net.minecraft.item.ArmorItem.Type.CHESTPLATE, new FabricItemSettings()));
        public static final Item FROSTSTEEL_LEGGINGS = registerItem("froststeel_leggings",
                        new net.minecraft.item.ArmorItem(com.mythicalswords.armor.ModArmorMaterials.FROSTSTEEL, net.minecraft.item.ArmorItem.Type.LEGGINGS, new FabricItemSettings()));
        public static final Item FROSTSTEEL_BOOTS = registerItem("froststeel_boots",
                        new net.minecraft.item.ArmorItem(com.mythicalswords.armor.ModArmorMaterials.FROSTSTEEL, net.minecraft.item.ArmorItem.Type.BOOTS, new FabricItemSettings()));

        // ===== New-mineral weapons =====
        public static final Item ORICHALCUM_BLADE = registerItem("orichalcum_blade", new OrichalcumBladeItem());
        public static final Item URU_WARHAMMER = registerItem("uru_warhammer", new UruWarhammerItem());
        public static final Item VOIDSTEEL_EDGE = registerItem("voidsteel_edge", new VoidsteelEdgeItem());
        public static final Item FROSTSTEEL_SABER = registerItem("froststeel_saber", new FroststeelSaberItem());

        // ===== Relics & Consumables =====
        public static final Item AMBROSIA = registerItem("ambrosia",
                        new ConsumableRelicItem(new Item.Settings().maxCount(16), 8.0f, 100, ParticleTypes.HEART,
                                        java.util.List.of(
                                                        () -> new StatusEffectInstance(StatusEffects.REGENERATION, 200, 1),
                                                        () -> new StatusEffectInstance(StatusEffects.ABSORPTION, 1200, 0),
                                                        () -> new StatusEffectInstance(StatusEffects.SATURATION, 100, 0))));

        public static final Item PHOENIX_TEAR = registerItem("phoenix_tear",
                        new ConsumableRelicItem(new Item.Settings().maxCount(16), 20.0f, 200, ParticleTypes.FLAME,
                                        java.util.List.of(
                                                        () -> new StatusEffectInstance(StatusEffects.FIRE_RESISTANCE, 1200, 0),
                                                        () -> new StatusEffectInstance(StatusEffects.ABSORPTION, 1200, 1),
                                                        () -> new StatusEffectInstance(StatusEffects.REGENERATION, 100, 2))));

        public static final Item ELIXIR_OF_THE_GODS = registerItem("elixir_of_the_gods",
                        new ConsumableRelicItem(new Item.Settings().maxCount(8), 0.0f, 600, ParticleTypes.END_ROD,
                                        java.util.List.of(
                                                        () -> new StatusEffectInstance(StatusEffects.STRENGTH, 600, 1),
                                                        () -> new StatusEffectInstance(StatusEffects.RESISTANCE, 600, 1),
                                                        () -> new StatusEffectInstance(StatusEffects.REGENERATION, 300, 1),
                                                        () -> new StatusEffectInstance(StatusEffects.SPEED, 600, 1))));

        public static final Item PHOENIX_FEATHER = registerItem("phoenix_feather",
                        new PhoenixFeatherItem(new Item.Settings().maxCount(8).rarity(net.minecraft.util.Rarity.EPIC)));

        public static final Item VOID_PEARL = registerItem("void_pearl",
                        new VoidPearlItem(new Item.Settings().maxCount(16)));

        public static final Item TITAN_BREW = registerItem("titan_brew",
                        new ConsumableRelicItem(new Item.Settings().maxCount(16), 20.0f, 200, ParticleTypes.ENCHANTED_HIT,
                                        java.util.List.of(
                                                        () -> new StatusEffectInstance(StatusEffects.HEALTH_BOOST, 1200, 2),
                                                        () -> new StatusEffectInstance(StatusEffects.RESISTANCE, 1200, 1),
                                                        () -> new StatusEffectInstance(StatusEffects.STRENGTH, 1200, 0))));

        public static final Item HERMES_DRAUGHT = registerItem("hermes_draught",
                        new ConsumableRelicItem(new Item.Settings().maxCount(16), 0.0f, 100, ParticleTypes.CLOUD,
                                        java.util.List.of(
                                                        () -> new StatusEffectInstance(StatusEffects.SPEED, 300, 2),
                                                        () -> new StatusEffectInstance(StatusEffects.JUMP_BOOST, 300, 1),
                                                        () -> new StatusEffectInstance(StatusEffects.SLOW_FALLING, 300, 0))));

        public static final Item BERSERKER_DRAUGHT = registerItem("berserker_draught",
                        new ConsumableRelicItem(new Item.Settings().maxCount(16), 0.0f, 300, ParticleTypes.ANGRY_VILLAGER,
                                        java.util.List.of(
                                                        () -> new StatusEffectInstance(StatusEffects.STRENGTH, 400, 2),
                                                        () -> new StatusEffectInstance(StatusEffects.HASTE, 400, 1),
                                                        () -> new StatusEffectInstance(StatusEffects.SPEED, 400, 1),
                                                        () -> new StatusEffectInstance(StatusEffects.GLOWING, 400, 0))));

        public static final Item MEDUSA_EYE = registerItem("medusa_eye",
                        new MedusaEyeItem(new Item.Settings().maxCount(1).rarity(net.minecraft.util.Rarity.RARE)));

        public static final Item STORM_VIAL = registerItem("storm_vial",
                        new StormVialItem(new Item.Settings().maxCount(16)));

        public static final Item RAINBOW_BRIDGE_FRAGMENT = registerItem("rainbow_bridge_fragment",
                        new RainbowBridgeFragmentItem());

        // Greek Materials
        public static final Item RAW_SACRED_IRON = registerItem("raw_sacred_iron",
                        new RawSacredIronItem());

        public static final Item SACRED_IRON_INGOT = registerItem("sacred_iron_ingot",
                        new SacredIronIngotItem());

        // Greek Special Materials
        public static final Item SHARD_OF_DIVINITY = registerItem("shard_of_divinity",
                        new ShardOfDivinityItem());

        public static final Item FEATHER_OF_VICTORY = registerItem("feather_of_victory",
                        new FeatherOfVictoryItem());

        public static final Item BRONCE_BENDITO = registerItem("bronce_bendito",
                        new BronceBenditoItem());

        // Japanese Materials
        public static final Item RAW_TAMAHAGANE = registerItem("raw_tamahagane",
                        new RawTamahaganeItem());

        public static final Item TAMAHAGANE_INGOT = registerItem("tamahagane_ingot",
                        new TamahaganeIngotItem());

        // Japanese Special Materials
        public static final Item GEM_OF_BISHAMON = registerItem("gem_of_bishamon",
                        new GemOfBishamonItem());

        public static final Item SOUL_SWORDSMITH = registerItem("soul_swordsmith",
                        new SoulSwordsmithItem());

        public static final Item SACRED_WATER_OF_AMATERASU = registerItem("sacred_water_of_amaterasu",
                        new SacredWaterOfAmaterasuItem());

        public static final Item MANGO_LARGO_JAPONES = registerItem("mango_largo_japones",
                        new MangoLargoJaponesItem());

        // Mesoamerican Materials
        public static final Item RAW_OBSIDIANA_RITUAL = registerItem("raw_obsidiana_ritual",
                        new RawObsidianaRitualItem());

        public static final Item OBSIDIANA_RITUAL_SHARD = registerItem("obsidiana_ritual_shard",
                        new ObsidianaRitualShard());

        // Mesoamerican Special Materials
        public static final Item FILO_DE_PLUMA_DE_QUETZAL = registerItem("filo_de_pluma_de_quetzal",
                        new FiloDePlumaDeQuetzalItem());

        public static final Item PALO_RITUAL = registerItem("palo_ritual",
                        new PaloRitualItem());

        // Chinese Materials
        public static final Item RAW_JADE_IMPERIAL = registerItem("raw_jade_imperial",
                        new RawJadeImperialItem());

        public static final Item JADE_IMPERIAL_INGOT = registerItem("jade_imperial_ingot",
                        new JadeImperialIngotItem());

        // Chinese Special Materials
        public static final Item DUST_OF_LONGEVITY = registerItem("dust_of_longevity",
                        new DustOfLongevityItem());

        public static final Item ESSENCE_OF_RIGHTEOUSNESS = registerItem("essence_of_righteousness",
                        new EssenceOfRighteousnessItem());

        public static final Item SOUL_FRAGMENT = registerItem("soul_fragment",
                        new SoulFragmentItem());

        // Legendary Blacksmith's hammer (mini-boss weapon, 100% drop)
        public static final Item BLACKSMITH_HAMMER = registerItem("blacksmith_hammer",
                        new Item(new FabricItemSettings().maxCount(1)));

        // In-game guide book (opens GuideScreen)
        public static final Item GUIDE_BOOK = registerItem("guide_book",
                        new com.mythicalswords.item.custom.GuideBookItem(new FabricItemSettings().maxCount(1)));

        public static final Item LOVERS_BOND_TOKEN = registerItem("lovers_bond_token",
                        new LoversBondTokenItem());

        public static final Item MOONSTONE_SHARD = registerItem("moonstone_shard",
                        new MoonstoneShardItem());

        // Hindu Special Materials
        public static final Item AGNIS_FLAME_CORE = registerItem("agnis_flame_core",
                        new AgnisFlameCore());

        public static final Item VAJRA_CRYSTAL = registerItem("vajra_crystal",
                        new VajraCrystal());

        public static final Item BAMBOO_REINFORCED_SHAFT = registerItem("bamboo_reinforced_shaft",
                        new BambooReinforcedShaft());

        public static final Item BINDING_CLOTH_OF_THE_MONKS = registerItem("binding_cloth_of_the_monks",
                        new BindingClothOfTheMonks());

        // Compasses — each points to a mythology structure
        public static final Item CAMELOT_COMPASS = registerItem("camelot_compass",
                        new com.mythicalswords.item.custom.CamelotCompassItem(new FabricItemSettings().maxCount(1)));

        public static final Item VALHALLA_COMPASS = registerItem("valhalla_compass",
                        new MythicalCompassItem(new FabricItemSettings().maxCount(1),
                            ModStructures.VALHALLA, "Valhalla Hall", Formatting.AQUA));

        public static final Item TRICKSTER_COMPASS = registerItem("trickster_compass",
                        new MythicalCompassItem(new FabricItemSettings().maxCount(1),
                            ModStructures.TRICKSTER, "Trickster's Cave", Formatting.DARK_PURPLE));

        public static final Item GREEK_COMPASS = registerItem("greek_compass",
                        new MythicalCompassItem(new FabricItemSettings().maxCount(1),
                            ModStructures.GREEK, "Greek Temple", Formatting.GOLD));

        public static final Item BAMBOO_COMPASS = registerItem("bamboo_compass",
                        new MythicalCompassItem(new FabricItemSettings().maxCount(1),
                            ModStructures.BAMBOO, "Bamboo Temple", Formatting.GREEN));

        public static final Item ONI_COMPASS = registerItem("oni_compass",
                        new MythicalCompassItem(new FabricItemSettings().maxCount(1),
                            ModStructures.ONI, "Oni Fortress", Formatting.RED));

        public static final Item AZTEC_COMPASS = registerItem("aztec_compass",
                        new MythicalCompassItem(new FabricItemSettings().maxCount(1),
                            ModStructures.AZTEC, "Aztec Pyramid", Formatting.YELLOW));

        public static final Item DESERT_COMPASS = registerItem("desert_compass",
                        new MythicalCompassItem(new FabricItemSettings().maxCount(1),
                            ModStructures.DESERT, "Desert Tomb", Formatting.GOLD));

        public static final Item CELESTIAL_COMPASS = registerItem("celestial_compass",
                        new MythicalCompassItem(new FabricItemSettings().maxCount(1),
                            ModStructures.CELESTIAL, "Celestial Palace", Formatting.LIGHT_PURPLE));

        // Mythical Weapons
        public static final Item GRAM = registerItem("gram",
                        new GramItem());

        public static final Item EXCALIBUR = registerItem("excalibur",
                        new ExcaliburItem());

        // Norse Weapons
        public static final Item SKOFNUNG = registerItem("skofnung",
                        new SkofnungItem());

        public static final Item HOFUND = registerItem("hofund",
                        new HofundItem());

        // Greek Weapons
        public static final Item HARPE = registerItem("harpe",
                        new HarpeItem());

        public static final Item XIPHOS_SAGRADO = registerItem("xiphos_sagrado",
                        new XiphosSagradoItem());

        public static final Item NIKE_BLADE = registerItem("nike_blade",
                        new NikeBladeItem());

        public static final Item AEGIS_EDGE = registerItem("aegis_edge",
                        new AegisEdgeItem());

        // Japanese Weapons
        public static final Item KUSANAGI_NO_TSURUGI = registerItem("kusanagi_no_tsurugi",
                        new KusanagiItem());

        public static final Item MURAMASA = registerItem("muramasa",
                        new MuramasaItem());

        public static final Item TOTSUKA_NO_TSURUGI = registerItem("totsuka_no_tsurugi",
                        new TotsukaItem());

        // Mesoamerican Weapons
        public static final Item XIUHCOATL = registerItem("xiuhcoatl",
                        new XiuhcoatlItem());

        // Legendary Boss-Drop Weapons
        public static final Item GUNGNIR = registerItem("gungnir",
                        new GungnirItem());

        public static final Item LAEVATEINN = registerItem("laevateinn",
                        new LaevateinnItem());

        // Phase 4: Egyptian Weapons
        public static final Item KHOPESH = registerItem("khopesh",
                        new KhopeshItem());

        public static final Item WAS_SCEPTER = registerItem("was_scepter",
                        new WasScepterItem());

        // Phase 4: Chinese Weapons
        public static final Item RUYI_JINGU_BANG = registerItem("ruyi_jingu_bang",
                        new RuyiJinguBangItem());

        public static final Item JIAN = registerItem("jian",
                        new JianItem());

        // Spawn Eggs
        public static final Item REY_ARTURO_SPAWN_EGG = registerItem("rey_arturo_spawn_egg",
                        new SpawnEggItem(ModEntities.REY_ARTURO, 0x996600, 0xFFD700, new FabricItemSettings()));

        public static final Item ODIN_SPAWN_EGG = registerItem("odin_spawn_egg",
                        new SpawnEggItem(ModEntities.ODIN, 0x00008B, 0xC0C0C0, new FabricItemSettings()));

        public static final Item LOKI_SPAWN_EGG = registerItem("loki_spawn_egg",
                        new SpawnEggItem(ModEntities.LOKI, 0x006400, 0xFFD700, new FabricItemSettings()));

        public static final Item ATENEA_SPAWN_EGG = registerItem("atenea_spawn_egg",
                        new SpawnEggItem(ModEntities.ATENEA, 0xFFFFFF, 0xFFD700, new FabricItemSettings()));

        public static final Item SUSANOO_SPAWN_EGG = registerItem("susanoo_spawn_egg",
                        new SpawnEggItem(ModEntities.SUSANOO, 0x00BFFF, 0x696969, new FabricItemSettings()));

        public static final Item ONI_OSCURO_SPAWN_EGG = registerItem("oni_oscuro_spawn_egg",
                        new SpawnEggItem(ModEntities.ONI_OSCURO, 0x8B0000, 0x000000, new FabricItemSettings()));

        public static final Item IZANAGI_SPAWN_EGG = registerItem("izanagi_spawn_egg",
                        new SpawnEggItem(ModEntities.IZANAGI, 0xFFFFFF, 0xE0FFFF, new FabricItemSettings()));

        public static final Item QUETZALCOATL_SPAWN_EGG = registerItem("quetzalcoatl_spawn_egg",
                        new SpawnEggItem(ModEntities.QUETZALCOATL, 0x00FF00, 0xFFD700, new FabricItemSettings()));

        public static final Item LEGENDARY_BLACKSMITH_SPAWN_EGG = registerItem("legendary_blacksmith_spawn_egg",
                        new SpawnEggItem(ModEntities.LEGENDARY_BLACKSMITH, 0x8B4513, 0xFF4500,
                                        new FabricItemSettings()));

        // Phase 4: Egyptian Boss Spawn Eggs
        public static final Item ANUBIS_SPAWN_EGG = registerItem("anubis_spawn_egg",
                        new SpawnEggItem(ModEntities.ANUBIS, 0x000000, 0xFFD700, new FabricItemSettings()));

        public static final Item RIDEABLE_DRAGON_SPAWN_EGG = registerItem("rideable_dragon_spawn_egg",
                        new SpawnEggItem(ModEntities.RIDEABLE_DRAGON, 0x2a2a3a, 0x9a3acc, new FabricItemSettings()));

        public static final Item RA_SPAWN_EGG = registerItem("ra_spawn_egg",
                        new SpawnEggItem(ModEntities.RA, 0xFFD700, 0xFF4500, new FabricItemSettings()));

        public static final Item CELESTIAL_GUARDIAN_SPAWN_EGG = registerItem("celestial_guardian_spawn_egg",
                        new SpawnEggItem(ModEntities.CELESTIAL_GUARDIAN, 0xE8D48A, 0x9BE8FF, new FabricItemSettings()));

        // Minion Spawn Eggs (v0.6 F3)
        public static final Item DRAUGR_SPAWN_EGG = registerItem("draugr_spawn_egg",
                        new SpawnEggItem(ModEntities.DRAUGR, 0x8FA3AD, 0x28323A, new FabricItemSettings()));
        public static final Item ONI_MENOR_SPAWN_EGG = registerItem("oni_menor_spawn_egg",
                        new SpawnEggItem(ModEntities.ONI_MENOR, 0xC53F3F, 0xFFD75E, new FabricItemSettings()));
        public static final Item MOMIA_SIRVIENTE_SPAWN_EGG = registerItem("momia_sirviente_spawn_egg",
                        new SpawnEggItem(ModEntities.MOMIA_SIRVIENTE, 0xD8C9A3, 0x8A7A55, new FabricItemSettings()));
        public static final Item GUERRERO_JAGUAR_SPAWN_EGG = registerItem("guerrero_jaguar_spawn_egg",
                        new SpawnEggItem(ModEntities.GUERRERO_JAGUAR, 0xD8A03F, 0x4A3612, new FabricItemSettings()));
        public static final Item HOPLITA_ESPECTRAL_SPAWN_EGG = registerItem("hoplita_espectral_spawn_egg",
                        new SpawnEggItem(ModEntities.HOPLITA_ESPECTRAL, 0x9ADFD4, 0x2F5049, new FabricItemSettings()));
        public static final Item SOLDADO_TERRACOTA_SPAWN_EGG = registerItem("soldado_terracota_spawn_egg",
                        new SpawnEggItem(ModEntities.SOLDADO_TERRACOTA, 0xB5836B, 0x6E4A36, new FabricItemSettings()));

        // Phase 4: Chinese Boss Spawn Egg
        public static final Item SUN_WUKONG_SPAWN_EGG = registerItem("sun_wukong_spawn_egg",
                        new SpawnEggItem(ModEntities.SUN_WUKONG, 0x8B4513, 0xFFD700, new FabricItemSettings()));

        // Custom creative tab for Mythical Swords
        public static final RegistryKey<ItemGroup> MYTHICAL_SWORDS_GROUP = RegistryKey.of(
                        RegistryKeys.ITEM_GROUP,
                        new Identifier(MythicalSwords.MOD_ID, "mythical_swords"));

        /**
         * Helper method to register items
         */
        private static Item registerItem(String name, Item item) {
                return Registry.register(
                                Registries.ITEM,
                                new Identifier(MythicalSwords.MOD_ID, name),
                                item);
        }

        /**
         * Register the creative tab
         */
        public static void registerItemGroup() {
                Registry.register(Registries.ITEM_GROUP, MYTHICAL_SWORDS_GROUP,
                                FabricItemGroup.builder()
                                                .icon(() -> new ItemStack(EXCALIBUR))
                                                .displayName(Text.translatable(
                                                                "itemGroup.mythicalswords.mythical_swords"))
                                                .build());

                // Add items to the custom tab
                ItemGroupEvents.modifyEntriesEvent(MYTHICAL_SWORDS_GROUP).register(content -> {
                                // Ores and Base Materials
                        content.add(ModBlocks.MYTHRIL_ORE);
                        content.add(RAW_MYTHRIL);
                        content.add(ModBlocks.NORTHSTEEL_ORE);
                        content.add(RAW_NORTHSTEEL);
                        content.add(ModBlocks.SACRED_IRON_ORE);
                        content.add(RAW_SACRED_IRON);
                        content.add(ModBlocks.TAMAHAGANE_ORE);
                        content.add(RAW_TAMAHAGANE);
                        content.add(ModBlocks.JADE_IMPERIAL_ORE);
                        content.add(RAW_JADE_IMPERIAL);
                        content.add(ModBlocks.ORICHALCUM_ORE);
                        content.add(RAW_ORICHALCUM);
                        content.add(ORICHALCUM_INGOT);
                        content.add(ModBlocks.URU_ORE);
                        content.add(RAW_URU);
                        content.add(URU_INGOT);
                        content.add(ModBlocks.VOIDSTEEL_ORE);
                        content.add(RAW_VOIDSTEEL);
                        content.add(VOIDSTEEL_INGOT);
                        content.add(ModBlocks.FROSTSTEEL_ORE);
                        content.add(RAW_FROSTSTEEL);
                        content.add(FROSTSTEEL_INGOT);
                        content.add(MYTHRIL_INGOT);
                        content.add(SACRED_IRON_INGOT);
                        content.add(TAMAHAGANE_INGOT);
                        content.add(JADE_IMPERIAL_INGOT);
                        content.add(ModBlocks.OBSIDIANA_RITUAL_ORE);
                        content.add(OBSIDIANA_RITUAL_SHARD);

                        // Special Blocks
                        content.add(ModBlocks.MYTHICAL_FORGE);
                        content.add(ModBlocks.BOSS_ALTAR);
                        content.add(ModBlocks.CELESTIAL_STONE);
                        content.add(ModBlocks.CELESTIAL_BRICKS);
                        content.add(ModBlocks.CELESTIAL_PORTAL_FRAME);
                        content.add(BLACKSMITH_HAMMER);
                        content.add(GUIDE_BOOK);

                        // Compasses
                        content.add(CAMELOT_COMPASS);
                        content.add(VALHALLA_COMPASS);
                        content.add(TRICKSTER_COMPASS);
                        content.add(GREEK_COMPASS);
                        content.add(BAMBOO_COMPASS);
                        content.add(ONI_COMPASS);
                        content.add(AZTEC_COMPASS);
                        content.add(DESERT_COMPASS);
                        content.add(CELESTIAL_COMPASS);

                        // Crafting Materials (Components)
                        content.add(NORTHSTEEL_INGOT);
                        content.add(SUN_BLESSED_ALLOY);
                        content.add(DRAGON_FANG_FRAGMENT);

                        // Norse Special Materials
                        content.add(SPIRITBOUND_LEATHER);
                        content.add(FROZEN_SOUL_CRYSTAL);
                        content.add(RAINBOW_BRIDGE_FRAGMENT);

                        // Greek Special Materials
                        content.add(SHARD_OF_DIVINITY);
                        content.add(FEATHER_OF_VICTORY);
                        content.add(BRONCE_BENDITO);

                        // Japanese Special Materials
                        content.add(GEM_OF_BISHAMON);
                        content.add(SOUL_SWORDSMITH);
                        content.add(SACRED_WATER_OF_AMATERASU);
                        content.add(MANGO_LARGO_JAPONES);

                        // Chinese Special Materials
                        content.add(DUST_OF_LONGEVITY);
                        content.add(ESSENCE_OF_RIGHTEOUSNESS);
                        content.add(SOUL_FRAGMENT);
                        content.add(LOVERS_BOND_TOKEN);
                        content.add(MOONSTONE_SHARD);

                        // Hindu Special Materials
                        content.add(AGNIS_FLAME_CORE);
                        content.add(VAJRA_CRYSTAL);
                        content.add(BAMBOO_REINFORCED_SHAFT);
                        content.add(BINDING_CLOTH_OF_THE_MONKS);

                        // Weapons
                        content.add(GRAM);
                        content.add(EXCALIBUR);
                        content.add(SKOFNUNG);
                        content.add(HOFUND);
                        content.add(HARPE);
                        content.add(XIPHOS_SAGRADO);
                        content.add(NIKE_BLADE);
                        content.add(GUNGNIR);
                        content.add(LAEVATEINN);
                        content.add(AEGIS_EDGE);
                        content.add(KUSANAGI_NO_TSURUGI);
                        content.add(MURAMASA);
                        content.add(TOTSUKA_NO_TSURUGI);
                        content.add(XIUHCOATL);

                        // Phase 4 Weapons
                        content.add(KHOPESH);
                        content.add(WAS_SCEPTER);
                        content.add(RUYI_JINGU_BANG);
                        content.add(JIAN);
                        content.add(ORICHALCUM_BLADE);
                        content.add(URU_WARHAMMER);
                        content.add(VOIDSTEEL_EDGE);
                        content.add(FROSTSTEEL_SABER);

                        // Mythic Armor
                        content.add(ORICHALCUM_HELMET); content.add(ORICHALCUM_CHESTPLATE); content.add(ORICHALCUM_LEGGINGS); content.add(ORICHALCUM_BOOTS);
                        content.add(URU_HELMET); content.add(URU_CHESTPLATE); content.add(URU_LEGGINGS); content.add(URU_BOOTS);
                        content.add(VOIDSTEEL_HELMET); content.add(VOIDSTEEL_CHESTPLATE); content.add(VOIDSTEEL_LEGGINGS); content.add(VOIDSTEEL_BOOTS);
                        content.add(FROSTSTEEL_HELMET); content.add(FROSTSTEEL_CHESTPLATE); content.add(FROSTSTEEL_LEGGINGS); content.add(FROSTSTEEL_BOOTS);
                        content.add(CELESTIAL_INGOT);
                        content.add(CELESTIAL_HEART);
                        content.add(CELESTIAL_HELMET); content.add(CELESTIAL_CHESTPLATE); content.add(CELESTIAL_LEGGINGS); content.add(CELESTIAL_BOOTS);

                        // Relics & Consumables
                        content.add(AMBROSIA);
                        content.add(PHOENIX_TEAR);
                        content.add(ELIXIR_OF_THE_GODS);
                        content.add(PHOENIX_FEATHER);
                        content.add(VOID_PEARL);
                        content.add(TITAN_BREW);
                        content.add(HERMES_DRAUGHT);
                        content.add(BERSERKER_DRAUGHT);
                        content.add(MEDUSA_EYE);
                        content.add(STORM_VIAL);

                        // Spawn Eggs
                        content.add(REY_ARTURO_SPAWN_EGG);
                        content.add(ODIN_SPAWN_EGG);
                        content.add(LOKI_SPAWN_EGG);
                        content.add(ATENEA_SPAWN_EGG);
                        content.add(SUSANOO_SPAWN_EGG);
                        content.add(ONI_OSCURO_SPAWN_EGG);
                        content.add(IZANAGI_SPAWN_EGG);
                        content.add(QUETZALCOATL_SPAWN_EGG);
                        content.add(LEGENDARY_BLACKSMITH_SPAWN_EGG);

                        // Phase 4 Spawn Eggs
                        content.add(ANUBIS_SPAWN_EGG);
                        // Rideable dragon hidden for now (registered but not shown in creative tab)
                        // content.add(RIDEABLE_DRAGON_SPAWN_EGG);
                        content.add(RA_SPAWN_EGG);
                        content.add(SUN_WUKONG_SPAWN_EGG);
                        content.add(CELESTIAL_GUARDIAN_SPAWN_EGG);
                        content.add(DRAUGR_SPAWN_EGG);
                        content.add(ONI_MENOR_SPAWN_EGG);
                        content.add(MOMIA_SIRVIENTE_SPAWN_EGG);
                        content.add(GUERRERO_JAGUAR_SPAWN_EGG);
                        content.add(HOPLITA_ESPECTRAL_SPAWN_EGG);
                        content.add(SOLDADO_TERRACOTA_SPAWN_EGG);
                });

                MythicalSwords.LOGGER.info("Registered Mythical Swords creative tab");
        }

        /**
         * Initialize and register all items
         */
        public static void register() {
                MythicalSwords.LOGGER.info("Registering items for " + MythicalSwords.MOD_ID);
                registerItemGroup();
        }
}
