# ROADMAP TÉCNICO — Arquitectura, pipeline de assets y tooling

Compañero de `ROADMAP.md`: el CÓMO de cada versión. Arquitectura de código, uso de
Blockbench/Blender/scripts, datagen, testing e infra.

---

## Estado actual (baseline v0.5/v0.6-F0.5)

```
com.mythicalswords (190+ archivos)
├── core/          registros manuales (ModItems ~650 líneas, ModBlocks, ModEntities...)
├── systems/       MythicalForgeSystem, WeaponLevelingSystem, AffinityEffectSystem
├── abilities/     13 habilidades + CooldownManager + ServerScheduler
├── enchantments/  7 vanilla-style + 7 ForgeExclusiveEnchantment (base class ✓)
├── entity/        13 GeckoLib entities + MythicalBossEntity (fases 60/30%)
├── client/        models/renderers GeckoLib, MythicalForgeScreen, effects/
├── weapons/       24 clases de espada, todo extiende MythicalWeaponItem (SwordItem)
├── config/        ModConfig (GSON, lazy singleton) ✓ nuevo
└── structures/    9 generators programáticos (no jigsaw)
```

**Deuda técnica conocida:**
- Sin git, sin CI, sin tests
- Datagen declarado (`MythicalSwordsDataGenerator`) pero vacío — todos los JSONs a mano (~250 archivos)
- Tooltips de armas con `Text.literal` (no traducibles)
- Mixins template sin uso (ExampleMixin)
- Estructuras programáticas (difíciles de editar vs jigsaw)
- ModItems monolítico — creative tab a mano, item por item

## Principios técnicos (aplican a todo lo nuevo)

1. **Familia nueva = base class** — patrón `ForgeExclusiveEnchantment` / `MythicalBossEntity`: comportamiento común arriba, datos en constructor
2. **Datos declarativos > código imperativo** — tablas/records/maps estáticos (patrón `RUNE_ENCHANTS`), no if-cadenas
3. **Todo JSON nuevo por datagen** a partir de v0.6 F3; legacy se migra por lotes
4. **Compat aislada en `compat/`** — nada del core importa mods externos; guard `FabricLoader.isModLoaded()`
5. **Texturas 2D = script regenerable** (`scripts/gen-*.ps1`); **modelos 3D = Blockbench** con export versionado
6. **Config crece con cada sistema** — cada valor de balance nuevo entra a ModConfig el mismo día

---

## Pipeline de assets (transversal)

### Texturas 2D (items, bloques, GUI, armor layers)
- PowerShell + System.Drawing (`scripts/gen-*.ps1`) — determinista, diffeable, regenerable
- Convención: 1 script por familia (gen-forge, gen-altar, gen-celestial, gen-forge-gui)
- Retoque a mano: abrir en Blockbench (paint tools) y guardar — el PNG manda si diverge del script; anotar en el script "// retocado a mano, no regenerar"

### Modelos 3D de bloques/items — Blockbench MCP playbook (lecciones reales)
1. `create_project` formato `java_block`
2. `create_texture` con `data`=ruta absoluta — el nombre queda del ARCHIVO, no del param
3. `place_cube` — la textura se pasa por llamada; `faces: true` = auto-UV
4. ⚠️ **PITFALL**: `faces` array con UV custom NO bindea la textura (cara queda verde pastel = sin textura). Arreglo: `risky_eval` seteando `cube.faces[f].texture = tex.uuid` + `uv`
5. ⚠️ **PITFALL**: `apply_texture applyTo:"blank"` puede pisar caras ya asignadas — verificar con screenshot
6. `set_camera_angle` + screenshot SIEMPRE antes de exportar (4 ángulos: iso, frente, lado, top)
7. `export_model` codec `java_block` → **revisar UVs exportados** (a veces salen `[0,0,1,1]`) → corregir en el JSON final a mano
8. JSON final: agregar `"parent": "minecraft:block/block"` (display transforms), `shade:false` en caras emisivas
9. Código: `.nonOpaque()` en settings si el modelo no es cubo lleno; subir `luminance` si tiene partes brillantes
10. Items held 3D: sección `display` a mano (thirdperson/firstperson/gui/ground) — plantilla en `models/item/blacksmith_hammer.json`

### Entidades GeckoLib (mobs, bosses)
- Blockbench con formato **geckolib** (plugin) → exporta `geo.json` + `animation.json`
- Animación vía MCP: `create_animation`, `manage_keyframes`, `animation_timeline`, `batch_keyframe_operations`, `animation_graph_editor` (easing)
- Reuso: esqueleto humanoide base compartido entre bosses/esbirros humanoides — solo cambia textura y proporciones; serpentinos (Quetzalcoatl, Dragón Emperador) comparten rig segmentado
- Convención de anims: `idle`, `walk`, `attack_melee`, `attack_special`, `phase_transition`, `death`

