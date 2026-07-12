# ROADMAP MAESTRO — Mythical Swords Mod

Plan a lo grande: de v0.5 (actual, probada) hasta v1.0 publicable y post-release.
Cada versión es jugable y estable al cerrarse. `PLAN-v0.5.md` (completado) y `PLAN-v0.6.md` (activo) son los detalles de sus versiones; este documento es la vista completa.

## Visión

El mod definitivo de mitologías: 7+ panteones con armas legendarias, bosses épicos,
esbirros, estructuras, dimensiones y progresión completa — del primer lingote de
mythril hasta montar un dragón en una isla celestial.

## Pilares de diseño

1. **Toda arma cuenta una historia** — cada item viene de una mitología real, con obtención temática (boss, forja, estructura)
2. **Progresión en loop cerrado** — minar → forjar → matar boss → materiales → runas/celestial → dimensión → dragón
3. **El mundo se siente habitado** — estructuras con guardianes, esbirros renovables, aldeas míticas
4. **Nada hardcodeado que el jugador quiera ajustar** — config JSON crece con cada sistema
5. **Trilingüe siempre** — en_us, es_es, es_mx en el mismo commit que el contenido

---

## v0.6 — "Mundo Vivo" (ACTIVA — detalle en PLAN-v0.6.md)

- ✅ F0 gate testing v0.5 | ✅ F0.5 GUI forja con textura
- F1: menas por bioma real + compat REI (categoría Forja Mítica)
- F2: combate bosses 2.0 — anims ataque GeckoLib, habilidad por fase, enrage, telegrafiado
- F3: 6 esbirros por mitología (draugr, oni menor, momia, guerrero jaguar, hoplita espectral, soldado de terracota) — spawn en estructuras, invocados por bosses, dropean materiales rúnicos (fuente renovable)
- F4: árbol de advancements por mitología + libro guía in-game
- F5: Dimensión Celestial — portal de celestial_stone, islas flotantes, templo, boss final **Guardián Celestial** (rota resistencias por afinidad) → **Corazón Celestial**
- F6: dragón montable obtenible (Corazón → huevo → eclosión → montura) + advancement "Jinete Celestial"

## v0.7 — "Arsenal" (armas y combate del jugador)

### Armas 3D en mano
- Modelos Blockbench para las 26 armas, por lotes: LEGENDARY (Excalibur, Kusanagi, Gungnir, Totsuka, Ruyi Jingu Bang) → EPIC → RARE/COMMON
- Display transforms cuidados (1ra/3ra persona, GUI, suelo, marco)

### Nuevos tipos de arma (por mitología, no solo espadas)
- **Hachas de guerra**: Labrys minoica (greek), Hacha de Perun (eslava nueva), Tomahawk sagrado
- **Lanzas**: Gungnir ya existe como espada → rework a lanza real con alcance +1, Trishula de Shiva (hindú nueva), Lanza de Longinus
- **Arcos/proyectiles**: Arco de Artemisa (flechas lunares), Arco de Houyi (chino, derriba "soles" = daño masivo a voladores), Chakram de Vishnu (boomerang)
- **Martillos**: Mjölnir (nórdico, lanzable + regresa, rayo en impacto) — el martillo del herrero ya sentó la base 3D
- **Escudos míticos**: Égida real (escudo con cara de Medusa: chance de petrificar=slowness IV), Escudo de Aquiles, Svalinn (inmune a fuego)

### Sistema de combate del jugador
- Combos: 3 golpes seguidos con arma mítica → 4to golpe potenciado (partícula + sonido)
- Parry: bloquear en ventana de 10 ticks → contraataque disponible 2s
- Dual wield temático: wakizashi off-hand con katanas japonesas

### Sonido completo
- Sonidos custom reales (grabados/sintetizados): swing por tier, forja (martillazo, temple), boss themes por mitología (loops cortos), runas al grabar
- Música de dimensión celestial

## v0.8 — "Civilizaciones" (el mundo habitado)

### Aldeas míticas (1 por mitología, 7 total)
- Estructuras de aldea: nórdica (longhouses), japonesa (dojo + torii), griega (ágora + columnas), egipcia (oasis + obelisco), mesoamericana (plaza + pirámide menor), china (pagoda), artúrica (castillo menor con mercado)
- NPCs aldeanos custom (GeckoLib) con profesiones: herrero mítico (repara barato), sabio (vende mapas a estructuras), guardia (defiende de esbirros hostiles)
- Trading: monedas por mitología (dracma, ryō, deben) o trueque con materiales

### Herrero Legendario amistoso
- Variante NPC pacífica del mini-boss: aparece en aldeas tras derrotar al hostil
- Servicios únicos: re-roll de una runa, transferir upgrades entre armas, desbloquear el 6to slot de upgrade (config)

### Mini-dungeons (contenido repetible entre bosses)
- 3-4 salas procedurales por mitología con esbirros + mini-jefe (esbirro élite con nombre) + cofre con loot table temática
- Llaves de dungeon dropeadas por esbirros del overworld (loop de juego nocturno)

### Eventos de mundo
- **Invasión mítica**: cada N días (config), oleada de esbirros de una mitología aleatoria ataca al jugador/aldea — recompensa: materiales rúnicos x3
- **Luna de sangre azteca**: esbirros jaguar spawn en superficie, Sed de Sangre cura doble

## v0.9 — "Dioses Mayores" (segunda generación de bosses)

