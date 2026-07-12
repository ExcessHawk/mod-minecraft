# ⚔️ Elemental Affinity System Guide

Complete guide to the Elemental Affinity System in Mythical Swords.

---

## 🌟 Overview

Every mythical weapon in this mod has an **Elemental Affinity** that grants special combat effects and damage bonuses based on biomes, mobs, and environmental conditions.

### The 6 Affinities

| Affinity | Symbol | Primary Effect | Color Theme |
|----------|--------|---------------|-------------|
| **FIRE** | 🔥 | Burns target (8s) | Orange/Red |
| **ICE** | ❄️ | Slowness II (5s) | Cyan/White |
| **LIGHTNING** | ⚡ | 20% lightning strike | Yellow/Blue |
| **DIVINE** | ✨ | Glowing + weakness vs undead | Gold/White |
| **DARK** | 🌑 | Wither I (5s) | Black/Purple |
| **NATURE** | 🌿 | Poison I (4s) + regen | Green |

---

## 🔥 FIRE Affinity

**Weapons:** Laevateinn, Xiuhcoatl  
**Theme:** Destruction and heat

### Primary Effect
- Sets target on fire for 8 seconds
- Spawns flame particles on hit

### Biome Bonuses
- **+25% damage** in:
  - Nether (all biomes)
  - Desert
  - Badlands

### Mob Bonuses
- **+30% damage** vs:
  - Strays
  - Snow Golems
  - Any ice/snow-related mobs

### Strategy
- Excellent for Nether exploration
- Great against frozen/cold enemies
- Pair with Fire Resistance for safety

---

## ❄️ ICE Affinity

**Weapons:** Skofnung  
**Theme:** Frost and control

### Primary Effect
- Applies Slowness II for 5 seconds
- Spawns snowflake particles

### Biome Bonuses
- **+25% damage** in:
  - Snowy biomes
  - Frozen ocean
  - Ice spikes
  - Snowy taiga

### Mob Bonuses
- **+30% damage** vs:
  - Blazes
  - Magma Cubes
  - Fire-based mobs

### Strategy
- Perfect for crowd control
- Excellent in frozen biomes
- Dominates Nether mobs

---

## ⚡ LIGHTNING Affinity

**Weapons:** Gram, Jian (Phase 4)  
**Theme:** Speed and shock

### Primary Effect
- **20% chance** to summon lightning bolt on hit
- Spawns electric spark particles
- Lightning deals AoE damage

### Biome Bonuses
- **+25% damage** during:
  - Rain
  - Thunderstorms

### Mob Bonuses
- **+30% damage** vs:
  - Guardians
  - Drowned
  - Water-based mobs

### Strategy
- Devastating in storms
- High burst damage potential
- Can chain lightning in groups

---

## ✨ DIVINE Affinity

**Weapons:** Excalibur, Caliburn, Harpe, Xiphos Sagrado, Aegis Edge, Was Scepter (Phase 4)  
**Theme:** Light and purity

### Primary Effect
- Applies Glowing effect
- **If target is undead:** Weakness II for 5 seconds
- Spawns golden/white particles

### Biome Bonuses
- **+25% damage** in:
  - The End dimension

### Mob Bonuses
- **+50% damage** vs:
  - Zombies (all variants)
  - Skeletons (all variants)
  - Phantoms
  - Wither
  - All undead mobs

### Strategy
- BEST against undead
- Essential for night survival
- Strongest single-target bonus

---

## 🌑 DARK Affinity

**Weapons:** Clarent, Muramasa, Khopesh (Phase 4)  
**Theme:** Shadows and corruption

### Primary Effect
- Applies Wither I for 5 seconds
- Spawns dark smoke particles
- Bypasses armor partially

### Biome Bonuses
- **+25% damage**:
  - At night (not day)
  - Underground (Y < 50)
  - In dark areas

### Mob Bonuses
- **+30% damage** vs:
  - Villagers
  - Illagers (all types)
  - Witches

### Strategy
- Powerful at night
- Excellent for caves/underground
- Wither effect ignores armor

---

## 🌿 NATURE Affinity

**Weapons:** Hofund, Kusanagi-no-Tsurugi, Totsuka-no-Tsurugi, Naginata Bishamon, Nike Blade, Ruyi Jingu Bang (Phase 4)  
**Theme:** Life and poison

### Primary Effect
- Applies Poison I for 4 seconds
- Spawns leaf/green particles
- **Special:** Attacker gains Regeneration I in forest biomes

### Biome Bonuses
- **Regeneration** (not damage) in:
  - Forest (all variants)
  - Jungle
  - Taiga

### Mob Bonuses
- **+30% damage** vs:
  - Piglins
  - Hoglins
  - Ghasts
  - Blazes
  - All Nether mobs

### Strategy
- Self-healing in forests
- Perfect anti-Nether weapon
- Sustainable farming tool

---

