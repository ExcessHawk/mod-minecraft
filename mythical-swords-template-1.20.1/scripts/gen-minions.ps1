Add-Type -AssemblyName System.Drawing
# Minion entity textures, vanilla 64x32 old-skin layout matching minion.geo.json:
#   head uv(0,0) 8x8x8 | legs uv(0,16) | body uv(16,16) | arms uv(40,16)
# One themed palette per mythology minion.
$DST = "C:\Users\alfredo\Desktop\mod\mod-minecraft\mythical-swords-template-1.20.1\src\main\resources\assets\mythicalswords\textures\entity"

function HexC($h) {
  $h = $h.TrimStart('#')
  [System.Drawing.Color]::FromArgb(255,
    [Convert]::ToInt32($h.Substring(0,2),16),
    [Convert]::ToInt32($h.Substring(2,2),16),
    [Convert]::ToInt32($h.Substring(4,2),16))
}
$clear = [System.Drawing.Color]::FromArgb(0,0,0,0)

# skin=head/arms base, cloth=body/legs, dark=shadow lines, accent=eyes/marks
$pal = @{
  "draugr"            = @("8fa3ad","3d4a52","28323a","7fd4ff")  # pale corpse, dark iron, ice eyes
  "oni_menor"         = @("c53f3f","2b2430","1a161e","ffd75e")  # red skin, black garb, gold eyes
  "momia_sirviente"   = @("d8c9a3","c2b189","8a7a55","2b2430")  # bandages, dark hollow eyes
  "guerrero_jaguar"   = @("d8a03f","7a5a20","4a3612","1a1a1a")  # jaguar pelt, dark spots
  "hoplita_espectral" = @("9adfd4","5a8f86","2f5049","e8fff9")  # spectral teal, pale glow eyes
  "soldado_terracota" = @("b5836b","96684f","6e4a36","3a2a20")  # fired clay, crack lines
}

$rng = New-Object System.Random(777)

foreach ($name in $pal.Keys) {
  $skin = HexC $pal[$name][0]; $cloth = HexC $pal[$name][1]; $dark = HexC $pal[$name][2]; $accent = HexC $pal[$name][3]
  $b = New-Object System.Drawing.Bitmap(64, 32)
  for ($y=0;$y -lt 32;$y++){for($x=0;$x -lt 64;$x++){$b.SetPixel($x,$y,$clear)}}

  function FillR($x,$y,$w,$h,$c){ for($yy=$y;$yy -lt ($y+$h);$yy++){for($xx=$x;$xx -lt ($x+$w);$xx++){$b.SetPixel($xx,$yy,$c)}} }
  function Noise($x,$y,$w,$h,$c,$n){ for($i=0;$i -lt $n;$i++){ $b.SetPixel($x+$rng.Next($w), $y+$rng.Next($h), $c) } }

  # head cross: (8,0)-(24,8) top/bottom, (0,8)-(32,16) sides
  FillR 8 0 16 8 $skin
  FillR 0 8 32 8 $skin
  Noise 0 8 32 8 $dark 14; Noise 8 0 16 8 $dark 8
  # face on front (8,8)-(16,16): eyes + mouth
  $b.SetPixel(10,11,$accent); $b.SetPixel(11,11,$accent)
  $b.SetPixel(13,11,$accent); $b.SetPixel(14,11,$accent)
  FillR 11 14 3 1 $dark

  # legs cross: (0,16)-(16,32) top strip (4,16)-(12,20), sides (0,20)-(16,32)
  FillR 4 16 8 4 $cloth
  FillR 0 20 16 12 $cloth
  Noise 0 20 16 12 $dark 12

  # body: (16,16)-(40,32): top strip (20,16)-(36,20), sides (16,20)-(40,32)
  FillR 20 16 16 4 $cloth
  FillR 16 20 24 12 $cloth
  Noise 16 20 24 12 $dark 16
  # belt
  FillR 16 26 24 1 $dark

  # arms: (40,16)-(56,32)
  FillR 44 16 8 4 $skin
  FillR 40 20 16 12 $skin
  Noise 40 20 16 12 $dark 10

  # theme details
  switch ($name) {
    "momia_sirviente" { foreach($yy in 21,24,27,30){ FillR 0 $yy 56 1 $dark } ; foreach($yy in 9,12){ FillR 0 $yy 32 1 $dark } }
    "guerrero_jaguar" { Noise 0 8 32 8 $accent 10; Noise 16 20 24 12 $accent 14; Noise 40 20 16 12 $accent 8 }
    "soldado_terracota" { foreach($p in @(@(3,22),@(4,23),@(20,24),@(21,25),@(22,26),@(45,22),@(46,23),@(10,10),@(11,11))){ $b.SetPixel($p[0],$p[1],$dark) } }
    "hoplita_espectral" { FillR 8 8 16 2 $cloth; FillR 20 16 16 4 $dark } # helmet band + bronze chest top
    "draugr" { FillR 16 20 24 3 $dark } # rusted chest armor strip
    "oni_menor" { $b.SetPixel(9,8,$accent); $b.SetPixel(15,8,$accent) } # horn nubs
  }

  $b.Save((Join-Path $DST "minion_$name.png"), [System.Drawing.Imaging.ImageFormat]::Png)
  $b.Dispose()
  Write-Output "wrote minion_$name"
}
