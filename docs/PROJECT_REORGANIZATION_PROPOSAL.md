# 📁 Propuesta de Reorganización del Proyecto por Categorías

## 🎯 Objetivo

Reorganizar la estructura de carpetas del mod para separar los assets (texturas, modelos, JSONs) por categorías lógicas, facilitando:
- ✅ Navegación más intuitiva
- ✅ Mantenimiento más fácil
- ✅ Escalabilidad para futuras mitologías
- ✅ Colaboración en equipo más clara

---

## 📂 ESTRUCTURA ACTUAL (Problemática)

```
src/main/resources/assets/mythicalswords/
├── textures/
│   ├── block/
│   │   └── (6 ores + boss_altar mezclados)
│   └── item/
│       └── (54 items todos mezclados: armas, materiales, lingotes, etc.)
│
└── models/
    ├── block/
    │   └── (6 modelos mezclados)
    └── item/
        └── (54 modelos todos mezclados)
```

**Problemas:**
- ❌ Difícil encontrar un item específico entre 54 archivos
- ❌ No hay separación lógica entre armas, materiales, ores
- ❌ Difícil ver qué mitología tiene qué items
- ❌ Complicado para nuevos colaboradores

---

## 📂 ESTRUCTURA PROPUESTA (Organizada)

### Opción 1: Por Tipo de Item (Recomendada)

```
src/main/resources/assets/mythicalswords/
├── textures/
│   ├── block/
│   │   ├── ores/
│   │   │   ├── mythril_ore.png
│   │   │   ├── northsteel_ore.png
│   │   │   ├── sacred_iron_ore.png
│   │   │   ├── tamahagane_ore.png
│   │   │   ├── obsidiana_ritual_ore.png
│   │   │   └── jade_imperial_ore.png
│   │   │
│   │   └── special/
│   │       └── boss_altar.png
│   │
│   └── item/
│       ├── weapons/
│       │   ├── arthurian/
│       │   │   ├── excalibur.png
│       │   │   ├── caliburn.png
│       │   │   └── clarent.png
│       │   │
│       │   ├── norse/
│       │   │   ├── gram.png
│       │   │   ├── skofnung.png
│       │   │   ├── hofund.png
│       │   │   ├── gungnir.png
│       │   │   └── laevateinn.png
│       │   │
│       │   ├── greek/
│       │   │   ├── harpe.png
│       │   │   ├── xiphos_sagrado.png
│       │   │   ├── nike_blade.png
│       │   │   └── aegis_edge.png
│       │   │
│       │   ├── japanese/
│       │   │   ├── kusanagi_no_tsurugi.png
│       │   │   ├── muramasa.png
│       │   │   ├── totsuka_no_tsurugi.png
│       │   │   ├── masamune.png
│       │   │   └── naginata_bishamon.png
│       │   │
│       │   └── mesoamerican/
│       │       └── xiuhcoatl.png
│       │
│       ├── materials/
│       │   ├── ingots/
│       │   │   ├── mythril_ingot.png
│       │   │   ├── northsteel_ingot.png
│       │   │   ├── sacred_iron_ingot.png
│       │   │   ├── tamahagane_ingot.png
│       │   │   └── jade_imperial_ingot.png
│       │   │
│       │   ├── raw/
│       │   │   ├── raw_mythril.png
│       │   │   ├── raw_northsteel.png
│       │   │   ├── raw_sacred_iron.png
│       │   │   ├── raw_tamahagane.png
│       │   │   ├── raw_obsidiana_ritual.png
│       │   │   └── raw_jade_imperial.png
│       │   │
│       │   ├── special/
│       │   │   ├── norse/
│       │   │   │   ├── spiritbound_leather.png
│       │   │   │   ├── frozen_soul_crystal.png
│       │   │   │   └── rainbow_bridge_fragment.png
│       │   │   │
│       │   │   ├── greek/
│       │   │   │   ├── shard_of_divinity.png
│       │   │   │   ├── feather_of_victory.png
│       │   │   │   └── bronce_bendito.png
│       │   │   │
│       │   │   ├── japanese/
│       │   │   │   ├── gem_of_bishamon.png
│       │   │   │   ├── soul_swordsmith.png
│       │   │   │   ├── sacred_water_of_amaterasu.png
│       │   │   │   └── mango_largo_japones.png
│       │   │   │
│       │   │   ├── mesoamerican/
│       │   │   │   ├── filo_de_pluma_de_quetzal.png
│       │   │   │   └── palo_ritual.png
│       │   │   │
│       │   │   ├── chinese/
│       │   │   │   ├── dust_of_longevity.png
│       │   │   │   ├── essence_of_righteousness.png
│       │   │   │   ├── soul_fragment.png
│       │   │   │   ├── lovers_bond_token.png
│       │   │   │   └── moonstone_shard.png
│       │   │   │
│       │   │   └── general/
│       │   │       ├── sun_blessed_alloy.png
│       │   │       └── dragon_fang_fragment.png
│       │   │
│       │   └── test/
│       │       └── test_item.png
│       │
│       └── special_items/
│           └── camelot_compass.png
│
└── models/
    ├── block/
    │   ├── ores/
    │   │   ├── mythril_ore.json
    │   │   ├── northsteel_ore.json
    │   │   ├── sacred_iron_ore.json
    │   │   ├── tamahagane_ore.json
    │   │   ├── obsidiana_ritual_ore.json
    │   │   └── jade_imperial_ore.json
    │   │
    │   └── special/
    │       └── boss_altar.json
    │
    └── item/
        ├── weapons/
        │   ├── arthurian/
        │   │   ├── excalibur.json
        │   │   ├── caliburn.json
        │   │   └── clarent.json
        │   │
        │   ├── norse/
        │   │   ├── gram.json
        │   │   ├── skofnung.json
        │   │   ├── hofund.json
        │   │   ├── gungnir.json
        │   │   └── laevateinn.json
        │   │
        │   ├── greek/
        │   │   ├── harpe.json
        │   │   ├── xiphos_sagrado.json
        │   │   ├── nike_blade.json
        │   │   └── aegis_edge.json
        │   │
        │   ├── japanese/
        │   │   ├── kusanagi_no_tsurugi.json
        │   │   ├── muramasa.json
        │   │   ├── totsuka_no_tsurugi.json
        │   │   ├── masamune.json
        │   │   └── naginata_bishamon.json
        │   │
        │   └── mesoamerican/
        │       └── xiuhcoatl.json
        │
        ├── materials/
        │   ├── ingots/
        │   │   ├── mythril_ingot.json
        │   │   ├── northsteel_ingot.json
        │   │   ├── sacred_iron_ingot.json
        │   │   ├── tamahagane_ingot.json
        │   │   └── jade_imperial_ingot.json
        │   │
        │   ├── raw/
        │   │   ├── raw_mythril.json
        │   │   ├── raw_northsteel.json
        │   │   ├── raw_sacred_iron.json
        │   │   ├── raw_tamahagane.json
        │   │   ├── raw_obsidiana_ritual.json
        │   │   └── raw_jade_imperial.json
        │   │
        │   └── special/
        │       ├── norse/
        │       │   ├── spiritbound_leather.json
        │       │   ├── frozen_soul_crystal.json
        │       │   └── rainbow_bridge_fragment.json
        │       │
        │       ├── greek/
        │       │   ├── shard_of_divinity.json
        │       │   ├── feather_of_victory.json
        │       │   └── bronce_bendito.json
        │       │
        │       ├── japanese/
        │       │   ├── gem_of_bishamon.json
        │       │   ├── soul_swordsmith.json
        │       │   ├── sacred_water_of_amaterasu.json
        │       │   └── mango_largo_japones.json
        │       │
        │       ├── mesoamerican/
        │       │   ├── filo_de_pluma_de_quetzal.json
        │       │   └── palo_ritual.json
        │       │
        │       ├── chinese/
        │       │   ├── dust_of_longevity.json
        │       │   ├── essence_of_righteousness.json
        │       │   ├── soul_fragment.json
        │       │   ├── lovers_bond_token.json
        │       │   └── moonstone_shard.json
        │       │
        │       └── general/
        │           ├── sun_blessed_alloy.json
        │           └── dragon_fang_fragment.json
        │
        └── special_items/
            └── camelot_compass.json
```

