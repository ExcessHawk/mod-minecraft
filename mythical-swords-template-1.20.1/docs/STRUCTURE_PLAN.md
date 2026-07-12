# Structure & Boss Spawn Plan

## Current State Analysis

### Existing Structures
| Structure | File | Boss | Biome | Status |
|-----------|------|------|-------|--------|
| Arthurian Castle | ArthuranCastleGenerator | Rey Arturo | Plains/Forest | ✅ Built, ⚠️ bugs |
| Valhalla Hall | ValhallaHallGenerator | Odin | Taiga/Mountains | ✅ Built, ⚠️ bugs |
| Trickster's Cave | TrickstersCaveGenerator | Loki | Dark Forest | ✅ Built, ⚠️ bugs |
| Greek Temple | GreekTempleGenerator | Atenea | Plains/Hills | ✅ Built, ⚠️ bugs |

### Critical Bugs Found

#### BUG 1 — BossAltarBlock hardcoded to ReyArturo
ALL four structures place the same `BossAltarBlock` which internally hardcodes
`ModEntities.REY_ARTURO`. Right-clicking an altar in Valhalla Hall spawns King Arthur,
not Odin. Same for Loki's cave and Greek Temple.

**Fix**: Add NBT data to the altar block at placement time identifying which boss to spawn.
`BossAltarBlock` reads `"BossId"` from the block's NBT via a `BlockEntity` or a
`BlockState` property. Simplest approach: a companion `BossAltarBlockEntity` that stores
`bossId` as a string.

#### BUG 2 — Arthurian Castle chests are empty
`addDecorations()` places two `Blocks.CHEST` at `(3,1,8)` and `(-3,1,8)` but never
populates them. No `ChestBlockEntity` items set.

**Fix**: Fill with thematic loot — iron/gold ingots, bread, `MYTHRIL_INGOT` (rare),
`SACRED_IRON_INGOT`, name tag, enchanted book.

#### BUG 3 — ValhallaHall / TrickstersCave chests also empty
TrickstersCave places two chests in alcoves + suspicious gravel but no loot assigned.
ValhallaHall places no chest at all.

**Fix**: Add loot to all structures per-theme.

#### BUG 4 — Loki and Atenea share no dedicated ritual
Valhalla Hall has a single altar meant for Odin. Loki's cave has a separate altar, but
BossAltarBlock spawns ReyArturo. Greek Temple altar same problem.

#### BUG 5 — Structure chests not using loot tables
Loot is hardcoded with fixed slots. If player places a chest and an item is already
there it overwrites slot 0. Should use randomized slot placement.

---

## Missing Structures (5 needed)

### 1. Bamboo Temple — Japanese (Susanoo + Izanagi)
- **Boss**: Susanoo (primary), Izanagi (secondary altar)
- **Biome**: Jungle / Bamboo Jungle
- **Theme**: Japanese shrine/torii gate structure
- **Materials**: Bamboo, Jungle Wood, Lanterns, Cherry Planks, Stone Bricks
- **Size**: ~30×20 footprint, 10 blocks tall
- **Loot chest**: Acero Tamahagane Ingot, Gem of Bishamon, enchanted book, gold

### 2. Oni Fortress — Japanese (Oni Oscuro)
- **Boss**: Oni Oscuro
- **Biome**: Dark Forest (overlaps Loki but different structure)
- **Theme**: Demon fortress with obsidian and nether bricks
- **Materials**: Nether Bricks, Obsidian, Crying Obsidian, Magma Blocks
- **Size**: ~20×20, 12 blocks tall
- **Loot chest**: Muramasa or Totsuka weapon component, rare materials

### 3. Aztec Pyramid — Mesoamerican (Quetzalcoatl)
- **Boss**: Quetzalcoatl
- **Biome**: Jungle / Sparse Jungle
- **Theme**: Step pyramid, 5 tiers descending to center altar
- **Materials**: Sandstone, Chiseled Sandstone, Terracotta, Jungle Wood
- **Size**: ~35×35 footprint, 15 blocks tall
- **Loot chest**: Jade Imperial Ingot, Obsidiana Ritual Shard, Agnis Flame Core

### 4. Desert Tomb — Egyptian (Anubis + Ra)
- **Boss**: Anubis (lower chamber), Ra (upper shrine)
- **Biome**: Desert / Badlands
- **Theme**: Egyptian pyramid with inner chambers, hieroglyph pillars
- **Materials**: Sandstone, Smooth Sandstone, Cut Sandstone, Gold Blocks, Chiseled Sandstone
- **Size**: ~40×40, 20 blocks tall
- **Loot chest**: Khopesh/Was Scepter materials, gold, lapis
- **Special**: Two altars — underground for Anubis, rooftop shrine for Ra