### Blender MCP — SOLO para
- Renders promocionales (CurseForge/Modrinth gallery) — importar geo + posar + ciclos
- Concepts/bocetos de estructuras grandes antes de construirlas
- NO para modelos de juego (el formato de juego sale de Blockbench)

---

## v0.6 "Mundo Vivo" — arquitectura

### F1 Menas por bioma
- Refactor `ModOreGeneration`: tabla declarativa
  ```java
  record OreEntry(Block ore, Predicate<BiomeSelectionContext> biomes,
                  int veinSize, int perChunk, int minY, int maxY) {}
  ```
  Lista estática + 1 loop de registro. `BiomeSelectors.tag()` / `.includeByKey()`.
- Config: multiplicador global de frecuencia de menas (`oreFrequencyMultiplier`)

### F1 REI
- `compat/rei/` con entrypoint `rei_client` en fabric.mod.json (`"suggests"` en deps)
- 3 display categories: Reparación, Mejora, Runas — datos leídos de los mismos maps públicos de `MythicalForgeSystem` (exponer getters inmutables, no duplicar tablas)

### F2 Combate 2.0
- `MythicalBossEntity` gana **state machine**:
  ```java
  enum BossAttack { NONE, MELEE, SPECIAL, TELEGRAPHING }
  // tick(): TELEGRAPHING (20 ticks, partículas) -> ejecuta -> cooldown
  ```
- 1 `AnimationController` extra por entidad para ataques (`triggerableAnim`)
- `TelegraphSystem` util: círculo/línea de partículas server-side en el suelo
- Cada boss define su tabla de ataques: `List<AttackDefinition>(anim, damage, range, telegraphTicks, phase)` — datos, no subclases nuevas

### F3 Esbirros
- Base `MythicalMinionEntity extends HostileEntity implements GeoEntity` — anims compartidas, drops por loot table (datagen desde aquí)
- Spawn: `structure/*.json` spawn_overrides (no spawn global) + `MythicalBossEntity.summonMinions(tipo, n)` en fase 2
- **Aquí arranca datagen real**: `FabricTagProvider`, `SimpleFabricLootTableProvider`, `FabricLanguageProvider` para lo nuevo

### F4 Progresión
- Advancements por datagen (`FabricAdvancementProvider`) — árbol generado de la misma tabla de bosses de `BossAltarBlock.BOSS_REGISTRY` (mover ese registry a `core/` como fuente única)
- Guía: `GuideBookItem` + `GuideScreen` con contenido **data-driven**: páginas en `assets/mythicalswords/guide/*.json` (título, texto, item a renderizar) — agregar página = agregar JSON

### F5 Dimensión Celestial
- Datapack: `dimension/celestial.json` + `dimension_type` + noise settings tipo end (islas flotantes) — sin ChunkGenerator custom en Java salvo necesidad
- `CelestialPortalBlock`: lógica propia de teleport (`FabricDimensions.teleport`), NO nether portal API
- Familia de bloques: `celestial_stone` (+ bricks, pulido) — texturas por script, modelos cube_all por datagen
- Guardián Celestial: reusa state machine F2 + mecánica nueva `AffinityRotationGoal` (cambia resistencia cada 25% vida, partícula del color de la afinidad activa)

### F6 Dragón
- `MythicalDragonEggBlock` + BlockEntity con timer de eclosión (NBT) → spawn cría con `Owner` UUID
- Crecimiento por edad (DataTracker int) → escala del modelo GeckoLib por edad

## v0.7 "Arsenal" — arquitectura

- **Refactor clave**: extraer interfaz de `MythicalWeaponItem`
  ```java
  interface MythicalWeapon { WeaponTier tier(); ElementalAffinity affinity(); String mythology(); }
  ```
  Implementaciones: `MythicalSwordItem` (rename del actual), `MythicalSpearItem` (alcance +1 vía attribute `generic.attack_range`? 1.20.1 no lo tiene → raycast custom en use), `MythicalBowItem`, `MythicalThrownItem`, `MythicalShieldItem`
  — Forja/runas/leveling pasan a chequear la interfaz, no la clase
