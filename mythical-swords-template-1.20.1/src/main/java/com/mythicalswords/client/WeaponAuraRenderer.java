package com.mythicalswords.client;

import com.mythicalswords.weapons.ElementalAffinity;
import com.mythicalswords.weapons.MythicalWeaponItem;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.GraphicsMode;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class WeaponAuraRenderer {

    private static final double CULL_DISTANCE_SQ = 32.0 * 32.0;
    private static final int PARTICLE_TICK_INTERVAL = 5;

    public static void register() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.world == null || client.player == null) return;
            ParticlePool.resetTick();
            tick(client);
        });
    }

    private static void tick(MinecraftClient client) {
        World world = client.world;
        PlayerEntity localPlayer = client.player;
        long time = world.getTime();

        if (time % PARTICLE_TICK_INTERVAL != 0) return;

        // Scale particle count by graphics setting
        int maxParticles = getMaxParticlesForSettings(client);

        for (PlayerEntity player : world.getPlayers()) {
            // Cull far players
            if (localPlayer.squaredDistanceTo(player) > CULL_DISTANCE_SQ) continue;

            ItemStack mainHand = player.getMainHandStack();
            if (!(mainHand.getItem() instanceof MythicalWeaponItem weapon)) continue;

            int level = weapon.getLevel(mainHand);
            if (level < 1) continue;

            int particleCount = Math.min(maxParticles, 1 + level / 15);
            spawnAura(world, player, weapon.getAffinity(), particleCount);
        }
    }

    private static void spawnAura(World world, PlayerEntity player,
                                   ElementalAffinity affinity, int count) {
        ParticleEffect particle = particleFor(affinity);
        if (particle == null) return;

        Vec3d base = player.getPos().add(0, 1.0, 0);
        double spread = 0.35;

        for (int i = 0; i < count; i++) {
            if (!ParticlePool.acquire()) break;
            double ox = (Math.random() - 0.5) * spread;
            double oy = (Math.random() - 0.5) * spread;
            double oz = (Math.random() - 0.5) * spread;
            world.addParticle(particle, base.x + ox, base.y + oy, base.z + oz, 0, 0.04, 0);
        }
    }

    private static ParticleEffect particleFor(ElementalAffinity affinity) {
        return switch (affinity) {
            case FIRE      -> ParticleTypes.FLAME;
            case ICE       -> ParticleTypes.SNOWFLAKE;
            case LIGHTNING -> ParticleTypes.ELECTRIC_SPARK;
            case DIVINE    -> ParticleTypes.END_ROD;
            case DARK      -> ParticleTypes.ASH;
            case NATURE    -> ParticleTypes.HAPPY_VILLAGER;
        };
    }

    private static int getMaxParticlesForSettings(MinecraftClient client) {
        GraphicsMode mode = client.options.getGraphicsMode().getValue();
        return switch (mode) {
            case FAST     -> 1;
            case FANCY    -> 2;
            case FABULOUS -> 3;
        };
    }
}