### 5. Celestial Palace — Chinese (Sun Wukong)
- **Boss**: Sun Wukong
- **Biome**: Mountain / Windswept Hills (floating clouds feel)
- **Theme**: Pagoda-style tiered palace on elevated platform
- **Materials**: Quartz, Purpur, End Stone Bricks, Gold Blocks, Lanterns
- **Size**: ~25×25, 18 blocks tall
- **Loot chest**: Ruyi Jingu Bang components, celestial materials

---

## BossAltarBlock Redesign

### Solution: Store bossId in BlockEntity NBT
```
BossAltarBlock (existing block class)
  └── BossAltarBlockEntity (new, stores "BossId" string)
      └── createMenu() / writeScreenOpeningData() NOT needed
          Just: readNbt/writeNbt for bossId
```

Each structure generator calls a helper:
```java
placeAltarWithBoss(world, pos, "odin");       // Valhalla Hall
placeAltarWithBoss(world, pos, "loki");       // Trickster's Cave
placeAltarWithBoss(world, pos, "atenea");     // Greek Temple
placeAltarWithBoss(world, pos, "rey_arturo"); // Arthurian Castle
placeAltarWithBoss(world, pos, "susanoo");    // Bamboo Temple
// etc.
```

`BossAltarBlock.onUse()` reads the BlockEntity's bossId and looks up
`Registries.ENTITY_TYPE.get(new Identifier("mythicalswords", bossId))` to spawn
the correct boss.

### Summoning items per boss (held when right-clicking altar)
| Boss | Item Required | Drop hint |
|------|--------------|-----------|
| Rey Arturo | Nether Star | ⭐ already implemented |
| Odin | RAINBOW_BRIDGE_FRAGMENT | (from Wither Skeletons 2%) |
| Loki | FROZEN_SOUL_CRYSTAL | (from Strays 5%) |
| Atenea | SHARD_OF_DIVINITY | (craftable + loot) |
| Susanoo | GEM_OF_BISHAMON | (mob drop) |
| Oni Oscuro | SOUL_SWORDSMITH | |
| Izanagi | SACRED_WATER_OF_AMATERASU | |
| Quetzalcoatl | JADE_IMPERIAL_INGOT | |
| Anubis | Wither Skull (vanilla) | |
| Ra | SUN_BLESSED_ALLOY | |
| Sun Wukong | DRAGON_FANG_FRAGMENT | |

---

## Loot Table Plan

### Arthurian Castle
- **Main chests (×2)**: 3–6 gold ingots, 2–4 iron ingots, 1 bread, 1 Sacred Iron Ingot (30%), enchanted book (20%), 0–1 Mythril Ingot (10%)
- **Post-boss loot**: Excalibur (100%, from boss drop — already works)

### Valhalla Hall
- **Loot chest (×1, new)**: 2–4 Northsteel Ingot, Spiritbound Leather (40%), Rainbow Bridge Fragment (25%), enchanted book (20%)

### Trickster's Cave
- **Hidden chests (×2)**: Frozen Soul Crystal (50%), gold ingots, random enchanted book, 0–1 Mythril Ingot (15%)

### Greek Temple
- **Chest (×1, existing)**: Already populated ✅ — Shard of Divinity (60%), gold, lapis, Feather of Victory (40%)

### Bamboo Temple
- **Chest (×2)**: Tamahagane Ingot, Gem of Bishamon (50%), arrows, food

### Oni Fortress
- **Chest (×1)**: Muramasa material, Soul Swordsmith (40%), nether items

### Aztec Pyramid
- **Chest (×2)**: Jade Imperial Ingot, Obsidiana Ritual Shard (50%), Agnis Flame Core (30%)

### Desert Tomb
- **Chest (×3)**: Gold blocks, Lapis, cut sandstone for flavor, Scarab Amulet placeholder (40%)

### Celestial Palace
- **Chest (×2)**: Cloud Fragment, Golden Hair (40%), gold blocks, XP bottles

---

## Implementation Order

1. **Fix BossAltarBlock** — convert to use BlockEntity NBT for boss ID, per-boss summon item
2. **Fix existing structure chests** — add loot to Castle and Valhalla Hall
3. **Register new structures in ModStructures** with correct biomes
4. **Implement 5 new structure generators**
5. **Fix ModStructures duplication bug** (already done — Overworld filter)

---

## Structure Generation Parameters

```
STRUCTURE_RARITY      = 1 in 100 chunks
MIN_DISTANCE          = 50 chunks (800 blocks) between any two structures
SPAWN_EXCLUSION_ZONE  = 15 chunks from world spawn (240 blocks)
```

All structures: at least 240 blocks from world spawn, 800 blocks apart from each other.
```
