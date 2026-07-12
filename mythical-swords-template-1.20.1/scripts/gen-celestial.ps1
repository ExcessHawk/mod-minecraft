Add-Type -AssemblyName System.Drawing
# Celestial endgame set: ingot icon + 4 armor icons + worn layers.
# Radiant white-gold with cyan trim. Mirrors gen-armor.ps1 style.
$AS = "C:\Users\alfredo\Desktop\mod\mod-minecraft\mythical-swords-template-1.20.1\src\main\resources\assets\mythicalswords\textures"
$ITEM = "$AS\item"; $ARMOR = "$AS\models\armor"
if (-not (Test-Path $ARMOR)) { New-Item -ItemType Directory -Force $ARMOR | Out-Null }
function HexC($h){ $h=$h.TrimStart('#'); [System.Drawing.Color]::FromArgb(255,[Convert]::ToInt32($h.Substring(0,2),16),[Convert]::ToInt32($h.Substring(2,2),16),[Convert]::ToInt32($h.Substring(4,2),16)) }
function Lerp($a,$b,$t){ [System.Drawing.Color]::FromArgb(255, [int]($a.R+($b.R-$a.R)*$t), [int]($a.G+($b.G-$a.G)*$t), [int]($a.B+($b.B-$a.B)*$t)) }
$clear=[System.Drawing.Color]::FromArgb(0,0,0,0)

$base=HexC "e8d48a"; $lt=HexC "fff8d0"; $dk=HexC "b8923a"; $trim=HexC "9be8ff"

# ---- Ingot icon ----
$b=New-Object System.Drawing.Bitmap(16,16)
for($y=0;$y -lt 16;$y++){for($x=0;$x -lt 16;$x++){$b.SetPixel($x,$y,$clear)}}
$g=[System.Drawing.Graphics]::FromImage($b)
$bb=New-Object System.Drawing.SolidBrush($base);$lb=New-Object System.Drawing.SolidBrush($lt);$db=New-Object System.Drawing.SolidBrush($dk);$tb=New-Object System.Drawing.SolidBrush($trim)
# ingot body (offset parallelogram-ish)
$g.FillRectangle($bb,3,6,10,5)
$g.FillRectangle($bb,2,7,12,3)
$g.FillRectangle($lb,3,6,10,1); $g.FillRectangle($lb,2,7,1,3)   # top+left shine
$g.FillRectangle($db,13,7,1,3); $g.FillRectangle($db,3,10,10,1) # right+bottom shade
# cyan celestial glint
$b.SetPixel(5,8,$trim); $b.SetPixel(6,8,$trim); $b.SetPixel(10,9,$trim)
$b.SetPixel(4,7,$lt); $b.SetPixel(11,8,$lt)
# floating sparkles
$b.SetPixel(2,3,$trim); $b.SetPixel(13,4,$trim); $b.SetPixel(8,2,$lt)
$g.Dispose(); $b.Save((Join-Path $ITEM "celestial_ingot.png"),[System.Drawing.Imaging.ImageFormat]::Png); $b.Dispose()
Write-Output "wrote celestial_ingot"