## 📊 Weapon Distribution by Affinity

### DIVINE (Most Common)
- Excalibur
- Caliburn
- Harpe
- Xiphos Sagrado
- Aegis Edge
- Was Scepter (Phase 4)

**Total:** 6 weapons

### NATURE
- Hofund
- Kusanagi-no-Tsurugi
- Totsuka-no-Tsurugi
- Naginata Bishamon
- Nike Blade
- Ruyi Jingu Bang (Phase 4)

**Total:** 6 weapons

### DARK
- Clarent
- Muramasa
- Khopesh (Phase 4)

**Total:** 3 weapons

### FIRE
- Laevateinn
- Xiuhcoatl

**Total:** 2 weapons

### LIGHTNING
- Gram
- Jian (Phase 4)

**Total:** 2 weapons

### ICE
- Skofnung

**Total:** 1 weapon

---

## 🎨 Visual Effects

Each affinity has unique visual feedback:

### Particle Effects
- **FIRE:** Orange/red flames, lava particles
- **ICE:** Snowflakes, white particles
- **LIGHTNING:** Electric sparks, end rod particles
- **DIVINE:** Golden light, enchantment glitter
- **DARK:** Black smoke, soul particles
- **NATURE:** Green leaves, composter particles

### Sound Effects
- **FIRE:** Fire charge use
- **ICE:** Glass breaking
- **LIGHTNING:** Thunder crack
- **DIVINE:** Enchantment table
- **DARK:** Wither hurt
- **NATURE:** Grass breaking

### Enhanced Proc Effects
When a significant affinity bonus activates (>25%), enhanced particles and sounds play to signal the power boost!

---

## ⚙️ Technical Details

### How It Works
1. When you hit with a mythical weapon, `AffinityEffectSystem.applyAffinityEffect()` is called
2. The system applies the primary status effect (burn, slow, etc.)
3. Particles spawn at target location
4. Biome and mob bonuses are calculated
5. If bonus > 25%, enhanced visual effects play

### Thread Safety
- All effects run server-side only
- Client receives particle sync
- Prevents crashes with async biome loading

### Performance
- Optimized particle counts
- Efficient biome checking with caching
- Minimal performance impact

---

## 🎯 Choosing Your Affinity

### For PvE (Survival)
1. **DIVINE** - Best overall (undead everywhere)
2. **NATURE** - Nether exploration + healing
3. **DARK** - Cave/night survival

### For Boss Fights
1. **DIVINE** - Most bosses have high HP
2. **LIGHTNING** - High burst damage
3. **FIRE** - Consistent DoT

### For Specific Situations
- **Nether:** Nature > Fire > Ice
- **Ocean:** Lightning > Ice
- **Night:** Dark > Divine
- **Snow Biomes:** Ice > Fire
- **Thunderstorms:** Lightning

---

## 💡 Pro Tips

### Synergy Tips
1. **Lightning in storms** = Massive AoE clears
2. **Nature in jungles** = Never die (regen + poison)
3. **Dark at night underground** = Always optimal
4. **Divine + Smite** = 2x undead multiplier
5. **Fire in Nether** = Constant 25% bonus

### Combo Strategies
- Slow with Ice → Finish with Lightning
- Burn with Fire → Knockback with Nature
- Glowing with Divine → Snipe from distance
- Wither with Dark → Let DoT finish them

### Enchantment Pairings
- **Sharpness:** Works with all affinities
- **Smite:** Stack with DIVINE for undead deletion
- **Bane of Arthropods:** Less useful (no affinity bonus)
- **Looting:** More boss drops!

---

## 📈 Damage Calculations

### Base Damage Formula
```
Total Damage = Weapon Base Damage
  + Biome Bonus (if applicable)
  + Mob Type Bonus (if applicable)
  + Enchantment damage
```

### Example Calculations

**Excalibur (DIVINE) vs Zombie at night:**
- Base: 12 damage
- Undead Bonus: +6 (50%)
- **Total:** 18 damage

**Gram (LIGHTNING) vs Drowned in storm:**
- Base: 13 damage
- Storm Bonus: +3.25 (25%)
- Aquatic Bonus: +3.9 (30%)
- **Total:** 20.15 damage
- **PLUS** 20% chance for lightning (additional ~15 damage)

**Kusanagi (NATURE) vs Piglin in Nether forest:**
- Base: 14 damage
- Nether Mob Bonus: +4.2 (30%)
- **Total:** 18.2 damage
- **PLUS** attacker gets Regeneration

---

## 🔄 Future Updates

### Planned Features
- [ ] Affinity leveling system
- [ ] Dual-affinity weapons
- [ ] Affinity-specific enchantments
- [ ] Weather manipulation abilities
- [ ] Elemental shields/armor

### Balance Changes
- Ongoing monitoring of damage values
- Community feedback integration
- Potential proc chance adjustments

---

**Master the affinities, master combat! ⚔️✨**
