package com.mythicalswords.core;

import com.mythicalswords.MythicalSwords;
import com.mythicalswords.enchantments.BerserkerEnchantment;
import com.mythicalswords.enchantments.DivineStrikeEnchantment;
import com.mythicalswords.enchantments.ElementalEdgeEnchantment;
import com.mythicalswords.enchantments.FrostAuraEnchantment;
import com.mythicalswords.enchantments.LifestealEnchantment;
import com.mythicalswords.enchantments.SoulReaperEnchantment;
import com.mythicalswords.enchantments.ThunderCallerEnchantment;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModEnchantments {

    public static final Enchantment ELEMENTAL_EDGE = Registry.register(
        Registries.ENCHANTMENT,
        new Identifier(MythicalSwords.MOD_ID, "elemental_edge"),
        new ElementalEdgeEnchantment()
    );

    public static final Enchantment DIVINE_STRIKE = Registry.register(
        Registries.ENCHANTMENT,
        new Identifier(MythicalSwords.MOD_ID, "divine_strike"),
        new DivineStrikeEnchantment()
    );

    public static final Enchantment THUNDER_CALLER = Registry.register(
        Registries.ENCHANTMENT,
        new Identifier(MythicalSwords.MOD_ID, "thunder_caller"),
        new ThunderCallerEnchantment()
    );

    public static final Enchantment LIFESTEAL = Registry.register(
        Registries.ENCHANTMENT,
        new Identifier(MythicalSwords.MOD_ID, "lifesteal"),
        new LifestealEnchantment()
    );

    public static final Enchantment FROST_AURA = Registry.register(
        Registries.ENCHANTMENT,
        new Identifier(MythicalSwords.MOD_ID, "frost_aura"),
        new FrostAuraEnchantment()
    );

    public static final Enchantment SOUL_REAPER = Registry.register(
        Registries.ENCHANTMENT,
        new Identifier(MythicalSwords.MOD_ID, "soul_reaper"),
        new SoulReaperEnchantment()
    );

    public static final Enchantment BERSERKER = Registry.register(
        Registries.ENCHANTMENT,
        new Identifier(MythicalSwords.MOD_ID, "berserker"),
        new BerserkerEnchantment()
    );

    // ===== Forge-exclusive mythology enchantments (applied with rune materials) =====

    public static final Enchantment ODIN_RUNE = Registry.register(
        Registries.ENCHANTMENT,
        new Identifier(MythicalSwords.MOD_ID, "odin_rune"),
        new com.mythicalswords.enchantments.OdinRuneEnchantment()
    );

    public static final Enchantment IAIJUTSU = Registry.register(
        Registries.ENCHANTMENT,
        new Identifier(MythicalSwords.MOD_ID, "iaijutsu"),
        new com.mythicalswords.enchantments.IaijutsuEnchantment()
    );

    public static final Enchantment AEGIS_WARD = Registry.register(
        Registries.ENCHANTMENT,
        new Identifier(MythicalSwords.MOD_ID, "aegis_ward"),
        new com.mythicalswords.enchantments.AegisWardEnchantment()
    );

    public static final Enchantment ANUBIS_CURSE = Registry.register(
        Registries.ENCHANTMENT,
        new Identifier(MythicalSwords.MOD_ID, "anubis_curse"),
        new com.mythicalswords.enchantments.AnubisCurseEnchantment()
    );

    public static final Enchantment SOLAR_WRATH = Registry.register(
        Registries.ENCHANTMENT,
        new Identifier(MythicalSwords.MOD_ID, "solar_wrath"),
        new com.mythicalswords.enchantments.SolarWrathEnchantment()
    );

    public static final Enchantment BLOOD_THIRST = Registry.register(
        Registries.ENCHANTMENT,
        new Identifier(MythicalSwords.MOD_ID, "blood_thirst"),
        new com.mythicalswords.enchantments.BloodThirstEnchantment()
    );

    public static final Enchantment MONKEY_STEP = Registry.register(
        Registries.ENCHANTMENT,
        new Identifier(MythicalSwords.MOD_ID, "monkey_step"),
        new com.mythicalswords.enchantments.MonkeyStepEnchantment()
    );

    public static void register() {
        MythicalSwords.LOGGER.info("Registering ModEnchantments for " + MythicalSwords.MOD_ID);
    }
}
