package com.mythicalswords.weapons;

import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Represents the elemental affinity of a mythical weapon.
 * Each affinity provides different bonuses against specific enemies and in certain biomes.
 */
public enum ElementalAffinity {
    FIRE,
    ICE,
    LIGHTNING,
    DIVINE,
    DARK,
    NATURE;
    
    /**
     * Legacy no-context damage bonus. Always returns 1.0f (no bonus) because
     * affinity bonuses require world/position/target context.
     * Kept only for binary compatibility — use the context-aware overload.
     *
     * @return damage multiplier (1.0f = no bonus)
     * @deprecated Use {@link #calculateBonus(World, BlockPos, LivingEntity)} instead
     */
    @Deprecated
    public float calculateBonus() {
        return 1.0f;
    }
    
    /**
     * Calculate comprehensive damage bonus based on affinity, biome, and target type.
     * This method combines biome-based bonuses, entity-type bonuses, and affinity matchups.
     * 
     * @param world The world where the attack is happening
     * @param pos The position of the attack
     * @param target The entity being attacked
     * @return damage multiplier (1.0f = no bonus, 1.5f = 50% bonus, etc.)
     */
    public float calculateBonus(World world, BlockPos pos, LivingEntity target) {
        float multiplier = 1.0f;
        
        // Add biome-based bonus
        if (isInAffinityBiome(world, pos)) {
            multiplier += 0.5f; // +50% damage in favorable biomes
        }
        
        // Add entity-type bonus
        multiplier += getEntityTypeBonus(target);
        
        // Add affinity vs affinity matchup bonus
        multiplier += getAffinityMatchupBonus(target);
        
        return multiplier;
    }
    
    /**
     * Check if the current position is in a biome that benefits this affinity.
     * 
     * @param world The world to check
     * @param pos The position to check
     * @return true if in a favorable biome
     */
    private boolean isInAffinityBiome(World world, BlockPos pos) {
        // Safety check: only run on server to avoid threading issues
        if (world.isClient) return false;
        
        try {
            var biomeEntry = world.getBiome(pos);
            if (biomeEntry == null) return false;
            
            return switch (this) {
                case FIRE -> {
                    // Nether, Desert, Badlands
                    var key = biomeEntry.getKey();
                    yield key.isPresent() && (
                        key.get().getValue().getPath().contains("nether") ||
                        key.get().getValue().getPath().contains("desert") ||
                        key.get().getValue().getPath().contains("badlands")
                    );
                }
                case ICE -> {
                    // Snowy, Frozen biomes
                    var key = biomeEntry.getKey();
                    yield key.isPresent() && (
                        key.get().getValue().getPath().contains("frozen") ||
                        key.get().getValue().getPath().contains("snowy") ||
                        key.get().getValue().getPath().contains("ice")
                    );
                }
                case LIGHTNING -> world.isRaining() || world.isThundering();
                case DIVINE -> world.getRegistryKey() == World.END;
                case DARK -> !world.isDay() || pos.getY() < 50; // Night or underground
                case NATURE -> {
                    // Forest, Jungle biomes
                    var key = biomeEntry.getKey();
                    yield key.isPresent() && (
                        key.get().getValue().getPath().contains("forest") ||
                        key.get().getValue().getPath().contains("jungle") ||
                        key.get().getValue().getPath().contains("taiga")
                    );
                }
            };
        } catch (Exception e) {
            // Silently fail if biome access causes issues
            return false;
        }
    }
    
    /**
     * Get bonus damage multiplier against specific entity types.
     * 
     * @param target The entity being attacked
     * @return bonus multiplier (0.0f = no bonus, 0.75f = +75% damage)
     */
    private float getEntityTypeBonus(LivingEntity target) {
        String entityName = target.getType().toString().toLowerCase();
        
        return switch (this) {
            case FIRE -> {
                // Bonus vs ice/snow mobs (Strays, Snow Golems)
                yield (entityName.contains("stray") || entityName.contains("snow")) ? 0.75f : 0f;
            }
            case ICE -> {
                // Bonus vs fire mobs (Blazes, Magma Cubes)
                yield (entityName.contains("blaze") || entityName.contains("magma")) ? 0.75f : 0f;
            }
            case LIGHTNING -> {
                // Bonus vs water mobs (Guardians, Drowned)
                yield (entityName.contains("guardian") || entityName.contains("drowned")) ? 0.75f : 0f;
            }
            case DIVINE -> target.isUndead() ? 1.0f : 0f; // +100% vs undead
            case DARK -> {
                // Bonus vs Villagers, Illagers
                yield (entityName.contains("villager") || entityName.contains("illager") || 
                       entityName.contains("witch")) ? 0.75f : 0f;
            }
            case NATURE -> {
                // Bonus vs Nether mobs
                yield (entityName.contains("piglin") || entityName.contains("hoglin") ||
                       entityName.contains("ghast") || entityName.contains("blaze")) ? 0.75f : 0f;
            }
        };
    }
    
    /**
     * Get bonus damage based on affinity vs affinity matchups.
     * Some affinities are strong against others (e.g., Fire vs Ice).
     * 
     * @param target The entity being attacked
     * @return bonus multiplier (0.0f = no bonus, 0.5f = +50% damage)
     */
    private float getAffinityMatchupBonus(LivingEntity target) {
        // Enemies have no explicit affinity yet, so we use entity type as a proxy
        // (e.g. Blazes count as Fire). Swap to real affinity matchups once mobs carry one.
        String entityName = target.getType().toString().toLowerCase();
        
        return switch (this) {
            case FIRE -> {
                // Fire is strong against Ice
                yield (entityName.contains("stray") || entityName.contains("snow") || 
                       entityName.contains("ice")) ? 0.5f : 0f;
            }
            case ICE -> {
                // Ice is strong against Fire
                yield (entityName.contains("blaze") || entityName.contains("magma") || 
                       entityName.contains("fire")) ? 0.5f : 0f;
            }
            case LIGHTNING -> {
                // Lightning is strong against Water
                yield (entityName.contains("guardian") || entityName.contains("drowned") || 
                       entityName.contains("squid")) ? 0.5f : 0f;
            }
            case DIVINE -> {
                // Divine is strong against Dark/Undead
                yield (target.isUndead() || entityName.contains("wither") || 
                       entityName.contains("phantom")) ? 0.5f : 0f;
            }
            case DARK -> {
                // Dark is strong against Divine/Light
                yield (entityName.contains("villager") || entityName.contains("iron_golem") || 
                       entityName.contains("allay")) ? 0.5f : 0f;
            }
            case NATURE -> {
                // Nature is strong against constructs and nether mobs
                yield (entityName.contains("golem") || entityName.contains("piglin") || 
                       entityName.contains("hoglin")) ? 0.5f : 0f;
            }
        };
    }
}
