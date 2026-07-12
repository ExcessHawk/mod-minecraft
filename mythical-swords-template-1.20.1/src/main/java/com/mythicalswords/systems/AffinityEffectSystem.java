package com.mythicalswords.systems;

import com.mythicalswords.weapons.ElementalAffinity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LightningEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;

import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

/**
 * System for handling affinity-based effects and bonuses
 * 
 * IMPORTANT: This system applies STATUS EFFECTS and PARTICLES only.
 * Bonus damage is NOT applied here to avoid infinite loops.
 * 
 * Previous bug: Calling target.damage() in postHit() caused infinite recursion
 * Solution: Use Fabric's AttackEntityCallback event instead of postHit() override
 * 
 * Thread safety: All methods check world.isClient and use try-catch for biome access
 */
public class AffinityEffectSystem {

    private static final Random RANDOM = new Random();

    /**
     * Apply affinity effect when weapon hits target
     * 
     * @param target The entity being hit
     * @param attacker The entity attacking
     * @param affinity The elemental affinity of the weapon
     * @param world The world where the attack is happening
     */
    public static void applyAffinityEffect(LivingEntity target, LivingEntity attacker, ElementalAffinity affinity,
            World world) {
        if (world.isClient)
            return; // Only run on server

        switch (affinity) {
            case FIRE:
                applyFireEffect(target, attacker, world);
                break;
            case ICE:
                applyIceEffect(target, attacker, world);
                break;
            case LIGHTNING:
                applyLightningEffect(target, attacker, world);
                break;
            case DIVINE:
                applyDivineEffect(target, attacker, world);
                break;
            case DARK:
                applyDarkEffect(target, attacker, world);
                break;
            case NATURE:
                applyNatureEffect(target, attacker, world);
                break;
        }
    }
    
    /**
     * Apply enhanced visual and audio feedback when affinity bonus procs
     * This is called when a significant affinity bonus is applied
     * 
     * @param target The entity being hit
     * @param attacker The entity attacking
     * @param affinity The elemental affinity
     * @param world The world
     * @param bonusMultiplier The damage multiplier that was applied
     */
    public static void applyAffinityProcEffect(LivingEntity target, LivingEntity attacker, 
            ElementalAffinity affinity, World world, float bonusMultiplier) {
        if (world.isClient) return;
        
        // Only show proc effects for significant bonuses (>25%)
        if (bonusMultiplier < 1.25f) return;
        
        if (world instanceof ServerWorld serverWorld) {
            // Spawn enhanced particles based on affinity
            switch (affinity) {
                case FIRE:
                    serverWorld.spawnParticles(ParticleTypes.FLAME,
                        target.getX(), target.getY() + 1, target.getZ(),
                        20, 0.5, 0.5, 0.5, 0.2);
                    serverWorld.spawnParticles(ParticleTypes.LAVA,
                        target.getX(), target.getY() + 1, target.getZ(),
                        5, 0.3, 0.3, 0.3, 0.1);
                    playAffinitySound(serverWorld, target.getBlockPos(), affinity);
                    break;
                    
                case ICE:
                    serverWorld.spawnParticles(ParticleTypes.SNOWFLAKE,
                        target.getX(), target.getY() + 1, target.getZ(),
                        25, 0.5, 0.5, 0.5, 0.1);
                    serverWorld.spawnParticles(ParticleTypes.ITEM_SNOWBALL,
                        target.getX(), target.getY() + 1, target.getZ(),
                        10, 0.3, 0.3, 0.3, 0.15);
                    playAffinitySound(serverWorld, target.getBlockPos(), affinity);
                    break;
                    
                case LIGHTNING:
                    serverWorld.spawnParticles(ParticleTypes.ELECTRIC_SPARK,
                        target.getX(), target.getY() + 1, target.getZ(),
                        20, 0.5, 0.5, 0.5, 0.3);
                    serverWorld.spawnParticles(ParticleTypes.END_ROD,
                        target.getX(), target.getY() + 1, target.getZ(),
                        10, 0.3, 0.3, 0.3, 0.2);
                    playAffinitySound(serverWorld, target.getBlockPos(), affinity);
                    break;
                    
                case DIVINE:
                    serverWorld.spawnParticles(ParticleTypes.END_ROD,
                        target.getX(), target.getY() + 1, target.getZ(),
                        20, 0.5, 0.5, 0.5, 0.15);
                    serverWorld.spawnParticles(ParticleTypes.ENCHANT,
                        target.getX(), target.getY() + 1, target.getZ(),
                        15, 0.5, 0.5, 0.5, 1.0);
                    playAffinitySound(serverWorld, target.getBlockPos(), affinity);
                    break;
                    
                case DARK:
                    serverWorld.spawnParticles(ParticleTypes.LARGE_SMOKE,
                        target.getX(), target.getY() + 1, target.getZ(),
                        20, 0.5, 0.5, 0.5, 0.1);
                    serverWorld.spawnParticles(ParticleTypes.SOUL,
                        target.getX(), target.getY() + 1, target.getZ(),
                        10, 0.3, 0.3, 0.3, 0.05);
                    playAffinitySound(serverWorld, target.getBlockPos(), affinity);
                    break;
                    
                case NATURE:
                    serverWorld.spawnParticles(ParticleTypes.COMPOSTER,
                        target.getX(), target.getY() + 1, target.getZ(),
                        20, 0.5, 0.5, 0.5, 0.15);
                    serverWorld.spawnParticles(ParticleTypes.HAPPY_VILLAGER,
                        target.getX(), target.getY() + 1, target.getZ(),
                        10, 0.5, 0.5, 0.5, 0.1);
                    playAffinitySound(serverWorld, target.getBlockPos(), affinity);
                    break;
            }
        }
    }
    
