# Plan v0.5 — Materiales + Encantamientos + Config

Enfoque elegido: materiales y encantamientos nuevos. Dragón montable queda oculto. Config JSON simple: sí.

## Estado actual relevante

- Forja Mítica (`MythicalForgeSystem.java`) funciona pero **no valida materiales**: 2 items cualesquiera = upgrade, cualquier item repara, encantar no checa duplicados/conflictos.
- Bloque de forja usa modelo de furnace + textura de boss_altar (placeholder). GUI dibujada con rectángulos, sin textura.
- 7 encantamientos existentes: elemental_edge, divine_strike, thunder_caller, lifesteal, frost_aura, soul_reaper, berserker.
- 4 bosses SIN loot table: susanoo, oni_oscuro, izanagi, quetzalcoatl.

## Fase 1 — Overhaul Forja Mítica

- [x] Fix creative tab: `MYTHICAL_FORGE` y `OBSIDIANA_RITUAL_ORE` agregados al tab en `ModItems.java`
- [x] GUI: contornos de slots visibles (inventario del jugador ahora dibuja slots; slots de forja con borde grueso de color; panel de inventario separado; texto "Upgrades" ya no se encima con título de inventario)
- [x] Nombre en juego: `block.mythicalswords.mythical_forge` agregado a en_us/es_es/es_mx ("Forja Mítica")
- [x] Validación de recetas de forja (`MythicalForgeSystem.java`):
  - Reparar: exige material según mitología del arma (arthurian→mythril, norse→northsteel, greek→sacred_iron, japanese→tamahagane, chinese→jade_imperial, mesoamerican→obsidiana_shard, egyptian→bronce_bendito, atlantean→orichalcum, frost→froststeel, void→voidsteel); consume 1 (antes consumía el stack completo)
  - Mejorar: lingote de su mitología + catalizador por tier (COMMON→soul_fragment, RARE→dust_of_longevity, EPIC→essence_of_righteousness, LEGENDARY→shard_of_divinity), en cualquier orden; consume 1 de cada
  - Encantar: reglas de yunque — no duplica, respeta nivel máximo, rechaza conflictos (canCombine), valida isAcceptableItem
  - Guard: no sobrescribe slot de salida ocupado
  - Mensajes traducibles (9 keys `message.mythicalswords.forge.*` en 3 idiomas)
- [x] Textura propia del bloque: 3 texturas nuevas (top=crisol fundido, front=boca encendida + emblema dorado, side=ladrillos con remaches) generadas con `scripts/gen-forge.ps1`
- [x] Modelo 3D custom (hecho en Blockbench vía MCP): base escalonada + cuerpo con boca de fuego + mesa con crisol hundido de metal fundido (shade:false para glow); bloque `nonOpaque()` + luminance 7
- [x] Partículas: ambientales en el bloque (flamas/humo/lava vía randomDisplayTick) + ráfaga server-side al forjar (lava+crit+flame en la posición de la forja)
- [ ] Textura de GUI propia (reemplazar rectángulos) — opcional, el GUI ya es legible

## Extras Blockbench (2026-07-11)

- [x] Boss Altar modelo 3D: pedestal escalonado + columna con runas moradas brillantes + tapa con círculo de invocación + orbe perlado encima; 3 texturas nuevas (`scripts/gen-altar.ps1`); bloque `nonOpaque()`
- [x] Martillo del Herrero Legendario: item nuevo `blacksmith_hammer` con modelo 3D (mango madera + cabeza acero con banda dorada) — reemplaza el pico de hierro placeholder (TODO resuelto en `LegendaryBlacksmithEntity`); en creative tab + lang 3 idiomas; el herrero lo dropea 100% al morir
- Posible siguiente: modelos 3D en mano para armas legendarias (26 armas, scope grande — decidir si vale)

## Fase 5 (prerequisito, va antes de Fase 2) — Loot tables faltantes

