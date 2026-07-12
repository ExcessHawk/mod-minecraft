Add-Type -AssemblyName System.Drawing
# Blacksmith hammer mini-atlas (16x16): wood handle strip, steel head with
# gold band, darker striking faces. Used by the 3D item model UVs.
$DST = "C:\Users\alfredo\Desktop\mod\mod-minecraft\mythical-swords-template-1.20.1\src\main\resources\assets\mythicalswords\textures\item"

function HexC($h) {
  $h = $h.TrimStart('#')
  [System.Drawing.Color]::FromArgb(255,
    [Convert]::ToInt32($h.Substring(0,2),16),
    [Convert]::ToInt32($h.Substring(2,2),16),
    [Convert]::ToInt32($h.Substring(4,2),16))
}

$woodBase  = HexC "6b4a2a"; $woodDark = HexC "55381f"; $woodLight = HexC "8a6238"
$steelBase = HexC "8f97a3"; $steelHi  = HexC "b7bfc9"; $steelDark = HexC "5c636e"
$gold      = HexC "c9a227"; $goldHi   = HexC "ffd75e"
$faceDark  = HexC "474e58"

$bmp = New-Object System.Drawing.Bitmap(16, 16)
for ($y = 0; $y -lt 16; $y++) { for ($x = 0; $x -lt 16; $x++) { $bmp.SetPixel($x, $y, $steelBase) } }

# Handle wood strip: x0-2, full height, vertical grain
for ($y = 0; $y -lt 16; $y++) {
  $bmp.SetPixel(0, $y, $woodDark)
  $bmp.SetPixel(1, $y, $woodBase)
  $bmp.SetPixel(2, $y, $woodLight)
}
foreach ($y in @(3,7,11)) { $bmp.SetPixel(1, $y, $woodDark) }

# Head long side: x4-15, y0-5. Steel with highlight top + gold band at x9-10
for ($x = 4; $x -lt 16; $x++) {
  $bmp.SetPixel($x, 0, $steelHi)
  $bmp.SetPixel($x, 5, $steelDark)
}
for ($y = 0; $y -le 5; $y++) {
  $bmp.SetPixel(9,  $y, $gold)
  $bmp.SetPixel(10, $y, $gold)
}
$bmp.SetPixel(9, 0, $goldHi); $bmp.SetPixel(10, 3, $goldHi)

# Striking faces: x4-9, y8-13 darker steel with rim
for ($y = 8; $y -le 13; $y++) { for ($x = 4; $x -le 9; $x++) { $bmp.SetPixel($x, $y, $faceDark) } }
for ($x = 4; $x -le 9; $x++) { $bmp.SetPixel($x, 8, $steelDark) }
for ($y = 8; $y -le 13; $y++) { $bmp.SetPixel(4, $y, $steelDark) }
$bmp.SetPixel(6, 10, $steelBase); $bmp.SetPixel(7, 11, $steelBase)

# Pommel: x12-14, y8-10 gold cap
for ($y = 8; $y -le 10; $y++) { for ($x = 12; $x -le 14; $x++) { $bmp.SetPixel($x, $y, $gold) } }
$bmp.SetPixel(12, 8, $goldHi)

$bmp.Save((Join-Path $DST "blacksmith_hammer.png"), [System.Drawing.Imaging.ImageFormat]::Png)
$bmp.Dispose()
Write-Output "wrote blacksmith_hammer"
