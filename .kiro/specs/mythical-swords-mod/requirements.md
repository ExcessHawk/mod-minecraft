# Requirements Document

## Introduction

Este documento define los requisitos para un mod de Minecraft Java Edition 1.20.1 que añade espadas y armas míticas de diferentes mitologías globales con encantamientos especiales únicos. El mod incluirá armas legendarias como Excalibur, lanzas míticas, y otras armas icónicas con habilidades especiales que van más allá de los encantamientos estándar de Minecraft.

## Glossary

- **MythicalSwordsMod**: El sistema de mod que añade armas míticas al juego
- **MythicalWeapon**: Cualquier arma legendaria añadida por el mod (espadas, lanzas, etc.)
- **SpecialEnchantment**: Encantamiento único que no existe en Minecraft vanilla
- **WeaponAbility**: Habilidad especial activable de un arma mítica
- **Player**: El jugador de Minecraft que usa las armas
- **CraftingRecipe**: Receta personalizada para crear armas míticas
- **WeaponTexture**: Archivo de imagen que define la apariencia visual del arma
- **LootTable**: Tabla que define dónde y cómo se pueden obtener las armas
- **MythicalOre**: Mineral personalizado añadido por el mod para craftear armas
- **MythicalIngot**: Lingote procesado de minerales míticos usado en recetas
- **WeaponLevel**: Nivel de experiencia de un arma mítica que mejora con el uso
- **MythicalBoss**: Jefe personalizado basado en criaturas mitológicas
- **WeaponAura**: Efecto visual de partículas alrededor de un arma mítica
- **MythicalForge**: Bloque especial para mejorar y reparar armas míticas
- **ElementalAffinity**: Tipo elemental de un arma que afecta su efectividad contra ciertos enemigos

## Requirements

### Requirement 1

**User Story:** Como jugador, quiero poder obtener armas míticas legendarias de diferentes mitologías globales, para experimentar con armas icónicas de diversas culturas.

#### Acceptance Criteria

1. THE MythicalSwordsMod SHALL register the following craftable weapons from Arthurian mythology: Clarent, Caliburn
2. THE MythicalSwordsMod SHALL register the following craftable weapons from Greek mythology: Niké Blade, Harpe, Xiphos Sagrado
3. THE MythicalSwordsMod SHALL register the following craftable weapons from Norse mythology: Gram, Skofnung, Hofund
4. THE MythicalSwordsMod SHALL register the following craftable weapons from Japanese mythology: Masamune, Naginata de Bishamon
5. THE MythicalSwordsMod SHALL register the following craftable weapons from Mesoamerican mythology: Macuahuitl, Atl-Tlachinolli
6. THE MythicalSwordsMod SHALL register the following craftable weapons from Chinese mythology: Hoja de los Ocho Inmortales, Zhanlu, Gan Jiang, Hoja Lunar
7. THE MythicalSwordsMod SHALL register the following craftable weapons from Hindu mythology: Astra de Agni, Vajra
8. THE MythicalSwordsMod SHALL register the following craftable weapons from Egyptian mythology: Lanza de Sobek, Daga de Escorpión
9. THE MythicalSwordsMod SHALL register the following craftable weapons from Celtic mythology: Red Branch Blade
10. THE MythicalSwordsMod SHALL register the following boss-drop exclusive weapons: Excalibur (artúrica), Aegis Edge (griega), Gungnir (nórdica), Laevateinn (nórdica), Kusanagi-no-Tsurugi (japonesa), Muramasa (japonesa), Totsuka-no-Tsurugi (japonesa), Serpiente de Fuego (mesoamericana), Ruyi Jingu Bang (china), Sudarshana Chakra (hindú), Trishula (hindú), Khopesh de Ra (egipcia), Báculo de Anubis (egipcia), Fragarach (celta), Gae Bulg (celta), Espada del Dragón Amarillo (china/coreana)
11. WHEN a Player crafts or obtains a MythicalWeapon, THE MythicalSwordsMod SHALL add the weapon to the player inventory
12. THE MythicalSwordsMod SHALL ensure each MythicalWeapon has unique base damage and durability values
13. THE MythicalSwordsMod SHALL ensure boss-drop weapons have significantly higher base stats than craftable weapons

