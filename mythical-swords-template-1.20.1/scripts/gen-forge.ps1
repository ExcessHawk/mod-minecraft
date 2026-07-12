Add-Type -AssemblyName System.Drawing
# Mythical Forge block textures: dark rune-stone body with gold studs,
# molten crucible on top, burning mouth on the front. Writes 3 PNGs.
$DST = "C:\Users\alfredo\Desktop\mod\mod-minecraft\mythical-swords-template-1.20.1\src\main\resources\assets\mythicalswords\textures\block"

function HexC($h) {
  $h = $h.TrimStart('#')
  [System.Drawing.Color]::FromArgb(255,
    [Convert]::ToInt32($h.Substring(0,2),16),
    [Convert]::ToInt32($h.Substring(2,2),16),
    [Convert]::ToInt32($h.Substring(4,2),16))
}

$stone      = HexC "4b4650"
$stoneDark  = HexC "353139"
$stoneLight = HexC "5d5766"
$mortar     = HexC "27242c"
$gold       = HexC "c9a227"
$goldHi     = HexC "ffd75e"
$goldSh     = HexC "8a6d14"
$moltCore   = HexC "fff3b0"
$moltBright = HexC "ffc23d"
$moltMid    = HexC "ee7a1f"
$moltDeep   = HexC "b03d12"
$hollow     = HexC "191621"

$noiseDark  = @(@(1,2),@(5,1),@(9,2),@(14,1),@(2,6),@(13,5),@(0,10),@(10,8),@(15,9),@(3,14),@(12,14),@(6,4))
$noiseLight = @(@(3,1),@(12,2),@(1,5),@(14,7),@(2,12),@(11,6),@(9,15),@(7,13))

function NewStone() {
  $bmp = New-Object System.Drawing.Bitmap(16, 16)
  for ($y = 0; $y -lt 16; $y++) { for ($x = 0; $x -lt 16; $x++) { $bmp.SetPixel($x, $y, $stone) } }
  foreach ($p in $noiseDark)  { $bmp.SetPixel($p[0], $p[1], $stoneDark) }
  foreach ($p in $noiseLight) { $bmp.SetPixel($p[0], $p[1], $stoneLight) }
  return $bmp
}

function Stud($bmp, $x, $y) {
  $bmp.SetPixel($x,   $y,   $goldHi)
  $bmp.SetPixel($x+1, $y,   $gold)
  $bmp.SetPixel($x,   $y+1, $gold)
  $bmp.SetPixel($x+1, $y+1, $goldSh)
}

# ===== TOP: crucible of molten metal =====
$top = NewStone
Stud $top 0 0; Stud $top 14 0; Stud $top 0 14; Stud $top 14 14
# crucible rim
for ($i = 3; $i -le 12; $i++) {
  $top.SetPixel($i, 3, $mortar); $top.SetPixel($i, 12, $mortar)
  $top.SetPixel(3, $i, $mortar); $top.SetPixel(12, $i, $mortar)
}
# molten pool, layered heat gradient
for ($y = 4; $y -le 11; $y++) { for ($x = 4; $x -le 11; $x++) { $top.SetPixel($x, $y, $moltDeep) } }
for ($y = 5; $y -le 10; $y++) { for ($x = 5; $x -le 10; $x++) { $top.SetPixel($x, $y, $moltMid) } }
for ($y = 6; $y -le 9;  $y++) { for ($x = 6; $x -le 9;  $x++) { $top.SetPixel($x, $y, $moltBright) } }
for ($y = 7; $y -le 8;  $y++) { for ($x = 7; $x -le 8;  $x++) { $top.SetPixel($x, $y, $moltCore) } }
$top.SetPixel(5, 6, $moltCore); $top.SetPixel(10, 9, $moltCore)
$top.Save((Join-Path $DST "mythical_forge_top.png"), [System.Drawing.Imaging.ImageFormat]::Png)
$top.Dispose(); Write-Output "wrote mythical_forge_top"

# ===== FRONT: burning mouth + gold emblem =====
$front = NewStone
# gold trim across the top edge
for ($x = 0; $x -lt 16; $x++) { $front.SetPixel($x, 0, $gold) }
$front.SetPixel(3, 0, $goldHi); $front.SetPixel(8, 0, $goldHi); $front.SetPixel(13, 0, $goldHi)
# small gold diamond emblem
foreach ($p in @(@(7,2),@(8,2),@(6,3),@(9,3),@(6,4),@(9,4),@(7,5),@(8,5))) { $front.SetPixel($p[0], $p[1], $gold) }
foreach ($p in @(@(7,3),@(8,3),@(7,4),@(8,4))) { $front.SetPixel($p[0], $p[1], $goldHi) }
# mouth frame
for ($x = 3; $x -le 12; $x++) { $front.SetPixel($x, 8, $mortar) }
for ($y = 9; $y -le 15; $y++) { $front.SetPixel(3, $y, $mortar); $front.SetPixel(12, $y, $mortar) }
# hollow interior
for ($y = 9; $y -le 15; $y++) { for ($x = 4; $x -le 11; $x++) { $front.SetPixel($x, $y, $hollow) } }
# fire bed
for ($x = 4; $x -le 11; $x++) { $front.SetPixel($x, 15, $moltDeep); $front.SetPixel($x, 14, $moltMid) }
foreach ($x in @(5,8,10)) { $front.SetPixel($x, 14, $moltBright) }
foreach ($x in @(5,7,9))  { $front.SetPixel($x, 13, $moltBright) }
foreach ($x in @(6,10))   { $front.SetPixel($x, 13, $moltMid) }
$front.SetPixel(6, 12, $moltCore); $front.SetPixel(9, 12, $moltBright); $front.SetPixel(7, 11, $moltBright)
# bottom corner studs
Stud $front 0 14; Stud $front 14 14
$front.Save((Join-Path $DST "mythical_forge_front.png"), [System.Drawing.Imaging.ImageFormat]::Png)
$front.Dispose(); Write-Output "wrote mythical_forge_front"

# ===== SIDE: rune-stone bricks =====
$side = NewStone
for ($x = 0; $x -lt 16; $x++) { $side.SetPixel($x, 4, $mortar); $side.SetPixel($x, 10, $mortar) }
foreach ($y in @(0,1,2,3))      { $side.SetPixel(5, $y, $mortar); $side.SetPixel(11, $y, $mortar) }
foreach ($y in @(5,6,7,8,9))    { $side.SetPixel(2, $y, $mortar); $side.SetPixel(8, $y, $mortar); $side.SetPixel(13, $y, $mortar) }
foreach ($y in @(11,12,13,14,15)) { $side.SetPixel(6, $y, $mortar); $side.SetPixel(12, $y, $mortar) }
Stud $side 0 0; Stud $side 14 0; Stud $side 0 14; Stud $side 14 14
$side.Save((Join-Path $DST "mythical_forge_side.png"), [System.Drawing.Imaging.ImageFormat]::Png)
$side.Dispose(); Write-Output "wrote mythical_forge_side"
