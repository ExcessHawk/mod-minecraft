Add-Type -AssemblyName System.Drawing
# Boss Altar 3D-model textures: dark ritual stone, glowing purple rune band,
# summoning-circle top. Writes 3 PNGs (the legacy flat boss_altar.png stays).
$DST = "C:\Users\alfredo\Desktop\mod\mod-minecraft\mythical-swords-template-1.20.1\src\main\resources\assets\mythicalswords\textures\block"

function HexC($h) {
  $h = $h.TrimStart('#')
  [System.Drawing.Color]::FromArgb(255,
    [Convert]::ToInt32($h.Substring(0,2),16),
    [Convert]::ToInt32($h.Substring(2,2),16),
    [Convert]::ToInt32($h.Substring(4,2),16))
}

$stone     = HexC "2c2438"
$stoneDark = HexC "1e1826"
$stoneLight= HexC "3a3048"
$mortar    = HexC "141019"
$runeGlow  = HexC "b46cff"
$runeHi    = HexC "e0b3ff"
$runeDeep  = HexC "7a3bc4"
$gemCore   = HexC "f6e6ff"

$noiseDark  = @(@(1,2),@(5,1),@(9,2),@(14,1),@(2,6),@(13,5),@(0,10),@(10,8),@(15,9),@(3,14),@(12,14),@(6,4))
$noiseLight = @(@(3,1),@(12,2),@(1,5),@(14,7),@(2,12),@(11,6),@(9,15),@(7,13))

function NewStone() {
  $bmp = New-Object System.Drawing.Bitmap(16, 16)
  for ($y = 0; $y -lt 16; $y++) { for ($x = 0; $x -lt 16; $x++) { $bmp.SetPixel($x, $y, $stone) } }
  foreach ($p in $noiseDark)  { $bmp.SetPixel($p[0], $p[1], $stoneDark) }
  foreach ($p in $noiseLight) { $bmp.SetPixel($p[0], $p[1], $stoneLight) }
  return $bmp
}

# ===== STONE: plain dark ritual stone with cracks =====
$s = NewStone
foreach ($p in @(@(4,7),@(5,8),@(6,8),@(7,9),@(11,3),@(11,4),@(12,5),@(2,11),@(3,12))) { $s.SetPixel($p[0], $p[1], $mortar) }
$s.Save((Join-Path $DST "boss_altar_stone.png"), [System.Drawing.Imaging.ImageFormat]::Png)
$s.Dispose(); Write-Output "wrote boss_altar_stone"

# ===== RUNES: stone with glowing rune glyphs band =====
$r = NewStone
# three glyphs across the middle band (rows 4-11)
# glyph 1: vertical stroke + arms
foreach ($p in @(@(2,5),@(2,6),@(2,7),@(2,8),@(2,9),@(1,6),@(3,7),@(1,9))) { $r.SetPixel($p[0], $p[1], $runeGlow) }
# glyph 2: diamond eye
foreach ($p in @(@(7,4),@(6,5),@(8,5),@(5,6),@(9,6),@(6,7),@(8,7),@(7,8),@(7,10),@(7,11))) { $r.SetPixel($p[0], $p[1], $runeGlow) }
$r.SetPixel(7, 6, $runeHi)
# glyph 3: angular Z
foreach ($p in @(@(12,5),@(13,5),@(14,5),@(13,6),@(12,7),@(12,8),@(13,8),@(14,8),@(14,9),@(13,10))) { $r.SetPixel($p[0], $p[1], $runeGlow) }
foreach ($p in @(@(2,5),@(13,5),@(7,4))) { $r.SetPixel($p[0], $p[1], $runeHi) }
foreach ($p in @(@(2,9),@(13,10),@(7,11))) { $r.SetPixel($p[0], $p[1], $runeDeep) }
$r.Save((Join-Path $DST "boss_altar_runes.png"), [System.Drawing.Imaging.ImageFormat]::Png)
$r.Dispose(); Write-Output "wrote boss_altar_runes"

# ===== TOP: summoning circle =====
$t = NewStone
# outer ring (approx circle r=6)
$ring = @(@(5,2),@(6,2),@(7,2),@(8,2),@(9,2),@(10,2),@(3,3),@(4,3),@(11,3),@(12,3),@(2,4),@(13,4),@(2,5),@(13,5),@(1,6),@(14,6),@(1,7),@(14,7),@(1,8),@(14,8),@(1,9),@(14,9),@(2,10),@(13,10),@(2,11),@(13,11),@(3,12),@(4,12),@(11,12),@(12,12),@(5,13),@(6,13),@(7,13),@(8,13),@(9,13),@(10,13))
foreach ($p in $ring) { $t.SetPixel($p[0], $p[1], $runeDeep) }
foreach ($p in @(@(7,2),@(1,7),@(14,8),@(8,13))) { $t.SetPixel($p[0], $p[1], $runeGlow) }
# inner star / cross
foreach ($p in @(@(7,5),@(8,5),@(5,7),@(5,8),@(10,7),@(10,8),@(7,10),@(8,10),@(6,6),@(9,6),@(6,9),@(9,9))) { $t.SetPixel($p[0], $p[1], $runeGlow) }
# bright core (gem seat) - also used as gem UV region
foreach ($p in @(@(7,7),@(8,7),@(7,8),@(8,8))) { $t.SetPixel($p[0], $p[1], $gemCore) }
foreach ($p in @(@(6,7),@(9,8),@(7,6),@(8,9))) { $t.SetPixel($p[0], $p[1], $runeHi) }
$t.Save((Join-Path $DST "boss_altar_top.png"), [System.Drawing.Imaging.ImageFormat]::Png)
$t.Dispose(); Write-Output "wrote boss_altar_top"
