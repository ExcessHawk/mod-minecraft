# 🔧 Correcciones de Texturas Necesarias - URGENTE

## 🚨 Problemas Críticos Detectados

### 1. ❌ **Spawn Egg del Herrero Legendario - FALTA COMPLETAMENTE**

**Problema:** El spawn egg está registrado en el código pero NO tiene modelo ni textura.

**Archivos afectados:**
- ✅ Código Java: `ModItems.java` - LEGENDARY_BLACKSMITH_SPAWN_EGG registrado
- ✅ Modelo JSON: `legendary_blacksmith_spawn_egg.json` - CREADO AHORA
- ❌ Textura: NO EXISTE (Minecraft generará automáticamente desde colores del código)

**Solución:**
El spawn egg usa colores definidos en el código Java:
```java
new SpawnEggItem(ModEntities.LEGENDARY_BLACKSMITH, 0x8B4513, 0xFF4500, ...)
```
- Color base: `0x8B4513` (marrón - saddle brown)
- Color de manchas: `0xFF4500` (naranja-rojo - orange red)

**Estado:** ✅ SOLUCIONADO - Minecraft generará automáticamente la textura del spawn egg desde estos colores.

---

### 2. ⚠️ **Jade Imperial Ore - Usando Textura Incorrecta**

**Problema:** El bloque de mena de jade está usando la textura de mythril ore (copiada).

**Archivo:** `jade_imperial_ore.png` (en carpeta `/block/`)

**Necesita:** Textura única con vetas de jade verde (#00A86B, #50C878) y destellos dorados (#FFD700) sobre piedra gris.

**Prompt en:** `IMAGE_PROMPTS_MATERIALS.md` - Sección "Texturas de Bloques de Ore"

---

### 3. 🔄 **Brújula de Camelot - Usando Textura Vanilla**

**Problema:** La brújula está usando la textura vanilla de Minecraft.

**Archivo actual:** Referencia a `minecraft:item/compass`

**Necesita:** Textura personalizada con:
- Marco dorado (en vez de hierro)
- Efecto de encantamiento (brillo púrpura/azul)
- Símbolos artúricos (corona, nudos celtas)

**Modelo actual:**
```json
{
  "parent": "item/generated",
  "textures": {
    "layer0": "minecraft:item/compass"  // ← CAMBIAR A: "mythicalswords:item/camelot_compass"
  }
}
```

**Acción requerida:**
1. Crear textura: `camelot_compass.png`
2. Actualizar modelo para usar: `"layer0": "mythicalswords:item/camelot_compass"`

**Prompt en:** `IMAGE_PROMPTS_MATERIALS.md` - Sección "Items Especiales"

---

## 📋 Verificaciones Adicionales Necesarias

### 4. ⚠️ **Acero Tamahagane vs Tamahagane Ingot**

**Archivos:**
- `acero_tamahagane_ingot.png` - Existe
- `tamahagane_ingot.png` - Existe

**Pregunta:** ¿Son el mismo material o diferentes?

**En el código (ModItems.java):**
```java
public static final Item TAMAHAGANE_INGOT = registerItem("tamahagane_ingot", ...)
```

**Problema detectado:** 
- El código solo registra `tamahagane_ingot`
- Pero existe una textura `acero_tamahagane_ingot.png` que NO está siendo usada
- En el archivo de idioma hay: `"item.mythicalswords.acero_tamahagane_ingot": "Tamahagane Steel Ingot"`

**Acción requerida:** 
- ✅ Verificar si `acero_tamahagane_ingot` debe ser un item separado
- ✅ Si no, eliminar la textura y entrada de idioma no usadas
- ✅ Si sí, agregar el item al código

---

### 5. ⚠️ **Mango Largo Japonés - Verificar Textura**

**Archivo:** `mango_largo_japones.png`

**Pregunta:** ¿La textura actual es apropiada para un "mango largo japonés" (handle/grip)?

**Uso:** Material para craftear armas japonesas (Naginata de Bishamon)

**Acción requerida:**
- ✅ Revisar si la textura actual representa bien un mango/empuñadura japonesa
- ✅ Si no, crear nueva textura con estilo de madera lacada japonesa

---

## 🎨 Resumen de Texturas a Crear

### 🔴 Prioridad CRÍTICA (Afectan funcionalidad)
1. ~~legendary_blacksmith_spawn_egg~~ - ✅ SOLUCIONADO (auto-generado)
2. **jade_imperial_ore.png** (block) - Reemplazar copia de mythril

### 🟠 Prioridad ALTA (Items visibles importantes)
3. **camelot_compass.png** - Crear versión encantada
4. **jade_imperial_ingot.png** - Reemplazar placeholder
5. **raw_jade_imperial.png** - Reemplazar placeholder

### 🟡 Prioridad MEDIA (Materiales chinos)
6. **dust_of_longevity.png**
7. **essence_of_righteousness.png**
8. **soul_fragment.png**

### 🟢 Prioridad BAJA
9. **lovers_bond_token.png**
10. **moonstone_shard.png**

---

## 📝 Checklist de Correcciones

- [x] Crear modelo JSON para legendary_blacksmith_spawn_egg
- [ ] Crear textura única para jade_imperial_ore (block)
- [ ] Crear textura camelot_compass.png
- [ ] Actualizar modelo de camelot_compass para usar textura propia
- [ ] Verificar situación de acero_tamahagane_ingot
- [ ] Verificar textura de mango_largo_japones
- [ ] Crear 7 texturas de materiales chinos restantes

---

## 🔧 Archivos a Modificar

### Modelos que necesitan actualización:
```
src/main/resources/assets/mythicalswords/models/item/camelot_compass.json
```

Cambiar de:
```json
"layer0": "minecraft:item/compass"
```

A:
```json
"layer0": "mythicalswords:item/camelot_compass"
```

### Texturas a crear:
```
src/main/resources/assets/mythicalswords/textures/item/camelot_compass.png
src/main/resources/assets/mythicalswords/textures/block/jade_imperial_ore.png
src/main/resources/assets/mythicalswords/textures/item/jade_imperial_ingot.png
src/main/resources/assets/mythicalswords/textures/item/raw_jade_imperial.png
src/main/resources/assets/mythicalswords/textures/item/dust_of_longevity.png
src/main/resources/assets/mythicalswords/textures/item/essence_of_righteousness.png
src/main/resources/assets/mythicalswords/textures/item/soul_fragment.png
src/main/resources/assets/mythicalswords/textures/item/lovers_bond_token.png
src/main/resources/assets/mythicalswords/textures/item/moonstone_shard.png
```

---

**Fecha:** Diciembre 2025  
**Estado:** Documento de correcciones creado  
**Próximo paso:** Crear las texturas usando los prompts en IMAGE_PROMPTS_MATERIALS.md