### Requirement 2

**User Story:** Como jugador, quiero que cada arma mítica tenga encantamientos especiales únicos, para experimentar habilidades que no existen en Minecraft vanilla.

#### Acceptance Criteria

1. THE MythicalSwordsMod SHALL implement at least 3 unique SpecialEnchantments not available in vanilla Minecraft
2. WHEN a MythicalWeapon is created, THE MythicalSwordsMod SHALL apply at least one SpecialEnchantment to the weapon
3. THE MythicalSwordsMod SHALL ensure each SpecialEnchantment has a visible effect when used
4. WHEN a Player uses a MythicalWeapon with SpecialEnchantment, THE MythicalSwordsMod SHALL trigger the enchantment effect
5. THE MythicalSwordsMod SHALL prevent SpecialEnchantments from being applied to non-mythical weapons

### Requirement 3

**User Story:** Como jugador, quiero que las armas míticas tengan texturas únicas y visualmente distintivas, para poder identificarlas fácilmente y disfrutar de su apariencia legendaria.

#### Acceptance Criteria

1. THE MythicalSwordsMod SHALL provide a unique WeaponTexture for each MythicalWeapon
2. THE MythicalSwordsMod SHALL ensure each WeaponTexture is visually distinct from vanilla Minecraft weapons
3. WHEN a Player holds a MythicalWeapon, THE MythicalSwordsMod SHALL render the weapon with its custom WeaponTexture
4. THE MythicalSwordsMod SHALL create WeaponTextures in 16x16 pixel format compatible with Minecraft 1.20.1
5. THE MythicalSwordsMod SHALL ensure WeaponTextures reflect the mythological origin of each weapon

### Requirement 4

**User Story:** Como jugador, quiero poder craftear algunas armas míticas y obtener otras exclusivamente derrotando jefes en dungeons, para tener diferentes niveles de rareza y desafío.

#### Acceptance Criteria

1. THE MythicalSwordsMod SHALL define CraftingRecipes for at least 2 MythicalWeapons
2. THE MythicalSwordsMod SHALL designate at least 2 MythicalWeapons as boss-drop exclusive items without CraftingRecipes
3. WHEN a Player completes a valid CraftingRecipe, THE MythicalSwordsMod SHALL create the corresponding MythicalWeapon
4. WHEN a Player defeats a dungeon boss, THE MythicalSwordsMod SHALL drop the corresponding boss-exclusive MythicalWeapon
5. THE MythicalSwordsMod SHALL require rare or challenging materials in each CraftingRecipe
6. THE MythicalSwordsMod SHALL ensure boss-exclusive weapons are more powerful than craftable weapons

### Requirement 5

**User Story:** Como jugador, quiero que algunas armas míticas tengan habilidades especiales activables, para poder usar poderes únicos en combate o exploración.

#### Acceptance Criteria

1. THE MythicalSwordsMod SHALL implement at least 2 different WeaponAbilities across the mythical weapons
2. WHEN a Player right-clicks while holding a MythicalWeapon with WeaponAbility, THE MythicalSwordsMod SHALL activate the ability
3. THE MythicalSwordsMod SHALL apply a cooldown period after a WeaponAbility is used
4. WHEN a WeaponAbility is on cooldown, THE MythicalSwordsMod SHALL display the remaining cooldown time to the Player
5. THE MythicalSwordsMod SHALL ensure WeaponAbilities are thematically appropriate to the weapon's mythology

### Requirement 6

**User Story:** Como jugador, quiero encontrar y minar minerales especiales en el mundo, para poder craftear las armas míticas con materiales únicos y desafiantes.

#### Acceptance Criteria

1. THE MythicalSwordsMod SHALL add at least 2 different types of MythicalOre to world generation
2. WHEN a Player mines a MythicalOre block, THE MythicalSwordsMod SHALL drop the corresponding raw ore item
3. THE MythicalSwordsMod SHALL generate MythicalOres at rare spawn rates in specific dimension layers
4. WHEN a Player smelts raw MythicalOre, THE MythicalSwordsMod SHALL produce MythicalIngots
5. THE MythicalSwordsMod SHALL require MythicalIngots as primary materials in craftable weapon recipes
6. THE MythicalSwordsMod SHALL ensure CraftingRecipes require multiple steps and rare additional materials beyond MythicalIngots