---

### Opción 2: Por Mitología (Alternativa)

```
src/main/resources/assets/mythicalswords/
├── textures/
│   └── item/
│       ├── arthurian/
│       │   ├── weapons/
│       │   │   ├── excalibur.png
│       │   │   ├── caliburn.png
│       │   │   └── clarent.png
│       │   └── items/
│       │       └── camelot_compass.png
│       │
│       ├── norse/
│       │   ├── weapons/
│       │   │   ├── gram.png
│       │   │   ├── gungnir.png
│       │   │   └── ...
│       │   ├── ores/
│       │   │   └── northsteel_ore.png
│       │   ├── ingots/
│       │   │   └── northsteel_ingot.png
│       │   └── materials/
│       │       ├── spiritbound_leather.png
│       │       └── ...
│       │
│       ├── greek/
│       │   ├── weapons/
│       │   ├── ores/
│       │   ├── ingots/
│       │   └── materials/
│       │
│       └── ... (otras mitologías)
```

---

## 🔄 ACTUALIZACIÓN DE REFERENCIAS

### ⚠️ IMPORTANTE: Los modelos JSON deben actualizarse

Cuando movemos las texturas, los modelos JSON deben actualizar sus rutas:

**Ejemplo - Antes:**
```json
{
  "parent": "item/handheld",
  "textures": {
    "layer0": "mythicalswords:item/excalibur"
  }
}
```

