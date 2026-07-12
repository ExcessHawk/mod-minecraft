Add-Type -AssemblyName System.Drawing
# Mirrors the raw ore drops painted live in Blockbench: a lump/chunk shape per
# mineral with outline, highlight and a small detached nugget. Transparent bg.
$DST = "C:\Users\alfredo\Desktop\mod\mod-minecraft\mythical-swords-template-1.20.1\src\main\resources\assets\mythicalswords\textures\item"

function HexC($h) {
  $h = $h.TrimStart('#')
  [System.Drawing.Color]::FromArgb(255,
    [Convert]::ToInt32($h.Substring(0,2),16),
    [Convert]::ToInt32($h.Substring(2,2),16),
    [Convert]::ToInt32($h.Substring(4,2),16))
}

$blob = @(@(6,5),@(7,5),@(8,5),@(9,5),@(5,6),@(6,6),@(7,6),@(8,6),@(9,6),@(10,6),@(4,7),@(5,7),@(6,7),@(7,7),@(8,7),@(9,7),@(10,7),@(11,7),@(4,8),@(5,8),@(6,8),@(7,8),@(8,8),@(9,8),@(10,8),@(11,8),@(5,9),@(6,9),@(7,9),@(8,9),@(9,9),@(10,9),@(11,9),@(6,10),@(7,10),@(8,10),@(9,10),@(10,10),@(7,11),@(8,11),@(9,11),@(11,11),@(11,12),@(12,12),@(12,13))
$dark = @(@(6,5),@(7,5),@(8,5),@(9,5),@(5,6),@(10,6),@(4,7),@(11,7),@(4,8),@(11,8),@(5,9),@(11,9),@(6,10),@(10,10),@(7,11),@(8,11),@(9,11),@(10,9),@(9,10),@(12,13),@(12,12))
$hi   = @(@(6,6),@(7,6),@(8,6),@(5,7),@(6,7),@(7,8))

$pal = @{
  "raw_jade_imperial"    = @("46c369","8ceb9f","2a9650")
  "raw_mythril"          = @("b9e4e9","eafbfd","74acb6")
  "raw_northsteel"       = @("50a0d2","9bcdf2","28608d")
  "raw_obsidiana_ritual" = @("7349a5","af87d7","371f50")
  "raw_sacred_iron"      = @("e8b623","ffe47d","af7d17")
  "raw_tamahagane"       = @("6a6a78","9a9aab","2c2c36")
}

foreach ($name in $pal.Keys) {
  $base = HexC $pal[$name][0]; $light = HexC $pal[$name][1]; $shadow = HexC $pal[$name][2]
  $bmp = New-Object System.Drawing.Bitmap(16, 16)
  foreach ($p in $blob) { $bmp.SetPixel($p[0], $p[1], $base) }
  foreach ($p in $dark) { $bmp.SetPixel($p[0], $p[1], $shadow) }
  foreach ($p in $hi)   { $bmp.SetPixel($p[0], $p[1], $light) }
  $bmp.Save((Join-Path $DST "$name.png"), [System.Drawing.Imaging.ImageFormat]::Png)
  $bmp.Dispose()
  Write-Output "wrote $name"
}