### Requirement 7

**User Story:** Como jugador, quiero que el mod sea compatible con Minecraft 1.20.1 y funcione correctamente con otros mods, para poder usarlo en mi instalación existente sin conflictos.

#### Acceptance Criteria

1. THE MythicalSwordsMod SHALL be compatible with Minecraft Java Edition version 1.20.1
2. THE MythicalSwordsMod SHALL use Fabric mod loader API and Fabric API
3. WHEN the mod is loaded, THE MythicalSwordsMod SHALL initialize without causing crashes or errors
4. THE MythicalSwordsMod SHALL follow Minecraft modding best practices to minimize conflicts with other mods
5. THE MythicalSwordsMod SHALL register all items, enchantments, and recipes using proper Minecraft registry systems


### Requirement 8

**User Story:** Como jugador, quiero que mis armas míticas ganen experiencia y suban de nivel con el uso, para sentir progresión y ver cómo mis armas se vuelven más poderosas.

#### Acceptance Criteria

1. THE MythicalSwordsMod SHALL track a WeaponLevel for each MythicalWeapon instance
2. WHEN a Player kills a mob with a MythicalWeapon, THE MythicalSwordsMod SHALL increase the weapon experience points
3. WHEN a MythicalWeapon gains enough experience, THE MythicalSwordsMod SHALL increase its WeaponLevel
4. WHEN a MythicalWeapon levels up, THE MythicalSwordsMod SHALL increase its base damage and ability effectiveness
5. THE MythicalSwordsMod SHALL display the current WeaponLevel in the weapon tooltip

### Requirement 9

**User Story:** Como jugador, quiero enfrentar jefes mitológicos únicos en el mundo, para obtener las armas legendarias más poderosas como recompensa.

#### Acceptance Criteria

1. THE MythicalSwordsMod SHALL add the following MythicalBoss entities: Rey Arturo (drops Excalibur), Atenea (drops Aegis Edge), Odín (drops Gungnir), Loki (drops Laevateinn), Susanoo (drops Kusanagi-no-Tsurugi), Oni Oscuro (drops Muramasa), Izanagi (drops Totsuka-no-Tsurugi), Quetzalcóatl (drops Serpiente de Fuego), Sun Wukong (drops Ruyi Jingu Bang), Vishnu (drops Sudarshana Chakra), Shiva (drops Trishula), Ra (drops Khopesh de Ra), Anubis (drops Báculo de Anubis), Lugh (drops Fragarach), Cúchulainn (drops Gae Bulg), Dragón Amarillo (drops Espada del Dragón Amarillo)
2. THE MythicalSwordsMod SHALL spawn each MythicalBoss in specific structures or biomes appropriate to their mythology
3. WHEN a Player defeats a MythicalBoss, THE MythicalSwordsMod SHALL drop the corresponding boss-exclusive MythicalWeapon with 100 percent probability
4. THE MythicalSwordsMod SHALL ensure each MythicalBoss has unique attack patterns and abilities themed to their mythology
5. THE MythicalSwordsMod SHALL ensure MythicalBosses have health pools at least 3 times larger than the Ender Dragon
6. THE MythicalSwordsMod SHALL prevent MythicalBosses from respawning naturally after defeat

### Requirement 10

**User Story:** Como jugador, quiero que las armas míticas tengan efectos visuales impresionantes como auras y partículas, para que se vean tan legendarias como son.

#### Acceptance Criteria

1. THE MythicalSwordsMod SHALL render a unique WeaponAura for each MythicalWeapon type
2. WHEN a Player holds a MythicalWeapon, THE MythicalSwordsMod SHALL display particle effects around the weapon
3. WHEN a Player uses a WeaponAbility, THE MythicalSwordsMod SHALL display enhanced visual effects
4. THE MythicalSwordsMod SHALL play custom sound effects when MythicalWeapons are used
5. THE MythicalSwordsMod SHALL ensure WeaponAuras are thematically appropriate to each weapon's mythology

### Requirement 11

**User Story:** Como jugador, quiero usar una forja mítica especial para mejorar y reparar mis armas legendarias, para mantenerlas poderosas y personalizarlas.

