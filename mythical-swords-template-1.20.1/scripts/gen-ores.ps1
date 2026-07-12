Add-Type -AssemblyName System.Drawing
# Mirrors the ores painted live in Blockbench: same stone base, noise coords,
# gem clusters and per-mineral light/shadow palette. Writes the 6 ore PNGs.
$DST = "C:\Users\alfredo\Desktop\mod\mod-minecraft\mythical-swords-template-1.20.1\src\main\resources\assets\mythicalswords\textures\block"

function HexC($h) {
  $h = $h.TrimStart('#')
  [System.Drawing.Color]::FromArgb(255,
    [Convert]::ToInt32($h.Substring(0,2),16),
    [Convert]::ToInt32($h.Substring(2,2),16),
    [Convert]::ToInt32($h.Substring(4,2),16))
}

$stoneFill  = HexC "7a7a7e"
$noiseDark  = HexC "5e5e63"
$noiseLight = HexC "8f8f94"

$darkCoords  = @(@(1,2),@(5,1),@(9,2),@(14,1),@(2,6),@(7,7),@(13,5),@(0,10),@(4,9),@(10,8),@(15,9),@(3,14),@(8,13),@(12,14),@(6,4),@(11,12))
$lightCoords = @(@(3,1),@(7,3),@(12,2),@(1,5),@(9,6),@(14,7),@(5,8),@(2,12),@(11,6),@(7,11),@(14,13),@(9,15))

$gemBase = @(@(3,4),@(4,4),@(3,5),@(4,5),@(2,4),@(4,3),@(10,3),@(11,3),@(10,4),@(11,4),@(12,4),@(5,11),@(6,11),@(5,12),@(6,12),@(4,12),@(6,10),@(11,10),@(12,10),@(11,11),@(12,11),@(13,11),@(10,10),@(8,8),@(8,9))
$gemHi  = @(@(3,4),@(10,3),@(5,11),@(11,10),@(8,8))
$gemSh  = @(@(4,5),@(12,4),@(6,12),@(12,11),@(8,9))

$pal = @{
  "jade_imperial_ore"    = @("46c369","8ceb9f","2a9650")
  "mythril_ore"          = @("b9e4e9","eafbfd","74acb6")
  "northsteel_ore"       = @("50a0d2","9bcdf2","28608d")
  "obsidiana_ritual_ore" = @("7349a5","af87d7","371f50")
  "sacred_iron_ore"      = @("e8b623","ffe47d","af7d17")
  "tamahagane_ore"       = @("6a6a78","9a9aab","2c2c36")
}

foreach ($name in $pal.Keys) {
  $base = HexC $pal[$name][0]; $hi = HexC $pal[$name][1]; $sh = HexC $pal[$name][2]
  $bmp = New-Object System.Drawing.Bitmap(16, 16)
  for ($y = 0; $y -lt 16; $y++) { for ($x = 0; $x -lt 16; $x++) { $bmp.SetPixel($x, $y, $stoneFill) } }
  foreach ($p in $darkCoords)  { $bmp.SetPixel($p[0], $p[1], $noiseDark) }
  foreach ($p in $lightCoords) { $bmp.SetPixel($p[0], $p[1], $noiseLight) }
  foreach ($p in $gemBase) { $bmp.SetPixel($p[0], $p[1], $base) }
  foreach ($p in $gemHi)   { $bmp.SetPixel($p[0], $p[1], $hi) }
  foreach ($p in $gemSh)   { $bmp.SetPixel($p[0], $p[1], $sh) }
  $bmp.Save((Join-Path $DST "$name.png"), [System.Drawing.Imaging.ImageFormat]::Png)
  $bmp.Dispose()
  Write-Output "wrote $name"
}