- [x] susanoo → Kusanagi + tamahagane 3-6 + agua sagrada de Amaterasu 1-2 + diamantes
- [x] izanagi → Totsuka + tamahagane 3-6 + soul_swordsmith 1-2 + diamantes
- [x] oni_oscuro → Muramasa + soul_fragment 2-4 + tamahagane 2-4 + diamantes
- [x] quetzalcoatl → Xiuhcóatl + obsidiana ritual 3-6 + filo de pluma de quetzal 1-2 + esmeraldas
- [x] BUG encontrado: `boss_rey_arturo.json` no coincidía con el ID default `entities/rey_arturo` (nada overridea getLootTableId) — Rey Arturo tampoco dropeaba; renombrado a `rey_arturo.json`

## Fase 2 — Encantamientos nuevos por mitología

Exclusivos de la Forja: se aplican poniendo arma + **material rúnico** (1 nivel por runa, repetible hasta nivel máx). Ocultos de mesa de encantamientos/aldeanos/loot (`isAvailableForRandomSelection/EnchantedBookOffer = false`). Base común: `ForgeExclusiveEnchantment`.

- [x] Runa de Odín (nórdico, máx II): ejecuta enemigos bajo 15%/25% vida — runa: frozen_soul_crystal
- [x] Iaijutsu (japonés, máx III): +2.5 daño/nivel si el portador tiene vida llena — runa: gem_of_bishamon
- [x] Égida (griego, máx II): Resistencia 2s al golpear — runa: feather_of_victory
- [x] Maldición de Anubis (egipcio, máx II): Wither al objetivo — runa: moonstone_shard
- [x] Sol de Ra (egipcio, máx II): de día prende fuego + daño extra — runa: sun_blessed_alloy
- [x] Sed de Sangre (azteca, máx III): cura 1♥/nivel al matar — runa: filo_de_pluma_de_quetzal
- [x] Paso del Mono (chino, máx II): Velocidad 3s al golpear — runa: bamboo_reinforced_shaft

## Fase 3 — Materiales endgame

- [x] Lingote Celestial: receta shaped 3x3 con materiales de las 7 mitologías (frozen_soul_crystal, gem_of_bishamon, bamboo_shaft, feather_of_victory, shard_of_divinity al centro, sun_blessed_alloy, mythril, filo_de_pluma, obsidiana_shard) — exige progresar por varios bosses
- [x] Set de Armadura Celestial (4 piezas): tier sobre netherite (prot 4/7/9/4, tough 4.0, ench 20), material CELESTIAL en ModArmorMaterials, texturas icono+layers vía `scripts/gen-celestial.ps1`, recetas estándar con lingotes
- [~] DECISIÓN: en vez de 16 items "benditos" (4 sets × 4 piezas), un solo set celestial endgame — menos bloat, misma meta de progresión

## Fase 4 — Config JSON simple

- [x] `com.mythicalswords.config.ModConfig` — GSON puro, sin dependencias; crea `config/mythicalswords.json` con defaults al primer arranque; valores clampeados
- [x] Valores: forgeMaxUpgrades (5), forgeDamageBonusPerUpgrade (0.15), forgeCooldownReductionPerUpgrade (0.10), bossHealthMultiplier, bossDamageMultiplier, abilityCooldownMultiplier
- [x] Cableado: forja lee límite/bonos del config; cooldown de habilidades escala con multiplicador; bosses aplican multiplicadores de vida/daño en `MythicalBossEntity.initialize()` (solo primer spawn, no re-escala al recargar mundo)

## Orden de ejecución

Fase 1 → Fase 5 → Fase 2 → Fase 3 → Fase 4

## Pendientes fuera de alcance v0.5 (backlog)

- Dragón montable: forma legítima de obtención (decidido: posponer)
- Advancements por boss (solo hay 4)
- Animaciones de ataque de bosses (GeckoLib)
- ~23 texturas de item faltantes (125 modelos vs 102 texturas)
- Sonidos propios (hoy alias de vanilla)
- Menas por bioma (comentarios dicen bioma-específico, código genera en todo overworld)
- Mobs menores por mitología (esbirros de bosses)