#### Acceptance Criteria

1. THE MythicalSwordsMod SHALL add a MythicalForge block that can be crafted or found
2. WHEN a Player interacts with a MythicalForge, THE MythicalSwordsMod SHALL open a custom GUI
3. WHEN a Player places a MythicalWeapon in the MythicalForge with materials, THE MythicalSwordsMod SHALL repair the weapon durability
4. THE MythicalSwordsMod SHALL allow upgrading WeaponAbilities through the MythicalForge
5. THE MythicalSwordsMod SHALL require rare materials for forge operations

### Requirement 12

**User Story:** Como jugador, quiero que las armas tengan afinidades elementales que las hagan más efectivas contra ciertos enemigos, para añadir estrategia al combate.

#### Acceptance Criteria

1. THE MythicalSwordsMod SHALL assign an ElementalAffinity to each MythicalWeapon
2. WHEN a Player attacks a mob with a MythicalWeapon, THE MythicalSwordsMod SHALL apply bonus damage based on ElementalAffinity matchups
3. THE MythicalSwordsMod SHALL implement at least 4 different ElementalAffinity types
4. THE MythicalSwordsMod SHALL display the ElementalAffinity in the weapon tooltip
5. WHEN a fire-affinity weapon is used in cold biomes, THE MythicalSwordsMod SHALL apply increased effectiveness

### Requirement 13

**User Story:** Como jugador, quiero que algunas armas tengan habilidades atmosféricas como invocar rayos o tormentas, para sentir el poder de los dioses.

#### Acceptance Criteria

1. THE MythicalSwordsMod SHALL implement at least one weapon with lightning-summoning ability
2. WHEN a Player activates a lightning WeaponAbility, THE MythicalSwordsMod SHALL spawn lightning bolts at the target location
3. THE MythicalSwordsMod SHALL implement weather-affecting abilities for appropriate mythical weapons
4. WHEN atmospheric abilities are used, THE MythicalSwordsMod SHALL create appropriate visual and sound effects
5. THE MythicalSwordsMod SHALL ensure atmospheric abilities have balanced cooldowns and costs


### Requirement 14

**User Story:** Como jugador, quiero que las recetas de crafteo requieran materiales míticos especiales y múltiples pasos, para que obtener las armas crafteables sea un desafío significativo.

#### Acceptance Criteria

