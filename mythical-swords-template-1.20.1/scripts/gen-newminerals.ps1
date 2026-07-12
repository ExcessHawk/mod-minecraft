Add-Type -AssemblyName System.Drawing
$ROOT = "C:\Users\alfredo\Desktop\mod\mod-minecraft\mythical-swords-template-1.20.1\src\main\resources\assets\mythicalswords\textures"
$ITEM = "$ROOT\item"; $BLOCK = "$ROOT\block"
function HexC($h){ $h=$h.TrimStart('#'); [System.Drawing.Color]::FromArgb(255,[Convert]::ToInt32($h.Substring(0,2),16),[Convert]::ToInt32($h.Substring(2,2),16),[Convert]::ToInt32($h.Substring(4,2),16)) }
$clear=[System.Drawing.Color]::FromArgb(0,0,0,0)

# shared ore gem layout (same as gen-ores)
$stoneFill=HexC "7a7a7e"; $noiseDark=HexC "5e5e63"; $noiseLight=HexC "8f8f94"
$darkN=@(@(1,2),@(5,1),@(9,2),@(14,1),@(2,6),@(7,7),@(13,5),@(0,10),@(4,9),@(10,8),@(15,9),@(3,14),@(8,13),@(12,14),@(6,4),@(11,12))
$lightN=@(@(3,1),@(7,3),@(12,2),@(1,5),@(9,6),@(14,7),@(5,8),@(2,12),@(11,6),@(7,11),@(14,13),@(9,15))
$gemBase=@(@(3,4),@(4,4),@(3,5),@(4,5),@(2,4),@(4,3),@(10,3),@(11,3),@(10,4),@(11,4),@(12,4),@(5,11),@(6,11),@(5,12),@(6,12),@(4,12),@(6,10),@(11,10),@(12,10),@(11,11),@(12,11),@(13,11),@(10,10),@(8,8),@(8,9))
$gemHi=@(@(3,4),@(10,3),@(5,11),@(11,10),@(8,8)); $gemSh=@(@(4,5),@(12,4),@(6,12),@(12,11),@(8,9))
# raw chunk layout
$blob=@(@(6,5),@(7,5),@(8,5),@(9,5),@(5,6),@(6,6),@(7,6),@(8,6),@(9,6),@(10,6),@(4,7),@(5,7),@(6,7),@(7,7),@(8,7),@(9,7),@(10,7),@(11,7),@(4,8),@(5,8),@(6,8),@(7,8),@(8,8),@(9,8),@(10,8),@(11,8),@(5,9),@(6,9),@(7,9),@(8,9),@(9,9),@(10,9),@(11,9),@(6,10),@(7,10),@(8,10),@(9,10),@(10,10),@(7,11),@(8,11),@(9,11),@(11,11),@(11,12),@(12,12),@(12,13))
$blobDark=@(@(6,5),@(7,5),@(8,5),@(9,5),@(5,6),@(10,6),@(4,7),@(11,7),@(4,8),@(11,8),@(5,9),@(11,9),@(6,10),@(10,10),@(7,11),@(8,11),@(9,11),@(10,9),@(9,10),@(12,13),@(12,12))
$blobHi=@(@(6,6),@(7,6),@(8,6),@(5,7),@(6,7),@(7,8))
# ingot bar layout
$barBase=@(@(3,7),@(4,7),@(5,7),@(6,7),@(7,7),@(8,7),@(9,7),@(10,7),@(11,7),@(12,7),@(3,8),@(4,8),@(5,8),@(6,8),@(7,8),@(8,8),@(9,8),@(10,8),@(11,8),@(12,8))
$barHi=@(@(5,5),@(6,5),@(7,5),@(8,5),@(9,5),@(10,5),@(4,6),@(5,6),@(6,6),@(7,6),@(8,6),@(9,6),@(10,6),@(11,6))
$barDk=@(@(4,9),@(5,9),@(6,9),@(7,9),@(8,9),@(9,9),@(10,9),@(11,9),@(5,10),@(6,10),@(7,10),@(8,10),@(9,10),@(10,10))

function Ore($name,$b,$l,$d){ $base=HexC $b;$lt=HexC $l;$dk=HexC $d
  $bmp=New-Object System.Drawing.Bitmap(16,16)
  for($y=0;$y -lt 16;$y++){for($x=0;$x -lt 16;$x++){$bmp.SetPixel($x,$y,$stoneFill)}}
  foreach($p in $darkN){$bmp.SetPixel($p[0],$p[1],$noiseDark)}
  foreach($p in $lightN){$bmp.SetPixel($p[0],$p[1],$noiseLight)}
  foreach($p in $gemBase){$bmp.SetPixel($p[0],$p[1],$base)}
  foreach($p in $gemHi){$bmp.SetPixel($p[0],$p[1],$lt)}
  foreach($p in $gemSh){$bmp.SetPixel($p[0],$p[1],$dk)}
  $bmp.Save((Join-Path $BLOCK "${name}_ore.png"),[System.Drawing.Imaging.ImageFormat]::Png);$bmp.Dispose() }

function Raw($name,$b,$l,$d){ $base=HexC $b;$lt=HexC $l;$dk=HexC $d
  $bmp=New-Object System.Drawing.Bitmap(16,16)
  for($y=0;$y -lt 16;$y++){for($x=0;$x -lt 16;$x++){$bmp.SetPixel($x,$y,$clear)}}
  foreach($p in $blob){$bmp.SetPixel($p[0],$p[1],$base)}
  foreach($p in $blobDark){$bmp.SetPixel($p[0],$p[1],$dk)}
  foreach($p in $blobHi){$bmp.SetPixel($p[0],$p[1],$lt)}
  $bmp.Save((Join-Path $ITEM "raw_${name}.png"),[System.Drawing.Imaging.ImageFormat]::Png);$bmp.Dispose() }

function Ingot($name,$b,$l,$d){ $base=HexC $b;$lt=HexC $l;$dk=HexC $d
  $bmp=New-Object System.Drawing.Bitmap(16,16)
  for($y=0;$y -lt 16;$y++){for($x=0;$x -lt 16;$x++){$bmp.SetPixel($x,$y,$clear)}}
  foreach($p in $barBase){$bmp.SetPixel($p[0],$p[1],$base)}
  foreach($p in $barHi){$bmp.SetPixel($p[0],$p[1],$lt)}
  foreach($p in $barDk){$bmp.SetPixel($p[0],$p[1],$dk)}
  $bmp.Save((Join-Path $ITEM "${name}_ingot.png"),[System.Drawing.Imaging.ImageFormat]::Png);$bmp.Dispose() }

function Mineral($name,$b,$l,$d){ Ore $name $b $l $d; Raw $name $b $l $d; Ingot $name $b $l $d; Write-Output "wrote $name (ore/raw/ingot)" }

Mineral "orichalcum" "c0703a" "f0a868" "803a18"
Mineral "uru"        "4a5a72" "8a9ab2" "28303f"
Mineral "voidsteel"  "5a3a8a" "9a6ad0" "26163f"
Mineral "froststeel" "9ad8e8" "d8f4ff" "5a98b8"
