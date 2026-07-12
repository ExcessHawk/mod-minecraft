package com.mythicalswords.core;

import com.mythicalswords.MythicalSwords;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

public class ModSounds {

    public static final SoundEvent WEAPON_SWING_LEGENDARY = register("weapon.swing.legendary");
    public static final SoundEvent WEAPON_SWING_RARE     = register("weapon.swing.rare");
    public static final SoundEvent WEAPON_LEVEL_UP       = register("weapon.level_up");
    public static final SoundEvent ABILITY_ACTIVATE      = register("ability.activate");
    public static final SoundEvent BOSS_SPAWN            = register("boss.spawn");
    public static final SoundEvent BOSS_DEATH            = register("boss.death");
    public static final SoundEvent BOSS_PHASE_TRANSITION = register("boss.phase_transition");
    public static final SoundEvent FORGE_OPERATE         = register("forge.operate");

    private static SoundEvent register(String name) {
        Identifier id = new Identifier(MythicalSwords.MOD_ID, name);
        return Registry.register(Registries.SOUND_EVENT, id, SoundEvent.of(id));
    }

    public static void register() {
        MythicalSwords.LOGGER.info("Registering ModSounds for " + MythicalSwords.MOD_ID);
    }
}
