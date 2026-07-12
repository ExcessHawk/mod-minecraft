# Prompts de IA — Texturas de Armas

Prompts listos para DALL-E / Midjourney / Stable Diffusion / Gemini para reemplazar los
placeholders por arte final. Genera a alta resolución (512×512 o 1024×1024) y reescala a
**16×16** (nearest-neighbor) para el item de Minecraft, o úsalo tal cual (MC reescala).

## Preámbulo de estilo (pega antes de cada prompt)

```
Minecraft item texture, single weapon centered on a fully transparent background (alpha),
diagonal orientation pointing top-right like a vanilla Minecraft sword, clean readable
silhouette, vibrant saturated colors, soft inner glow matching the elemental affinity,
no text, no watermark, no shadow on ground, crisp edges for downscaling to 16x16.
```

## Paleta por afinidad
| Afinidad | Color dominante | Glow |
|----------|-----------------|------|
| Fuego | naranja-rojo | ámbar cálido |
| Hielo | cian/celeste | blanco-azul |
| Rayo | amarillo | blanco eléctrico |
| Divino | dorado | blanco cálido |
| Oscuridad | morado oscuro | violeta |
| Naturaleza | verde | verde claro |

---

## Armas

### Artúrica
- **Excalibur** (Divino) — `holy golden longsword, radiant blade with engraved runes, ornate cross guard with a blue gem, white-gold divine glow, regal`
- **Caliburn** (Divino) — `pristine silver-gold knightly sword, simple noble design, faint holy aura, the sword in the stone`
- **Clarent** (Divino) — `ceremonial golden short sword, red-gold blade, royal peace sword, soft warm glow`

### Nórdica
- **Gram** (Hielo) — `norse iron sword with frost-blue blade, ice crystals along the edge, runic engravings, cold cyan glow`
- **Skofnung** (Hielo) — `viking sword, pale frost steel blade, leather-wrapped grip, icy mist, blue-white glow`
- **Hofund** (Divino) — `golden bifrost sword, rainbow-shimmer edge, divine guardian blade, warm radiant glow`
- **Gungnir** (Rayo) — `norse throwing spear (not a sword), golden shaft, lightning-charged tip, electric yellow sparks`

### Griega
- **Harpe** (Divino) — `greek curved sickle-sword (hooked blade), bronze-gold finish, mythological, divine glow`
- **Xiphos Sagrado** (Divino) — `short greek leaf-shaped bronze sword, sacred engravings, golden glow`
- **Nike Blade** (Rayo) — `winged greek sword, white-gold blade with small feathered wings on the guard, electric glow`
- **Aegis Edge** (Divino) — `greek sword paired with a small aegis shield motif on the pommel, bronze and gold, divine light`

### Japonesa
- **Kusanagi-no-Tsurugi** (Naturaleza) — `legendary katana, green wind-infused blade, swirling breeze motif, jade green glow`
- **Muramasa** (Oscuridad) — `cursed katana, dark blade with blood-red edge, ominous purple-black aura, menacing`
- **Totsuka-no-Tsurugi** (Divino) — `ancient ceremonial sword (totsuka), golden divine blade, sealing motif, holy glow`

### Egipcia
- **Khopesh** (Oscuridad) — `egyptian khopesh sickle-sword, bronze hooked blade with hieroglyphs, dark purple soul-stealing aura`
- **Cetro Was** (Divino) — `egyptian was scepter (staff, not sword), golden rod topped with a stylized animal head, sun-disc, divine golden glow`

### China
- **Ruyi Jingu Bang** (Naturaleza) — `golden monkey-king staff (long pole, not sword), red-gold bands at each end, dynamic, green-gold glow`
- **Jian** (Rayo) — `chinese straight double-edged jian sword, slender elegant blade, tassel on the pommel, electric yellow glow`

### Mesoamericana
- **Xiuhcoatl** (Fuego) — `aztec fire-serpent weapon, obsidian-and-gold blade shaped like a flaming serpent, lava-orange glow`

---

## Generadas proceduralmente (placeholders actuales)
Estas se generaron con `scripts/gen-textures.js` (pixel-art 16×16 simple por afinidad).
Reemplázalas primero con arte IA:
- `excalibur.png`, `caliburn.png` (eran 16×16 crudas)
- `jian.png`, `ruyi_jingu_bang.png` (eran JPEG renombrados a .png → no renderizaban)

El resto de armas ya tienen arte IA de mayor resolución.
