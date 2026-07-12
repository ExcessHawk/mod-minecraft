Add-Type -AssemblyName System.Drawing
$ROOT = "C:\Users\alfredo\Desktop\mod\mod-minecraft\mythical-swords-template-1.20.1"
$ITEM = "$ROOT\src\main\resources\assets\mythicalswords\textures\item"
$BLOCK = "$ROOT\src\main\resources\assets\mythicalswords\textures\block"
$OUT = "$ROOT\docs\images"
if (-not (Test-Path $OUT)) { New-Item -ItemType Directory -Force $OUT | Out-Null }

function Sheet($title, [string[]]$paths, [string[]]$labels, [int]$cols, $outFile) {
  $cell = 96; $pad = 20; $head = 34
  $n = $paths.Count
  $rows = [math]::Ceiling($n / $cols)
  $w = $cols * $cell; $h = $head + $rows * ($cell + $pad)
  $bmp = New-Object System.Drawing.Bitmap($w, $h)
  $g = [System.Drawing.Graphics]::FromImage($bmp)
  $g.InterpolationMode = 'NearestNeighbor'; $g.PixelOffsetMode = 'Half'
  $g.Clear([System.Drawing.Color]::FromArgb(28, 28, 36))
  $g.TextRenderingHint = 'AntiAliasGridFit'
  $tf = New-Object System.Drawing.Font("Arial", 13, [System.Drawing.FontStyle]::Bold)
  $g.DrawString($title, $tf, [System.Drawing.Brushes]::Gold, 8, 6)
  $lf = New-Object System.Drawing.Font("Arial", 7.5)
  for ($i = 0; $i -lt $n; $i++) {
    $p = $paths[$i]
    $x = ($i % $cols) * $cell; $y = $head + [math]::Floor($i / $cols) * ($cell + $pad)
    if (Test-Path $p) {
      $img = [System.Drawing.Image]::FromFile($p)
      $sw = $img.Width; $sh = $img.Height; if ($sh -gt $sw * 1.5) { $sh = $sw }
      $dst = New-Object System.Drawing.Rectangle(($x + 8), $y, ($cell - 16), ($cell - 16))
      $src = New-Object System.Drawing.Rectangle(0, 0, $sw, $sh)
      $g.DrawImage($img, $dst, $src, [System.Drawing.GraphicsUnit]::Pixel)
      $img.Dispose()
    }
    $g.DrawString($labels[$i], $lf, [System.Drawing.Brushes]::White, ($x + 2), ($y + $cell - 16))
  }
  $g.Dispose(); $bmp.Save((Join-Path $OUT $outFile), [System.Drawing.Imaging.ImageFormat]::Png); $bmp.Dispose()
  Write-Output "wrote $outFile ($n)"
}

# Weapons
$wp = @("excalibur","gram","skofnung","hofund","gungnir","laevateinn","harpe","xiphos_sagrado","nike_blade","aegis_edge","kusanagi_no_tsurugi","muramasa","totsuka_no_tsurugi","masamune","xiuhcoatl","khopesh","was_scepter","ruyi_jingu_bang","jian")
$wl = @("Excalibur","Gram","Skofnung","Hofund","Gungnir","Laevateinn","Harpe","Xiphos","Nike Blade","Aegis Edge","Kusanagi","Muramasa","Totsuka","Masamune","Xiuhcoatl","Khopesh","Was Scepter","Ruyi Bang","Jian")
$wpath = $wp | ForEach-Object { "$ITEM\$_.png" }
Sheet "Armas / Weapons" $wpath $wl 6 "weapons.png"

# Ores + raw + ingots (block ores from BLOCK, rest from ITEM)
$opaths = @("$BLOCK\jade_imperial_ore.png","$ITEM\raw_jade_imperial.png","$ITEM\jade_imperial_ingot.png","$BLOCK\mythril_ore.png","$ITEM\raw_mythril.png","$ITEM\mythril_ingot.png","$BLOCK\northsteel_ore.png","$ITEM\raw_northsteel.png","$ITEM\northsteel_ingot.png","$BLOCK\sacred_iron_ore.png","$ITEM\raw_sacred_iron.png","$ITEM\sacred_iron_ingot.png","$BLOCK\tamahagane_ore.png","$ITEM\raw_tamahagane.png","$ITEM\tamahagane_ingot.png","$BLOCK\obsidiana_ritual_ore.png","$ITEM\raw_obsidiana_ritual.png","$ITEM\obsidiana_ritual_shard.png","$BLOCK\orichalcum_ore.png","$ITEM\raw_orichalcum.png","$ITEM\orichalcum_ingot.png","$BLOCK\uru_ore.png","$ITEM\raw_uru.png","$ITEM\uru_ingot.png","$BLOCK\voidsteel_ore.png","$ITEM\raw_voidsteel.png","$ITEM\voidsteel_ingot.png","$BLOCK\froststeel_ore.png","$ITEM\raw_froststeel.png","$ITEM\froststeel_ingot.png")
$olabels = @("Jade Ore","Raw Jade","Jade Ingot","Mythril Ore","Raw Mythril","Mythril Ingot","Northsteel Ore","Raw North.","North. Ingot","Sacred Ore","Raw Sacred","Sacred Ingot","Tamahagane Ore","Raw Tama.","Tama. Ingot","Obsidiana Ore","Raw Obsid.","Obsid. Shard","Orichalcum Ore","Raw Orichalcum","Orichalcum Ingot","Uru Ore","Raw Uru","Uru Ingot","Voidsteel Ore","Raw Voidsteel","Voidsteel Ingot","Froststeel Ore","Raw Froststeel","Froststeel Ingot")
Sheet "Minerales / Ores" $opaths $olabels 6 "ores.png"

# Relics
$rp = @("ambrosia","phoenix_tear","elixir_of_the_gods","phoenix_feather","void_pearl","titan_brew","hermes_draught","berserker_draught","medusa_eye","storm_vial")
$rl = @("Ambrosia","Phoenix Tear","Elixir Gods","Phoenix Feather","Void Pearl","Titan Brew","Hermes","Berserker","Medusa Eye","Storm Vial")
$rpath = $rp | ForEach-Object { "$ITEM\$_.png" }
Sheet "Reliquias / Relics" $rpath $rl 5 "relics.png"
