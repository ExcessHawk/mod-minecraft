Add-Type -AssemblyName System.Drawing
$DST = "C:\Users\alfredo\Desktop\mod\mod-minecraft\mythical-swords-template-1.20.1\src\main\resources\assets\mythicalswords\textures\item"

function HexC($h) {
  $h = $h.TrimStart('#')
  [System.Drawing.Color]::FromArgb(255, [Convert]::ToInt32($h.Substring(0,2),16), [Convert]::ToInt32($h.Substring(2,2),16), [Convert]::ToInt32($h.Substring(4,2),16))
}
$clear = [System.Drawing.Color]::FromArgb(0,0,0,0)

# ---- Potion bottle (liquid color param) ----
function Bottle($name, $liqHex, $liqLightHex) {
  $liq = HexC $liqHex; $liqL = HexC $liqLightHex
  $glass = HexC "c9d4dc"; $cork = HexC "8a6a3a"; $outline = HexC "3a3f48"
  $bmp = New-Object System.Drawing.Bitmap(16,16)
  for($y=0;$y -lt 16;$y++){for($x=0;$x -lt 16;$x++){$bmp.SetPixel($x,$y,$clear)}}
  # cork
  foreach($p in @(@(7,1),@(8,1),@(7,2),@(8,2))){$bmp.SetPixel($p[0],$p[1],$cork)}
  # neck
  foreach($p in @(@(7,3),@(8,3),@(7,4),@(8,4))){$bmp.SetPixel($p[0],$p[1],$glass)}
  # body outline + liquid: rows 5..14
  $rows = @{5=@(6,9);6=@(5,10);7=@(4,11);8=@(4,11);9=@(4,11);10=@(4,11);11=@(4,11);12=@(5,10);13=@(6,9)}
  foreach($y in $rows.Keys){
    $x0=$rows[$y][0]; $x1=$rows[$y][1]
    for($x=$x0;$x -le $x1;$x++){
      if($x -eq $x0 -or $x -eq $x1 -or $y -eq 5 -or $y -eq 13){ $bmp.SetPixel($x,$y,$glass) }
      else { $c = if($y -le 7){$liqL}else{$liq}; $bmp.SetPixel($x,$y,$c) }
    }
  }
  # glass shine
  $bmp.SetPixel(6,7,$glass);$bmp.SetPixel(6,8,$glass)
  $bmp.Save((Join-Path $DST "$name.png"),[System.Drawing.Imaging.ImageFormat]::Png); $bmp.Dispose()
  Write-Output "wrote $name"
}

Bottle "ambrosia" "e8b62a" "ffe27a"
Bottle "phoenix_tear" "e0521f" "ff9a3c"
Bottle "elixir_of_the_gods" "8fe6ff" "e8ffff"
Bottle "titan_brew" "3aa84a" "7ce08c"
Bottle "hermes_draught" "cfeaff" "ffffff"
Bottle "berserker_draught" "b01a1a" "e85a3a"
Bottle "storm_vial" "4a7ad0" "a0d0ff"

# ---- Medusa's Eye ----
$bmp = New-Object System.Drawing.Bitmap(16,16)
for($y=0;$y -lt 16;$y++){for($x=0;$x -lt 16;$x++){$bmp.SetPixel($x,$y,$clear)}}
$scler = HexC "e8e8d8"; $iris = HexC "3aa84a"; $iris2 = HexC "7ce08c"; $pupil = HexC "101410"; $snake = HexC "2a7a3a"
$cx=7.5;$cy=7.5
for($y=0;$y -lt 16;$y++){for($x=0;$x -lt 16;$x++){
  $dx=($x-$cx)/6.0;$dy=($y-$cy)/4.2;$d=[math]::Sqrt($dx*$dx+$dy*$dy)
  if($d -le 1.0){ $bmp.SetPixel($x,$y,$scler) }
}}
$ir=2.6
for($y=0;$y -lt 16;$y++){for($x=0;$x -lt 16;$x++){
  $d=[math]::Sqrt(($x-$cx)*($x-$cx)+($y-$cy)*($y-$cy))
  if($d -le $ir){ $c = if($d -gt $ir-1){$iris}else{$iris2}; $bmp.SetPixel($x,$y,$c) }
  if($d -le 1.1){ $bmp.SetPixel($x,$y,$pupil) }
}}
# little snake tendrils top
$bmp.SetPixel(4,2,$snake);$bmp.SetPixel(5,1,$snake);$bmp.SetPixel(10,1,$snake);$bmp.SetPixel(11,2,$snake);$bmp.SetPixel(7,1,$snake)
$bmp.Save((Join-Path $DST "medusa_eye.png"),[System.Drawing.Imaging.ImageFormat]::Png); $bmp.Dispose()
Write-Output "wrote medusa_eye"

# ---- Phoenix Feather ----
$bmp = New-Object System.Drawing.Bitmap(16,16)
for($y=0;$y -lt 16;$y++){for($x=0;$x -lt 16;$x++){$bmp.SetPixel($x,$y,$clear)}}
$shaft = HexC "b85a14"
$f1 = HexC "ff8a20"; $f2 = HexC "ffc24a"; $f3 = HexC "e0401a"
# diagonal shaft from bottom-left to top-right
for($i=0;$i -lt 12;$i++){ $x=3+$i; $y=13-$i; if($x -lt 16 -and $y -ge 0){$bmp.SetPixel($x,$y,$shaft)} }
# barbs around shaft
for($i=1;$i -lt 11;$i++){
  $x=3+$i; $y=13-$i
  $c = if($i%2 -eq 0){$f2}else{$f1}
  if($x-1 -ge 0 -and $y -lt 16){$bmp.SetPixel($x-1,$y,$c)}
  if($y+1 -lt 16){$bmp.SetPixel($x,$y+1,$f3)}
  if($x+1 -lt 16 -and $y-1 -ge 0){$bmp.SetPixel($x+1,$y-1,$c)}
}
$bmp.Save((Join-Path $DST "phoenix_feather.png"),[System.Drawing.Imaging.ImageFormat]::Png); $bmp.Dispose()
Write-Output "wrote phoenix_feather"

# ---- Void Pearl ----
$bmp = New-Object System.Drawing.Bitmap(16,16)
for($y=0;$y -lt 16;$y++){for($x=0;$x -lt 16;$x++){$bmp.SetPixel($x,$y,$clear)}}
$dark = HexC "1a0f2e"; $mid = HexC "4a2a7a"; $glow = HexC "9a6ad0"; $spark = HexC "d8c0ff"
$cx=7.5;$cy=7.5;$r=5.5
for($y=0;$y -lt 16;$y++){for($x=0;$x -lt 16;$x++){
  $d=[math]::Sqrt(($x-$cx)*($x-$cx)+($y-$cy)*($y-$cy))
  if($d -le $r){
    $c = if($d -gt $r-1){$dark}elseif($d -gt $r-2.6){$mid}else{$glow}
    $bmp.SetPixel($x,$y,$c)
  }
}}
$bmp.SetPixel(5,5,$spark);$bmp.SetPixel(6,5,$spark);$bmp.SetPixel(9,9,$mid)
$bmp.Save((Join-Path $DST "void_pearl.png"),[System.Drawing.Imaging.ImageFormat]::Png); $bmp.Dispose()
Write-Output "wrote void_pearl"