### 7 bosses tier 2 (uno por panteón, invocación cara)
- **Zeus** (greek): rayos en área, invoca tormentras, fase voladora
- **Thor** (norse): Mjölnir boomerang, salto sísmico — dropea el Mjölnir del jugador
- **Amaterasu** (japanese): espejos de luz, ceguera, quema el campo
- **Ra en forma solar** (egyptian): rework del Ra actual como tier 2, disco solar
- **Tezcatlipoca** (mesoamerican): espejos de obsidiana, clones oscuros, roba el arma equipada 10s
- **Dragón Emperador** (chinese): serpentino volador tipo Quetzalcoatl, control de clima
- **Morgana** (arthurian): magia oscura, invoca caballeros corruptos, anti-Excalibur (buffea si la llevas)

### Mecánicas de raid
- Invocación: altar tier 2 construido con bloques celestiales + ofrenda (arma legendaria nivel máx de esa mitología — se consume!)
- Bossbar con fases visibles, arena delimitada, enrage timer suave (config)
- Loot: armas tier 2 ("Aspecto Divino" — mejoras de las originales), materiales para el set de armadura por mitología

### Sets de armadura por mitología (7 sets, ahora sí)
- Craft con material del dios tier 2; bonus de set temático (2pz/4pz): nórdico = resistencia a hielo + fuerza al bloquear; japonés = velocidad de ataque + iaijutsu gratis nivel 1; etc.
- Celestial queda como set "neutro" universal

## v1.0 — "Release" (calidad de publicación)

### Balance y pulido
- Pase de balance completo con hoja de cálculo: DPS por tier, TTK contra bosses, economía de materiales (drop rates vs costos)
- Modo dificultad del mod en config: Story / Normal / Mítico (multiplica todo)
- Pase de partículas/sonido/screenshake en TODOS los impactos

### Compatibilidad
- REI + EMI + JEI (las tres, abstraídas)
- WTHIT/Jade: tooltips de bloques del mod
- Trinkets: slots para reliquias (medusa_eye etc. como accesorios equipables)
- Patchouli opcional para la guía (mantener la propia como fallback)

### Infraestructura de release
- **Repo git + GitHub** (el proyecto HOY no tiene control de versiones — crítico)
- CI: GitHub Actions — build + validación JSON + GameTest en cada push
- GameTests: forja (4 operaciones), drops de bosses, receta celestial, config
- Datagen real (MythicalSwordsDataGenerator existe pero no genera nada): modelos/recetas/loot/lang generados, adiós JSONs a mano
- Publicación: CurseForge + Modrinth con descripción, galería (screenshots de Blockbench renders), changelog
- Wiki (GitHub wiki o página): tabla de armas, runas, bosses, biomas de menas
- Licencia de assets clarificada + créditos

### i18n
- Agregar pt_br, fr_fr, de_de (comunidades grandes de Minecraft)
- Auditoría de strings hardcodeadas restantes (tooltips de armas usan Text.literal — migrar a translatable)

## Post-1.0 (ideas registradas, sin compromiso)

- **Port 1.21.x** (NeoForge/Fabric multi-loader con Architectury) — decisión grande, evaluar demanda
- **Multijugador serio**: balance co-op de bosses (vida escala con jugadores cerca), raids de servidor
- **Panteones nuevos**: hindú (Trishula ya en v0.7), eslavo, celta, yoruba, polinesio — 1 por update de contenido
- **Pets míticos**: versiones cría de los bosses como mascotas cosméticas (drop raro)
- **Modo NG+**: re-invocar bosses con modificadores aleatorios (afijos) por mejor loot
- **Integración Origins**: clases por mitología al crear mundo
- **Eventos estacionales**: Ragnarök (invierno), Obon (agosto), Día de Muertos (noviembre)

---

## Infraestructura transversal (se hace DURANTE las versiones, no al final)

| Tema | Cuándo | Nota |
|------|--------|------|
| Git + GitHub | YA (antes de v0.6 F1) | Sin control de versiones hoy; riesgo real de perder trabajo |
| Datagen | v0.6-v0.7 gradual | Cada JSON nuevo se hace por datagen, los viejos se migran por lotes |
| GameTests | v0.7+ | Primero forja y loot (lo más frágil) |
| CI | Con el repo git | Build + JSON lint mínimo desde el día 1 del repo |
| Mixins reales | Cuando se necesiten | Los templates ExampleMixin se borran en la limpieza v1.0 |
| Performance | v0.9 | Profiling con Spark: partículas de auras, ServerScheduler, 13+ entidades GeckoLib en pantalla |

## Riesgos conocidos

1. **Sin git** — cualquier corrupción de disco pierde TODO. Mitigación: repo ya.
2. **Scope de v0.8/v0.9** — aldeas + NPCs es territorio de mods enteros. Mitigación: recortar a 3 aldeas primero, expandir después.
3. **GeckoLib anims a mano** — 12 bosses × ataques = mucho tiempo de animación. Mitigación: reusar esqueletos/anims base entre humanoides.
4. **Balance multi-tier** — con tier 2 el poder explota. Mitigación: hoja de balance desde v0.7, no al final.
5. **1.20.1 envejece** — para v1.0 puede convenir portar primero. Decidir en v0.9.

## Orden global

```
git+CI (ya) → v0.6 F1..F6 → v0.7 → v0.8 → v0.9 → decisión de port → v1.0 → post
```
