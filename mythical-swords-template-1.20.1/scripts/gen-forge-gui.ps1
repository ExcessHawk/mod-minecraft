Add-Type -AssemblyName System.Drawing
# Mythical Forge GUI texture (176x196 on a 256x256 sheet).
# Taller forge area so labels/button don't overlap:
#   mat1 (80,26) / weapon (44,48) / output (116,48) / mat2 (80,70)
#   Forge button zone at (118,88 50x16), inventory from y114, hotbar y172.
$DST = "C:\Users\alfredo\Desktop\mod\mod-minecraft\mythical-swords-template-1.20.1\src\main\resources\assets\mythicalswords\textures\gui"
if (-not (Test-Path $DST)) { New-Item -ItemType Directory -Force $DST | Out-Null }

function HexC($h) {
  $h = $h.TrimStart('#')
  [System.Drawing.Color]::FromArgb(255,
    [Convert]::ToInt32($h.Substring(0,2),16),
    [Convert]::ToInt32($h.Substring(2,2),16),
    [Convert]::ToInt32($h.Substring(4,2),16))
}

$clear    = [System.Drawing.Color]::FromArgb(0,0,0,0)
$panel    = HexC "2b1c08"
$panelHi  = HexC "5a3d14"
$panelDk  = HexC "140d04"
$black    = HexC "000000"
$titleBar = HexC "462e0c"
$slotBg   = HexC "171008"
$slotIn   = HexC "0a0704"
$grayBrd  = HexC "6f6455"
$goldBrd  = HexC "ffc933"
$goldDk   = HexC "8a6d14"
$greenBrd = HexC "33ff77"
$arrowC   = HexC "ffdd00"
$ember    = HexC "ee7a1f"

$GW = 176; $GH = 196
$bmp = New-Object System.Drawing.Bitmap(256, 256)
for ($y = 0; $y -lt 256; $y++) { for ($x = 0; $x -lt 256; $x++) { $bmp.SetPixel($x, $y, $clear) } }

function FillRect($x, $y, $w, $h, $c) {
  for ($yy = $y; $yy -lt ($y + $h); $yy++) { for ($xx = $x; $xx -lt ($x + $w); $xx++) { $script:bmp.SetPixel($xx, $yy, $c) } }
}

# ===== Panel with bevel =====
FillRect 0 0 $GW $GH $panel
FillRect 0 0 $GW 1 $black; FillRect 0 ($GH-1) $GW 1 $black
FillRect 0 0 1 $GH $black; FillRect ($GW-1) 0 1 $GH $black
FillRect 1 1 ($GW-2) 2 $panelHi; FillRect 1 1 2 ($GH-2) $panelHi
FillRect 1 ($GH-3) ($GW-2) 2 $panelDk; FillRect ($GW-3) 1 2 ($GH-2) $panelDk
$bmp.SetPixel(0,0,$clear); $bmp.SetPixel(($GW-1),0,$clear); $bmp.SetPixel(0,($GH-1),$clear); $bmp.SetPixel(($GW-1),($GH-1),$clear)

# ===== Title bar =====
FillRect 3 3 ($GW-6) 13 $titleBar
FillRect 3 16 ($GW-6) 1 $panelDk
$bmp.SetPixel(5,9,$ember); $bmp.SetPixel(($GW-6),9,$ember)
$bmp.SetPixel(6,9,$goldBrd); $bmp.SetPixel(($GW-7),9,$goldBrd)

function Socket($sx, $sy, $brd, $thick) {
  $b = $thick
  FillRect ($sx - $b) ($sy - $b) (16 + 2*$b) (16 + 2*$b) $brd
  FillRect $sx $sy 16 16 $slotBg
  FillRect $sx $sy 16 1 $slotIn
  FillRect $sx $sy 1 16 $slotIn
}

# ===== Forge slots (diamond layout, roomy) =====
Socket 80 26 $grayBrd 2    # material 1 (top)
Socket 44 48 $goldBrd 2    # weapon (left)
Socket 116 48 $greenBrd 2  # output (right)
Socket 80 70 $grayBrd 2    # material 2 (bottom)

# gold studs on the weapon socket corners
$bmp.SetPixel(42,46,$goldDk); $bmp.SetPixel(61,46,$goldDk); $bmp.SetPixel(42,65,$goldDk); $bmp.SetPixel(61,65,$goldDk)

# ===== Arrow weapon -> output at y54 =====
FillRect 66 54 44 2 $arrowC
$bmp.SetPixel(108,52,$arrowC); $bmp.SetPixel(109,53,$arrowC)
$bmp.SetPixel(108,57,$arrowC); $bmp.SetPixel(109,56,$arrowC)

# ===== Button backplate zone (widget draws on top; subtle frame) =====
FillRect 117 87 52 18 $panelDk
FillRect 118 88 50 16 $titleBar

# ===== Separator above inventory =====
FillRect 3 108 ($GW-6) 1 $panelDk
FillRect 3 109 ($GW-6) 1 $panelHi

# ===== Player inventory (rows y114) + hotbar (y172) =====
for ($row = 0; $row -lt 3; $row++) {
  for ($col = 0; $col -lt 9; $col++) {
    Socket (8 + $col * 18) (114 + $row * 18) $grayBrd 1
  }
}
for ($col = 0; $col -lt 9; $col++) { Socket (8 + $col * 18) 172 $grayBrd 1 }

$bmp.Save((Join-Path $DST "mythical_forge.png"), [System.Drawing.Imaging.ImageFormat]::Png)
$bmp.Dispose()
Write-Output "wrote gui/mythical_forge.png (176x196)"
