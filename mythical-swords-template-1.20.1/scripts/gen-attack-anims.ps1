# Injects attack_melee / attack_special into every boss .animation.json,
# keeping existing animations (idle etc.). Three rigs: humanoid, anubis
# (no arms -> lunge/bite), serpent (quetzalcoatl -> whip/coil).
$DIR = "C:\Users\alfredo\Desktop\mod\mod-minecraft\mythical-swords-template-1.20.1\src\main\resources\assets\mythicalswords\animations\entity"

function Rot($pairs) {
  $o = [ordered]@{}
  foreach ($p in $pairs) { $o[$p[0]] = $p[1] }
  return @{ rotation = $o }
}

$humanoidMelee = @{
  loop = $false; animation_length = 0.5
  bones = [ordered]@{
    right_arm = Rot @(@("0.0",@(0,0,0)), @("0.15",@(-130,0,-10)), @("0.3",@(-50,0,10)), @("0.5",@(0,0,0)))
    left_arm  = Rot @(@("0.0",@(0,0,0)), @("0.2",@(20,0,10)),   @("0.5",@(0,0,0)))
    body      = Rot @(@("0.0",@(0,0,0)), @("0.15",@(0,-20,0)),  @("0.3",@(8,12,0)),  @("0.5",@(0,0,0)))
    head      = Rot @(@("0.0",@(0,0,0)), @("0.3",@(10,0,0)),    @("0.5",@(0,0,0)))
  }
}
$humanoidSpecial = @{
  loop = $false; animation_length = 0.8
  bones = [ordered]@{
    right_arm = Rot @(@("0.0",@(0,0,0)), @("0.3",@(-170,0,-15)), @("0.5",@(-40,0,0)), @("0.8",@(0,0,0)))
    left_arm  = Rot @(@("0.0",@(0,0,0)), @("0.3",@(-170,0,15)),  @("0.5",@(-40,0,0)), @("0.8",@(0,0,0)))
    body      = Rot @(@("0.0",@(0,0,0)), @("0.3",@(-10,0,0)),    @("0.5",@(15,0,0)),  @("0.8",@(0,0,0)))
    head      = Rot @(@("0.0",@(0,0,0)), @("0.3",@(-15,0,0)),    @("0.5",@(10,0,0)),  @("0.8",@(0,0,0)))
  }
}
$anubisMelee = @{
  loop = $false; animation_length = 0.5
  bones = [ordered]@{
    body = Rot @(@("0.0",@(0,0,0)), @("0.15",@(-15,-10,0)), @("0.3",@(12,5,0)),  @("0.5",@(0,0,0)))
    head = Rot @(@("0.0",@(0,0,0)), @("0.2",@(-20,0,0)),    @("0.35",@(15,0,0)), @("0.5",@(0,0,0)))
  }
}
$anubisSpecial = @{
  loop = $false; animation_length = 0.8
  bones = [ordered]@{
    body = Rot @(@("0.0",@(0,0,0)), @("0.3",@(-25,0,0)), @("0.5",@(18,0,0)), @("0.8",@(0,0,0)))
    head = Rot @(@("0.0",@(0,0,0)), @("0.3",@(-25,0,0)), @("0.5",@(12,0,0)), @("0.8",@(0,0,0)))
  }
}
$serpentMelee = @{
  loop = $false; animation_length = 0.6
  bones = [ordered]@{
    body = Rot @(@("0.0",@(0,0,0)), @("0.2",@(-18,0,0)), @("0.35",@(10,0,0)), @("0.6",@(0,0,0)))
    s1   = Rot @(@("0.0",@(0,0,0)), @("0.25",@(-14,0,0)), @("0.4",@(8,0,0)),  @("0.6",@(0,0,0)))
    s2   = Rot @(@("0.0",@(0,0,0)), @("0.3",@(-10,0,0)),  @("0.45",@(6,0,0)), @("0.6",@(0,0,0)))
    s3   = Rot @(@("0.0",@(0,0,0)), @("0.35",@(-8,0,0)),  @("0.5",@(5,0,0)),  @("0.6",@(0,0,0)))
    s4   = Rot @(@("0.0",@(0,0,0)), @("0.4",@(-6,0,0)),   @("0.55",@(4,0,0)), @("0.6",@(0,0,0)))
    tail = Rot @(@("0.0",@(0,0,0)), @("0.45",@(-10,0,0)), @("0.6",@(0,0,0)))
  }
}
$serpentSpecial = @{
  loop = $false; animation_length = 0.9
  bones = [ordered]@{
    body = Rot @(@("0.0",@(0,0,0)), @("0.25",@(0,25,0)),  @("0.55",@(0,-25,0)), @("0.9",@(0,0,0)))
    s1   = Rot @(@("0.0",@(0,0,0)), @("0.3",@(0,20,0)),   @("0.6",@(0,-20,0)),  @("0.9",@(0,0,0)))
    s2   = Rot @(@("0.0",@(0,0,0)), @("0.35",@(0,15,0)),  @("0.65",@(0,-15,0)), @("0.9",@(0,0,0)))
    s3   = Rot @(@("0.0",@(0,0,0)), @("0.4",@(0,12,0)),   @("0.7",@(0,-12,0)),  @("0.9",@(0,0,0)))
    s4   = Rot @(@("0.0",@(0,0,0)), @("0.45",@(0,10,0)),  @("0.75",@(0,-10,0)), @("0.9",@(0,0,0)))
    tail = Rot @(@("0.0",@(0,0,0)), @("0.5",@(0,15,0)),   @("0.8",@(0,-15,0)),  @("0.9",@(0,0,0)))
  }
}

$rigs = @{
  "anubis"               = @($anubisMelee, $anubisSpecial)
  "quetzalcoatl"         = @($serpentMelee, $serpentSpecial)
  "atenea"               = @($humanoidMelee, $humanoidSpecial)
  "izanagi"              = @($humanoidMelee, $humanoidSpecial)
  "legendary_blacksmith" = @($humanoidMelee, $humanoidSpecial)
  "loki"                 = @($humanoidMelee, $humanoidSpecial)
  "odin"                 = @($humanoidMelee, $humanoidSpecial)
  "oni_oscuro"           = @($humanoidMelee, $humanoidSpecial)
  "ra"                   = @($humanoidMelee, $humanoidSpecial)
  "rey_arturo"           = @($humanoidMelee, $humanoidSpecial)
  "sun_wukong"           = @($humanoidMelee, $humanoidSpecial)
  "susanoo"              = @($humanoidMelee, $humanoidSpecial)
}

foreach ($name in $rigs.Keys) {
  $path = Join-Path $DIR "$name.animation.json"
  if (-not (Test-Path $path)) { Write-Output "SKIP $name (no file)"; continue }
  $json = Get-Content $path -Raw -Encoding utf8 | ConvertFrom-Json
  $anims = $json.animations
  $melee = $rigs[$name][0]; $special = $rigs[$name][1]
  $anims | Add-Member -NotePropertyName "attack_melee" -NotePropertyValue $melee -Force
  $anims | Add-Member -NotePropertyName "attack_special" -NotePropertyValue $special -Force
  $json | ConvertTo-Json -Depth 12 | Out-File $path -Encoding utf8
  Write-Output "ok $name"
}