1. THE MythicalSwordsMod SHALL add the following common mythical materials: Mythril Ingot, Sacred Iron, Bronce Bendito, Oro Ritual
2. THE MythicalSwordsMod SHALL add the following culture-specific materials: Shard of Divinity, Northsteel Ingot, Sun-Blessed Alloy, Jade Imperial, Obsidiana Ritual, Filo de Pluma de Quetzal, Acero Tamahagane, Gem of Bishamon, Ra's Sun Core, Scorpion Venom Gland, Agni's Flame Core, Vajra Crystal, Soul of the Swordsmith, Sacred Water of Amaterasu, Dust of Longevity, Caña del Nilo, Bone Handle, Bamboo Reinforced Shaft, Binding Cloth of the Monks, Spiritbound Leather, Frozen Soul Crystal, Celtic Moonstone, Druidic Oak Core, Dragon Fang Fragment, Feather of Victory, Stone of Destiny Fragment, Leather Grip, Mango Largo Japonés, Palo Ritual, Essence of Righteousness, Moonstone Shard, Soul Fragment, Lover's Bond Token, Rainbow Bridge Fragment, Peace Gem
3. THE MythicalSwordsMod SHALL define the following crafting recipe for Harpe: 2 Mythril Ingot, 1 Sacred Iron, 1 Shard of Divinity, 1 Leather Grip
4. THE MythicalSwordsMod SHALL define the following crafting recipe for Xiphos Sagrado: 2 Sacred Iron, 1 Bronce Bendito, 1 Shard of Divinity
5. THE MythicalSwordsMod SHALL define the following crafting recipe for Niké Blade: 2 Mythril Ingot, 1 Sun-Blessed Alloy, 1 Feather of Victory
6. THE MythicalSwordsMod SHALL define the following crafting recipe for Gram: 3 Northsteel Ingot, 1 Sun-Blessed Alloy, 1 Dragon Fang Fragment
7. THE MythicalSwordsMod SHALL define the following crafting recipe for Skofnung: 2 Northsteel Ingot, 1 Spiritbound Leather, 1 Frozen Soul Crystal
8. THE MythicalSwordsMod SHALL define the following crafting recipe for Hofund: 2 Northsteel Ingot, 2 Oro Ritual, 1 Rainbow Bridge Fragment
9. THE MythicalSwordsMod SHALL define the following crafting recipe for Macuahuitl: 1 Palo Ritual, 3 Obsidiana Ritual, 1 Filo de Pluma de Quetzal
10. THE MythicalSwordsMod SHALL define the following crafting recipe for Atl-Tlachinolli: 2 Obsidiana Ritual, 1 Agni's Flame Core, 1 Bamboo Reinforced Shaft
11. THE MythicalSwordsMod SHALL define the following crafting recipe for Naginata de Bishamon: 2 Acero Tamahagane, 1 Mango Largo Japonés, 1 Gem of Bishamon
12. THE MythicalSwordsMod SHALL define the following crafting recipe for Masamune: 3 Acero Tamahagane, 1 Soul of the Swordsmith, 1 Sacred Water of Amaterasu
13. THE MythicalSwordsMod SHALL define the following crafting recipe for Hoja de los Ocho Inmortales: 2 Jade Imperial, 1 Mythril Ingot, 1 Dust of Longevity
14. THE MythicalSwordsMod SHALL define the following crafting recipe for Zhanlu: 2 Jade Imperial, 2 Sacred Iron, 1 Essence of Righteousness
15. THE MythicalSwordsMod SHALL define the following crafting recipe for Gan Jiang: 2 Sacred Iron, 1 Mythril Ingot, 1 Soul Fragment, 1 Lover's Bond Token
16. THE MythicalSwordsMod SHALL define the following crafting recipe for Hoja Lunar: 2 Mythril Ingot, 1 Jade Imperial, 1 Moonstone Shard
17. THE MythicalSwordsMod SHALL define the following crafting recipe for Lanza de Sobek: 1 Ra's Sun Core, 2 Sacred Iron, 1 Caña del Nilo
18. THE MythicalSwordsMod SHALL define the following crafting recipe for Daga de Escorpión: 1 Sacred Iron, 1 Scorpion Venom Gland, 1 Bone Handle
19. THE MythicalSwordsMod SHALL define the following crafting recipe for Astra de Agni: 1 Agni's Flame Core, 2 Sun-Blessed Alloy, 1 Bamboo Reinforced Shaft
20. THE MythicalSwordsMod SHALL define the following crafting recipe for Vajra: 1 Vajra Crystal, 2 Sacred Iron, 1 Binding Cloth of the Monks
21. THE MythicalSwordsMod SHALL define the following crafting recipe for Red Branch Blade: 2 Sacred Iron, 1 Celtic Moonstone, 1 Druidic Oak Core
22. THE MythicalSwordsMod SHALL define the following crafting recipe for Clarent: 2 Mythril Ingot, 1 Sacred Iron, 1 Shard of Divinity, 1 Peace Gem
23. THE MythicalSwordsMod SHALL define the following crafting recipe for Caliburn: 2 Sacred Iron, 1 Mythril Ingot, 1 Stone of Destiny Fragment
24. WHEN a Player attempts to craft a MythicalWeapon without all required materials, THE MythicalSwordsMod SHALL not produce the weapon
25. THE MythicalSwordsMod SHALL ensure Shard of Divinity is obtainable from Greek temple structures
26. THE MythicalSwordsMod SHALL ensure Soul of the Swordsmith is obtainable as a drop from a mini-boss Legendary Blacksmith entity
27. THE MythicalSwordsMod SHALL ensure Gem of Bishamon is obtainable from Japanese temple chests
28. THE MythicalSwordsMod SHALL ensure Dust of Longevity is obtainable from Chinese temple chests
29. THE MythicalSwordsMod SHALL ensure Sacred Water of Amaterasu is obtainable from Shinto shrine structures
30. THE MythicalSwordsMod SHALL ensure Vajra Crystal is obtainable from Himalayan mountain peak structures
31. THE MythicalSwordsMod SHALL ensure Celtic Moonstone is obtainable from Celtic stone circle structures
32. THE MythicalSwordsMod SHALL ensure Ra's Sun Core is obtainable from Egyptian pyramid chests
33. THE MythicalSwordsMod SHALL ensure Filo de Pluma de Quetzal is obtainable from jungle temple structures
34. THE MythicalSwordsMod SHALL ensure all other special materials are obtainable through mining rare ores, defeating specific mobs, or exploring cultural structures