**Ejemplo - Después (Opción 1):**
```json
{
  "parent": "item/handheld",
  "textures": {
    "layer0": "mythicalswords:item/weapons/arthurian/excalibur"
  }
}
```

---

## 📋 PLAN DE MIGRACIÓN

### Fase 1: Preparación (30 min)
1. Crear backup completo del proyecto
2. Crear nueva estructura de carpetas vacía
3. Documentar todas las rutas actuales

### Fase 2: Migración de Texturas (1-2 horas)
1. Mover texturas de armas a `/weapons/[mitología]/`
2. Mover texturas de ores a `/block/ores/`
3. Mover texturas de lingotes a `/materials/ingots/`
4. Mover texturas de raw materials a `/materials/raw/`
5. Mover texturas de materiales especiales a `/materials/special/[mitología]/`

### Fase 3: Migración de Modelos (1-2 horas)
1. Mover modelos siguiendo la misma estructura
2. Actualizar referencias de texturas en cada JSON

### Fase 4: Actualización de Blockstates (30 min)
1. Actualizar referencias en blockstates si es necesario

### Fase 5: Testing (1 hora)
1. Compilar el mod: `./gradlew build`
2. Verificar que no hay errores de recursos faltantes
3. Ejecutar el juego y verificar que todas las texturas cargan
4. Verificar cada categoría de items

### Fase 6: Limpieza (15 min)
1. Eliminar carpetas antiguas vacías
2. Actualizar documentación
3. Commit de cambios

**Tiempo total estimado:** 4-6 horas

---

## ✅ VENTAJAS DE LA REORGANIZACIÓN

### Para Desarrollo:
- ✅ Fácil encontrar archivos específicos
- ✅ Agregar nuevas mitologías es más claro
- ✅ Menos errores al crear nuevos items
- ✅ Mejor para trabajo en equipo

### Para Mantenimiento:
- ✅ Identificar rápidamente qué falta
- ✅ Actualizar texturas por categoría
- ✅ Verificar completitud de cada mitología
- ✅ Debugging más rápido

### Para Escalabilidad:
- ✅ Agregar Hindu, Egyptian, Celtic será más ordenado
- ✅ Cada mitología tiene su espacio claro
- ✅ Fácil ver el progreso por categoría

---

## ⚠️ CONSIDERACIONES

### Compatibilidad con Minecraft:
- ✅ Minecraft soporta subcarpetas en assets
- ✅ Solo necesitas actualizar las rutas en los JSONs
- ✅ No afecta el rendimiento del juego

### Riesgos:
- ⚠️ Si no actualizas todos los JSONs, las texturas no cargarán
- ⚠️ Necesitas probar exhaustivamente después
- ⚠️ Puede romper mods que dependan del tuyo (si los hay)

### Recomendación:
- 🎯 **Hacer la reorganización AHORA** antes de agregar más mitologías
- 🎯 Usar **Opción 1** (por tipo de item) - más intuitiva
- 🎯 Hacer en una **rama separada de git** primero
- 🎯 Probar completamente antes de mergear

---

## 🛠️ SCRIPT DE MIGRACIÓN AUTOMÁTICA

Puedo crear un script de PowerShell que:
1. Cree la nueva estructura de carpetas
2. Mueva todos los archivos automáticamente
3. Actualice las referencias en los JSONs
4. Genere un reporte de cambios

¿Quieres que cree este script?

---

## 📊 COMPARACIÓN DE OPCIONES

| Aspecto | Opción 1: Por Tipo | Opción 2: Por Mitología |
|---------|-------------------|------------------------|
| **Encontrar armas** | Todas en `/weapons/` | Dispersas por mitología |
| **Encontrar materiales** | Todos en `/materials/` | Dispersas por mitología |
| **Ver progreso mitología** | Requiere buscar en varias carpetas | Todo junto en una carpeta |
| **Agregar nueva mitología** | Crear subcarpetas en cada tipo | Crear una carpeta raíz |
| **Intuitividad** | ⭐⭐⭐⭐⭐ | ⭐⭐⭐⭐ |
| **Mantenimiento** | ⭐⭐⭐⭐⭐ | ⭐⭐⭐ |
| **Recomendación** | ✅ **RECOMENDADA** | ⚠️ Alternativa |

---

## 🎯 DECISIÓN RECOMENDADA

**Implementar Opción 1: Organización por Tipo de Item**

**Razones:**
1. Más intuitivo para encontrar items similares
2. Mejor para mantenimiento a largo plazo
3. Estándar en la comunidad de modding
4. Facilita agregar nuevas mitologías sin reestructurar

**Próximos pasos:**
1. ¿Apruebas esta estructura?
2. ¿Quieres que cree el script de migración automática?
3. ¿Prefieres hacerlo manualmente con mi guía paso a paso?

---

**Fecha:** Diciembre 2025  
**Estado:** Propuesta pendiente de aprobación  
**Impacto:** Mejora significativa en organización del proyecto
