package com.mythicalswords.events;

import com.mythicalswords.core.ModSounds;
import com.mythicalswords.entity.MythicalBossEntity;
import com.mythicalswords.systems.WeaponLevelingSystem;
import com.mythicalswords.weapons.MythicalWeaponItem;
import com.mythicalswords.weapons.WeaponTier;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class WeaponEvents {

    public static void register() {
        ServerLivingEntityEvents.AFTER_DEATH.register((entity, damageSource) -> {
            handleMobKill(entity, damageSource);
        });
    }

    private static void handleMobKill(LivingEntity entity, DamageSource damageSource) {
        if (!(damageSource.getAttacker() instanceof PlayerEntity player)) return;

        ItemStack mainHandStack = player.getMainHandStack();
        if (!(mainHandStack.getItem() instanceof MythicalWeaponItem)) return;

        int xpToAward = getXpForMob(entity);
        boolean leveledUp = WeaponLevelingSystem.addExperience(mainHandStack, xpToAward);

        if (leveledUp) {
            handleLevelUp(player, mainHandStack);
        }
    }

    private static int getXpForMob(LivingEntity entity) {
        // Mod bosses give huge XP scaled by their tier
        if (entity instanceof MythicalBossEntity boss) {
            float maxHp = boss.getMaxHealth();
            if (maxHp >= 1500) return 1000;      // top-tier bosses (SunWukong 800hp = 1600 hearts... wait, hp not hearts)
            if (maxHp >= 1200) return 750;
            if (maxHp >= 900)  return 500;
            if (maxHp >= 600)  return 300;
            return 200;
        }

        EntityType<?> type = entity.getType();

        // Vanilla bosses
        if (type == EntityType.ENDER_DRAGON || type == EntityType.WITHER) return 500;

        // Elite hostile mobs
        if (type == EntityType.ELDER_GUARDIAN || type == EntityType.WITHER_SKELETON
                || type == EntityType.EVOKER || type == EntityType.RAVAGER
                || type == EntityType.PIGLIN_BRUTE) return 30;

        // Standard hostile mobs
        if (type == EntityType.ZOMBIE || type == EntityType.SKELETON
                || type == EntityType.CREEPER || type == EntityType.SPIDER
                || type == EntityType.ENDERMAN || type == EntityType.BLAZE
                || type == EntityType.WITCH || type == EntityType.PILLAGER
                || type == EntityType.VINDICATOR || type == EntityType.HOGLIN
                || type == EntityType.PHANTOM || type == EntityType.DROWNED
                || type == EntityType.HUSK || type == EntityType.STRAY) return 15;

        return 5;
    }

    private static void handleLevelUp(PlayerEntity player, ItemStack weaponStack) {
        MythicalWeaponItem weapon = (MythicalWeaponItem) weaponStack.getItem();
        int newLevel = weapon.getLevel(weaponStack);
        WeaponTier tier = weapon.getTier();

        // Play level-up sound
        player.getWorld().playSound(null, player.getX(), player.getY(), player.getZ(),
                ModSounds.WEAPON_LEVEL_UP, SoundCategory.PLAYERS, 1.0f, 1.0f);

        // Color and label by tier
        Formatting tierColor = switch (tier) {
            case LEGENDARY -> Formatting.GOLD;
            case EPIC      -> Formatting.DARK_PURPLE;
            case RARE      -> Formatting.AQUA;
            case COMMON    -> Formatting.WHITE;
        };

        // Milestone messages for special levels
        String suffix = "";
        if (newLevel == WeaponLevelingSystem.MAX_LEVEL) {
            suffix = " — MAX LEVEL!";
        } else if (newLevel % 10 == 0) {
            suffix = " — Milestone!";
        }

        player.sendMessage(
            Text.literal("⚔ ")
                .append(Text.translatable(weapon.getTranslationKey()).formatted(tierColor))
                .append(Text.literal(" reached Level " + newLevel + suffix).formatted(Formatting.GREEN)),
            true);

        // Particles scaled by tier and level
        if (player instanceof ServerPlayerEntity serverPlayer) {
            spawnLevelUpParticles(serverPlayer, tier, newLevel);
        }
    }

    private static void spawnLevelUpParticles(ServerPlayerEntity player, WeaponTier tier, int level) {
        ServerWorld world = player.getServerWorld();
        double x = player.getX(), y = player.getY() + 1, z = player.getZ();

        switch (tier) {
            case LEGENDARY -> {
                // Golden explosion + totem effect
                world.spawnParticles(ParticleTypes.TOTEM_OF_UNDYING, x, y, z, 40, 0.6, 0.8, 0.6, 0.4);
                world.spawnParticles(ParticleTypes.FLAME, x, y, z, 20, 0.4, 0.4, 0.4, 0.05);
                if (level % 10 == 0) {
                    world.spawnParticles(ParticleTypes.EXPLOSION_EMITTER, x, y + 0.5, z, 2, 0.3, 0.3, 0.3, 0);
                }
            }
            case EPIC -> {
                world.spawnParticles(ParticleTypes.ENCHANT, x, y, z, 30, 0.5, 0.6, 0.5, 0.2);
                world.spawnParticles(ParticleTypes.WITCH, x, y, z, 10, 0.3, 0.3, 0.3, 0.05);
            }
            case RARE -> {
                world.spawnParticles(ParticleTypes.ENCHANT, x, y, z, 20, 0.5, 0.5, 0.5, 0.1);
                world.spawnParticles(ParticleTypes.END_ROD, x, y, z, 8, 0.3, 0.3, 0.3, 0.05);
            }
            case COMMON ->
                world.spawnParticles(ParticleTypes.ENCHANT, x, y, z, 12, 0.4, 0.4, 0.4, 0.08);
        }
    }
}