- Mjölnir: `ThrownHammerEntity` (patrón trident: `PersistentProjectileEntity` + loyalty custom + rayo en impacto)
- Armas 3D en mano: modelos `elements` + `display` (plantilla martillo); si el resultado en GUI es feo → dual model (sprite en GUI + 3D en mano) con `DynamicItemRenderer` — decidir con las 5 LEGENDARY primero
- Combos/parry: `PlayerCombatSystem` server-side, `Map<UUID, CombatState>` con timestamps de ticks; eventos `AttackEntityCallback` + `ServerTickEvents`; feedback client por packet S2C
- Sonido: .ogg reales en `assets/mythicalswords/sounds/` — producción FUERA del repo (Audacity/jsfxr), el repo solo versiona el resultado

## v0.8 "Civilizaciones" — arquitectura

- **Migración a jigsaw**: estructuras nuevas con template pools + processor lists (editables con Structure Blocks in-game, exportadas a NBT) — los 9 generators programáticos viejos se quedan, lo nuevo no se hace en Java
- NPCs: `MythicalVillagerEntity` (PathAwareEntity + GeoEntity) con `TradeOfferList` custom; screen de trades: reusar `MerchantScreenHandler` vanilla si alcanza
- Mini-dungeons: jigsaw de 3-4 salas + `EliteMinionEntity` (esbirro + nombre + modifier aleatorio)
- Invasiones: `InvasionManager` (PersistentState del mundo) — timer, oleadas, recompensa; TODO config-driven
- **Gate de datagen**: para entrar a v0.8, el 100% de recetas/loot/advancements nuevos sale de datagen

## v0.9 "Dioses Mayores" — arquitectura

- Bosses tier 2 = mismas bases (state machine + AttackDefinition), CERO clases de framework nuevas — si algo no cabe, el framework de F2 se arregla primero
- Ofrenda de invocación: `Tier2AltarBlock` valida arma legendaria nivel máx (`WeaponLevelingSystem.MAX_LEVEL`) y la consume → summon
- Sets de armadura: generalizar `ArmorSetHandler` a registro:
  ```java
  record SetBonus(ArmorMaterial material, StatusEffect twoPiece, Runnable fourPieceTick) {}
  ```
- **Pase de performance** (Spark): pooling de partículas de auras (ParticlePool ya existe — auditar), distancia de render de GeckoLib anims, `ServerScheduler` a PriorityQueue si crece

## v1.0 "Release" — infraestructura

- `compat/` con adapters: interfaz propia `ForgeRecipeDisplay` + impl REI/EMI/JEI (solo la activa carga)
- GameTests (`src/gametest`): forja 4 operaciones (incluye rechazos), receta celestial, drops de los 12 bosses (loot table id = entity id — el bug de rey_arturo NO puede volver), config clamps
- CI GitHub Actions: `build.yml` = JDK17 + `gradle build` + lint JSON (script) + GameTest headless; artifact = jar
- Publicación: plugins `modrinth-minotaur` + `net.darkhax.curseforgegradle` en build.gradle, changelog desde git tags
- Limpieza: borrar mixins template, migrar `Text.literal` de tooltips a translatable, borrar `boss_altar.png` legacy si ya nada la usa
- Datagen: migración total de JSONs legacy (script de verificación: datagen output == JSONs actuales antes de borrar)

---

## Infra inmediata (antes de v0.6 F1)

1. **git init** en `mod-minecraft/` con `.gitignore`: `build/`, `.gradle/`, `run/`, `*.log`, `.kiro/` (evaluar), `console.log`
2. Primer commit = estado actual completo (código + scripts + docs)
3. Repo GitHub privado + push
4. CI mínima desde el día 1: build + validación JSON
5. Convención de commits: Conventional Commits (`feat:`, `fix:`, `assets:`)

## Matriz herramienta → tarea

| Tarea | Herramienta | Notas |
|-------|------------|-------|
| Textura item/bloque 16x16 | script PS1 | regenerable, diffeable |
| Textura GUI | script PS1 | layout documentado en el script |
| Retoque artístico de PNG | Blockbench paint | el PNG manda tras retoque |
| Modelo 3D bloque/item | Blockbench MCP → export java_block | playbook arriba, corregir UVs |
| Modelo + anims de entidad | Blockbench (formato geckolib) | rigs compartidos por familia |
| Animar ataques | Blockbench MCP (keyframes/timeline) | convención de nombres de anims |
| Renders promocionales | Blender MCP | importa geo, posa, render ciclos |
| Estructuras nuevas | Structure Blocks in-game + jigsaw | NO más generators Java |
| JSONs (recetas/loot/adv/modelos simples) | Datagen Fabric | desde v0.6 F3 |
| Sonidos | Audacity/jsfxr (fuera de repo) | solo .ogg final se versiona |