    /**
     * Play sound effect appropriate for the affinity
     */
    private static void playAffinitySound(ServerWorld world, BlockPos pos, ElementalAffinity affinity) {
        net.minecraft.sound.SoundEvent sound = switch (affinity) {
            case FIRE -> net.minecraft.sound.SoundEvents.ITEM_FIRECHARGE_USE;
            case ICE -> net.minecraft.sound.SoundEvents.BLOCK_GLASS_BREAK;
            case LIGHTNING -> net.minecraft.sound.SoundEvents.ENTITY_LIGHTNING_BOLT_THUNDER;
            case DIVINE -> net.minecraft.sound.SoundEvents.BLOCK_ENCHANTMENT_TABLE_USE;
            case DARK -> net.minecraft.sound.SoundEvents.ENTITY_WITHER_HURT;
            case NATURE -> net.minecraft.sound.SoundEvents.BLOCK_GRASS_BREAK;
        };
        
        world.playSound(null, pos, sound, net.minecraft.sound.SoundCategory.PLAYERS, 
            0.5f, 1.0f + (RANDOM.nextFloat() * 0.2f - 0.1f));
    }

    /**
     * Calculate bonus damage based on affinity, biome, and target type
     * DEPRECATED: This method is not used to avoid infinite loops in postHit()
     * Bonus damage is now handled through attribute modifiers instead
     * 
     * @deprecated Use attribute modifiers for damage bonuses
     */
    @Deprecated
    public static float calculateAffinityBonus(ElementalAffinity affinity, World world, BlockPos pos,
            LivingEntity target) {
        // This method is kept for potential future use but should NOT be called from postHit()
        float bonusDamage = 0f;

        // Biome-based bonuses
        if (isInAffinityBiome(affinity, world, pos)) {
            bonusDamage += 2.5f; // +25% base damage (~10 damage → 2.5 bonus)
        }

        // Target-based bonuses
        bonusDamage += getTargetTypeBonus(affinity, target);

        return bonusDamage;
    }

    // ===== FIRE AFFINITY =====
    private static void applyFireEffect(LivingEntity target, LivingEntity attacker, World world) {
        // Set target on fire for 8 seconds
        target.setOnFireFor(8);

        // Spawn fire particles
        if (world instanceof ServerWorld serverWorld) {
            serverWorld.spawnParticles(ParticleTypes.FLAME,
                    target.getX(), target.getY() + 1, target.getZ(),
                    10, 0.5, 0.5, 0.5, 0.1);
        }
    }

