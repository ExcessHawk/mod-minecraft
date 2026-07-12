# Plan v0.6 — "Mundo Vivo"

Temas elegidos: esbirros por mitología, Dimensión Celestial + boss final, combate de bosses 2.0, progresión (advancements + guía + dragón). Medianos: menas por bioma, compat JEI/REI.

Orden por dependencias. Testing de v0.5 va primero como compuerta (fase 0) — decidido: se planea todo y luego se prueba.

## Fase 0 — Gate: probar v0.5 en juego (ANTES de escribir código de v0.6)

✅ COMPLETADA 2026-07-11 — usuario probó el mod con todos los cambios de v0.5: todo funciona.

Checklist runClient:
- [ ] Forja: craftear (receta mythril+sacred_iron+shard+obsidiana), colocar, ver modelo 3D + partículas ambientales
- [ ] GUI: slots visibles, botón no encima del output, nombre "Forja Mítica"
- [ ] Reparar: material correcto repara y consume 1; incorrecto avisa qué pide
- [ ] Mejorar: lingote+catalizador correcto por tier; par inválido rechazado; límite 5
- [ ] Runas: las 7 aplican su encantamiento, suben nivel, respetan máximo; efectos en combate funcionan
- [ ] Libro encantado: no duplica, respeta conflictos
- [ ] Drops: matar susanoo/oni/izanagi/quetzalcoatl/rey_arturo → sueltan arma+materiales
- [ ] Celestial: craftear lingote 3x3, armadura completa, layers visibles puestas
- [ ] Config: editar mythicalswords.json (ej. bossHealthMultiplier 2.0) y verificar efecto
- [ ] Boss Altar 3D render + invocación sigue funcionando
- [ ] Herrero Legendario empuña martillo 3D y lo dropea

## Fase 0.5 — GUI de la Forja con textura propia (PRIMERO, pedido explícito)

- [x] Textura de GUI real 176x166 (`textures/gui/mythical_forge.png`, `scripts/gen-forge-gui.ps1`): panel oscuro con bisel, barra de título con brasas, sockets con marcos de color (dorado=arma, gris=materiales, verde=salida), flecha con punta, separador de inventario
- [x] `MythicalForgeScreen`: `drawTexture` en vez de rectángulos a mano
- [x] Etiquetas y botón traducibles: `screen.mythicalswords.forge.*` (Arma/Mat/Sale/Forjar) en 3 idiomas

## Fase 1 — Base técnica (mediana, va primero porque toca worldgen que todo lo demás usa)

### Menas por bioma ✅ (2026-07-12)
`ModOreGeneration` refactorizado a tabla declarativa (`record OreEntry` + loop):
- [x] mythril → IS_MOUNTAIN | northsteel → IS_TAIGA + nevados | sacred_iron → llanuras/meadow/flower_forest | tamahagane → cherry_grove + dark_forest | jade → jungle + bamboo | obsidiana → IS_BADLANDS + sparse_jungle | orichalcum → IS_DEEP_OCEAN | uru → picos (jagged/frozen/stony) | voidsteel → deep_dark + dripstone_caves (JSON ya lo tenía a y -64..-8) | froststeel → hielo (frozen_peaks/ice_spikes/snowy_slopes/frozen_ocean)
- [ ] Actualizar tooltips/guía con dónde encontrar cada una (va con la guía de F4)

### Compat REI ✅ (2026-07-12)
- [x] Plugin REI: categoría "Forja Mítica" (icono = bloque de forja, workstation registrada) con displays generados de las MISMAS tablas de `MythicalForgeSystem`: reparar (por arma), mejorar (por arma: lingote+catalizador), grabar runa (7, arma genérica cicla)
- [x] Dependencia opcional: `modCompileOnly` REI 12.1.785 + entrypoint `rei_client` — solo carga si REI está instalado; `suggests: roughlyenoughitems`
- Pendiente usuario: probar en su instancia con REI (buscar "Forja Mítica" o click en el bloque)

## Fase 2 — Combate de bosses 2.0 (grande)

Base ya existe: `MythicalBossEntity` con 3 fases (100%→60%→30%).
- [x] Animaciones de ataque GeckoLib (2026-07-12): `attack_melee` + `attack_special` inyectadas en los 12 animation.json vía `scripts/gen-attack-anims.ps1` — 3 rigs: humanoide (swing de brazo / doble slam), Anubis sin brazos (embestida/mordida), Quetzalcóatl serpiente (latigazo/espiral). Controller "attack" triggerable en los 12 + melee dispara anim vía `tryAttack` override
- [x] Ataque pesado telegrafiado (base, todos los bosses lo heredan): anillo de partículas FLAME + rugido 1s antes (esquivable), luego slam AoE radio 5 (daño ×1.5, knockback); cooldown 8s, en fase 3 se acelera (telegraph 0.7s, cooldown 5s); el boss se detiene durante el wind-up. Overrideable por boss (`executeHeavyAttack`)
- [x] Habilidad por fase — auditoría completada (2026-07-12) y huecos rellenados:
  - HALLAZGO: Susanoo, Izanagi, Oni Oscuro y Quetzalcóatl extendían `HostileEntity` directo — sin bossbar, sin fases, sin goals (¡no perseguían ni atacaban!). Migrados a `MythicalBossEntity` con goals + arma visual (drop chance 0, el loot table da la limpia)
  - Susanoo: tormenta (rayos a todos los jugadores ≤20 bloques) | Izanagi: purificación (+80 vida) + Juicio Divino (columna de luz, daño+glowing) | Oni Oscuro: oscuridad (Darkness+Wither área) + Pulso Oscuro | Quetzalcóatl: vendaval (avienta jugadores + levitación) + embestida serpiente (dash volador)
  - Atenea: Estrategia (resistencia+debilidad área) + Lanza de Luz (daño a distancia con línea de partículas) | Loki: desvanecimiento (invisibilidad) + Truco (teleport a tu espalda + veneno) | Herrero: forja rugiente (inmune fuego, prende cercanos) + chispas fundidas
  - Anubis y Ra: transiciones de fase agregadas (resistencia/soul burst; llamarada solar + sol moribundo)
  - Ya COMPLETOS de antes: Odin, Rey Arturo, Sun Wukong
