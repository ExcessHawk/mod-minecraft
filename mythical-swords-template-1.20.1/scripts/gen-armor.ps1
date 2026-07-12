Add-Type -AssemblyName System.Drawing
$AS = "C:\Users\alfredo\Desktop\mod\mod-minecraft\mythical-swords-template-1.20.1\src\main\resources\assets\mythicalswords\textures"
$ITEM = "$AS\item"; $ARMOR = "$AS\models\armor"
if (-not (Test-Path $ARMOR)) { New-Item -ItemType Directory -Force $ARMOR | Out-Null }
function HexC($h){ $h=$h.TrimStart('#'); [System.Drawing.Color]::FromArgb(255,[Convert]::ToInt32($h.Substring(0,2),16),[Convert]::ToInt32($h.Substring(2,2),16),[Convert]::ToInt32($h.Substring(4,2),16)) }
function Lerp($a,$b,$t){ [System.Drawing.Color]::FromArgb(255, [int]($a.R+($b.R-$a.R)*$t), [int]($a.G+($b.G-$a.G)*$t), [int]($a.B+($b.B-$a.B)*$t)) }
$clear=[System.Drawing.Color]::FromArgb(0,0,0,0)

# ---- Detailed 16x16 icons ----
function Icon($name,$piece,$base,$lt,$dk,$trim){
  $b=New-Object System.Drawing.Bitmap(16,16)
  for($y=0;$y -lt 16;$y++){for($x=0;$x -lt 16;$x++){$b.SetPixel($x,$y,$clear)}}
  $g=[System.Drawing.Graphics]::FromImage($b)
  $bb=New-Object System.Drawing.SolidBrush($base);$lb=New-Object System.Drawing.SolidBrush($lt);$db=New-Object System.Drawing.SolidBrush($dk);$tb=New-Object System.Drawing.SolidBrush($trim);$cb=New-Object System.Drawing.SolidBrush($clear)
  switch($piece){
    "helmet" {
      $g.FillRectangle($bb,4,2,8,7); $g.FillRectangle($bb,3,4,10,4)
      $g.FillRectangle($lb,4,2,8,1); $g.FillRectangle($lb,3,4,1,3)          # top + left highlight
      $g.FillRectangle($db,12,4,1,4); $g.FillRectangle($db,3,8,10,1)        # right + bottom shadow
      $g.FillRectangle($tb,3,6,10,1)                                        # brow trim
      $g.FillRectangle($cb,6,7,4,2)                                         # face opening
      $b.SetPixel(5,3,$lt);$b.SetPixel(10,3,$dk)
    }
    "chestplate" {
      $g.FillRectangle($bb,4,3,8,9); $g.FillRectangle($bb,2,3,3,6); $g.FillRectangle($bb,11,3,3,6)
      $g.FillRectangle($lb,4,3,8,1); $g.FillRectangle($lb,2,3,1,5)          # top + left highlight
      $g.FillRectangle($db,11,4,1,7); $g.FillRectangle($db,4,11,8,1)        # right + bottom shadow
      $g.FillRectangle($tb,4,4,8,1)                                         # collar trim
      $g.FillRectangle($db,7,5,2,6)                                         # center seam
      $b.SetPixel(3,4,$trim);$b.SetPixel(12,4,$trim)                        # shoulder studs
    }
    "leggings" {
      $g.FillRectangle($bb,4,3,8,3); $g.FillRectangle($bb,4,6,3,9); $g.FillRectangle($bb,9,6,3,9)
      $g.FillRectangle($tb,4,3,8,1)                                         # belt trim
      $g.FillRectangle($lb,4,6,1,8); $g.FillRectangle($lb,9,6,1,8)          # left highlight each leg
      $g.FillRectangle($db,6,6,1,9); $g.FillRectangle($db,11,6,1,9)         # right shadow
      $b.SetPixel(7,4,$dk)
    }
    "boots" {
      $g.FillRectangle($bb,3,9,4,6); $g.FillRectangle($bb,9,9,4,6)
      $g.FillRectangle($bb,3,13,5,2); $g.FillRectangle($bb,9,13,5,2)        # soles
      $g.FillRectangle($lb,3,9,4,1); $g.FillRectangle($lb,9,9,4,1)          # cuff highlight
      $g.FillRectangle($db,3,14,5,1); $g.FillRectangle($db,9,14,5,1)        # sole shadow
      $g.FillRectangle($tb,3,11,4,1); $g.FillRectangle($tb,9,11,4,1)        # trim band
    }
  }
  $g.Dispose(); $b.Save((Join-Path $ITEM "$name.png"),[System.Drawing.Imaging.ImageFormat]::Png); $b.Dispose()
}

# ---- Detailed worn armor layers (gradient + rivets + trim) ----
function Layers($mat,$base,$lt,$dk,$trim){
  $rng=New-Object System.Random(([System.Math]::Abs($mat.GetHashCode())))
  foreach($n in 1,2){
    $w=64;$h=32
    $b=New-Object System.Drawing.Bitmap($w,$h)
    # vertical metallic gradient (light top -> dark bottom) per row
    for($y=0;$y -lt $h;$y++){
      $t=$y/($h-1.0)
      $c=Lerp $lt $dk $t
      for($x=0;$x -lt $w;$x++){ $b.SetPixel($x,$y,$c) }
    }
    # plate segment lines (darker) every 8px
    for($y=0;$y -lt $h;$y+=8){ for($x=0;$x -lt $w;$x++){ $b.SetPixel($x,$y,$dk) } }
    # trim accent rows
    foreach($ty in 1,9){ if($ty -lt $h){ for($x=0;$x -lt $w;$x++){ $b.SetPixel($x,$ty,$trim) } } }
    # rivets / studs (scattered)
    for($i=0;$i -lt 60;$i++){ $rx=$rng.Next($w);$ry=$rng.Next($h); $b.SetPixel($rx,$ry,$dk) }
    for($i=0;$i -lt 30;$i++){ $rx=$rng.Next($w);$ry=$rng.Next($h); $b.SetPixel($rx,$ry,$lt) }
    $b.Save((Join-Path $ARMOR "${mat}_layer_$n.png"),[System.Drawing.Imaging.ImageFormat]::Png); $b.Dispose()
  }
}

$mats=@{
  "orichalcum"=@("c0703a","f0a868","803a18","ffe0a0")
  "uru"=@("5a7a9a","9ab2cc","2a3f55","cfe7f5")
  "voidsteel"=@("5a3a8a","9a6ad0","2a163f","c0a0ff")
  "froststeel"=@("9ad8e8","d8f4ff","5a98b8","ffffff")
}
foreach($m in $mats.Keys){
  $c=$mats[$m]; $base=HexC $c[0];$lt=HexC $c[1];$dk=HexC $c[2];$trim=HexC $c[3]
  foreach($p in "helmet","chestplate","leggings","boots"){ Icon "${m}_$p" $p $base $lt $dk $trim }
  Layers $m $base $lt $dk $trim
  Write-Output "refined armor $m"
}