### Requirement 15

**User Story:** Como desarrollador del mod, quiero que todos los items, bloques y entidades tengan IDs de registro consistentes, para facilitar el desarrollo y evitar conflictos.

#### Acceptance Criteria

1. THE MythicalSwordsMod SHALL use the namespace "mythicalswords" for all registry IDs
2. THE MythicalSwordsMod SHALL register craftable weapons with the following IDs: mythicalswords:harpe, mythicalswords:xiphos_sagrado, mythicalswords:nike_blade, mythicalswords:gram, mythicalswords:skofnung, mythicalswords:hofund, mythicalswords:macuahuitl, mythicalswords:atl_tlachinolli, mythicalswords:naginata_bishamon, mythicalswords:masamune, mythicalswords:hoja_ocho_inmortales, mythicalswords:zhanlu, mythicalswords:gan_jiang, mythicalswords:hoja_lunar, mythicalswords:lanza_sobek, mythicalswords:daga_escorpion, mythicalswords:astra_agni, mythicalswords:vajra, mythicalswords:red_branch_blade, mythicalswords:clarent, mythicalswords:caliburn
3. THE MythicalSwordsMod SHALL register boss-drop weapons with the following IDs: mythicalswords:excalibur, mythicalswords:aegis_edge, mythicalswords:gungnir, mythicalswords:laevateinn, mythicalswords:kusanagi, mythicalswords:muramasa, mythicalswords:totsuka, mythicalswords:serpiente_fuego, mythicalswords:ruyi_jingu_bang, mythicalswords:sudarshana_chakra, mythicalswords:trishula, mythicalswords:khopesh_ra, mythicalswords:baculo_anubis, mythicalswords:fragarach, mythicalswords:gae_bulg, mythicalswords:espada_dragon_amarillo
4. THE MythicalSwordsMod SHALL register all materials with consistent naming pattern using underscores for spaces
5. THE MythicalSwordsMod SHALL register all boss entities with the prefix "mythicalswords:boss_" followed by the boss name


### Requirement 16

**User Story:** Como jugador, quiero que el mod funcione suavemente sin causar lag, para disfrutar del juego sin problemas técnicos.

#### Acceptance Criteria

1. THE MythicalSwordsMod SHALL NOT reduce client FPS by more than 10 percent when 10 players hold mythical weapons simultaneously
2. THE MythicalSwordsMod SHALL complete particle rendering in less than 5 milliseconds per tick under typical conditions
3. THE MythicalSwordsMod SHALL complete boss AI updates in less than 10 milliseconds per tick with 3 active bosses under typical conditions
4. THE MythicalSwordsMod SHALL generate ore in chunks in less than 50 milliseconds per chunk
5. THE MythicalSwordsMod SHALL use less than 100 megabytes of additional memory when fully loaded
6. WHEN performance targets cannot be met under stress conditions, THE MythicalSwordsMod SHALL gracefully reduce visual effects and particle counts

### Requirement 17

**User Story:** Como jugador, quiero encontrar estructuras míticas en el mundo, para explorar y obtener materiales especiales.

#### Acceptance Criteria

1. THE MythicalSwordsMod SHALL generate cultural structures at least 1000 blocks apart from each other
2. THE MythicalSwordsMod SHALL ensure structures generate completely without being cut off by terrain
3. WHEN a structure generates, THE MythicalSwordsMod SHALL place loot chests with culture-specific materials
4. THE MythicalSwordsMod SHALL prevent structures from overlapping with vanilla Minecraft structures
5. THE MythicalSwordsMod SHALL generate structures only in biomes appropriate to their mythology
6. THE MythicalSwordsMod SHALL ensure at least one structure of each type generates within 5000 blocks of spawn
