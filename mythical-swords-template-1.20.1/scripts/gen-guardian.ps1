Add-Type -AssemblyName System.Drawing
# Celestial Guardian texture, 128x128, matching celestial_guardian.geo.json UVs:
#   body(0,0) 14x18x8 | belt(0,40) | core(44,40) | head(0,56) | crown(0,80)
#   arms(56,0)/(80,0) | robe(64,40) | wings(0,96)/(34,96)
$DST = "C:\Users\alfredo\Desktop\mod\mod-minecraft\mythical-swords-template-1.20.1\src\main\resources\assets\mythicalswords\textures\entity"

function HexC($h) {
  $h = $h.TrimStart('#')
  [System.Drawing.Color]::FromArgb(255,
    [Convert]::ToInt32($h.Substring(0,2),16),
    [Convert]::ToInt32($h.Substring(2,2),16),
    [Convert]::ToInt32($h.Substring(4,2),16))
}
$clear = [System.Drawing.Color]::FromArgb(0,0,0,0)

$plate   = HexC "e8d48a"   # gold-white armour
$plateHi = HexC "fff8d0"
$plateDk = HexC "b8923a"
$robe    = HexC "3f4c7a"   # deep sky-blue robe
$robeHi  = HexC "5e6fa8"
$robeDk  = HexC "27304f"
$cyan    = HexC "9be8ff"
$cyanHi  = HexC "e0fbff"
$wing    = HexC "f2ecd8"
$wingDk  = HexC "c9c0a4"
$dark    = HexC "1b1830"

$bmp = New-Object System.Drawing.Bitmap(128, 128)
for ($y=0; $y -lt 128; $y++) { for ($x=0; $x -lt 128; $x++) { $bmp.SetPixel($x, $y, $clear) } }
$rng = New-Object System.Random(555)

function FillR($x, $y, $w, $h, $c) {
  for ($yy=$y; $yy -lt ($y+$h); $yy++) { for ($xx=$x; $xx -lt ($x+$w); $xx++) {
    if ($xx -ge 0 -and $xx -lt 128 -and $yy -ge 0 -and $yy -lt 128) { $script:bmp.SetPixel($xx, $yy, $c) } } }
}
function Noise($x, $y, $w, $h, $c, $n) {
  for ($i=0; $i -lt $n; $i++) { $script:bmp.SetPixel($x + $rng.Next($w), $y + $rng.Next($h), $c) }
}

# ===== body: box 14x18x8 -> net width 2*(14+8)=44, height 8+18=26 at (0,0)
FillR 0 0 44 26 $plate
Noise 0 0 44 26 $plateDk 40
Noise 0 0 44 26 $plateHi 22
# chest trim lines
FillR 0 8 44 1 $plateDk
FillR 0 20 44 1 $plateDk
# cyan seams down the torso
for ($y=9; $y -le 19; $y++) { $bmp.SetPixel(11, $y, $cyan); $bmp.SetPixel(32, $y, $cyan) }

# ===== belt 10x6x6 at (0,40): net 2*(10+6)=32 wide, 6+6=12 tall
FillR 0 40 32 12 $plateDk
Noise 0 40 32 12 $dark 12
for ($x=0; $x -lt 32; $x+=4) { $bmp.SetPixel($x, 46, $cyan) }

# ===== core 6x6x2 at (44,40): net 2*(6+2)=16 wide, 2+6=8 tall
FillR 44 40 16 8 $cyan
FillR 46 42 8 4 $cyanHi
$bmp.SetPixel(49, 43, $plateHi); $bmp.SetPixel(52, 44, $plateHi)

# ===== head 10x10x10 at (0,56): net 40 wide, 20 tall
FillR 0 56 40 20 $plate
Noise 0 56 40 20 $plateDk 24
# face on front face (10,66)-(20,76): glowing eyes, no mouth (masked deity)
FillR 10 66 10 10 $plateHi
FillR 12 69 2 2 $cyanHi
FillR 16 69 2 2 $cyanHi
FillR 11 73 8 1 $plateDk

# ===== crown ring 11x2x11 at (0,80): net 2*(11+11)=44 wide, 11+2=13 tall
FillR 0 80 44 13 $plate
Noise 0 80 44 13 $plateHi 18
FillR 0 82 44 1 $cyan
# crown spikes at (46,80): small patch
FillR 46 80 6 8 $plateHi
FillR 47 81 4 6 $plate

# ===== arms 4x18x6 at (56,0) and (80,0): net 2*(4+6)=20 wide, 6+18=24 tall
foreach ($ax in 56, 80) {
  FillR $ax 0 20 24 $plate
  Noise $ax 0 20 24 $plateDk 18
  FillR $ax 6 20 1 $plateDk
  for ($y=8; $y -le 20; $y++) { $bmp.SetPixel($ax + 5, $y, $cyan) }
  # gauntlet
  FillR $ax 20 20 4 $plateDk
}

# ===== robe 12x16x6 at (64,40): net 2*(12+6)=36 wide, 6+16=22 tall
FillR 64 40 36 22 $robe
Noise 64 40 36 22 $robeDk 30
Noise 64 40 36 22 $robeHi 16
# hem + cyan glyphs
FillR 64 58 36 2 $plateDk
for ($x=66; $x -lt 100; $x+=6) { $bmp.SetPixel($x, 50, $cyan); $bmp.SetPixel($x+1, 51, $cyan) }

# ===== wings: 16x14x1 at (0,96) and (34,96); 11x8x1 at (0,112)/(34,112)
foreach ($wx in 0, 34) {
  FillR $wx 96 34 15 $wing
  Noise $wx 96 34 15 $wingDk 30
  # feather ribs
  for ($x=$wx; $x -lt ($wx+34); $x+=4) { for ($y=97; $y -lt 110; $y++) { $bmp.SetPixel($x, $y, $wingDk) } }
  FillR $wx 112 24 9 $wing
  Noise $wx 112 24 9 $wingDk 14
  for ($x=$wx; $x -lt ($wx+24); $x+=3) { $bmp.SetPixel($x, 116, $cyan) }
}

$bmp.Save((Join-Path $DST "celestial_guardian.png"), [System.Drawing.Imaging.ImageFormat]::Png)
$bmp.Dispose()
Write-Output "wrote celestial_guardian.png"