    // ===== ICE AFFINITY =====
    private static void applyIceEffect(LivingEntity target, LivingEntity attacker, World world) {
        // Apply Slowness II for 5 seconds
        target.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 100, 1));

        // Spawn snow particles
        if (world instanceof ServerWorld serverWorld) {
            serverWorld.spawnParticles(ParticleTypes.SNOWFLAKE,
                    target.getX(), target.getY() + 1, target.getZ(),
                    15, 0.5, 0.5, 0.5, 0.05);
        }
    }

    // ===== LIGHTNING AFFINITY =====
    private static void applyLightningEffect(LivingEntity target, LivingEntity attacker, World world) {
        // 20% chance to summon lightning bolt
        if (RANDOM.nextFloat() < 0.20f) {
            if (world instanceof ServerWorld serverWorld) {
                LightningEntity lightning = EntityType.LIGHTNING_BOLT.create(world);
                if (lightning != null) {
                    lightning.refreshPositionAfterTeleport(target.getX(), target.getY(), target.getZ());
                    serverWorld.spawnEntity(lightning);
                }
            }
        }

        // Spawn electric particles
        if (world instanceof ServerWorld serverWorld) {
            serverWorld.spawnParticles(ParticleTypes.ELECTRIC_SPARK,
                    target.getX(), target.getY() + 1, target.getZ(),
                    12, 0.3, 0.5, 0.3, 0.2);
        }
    }

    // ===== DIVINE AFFINITY =====
    private static void applyDivineEffect(LivingEntity target, LivingEntity attacker, World world) {
        // Apply Glowing
        target.addStatusEffect(new StatusEffectInstance(StatusEffects.GLOWING, 100, 0));

        // If target is undead, apply Weakness II
        if (target.isUndead()) {
            target.addStatusEffect(new StatusEffectInstance(StatusEffects.WEAKNESS, 100, 1));
        }

        // Spawn golden particles
        if (world instanceof ServerWorld serverWorld) {
            serverWorld.spawnParticles(ParticleTypes.END_ROD,
                    target.getX(), target.getY() + 1, target.getZ(),
                    10, 0.5, 0.5, 0.5, 0.1);
        }
    }

    // ===== DARK AFFINITY =====
    private static void applyDarkEffect(LivingEntity target, LivingEntity attacker, World world) {
        // Apply Wither I for 5 seconds
        target.addStatusEffect(new StatusEffectInstance(StatusEffects.WITHER, 100, 0));

        // Spawn dark smoke particles
        if (world instanceof ServerWorld serverWorld) {
            serverWorld.spawnParticles(ParticleTypes.LARGE_SMOKE,
                    target.getX(), target.getY() + 1, target.getZ(),
                    15, 0.5, 0.5, 0.5, 0.05);
        }
    }

    // ===== NATURE AFFINITY =====
    private static void applyNatureEffect(LivingEntity target, LivingEntity attacker, World world) {
        // Apply Poison I for 4 seconds
        target.addStatusEffect(new StatusEffectInstance(StatusEffects.POISON, 80, 0));

        // If attacker in forest/jungle, give them Regeneration
        if (isInAffinityBiome(ElementalAffinity.NATURE, world, attacker.getBlockPos())) {
            attacker.addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 100, 0));
        }

        // Spawn leaf particles
        if (world instanceof ServerWorld serverWorld) {
            serverWorld.spawnParticles(ParticleTypes.COMPOSTER,
                    target.getX(), target.getY() + 1, target.getZ(),
                    12, 0.5, 0.5, 0.5, 0.1);
        }
    }

    // ===== HELPER METHODS =====

    /**
     * Check if position is in a biome that benefits this affinity
     * SAFE VERSION: Uses try-catch to prevent crashes and only runs on server
     */
    private static boolean isInAffinityBiome(ElementalAffinity affinity, World world, BlockPos pos) {
        // Safety check: only run on server to avoid threading issues
        if (world.isClient) return false;
        
        try {
            // Safe biome access with null check
            var biomeEntry = world.getBiome(pos);
            if (biomeEntry == null) return false;

            return switch (affinity) {
                case FIRE -> {
                    // Nether, Desert, Badlands
                    var key = biomeEntry.getKey();
                    yield key.isPresent() && (key.get().getValue().getPath().contains("nether") ||
                            key.get().getValue().getPath().contains("desert") ||
                            key.get().getValue().getPath().contains("badlands"));
                }
                case ICE -> {
                    // Snowy, Frozen biomes
                    var key = biomeEntry.getKey();
                    yield key.isPresent() && (key.get().getValue().getPath().contains("frozen") ||
                            key.get().getValue().getPath().contains("snowy") ||
                            key.get().getValue().getPath().contains("ice"));
                }
                case LIGHTNING -> world.isRaining() || world.isThundering();
                case DIVINE -> world.getRegistryKey() == World.END;
                case DARK -> !world.isDay() || pos.getY() < 50; // Night or underground
                case NATURE -> {
                    // Forest, Jungle biomes
                    var key = biomeEntry.getKey();
                    yield key.isPresent() && (key.get().getValue().getPath().contains("forest") ||
                            key.get().getValue().getPath().contains("jungle") ||
                            key.get().getValue().getPath().contains("taiga"));
                }
            };
        } catch (Exception e) {
            // Silently fail if biome access causes issues
            return false;
        }
    }

    /**
     * Get bonus damage against specific target types
     */
    private static float getTargetTypeBonus(ElementalAffinity affinity, LivingEntity target) {
        return switch (affinity) {
            case FIRE -> {
                // Bonus vs ice/snow mobs (Strays, Snow Golems)
                String name = target.getType().toString().toLowerCase();
                yield (name.contains("stray") || name.contains("snow")) ? 3.0f : 0f;
            }
            case ICE -> {
                // Bonus vs fire mobs (Blazes, Magma Cubes)
                String name = target.getType().toString().toLowerCase();
                yield (name.contains("blaze") || name.contains("magma")) ? 3.0f : 0f;
            }
            case LIGHTNING -> {
                // Bonus vs water mobs (Guardians, Drowned)
                String name = target.getType().toString().toLowerCase();
                yield (name.contains("guardian") || name.contains("drowned")) ? 3.0f : 0f;
            }
            case DIVINE -> target.isUndead() ? 5.0f : 0f; // +50% vs undead
            case DARK -> {
                // Bonus vs Villagers, Illagers
                String name = target.getType().toString().toLowerCase();
                yield (name.contains("villager") || name.contains("illager") ||
                        name.contains("witch")) ? 3.0f : 0f;
            }
            case NATURE -> {
                // Bonus vs Nether mobs
                String name = target.getType().toString().toLowerCase();
                yield (name.contains("piglin") || name.contains("hoglin") ||
                        name.contains("ghast") || name.contains("blaze")) ? 3.0f : 0f;
            }
        };
    }
}