# ---- Armor icons (same shapes as gen-armor.ps1) ----
function Icon($name,$piece){
  $b=New-Object System.Drawing.Bitmap(16,16)
  for($y=0;$y -lt 16;$y++){for($x=0;$x -lt 16;$x++){$b.SetPixel($x,$y,$clear)}}
  $g=[System.Drawing.Graphics]::FromImage($b)
  $bb=New-Object System.Drawing.SolidBrush($base);$lb=New-Object System.Drawing.SolidBrush($lt);$db=New-Object System.Drawing.SolidBrush($dk);$tb=New-Object System.Drawing.SolidBrush($trim);$cb=New-Object System.Drawing.SolidBrush($clear)
  switch($piece){
    "helmet" {
      $g.FillRectangle($bb,4,2,8,7); $g.FillRectangle($bb,3,4,10,4)
      $g.FillRectangle($lb,4,2,8,1); $g.FillRectangle($lb,3,4,1,3)
      $g.FillRectangle($db,12,4,1,4); $g.FillRectangle($db,3,8,10,1)
      $g.FillRectangle($tb,3,6,10,1)
      $g.FillRectangle($cb,6,7,4,2)
      $b.SetPixel(5,3,$lt);$b.SetPixel(10,3,$dk)
    }
    "chestplate" {
      $g.FillRectangle($bb,4,3,8,9); $g.FillRectangle($bb,2,3,3,6); $g.FillRectangle($bb,11,3,3,6)
      $g.FillRectangle($lb,4,3,8,1); $g.FillRectangle($lb,2,3,1,5)
      $g.FillRectangle($db,11,4,1,7); $g.FillRectangle($db,4,11,8,1)
      $g.FillRectangle($tb,4,4,8,1)
      $g.FillRectangle($db,7,5,2,6)
      $b.SetPixel(3,4,$trim);$b.SetPixel(12,4,$trim)
    }
    "leggings" {
      $g.FillRectangle($bb,4,3,8,3); $g.FillRectangle($bb,4,6,3,9); $g.FillRectangle($bb,9,6,3,9)
      $g.FillRectangle($tb,4,3,8,1)
      $g.FillRectangle($lb,4,6,1,8); $g.FillRectangle($lb,9,6,1,8)
      $g.FillRectangle($db,6,6,1,9); $g.FillRectangle($db,11,6,1,9)
      $b.SetPixel(7,4,$dk)
    }
    "boots" {
      $g.FillRectangle($bb,3,9,4,6); $g.FillRectangle($bb,9,9,4,6)
      $g.FillRectangle($bb,3,13,5,2); $g.FillRectangle($bb,9,13,5,2)
      $g.FillRectangle($lb,3,9,4,1); $g.FillRectangle($lb,9,9,4,1)
      $g.FillRectangle($db,3,14,5,1); $g.FillRectangle($db,9,14,5,1)
      $g.FillRectangle($tb,3,11,4,1); $g.FillRectangle($tb,9,11,4,1)
    }
  }
  $g.Dispose(); $b.Save((Join-Path $ITEM "$name.png"),[System.Drawing.Imaging.ImageFormat]::Png); $b.Dispose()
}
foreach($p in "helmet","chestplate","leggings","boots"){ Icon "celestial_$p" $p; Write-Output "wrote celestial_$p" }

# ---- Worn layers (gradient + trim + sparkle rivets) ----
$rng=New-Object System.Random(20260711)
foreach($n in 1,2){
  $w=64;$h=32
  $b=New-Object System.Drawing.Bitmap($w,$h)
  for($y=0;$y -lt $h;$y++){
    $t=$y/($h-1.0)
    $c=Lerp $lt $dk $t
    for($x=0;$x -lt $w;$x++){ $b.SetPixel($x,$y,$c) }
  }
  for($y=0;$y -lt $h;$y+=8){ for($x=0;$x -lt $w;$x++){ $b.SetPixel($x,$y,$dk) } }
  foreach($ty in 1,9){ if($ty -lt $h){ for($x=0;$x -lt $w;$x++){ $b.SetPixel($x,$ty,$trim) } } }
  for($i=0;$i -lt 60;$i++){ $rx=$rng.Next($w);$ry=$rng.Next($h); $b.SetPixel($rx,$ry,$dk) }
  for($i=0;$i -lt 30;$i++){ $rx=$rng.Next($w);$ry=$rng.Next($h); $b.SetPixel($rx,$ry,$lt) }
  for($i=0;$i -lt 14;$i++){ $rx=$rng.Next($w);$ry=$rng.Next($h); $b.SetPixel($rx,$ry,$trim) }
  $b.Save((Join-Path $ARMOR "celestial_layer_$n.png"),[System.Drawing.Imaging.ImageFormat]::Png); $b.Dispose()
}
Write-Output "wrote celestial layers"
