Add-Type -AssemblyName System.Drawing
# Celestial dimension blocks: pale sky-stone, carved bricks, and the portal
# frame block. Radiant white-gold with cyan veins, matching the celestial armor.
$DST = "C:\Users\alfredo\Desktop\mod\mod-minecraft\mythical-swords-template-1.20.1\src\main\resources\assets\mythicalswords\textures\block"

function HexC($h) {
  $h = $h.TrimStart('#')
  [System.Drawing.Color]::FromArgb(255,
    [Convert]::ToInt32($h.Substring(0,2),16),
    [Convert]::ToInt32($h.Substring(2,2),16),
    [Convert]::ToInt32($h.Substring(4,2),16))
}

$base   = HexC "ddd6bc"
$light  = HexC "f2ecd8"
$dark   = HexC "b0a681"
$mortar = HexC "8d8464"
$cyan   = HexC "9be8ff"
$cyanHi = HexC "e0fbff"
$gold   = HexC "e8d48a"
$void   = HexC "1b1830"
$glow   = HexC "b46cff"

$noiseD = @(@(1,2),@(5,1),@(9,2),@(14,1),@(2,6),@(13,5),@(0,10),@(10,8),@(15,9),@(3,14),@(12,14),@(6,4))
$noiseL = @(@(3,1),@(12,2),@(1,5),@(14,7),@(2,12),@(11,6),@(9,15),@(7,13))

function NewStone() {
  $bmp = New-Object System.Drawing.Bitmap(16, 16)
  for ($y = 0; $y -lt 16; $y++) { for ($x = 0; $x -lt 16; $x++) { $bmp.SetPixel($x, $y, $base) } }
  foreach ($p in $noiseD) { $bmp.SetPixel($p[0], $p[1], $dark) }
  foreach ($p in $noiseL) { $bmp.SetPixel($p[0], $p[1], $light) }
  return $bmp
}

# ===== celestial_stone: sky-stone with cyan veins =====
$s = NewStone
foreach ($p in @(@(4,3),@(5,4),@(6,4),@(7,5),@(11,9),@(12,10),@(13,10),@(2,12),@(3,13))) { $s.SetPixel($p[0], $p[1], $cyan) }
$s.SetPixel(5, 4, $cyanHi); $s.SetPixel(12, 10, $cyanHi)
$s.Save((Join-Path $DST "celestial_stone.png"), [System.Drawing.Imaging.ImageFormat]::Png)
$s.Dispose(); Write-Output "wrote celestial_stone"

# ===== celestial_bricks: brick courses with gold accents =====
$b = NewStone
for ($x = 0; $x -lt 16; $x++) { $b.SetPixel($x, 4, $mortar); $b.SetPixel($x, 10, $mortar) }
foreach ($y in @(0,1,2,3))        { $b.SetPixel(5, $y, $mortar); $b.SetPixel(11, $y, $mortar) }
foreach ($y in @(5,6,7,8,9))      { $b.SetPixel(2, $y, $mortar); $b.SetPixel(8, $y, $mortar); $b.SetPixel(13, $y, $mortar) }
foreach ($y in @(11,12,13,14,15)) { $b.SetPixel(6, $y, $mortar); $b.SetPixel(12, $y, $mortar) }
foreach ($p in @(@(3,2),@(9,7),@(14,12),@(4,13))) { $b.SetPixel($p[0], $p[1], $gold) }
$b.Save((Join-Path $DST "celestial_bricks.png"), [System.Drawing.Imaging.ImageFormat]::Png)
$b.Dispose(); Write-Output "wrote celestial_bricks"

# ===== celestial_portal_frame: carved block with a glyph socket =====
$f = NewStone
for ($i = 3; $i -le 12; $i++) { $f.SetPixel($i, 3, $mortar); $f.SetPixel($i, 12, $mortar); $f.SetPixel(3, $i, $mortar); $f.SetPixel(12, $i, $mortar) }
for ($y = 4; $y -le 11; $y++) { for ($x = 4; $x -le 11; $x++) { $f.SetPixel($x, $y, $dark) } }
# star glyph in the socket
foreach ($p in @(@(7,5),@(8,5),@(6,6),@(9,6),@(5,7),@(10,7),@(5,8),@(10,8),@(6,9),@(9,9),@(7,10),@(8,10))) { $f.SetPixel($p[0], $p[1], $gold) }
foreach ($p in @(@(7,7),@(8,7),@(7,8),@(8,8))) { $f.SetPixel($p[0], $p[1], $cyanHi) }
$f.Save((Join-Path $DST "celestial_portal_frame.png"), [System.Drawing.Imaging.ImageFormat]::Png)
$f.Dispose(); Write-Output "wrote celestial_portal_frame"

# ===== celestial_portal: swirling starfield =====
$p = New-Object System.Drawing.Bitmap(16, 16)
for ($y = 0; $y -lt 16; $y++) {
  for ($x = 0; $x -lt 16; $x++) {
    # radial gradient from violet core to dark void edges
    $dx = $x - 7.5; $dy = $y - 7.5
    $d = [Math]::Sqrt($dx * $dx + $dy * $dy) / 10.6
    if ($d -gt 1) { $d = 1 }
    $c = [System.Drawing.Color]::FromArgb(255,
      [int]($glow.R + ($void.R - $glow.R) * $d),
      [int]($glow.G + ($void.G - $glow.G) * $d),
      [int]($glow.B + ($void.B - $glow.B) * $d))
    $p.SetPixel($x, $y, $c)
  }
}
$rng = New-Object System.Random(2026)
for ($i = 0; $i -lt 22; $i++) { $p.SetPixel($rng.Next(16), $rng.Next(16), $cyanHi) }
for ($i = 0; $i -lt 14; $i++) { $p.SetPixel($rng.Next(16), $rng.Next(16), $cyan) }
$p.Save((Join-Path $DST "celestial_portal.png"), [System.Drawing.Imaging.ImageFormat]::Png)
$p.Dispose(); Write-Output "wrote celestial_portal"
