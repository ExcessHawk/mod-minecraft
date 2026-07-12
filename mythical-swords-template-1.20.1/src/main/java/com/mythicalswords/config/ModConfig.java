package com.mythicalswords.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mythicalswords.MythicalSwords;
import net.fabricmc.loader.api.FabricLoader;

import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Simple JSON config (no external dependencies). Loaded once at mod init from
 * config/mythicalswords.json; the file is created with defaults on first run.
 * Delete a key (or the file) to get its default back.
 */
public class ModConfig {

    // Mythical Forge
    public int forgeMaxUpgrades = 5;
    public float forgeDamageBonusPerUpgrade = 0.15f;
    public float forgeCooldownReductionPerUpgrade = 0.10f;

    // Bosses (applied when a boss first spawns)
    public float bossHealthMultiplier = 1.0f;
    public float bossDamageMultiplier = 1.0f;

    // Weapon abilities
    public float abilityCooldownMultiplier = 1.0f;

    private static ModConfig instance;

    public static ModConfig get() {
        if (instance == null) load();
        return instance;
    }

    public static void load() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        Path path = FabricLoader.getInstance().getConfigDir().resolve("mythicalswords.json");
        try {
            if (Files.exists(path)) {
                instance = gson.fromJson(Files.readString(path), ModConfig.class);
                if (instance == null) instance = new ModConfig();
            } else {
                instance = new ModConfig();
                Files.writeString(path, gson.toJson(instance));
                MythicalSwords.LOGGER.info("Created default config at {}", path);
            }
        } catch (Exception e) {
            MythicalSwords.LOGGER.warn("Could not read config, using defaults: {}", e.getMessage());
            instance = new ModConfig();
        }
        instance.clamp();
    }

    private void clamp() {
        forgeMaxUpgrades = Math.max(0, Math.min(20, forgeMaxUpgrades));
        forgeDamageBonusPerUpgrade = clamp(forgeDamageBonusPerUpgrade, 0.0f, 2.0f);
        forgeCooldownReductionPerUpgrade = clamp(forgeCooldownReductionPerUpgrade, 0.0f, 0.18f);
        bossHealthMultiplier = clamp(bossHealthMultiplier, 0.1f, 10.0f);
        bossDamageMultiplier = clamp(bossDamageMultiplier, 0.1f, 10.0f);
        abilityCooldownMultiplier = clamp(abilityCooldownMultiplier, 0.1f, 10.0f);
    }

    private static float clamp(float v, float min, float max) {
        return Math.max(min, Math.min(max, v));
    }
}