- [ ] Sonidos de boss por fase (hoy alias vanilla) — pospuesto a v0.7 Arsenal (sonido completo)

## Fase 3 — Esbirros por mitología (grande)

6 mobs menores GeckoLib (✅ 2026-07-12), spawn en su estructura + invocados por su boss en fase 2. Arquitectura: base `MythicalMinionEntity` + geo/anims COMPARTIDOS (`minion.geo.json`, layout skin vanilla 64x32) + textura por esbirro (`scripts/gen-minions.ps1`); `MinionModel`/`MinionRenderer` genéricos.
- [x] Draugr (norse) — tanque lento (40❤, armor 8); drop frozen_soul_crystal 30% + huesos; invocado por Odin; spawn en valhalla
- [x] Oni Menor (japanese) — rápido frágil (24❤, spd .38); gem_of_bishamon 25%; invocado por Oni Oscuro; spawn en oni
- [x] Momia Sirviente (egyptian) — golpe aplica Slowness II; moonstone_shard 25%; invocada por Anubis; spawn en desert
- [x] Guerrero Jaguar (mesoamerican) — salta sobre presas a 3-6 bloques; filo_de_pluma 25%; invocado por Quetzalcóatl; spawn en aztec
- [x] Hoplita Espectral (greek) — 30% Resistencia II al recibir golpe (muro de escudos); feather_of_victory 25%; invocado por Atenea; spawn en greek
- [x] Soldado de Terracota (chinese) — la primera "muerte" se rearma al 50% vida (NBT persistente); bamboo_shaft 25%; spawn en bamboo (Wukong ya tiene clones propios)
- [x] Spawn via `spawn_overrides` en 6 estructuras (no spawn global); helper `summonMinions()` en la base de bosses
- [x] Loop de economía cerrado: esbirros = fuente renovable de materiales rúnicos
- NOTA: datagen se pospuso (los JSONs de F3 son manuales, consistentes con el resto) — arranca en F4 con advancements

## Fase 4 — Progresión (mediana)

- [ ] Árbol de advancements: root → rama por mitología (encontrar estructura → matar boss) → "Matadioses" (los 11) → "Forjador Celestial" (lingote) → apunta a dimensión
- [ ] Recompensas: XP + materiales rúnicos por advancement de boss
- [ ] Libro guía in-game (item + pantalla custom simple): forja y sus 4 operaciones, tabla de runas, mapa de menas por bioma, bosses y sus brújulas

## Fase 5 — Dimensión Celestial + boss final (muy grande, al último)

- [ ] Bloque `celestial_stone` (+ pulido) para el marco del portal
- [ ] Portal: marco de celestial_stone activado con Lingote Celestial (ya existe, cierra el loop de Fase 3 v0.5)
- [ ] Dimensión datapack: islas flotantes (noise end-like), cielo custom, sin lluvia
- [ ] Estructura: templo celestial con boss altar final
- [ ] Boss final: **Guardián Celestial** — GeckoLib grande, 3 fases, rota resistencias por afinidad elemental (obliga a cambiar de arma), invoca esbirros de TODAS las mitologías
- [ ] Drop: **Corazón Celestial** (único por kill)

## Fase 6 — Dragón montable por fin obtenible (chica)

- [ ] Receta: Corazón Celestial + materiales → Huevo de Dragón Mítico
- [ ] Eclosión (colocar huevo, tiempo) → cría → crece → montable con silla (entity ya existe y funciona)
- [ ] Quitar el "hidden" del creative tab / spawn egg
- [ ] Advancement final: "Jinete Celestial"

## Orden de ejecución

F0 (gate testing) → F1 → F2 → F3 → F4 → F5 → F6

Racional: F1 primero (worldgen base + tooling de recetas que la guía de F4 documenta). F2 antes que F3 porque los esbirros reusan la infra de fases/anims. F5 al final porque es lo más grande y F4 le da el camino de progresión. F6 depende del drop de F5.

## Fuera de alcance v0.6 (backlog v0.7+)

- Armas 3D en mano (26 modelos)
- Sonidos custom completos (solo bosses en F2)
- Textura GUI de la forja
- Mixins reales (los templates siguen sin uso)
