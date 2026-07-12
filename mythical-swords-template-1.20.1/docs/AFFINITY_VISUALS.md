# Affinity Visual Indicators - Implementation Documentation

## Task 22.2: Add visual indicators for affinity

**Status**: ✅ COMPLETE

**Requirements**: 10.2, 10.5

## Overview

This document describes the implementation of visual and audio indicators for elemental affinity effects in the Mythical Swords Mod. When a weapon with an elemental affinity deals bonus damage due to favorable conditions (biome, target type, or affinity matchup), enhanced visual and audio feedback is provided to the player.

## Implementation Details

### 1. Colored Particle Effects Per Affinity

Each elemental affinity has unique particle effects that spawn when a significant affinity bonus is triggered (>25% damage bonus):

#### FIRE Affinity
- **Primary Particles**: `FLAME` (20 particles)
- **Secondary Particles**: `LAVA` (5 particles)
- **Color Scheme**: Orange/Red (#FF4500, #FF8C00)
- **Effect**: Creates a fiery burst around the target

#### ICE Affinity
- **Primary Particles**: `SNOWFLAKE` (25 particles)
- **Secondary Particles**: `ITEM_SNOWBALL` (10 particles)
- **Color Scheme**: Light Blue/White (#B0E0E6, #FFFFFF)
- **Effect**: Creates a freezing burst with snowflakes

#### LIGHTNING Affinity
- **Primary Particles**: `ELECTRIC_SPARK` (20 particles)
- **Secondary Particles**: `END_ROD` (10 particles)
- **Color Scheme**: Yellow/White (#FFFF00, #FFFFFF)
- **Effect**: Creates electric sparks around the target

#### DIVINE Affinity
- **Primary Particles**: `END_ROD` (20 particles)
- **Secondary Particles**: `ENCHANT` (15 particles)
- **Color Scheme**: Gold/White (#FFD700, #FFFFFF)
- **Effect**: Creates a holy radiance with enchantment glyphs

#### DARK Affinity
- **Primary Particles**: `LARGE_SMOKE` (20 particles)
- **Secondary Particles**: `SOUL` (10 particles)
- **Color Scheme**: Purple/Black (#4B0082, #000000)
- **Effect**: Creates dark smoke with soul particles

#### NATURE Affinity
- **Primary Particles**: `COMPOSTER` (20 particles)
- **Secondary Particles**: `HAPPY_VILLAGER` (10 particles)
- **Color Scheme**: Green (#228B22, #90EE90)
- **Effect**: Creates leaf particles with sparkles

### 2. Sound Effects on Affinity Proc

Each affinity plays a thematic sound effect when the bonus damage is applied:

| Affinity | Sound Event | Description |
|----------|-------------|-------------|
| FIRE | `ITEM_FIRECHARGE_USE` | Whoosh of fire igniting |
| ICE | `BLOCK_GLASS_BREAK` | Sharp, crystalline shattering |
| LIGHTNING | `ENTITY_LIGHTNING_BOLT_THUNDER` | Thunder crack |
| DIVINE | `BLOCK_ENCHANTMENT_TABLE_USE` | Mystical chime |
| DARK | `ENTITY_WITHER_HURT` | Ominous wither sound |
| NATURE | `BLOCK_GRASS_BREAK` | Rustling leaves |

**Sound Parameters**:
- Volume: 0.5 (50% of max)
- Pitch: 1.0 ± 0.1 (slight random variation)
- Category: `PLAYERS`

### 3. Trigger Conditions

The enhanced visual and audio effects are triggered when:

1. A player attacks an entity with a mythical weapon
2. The weapon has an elemental affinity
3. The calculated damage multiplier is ≥ 1.25 (25% bonus or more)

**Bonus Sources**:
- **Biome Bonus**: +50% damage in favorable biomes
- **Entity Type Bonus**: +75% to +100% damage against specific mob types
- **Affinity Matchup**: +50% damage in favorable matchups

### 4. Code Architecture

#### Key Classes

**`AffinityEffectSystem.java`**
- Contains `applyAffinityProcEffect()` method
- Spawns particles and plays sounds based on affinity
- Only runs on server side to avoid desync
- Checks for minimum bonus threshold (25%)

**`AffinityEventHandler.java`**
- Registers Fabric's `AttackEntityCallback` event
- Calculates damage multiplier using `ElementalAffinity.calculateBonus()`
- Calls `applyAffinityProcEffect()` when bonus is significant
- Applies bonus damage separately to avoid infinite loops

**`ElementalAffinity.java`**
- Enum defining all 6 elemental affinities
- Contains `calculateBonus()` method for damage calculations
- Checks biome, entity type, and affinity matchups

#### Event Flow

```
Player attacks entity
    ↓
AttackEntityCallback triggered
    ↓
Check if weapon is MythicalWeaponItem
    ↓
Calculate damage multiplier (ElementalAffinity.calculateBonus)
    ↓
If multiplier ≥ 1.25:
    ↓
    Apply bonus damage
    ↓
    Call applyAffinityProcEffect()
        ↓
        Spawn colored particles
        ↓
        Play affinity sound
```

### 5. Performance Considerations

- **Server-side only**: All particle spawning and sound playing happens on the server
- **Threshold check**: Effects only trigger for significant bonuses (≥25%)
- **Particle count**: Limited to 20-25 particles per proc to avoid lag
- **Sound volume**: Set to 50% to avoid overwhelming other game sounds

### 6. Testing

The implementation includes a validator (`AffinityVisualsValidator.java`) that:
- Verifies all 6 affinities are configured
- Validates all particle types are accessible
- Validates all sound events are accessible
- Runs automatically on mod initialization

### 7. Requirements Validation

✅ **Requirement 10.2**: "THE MythicalSwordsMod SHALL display particle effects around the weapon"
- Implemented via `applyAffinityProcEffect()` with affinity-specific particles

✅ **Requirement 10.5**: "THE MythicalSwordsMod SHALL ensure WeaponAuras are thematically appropriate to each weapon's mythology"
- Each affinity has thematically appropriate particles and sounds
- Fire = flames, Ice = snowflakes, Lightning = sparks, etc.

## Usage Example

When a player attacks a zombie (undead) with Excalibur (DIVINE affinity):

1. **Damage Calculation**:
   - Base damage: 15
   - Divine vs Undead bonus: +100% (multiplier = 2.0)
   - Total damage: 30

2. **Visual Feedback**:
   - 20 END_ROD particles spawn around the zombie
   - 15 ENCHANT particles spawn around the zombie
   - Golden/white radiance effect

3. **Audio Feedback**:
   - `BLOCK_ENCHANTMENT_TABLE_USE` sound plays
   - Mystical chime indicates the divine bonus

## Future Enhancements

Potential improvements for future phases:
- Add weapon-level scaling to particle count
- Add custom particle textures for each affinity
- Add screen shake effect for critical procs
- Add boss-specific affinity interactions
- Add player buff indicators when in favorable biomes

## Related Files

- `src/main/java/com/mythicalswords/systems/AffinityEffectSystem.java`
- `src/main/java/com/mythicalswords/events/AffinityEventHandler.java`
- `src/main/java/com/mythicalswords/weapons/ElementalAffinity.java`
- `src/main/java/com/mythicalswords/validation/AffinityVisualsValidator.java`

## Changelog

- **Phase 2**: Initial implementation of affinity visual indicators
- **Task 22.2**: Completed colored particle effects and sound effects per affinity
