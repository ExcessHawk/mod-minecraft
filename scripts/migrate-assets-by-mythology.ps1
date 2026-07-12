# Script de Migracion de Assets por Mitologia
# Mythical Swords Mod - Reorganizacion de Estructura

Write-Host "========================================" -ForegroundColor Cyan
Write-Host "  MIGRACION DE ASSETS POR MITOLOGIA" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

$projectRoot = "mythical-swords-template-1.20.1"
$assetsPath = "$projectRoot/src/main/resources/assets/mythicalswords"
$texturesPath = "$assetsPath/textures"
$modelsPath = "$assetsPath/models"

# Crear backup
Write-Host "[1/6] Creando backup..." -ForegroundColor Yellow
$backupPath = "$projectRoot/backup_before_migration_$(Get-Date -Format 'yyyyMMdd_HHmmss')"
Copy-Item -Path "$projectRoot/src" -Destination $backupPath -Recurse -Force
Write-Host "  [OK] Backup creado en: $backupPath" -ForegroundColor Green
Write-Host ""

# Definir estructura de mitologias
$mythologies = @{
    "arthurian" = @{
        "weapons" = @("excalibur", "caliburn", "clarent")
        "items" = @("camelot_compass")
        "materials" = @()
    }
    "norse" = @{
        "weapons" = @("gram", "skofnung", "hofund", "gungnir", "laevateinn")
        "ores" = @("northsteel_ore")
        "ingots" = @("northsteel_ingot")
        "raw" = @("raw_northsteel")
        "materials" = @("spiritbound_leather", "frozen_soul_crystal", "rainbow_bridge_fragment")
    }
    "greek" = @{
        "weapons" = @("harpe", "xiphos_sagrado", "nike_blade", "aegis_edge")
        "ores" = @("sacred_iron_ore")
        "ingots" = @("sacred_iron_ingot")
        "raw" = @("raw_sacred_iron")
        "materials" = @("shard_of_divinity", "feather_of_victory", "bronce_bendito")
    }
    "japanese" = @{
        "weapons" = @("kusanagi_no_tsurugi", "muramasa", "totsuka_no_tsurugi", "masamune", "naginata_bishamon")
        "ores" = @("tamahagane_ore")
        "ingots" = @("tamahagane_ingot", "acero_tamahagane_ingot")
        "raw" = @("raw_tamahagane")
        "materials" = @("gem_of_bishamon", "soul_swordsmith", "sacred_water_of_amaterasu", "mango_largo_japones")
    }
    "mesoamerican" = @{
        "weapons" = @("xiuhcoatl")
        "ores" = @("obsidiana_ritual_ore")
        "raw" = @("raw_obsidiana_ritual")
        "materials" = @("filo_de_pluma_de_quetzal", "palo_ritual", "obsidiana_ritual_shard")
    }
    "chinese" = @{
        "weapons" = @()
        "ores" = @("jade_imperial_ore")
        "ingots" = @("jade_imperial_ingot")
        "raw" = @("raw_jade_imperial")
        "materials" = @("dust_of_longevity", "essence_of_righteousness", "soul_fragment", "lovers_bond_token", "moonstone_shard")
    }
    "general" = @{
        "ores" = @("mythril_ore")
        "ingots" = @("mythril_ingot")
        "raw" = @("raw_mythril")
        "materials" = @("sun_blessed_alloy", "dragon_fang_fragment")
        "special" = @("boss_altar")
        "test" = @("test_item")
    }
}

# Funcion para crear directorios
function Create-Directory {
    param($path)
    if (-not (Test-Path $path)) {
        New-Item -ItemType Directory -Path $path -Force | Out-Null
    }
}

# Funcion para mover archivo
function Move-Asset {
    param($source, $destination)
    if (Test-Path $source) {
        $destDir = Split-Path $destination -Parent
        Create-Directory $destDir
        Move-Item -Path $source -Destination $destination -Force
        return $true
    }
    return $false
}

# Funcion para actualizar referencias en JSON
function Update-JsonReferences {
    param($jsonPath, $oldPath, $newPath)
    
    if (Test-Path $jsonPath) {
        $content = Get-Content $jsonPath -Raw
        $updated = $content -replace [regex]::Escape($oldPath), $newPath
        Set-Content -Path $jsonPath -Value $updated -NoNewline
    }
}

Write-Host "[2/6] Creando nueva estructura de carpetas..." -ForegroundColor Yellow

# Crear estructura para cada mitologia
foreach ($mythology in $mythologies.Keys) {
    Write-Host "  Creando estructura para: $mythology" -ForegroundColor Cyan
    
    # Texturas
    Create-Directory "$texturesPath/item/$mythology/weapons"
    Create-Directory "$texturesPath/item/$mythology/materials"
    Create-Directory "$texturesPath/item/$mythology/items"
    Create-Directory "$texturesPath/block/$mythology/ores"
    
    # Modelos
    Create-Directory "$modelsPath/item/$mythology/weapons"
    Create-Directory "$modelsPath/item/$mythology/materials"
    Create-Directory "$modelsPath/item/$mythology/items"
    Create-Directory "$modelsPath/block/$mythology/ores"
}

Write-Host "  [OK] Estructura creada" -ForegroundColor Green
Write-Host ""

Write-Host "[3/6] Moviendo texturas de items..." -ForegroundColor Yellow
$movedTextures = 0

foreach ($mythology in $mythologies.Keys) {
    $items = $mythologies[$mythology]
    
    # Mover armas
    if ($items.ContainsKey("weapons")) {
        foreach ($weapon in $items["weapons"]) {
            $source = "$texturesPath/item/$weapon.png"
            $dest = "$texturesPath/item/$mythology/weapons/$weapon.png"
            if (Move-Asset $source $dest) {
                $movedTextures++
                Write-Host "  [OK] $weapon.png -> $mythology/weapons/" -ForegroundColor Gray
            }
        }
    }
    
    # Mover lingotes
    if ($items.ContainsKey("ingots")) {
        foreach ($ingot in $items["ingots"]) {
            $source = "$texturesPath/item/$ingot.png"
            $dest = "$texturesPath/item/$mythology/materials/$ingot.png"
            if (Move-Asset $source $dest) {
                $movedTextures++
                Write-Host "  [OK] $ingot.png -> $mythology/materials/" -ForegroundColor Gray
            }
        }
    }
    
    # Mover raw materials
    if ($items.ContainsKey("raw")) {
        foreach ($raw in $items["raw"]) {
            $source = "$texturesPath/item/$raw.png"
            $dest = "$texturesPath/item/$mythology/materials/$raw.png"
            if (Move-Asset $source $dest) {
                $movedTextures++
                Write-Host "  [OK] $raw.png -> $mythology/materials/" -ForegroundColor Gray
            }
        }
    }
    
    # Mover materiales especiales
    if ($items.ContainsKey("materials")) {
        foreach ($material in $items["materials"]) {
            $source = "$texturesPath/item/$material.png"
            $dest = "$texturesPath/item/$mythology/materials/$material.png"
            if (Move-Asset $source $dest) {
                $movedTextures++
                Write-Host "  [OK] $material.png -> $mythology/materials/" -ForegroundColor Gray
            }
        }
    }
    
    # Mover items especiales
    if ($items.ContainsKey("items")) {
        foreach ($item in $items["items"]) {
            $source = "$texturesPath/item/$item.png"
            $dest = "$texturesPath/item/$mythology/items/$item.png"
            if (Move-Asset $source $dest) {
                $movedTextures++
                Write-Host "  [OK] $item.png -> $mythology/items/" -ForegroundColor Gray
            }
        }
    }
    
    # Mover test items
    if ($items.ContainsKey("test")) {
        foreach ($test in $items["test"]) {
            $source = "$texturesPath/item/$test.png"
            $dest = "$texturesPath/item/$mythology/$test.png"
            if (Move-Asset $source $dest) {
                $movedTextures++
                Write-Host "  [OK] $test.png -> $mythology/" -ForegroundColor Gray
            }
        }
    }
}

Write-Host "  [OK] $movedTextures texturas de items movidas" -ForegroundColor Green
Write-Host ""

Write-Host "[4/6] Moviendo texturas de bloques..." -ForegroundColor Yellow
$movedBlocks = 0

foreach ($mythology in $mythologies.Keys) {
    $items = $mythologies[$mythology]
    
    # Mover ores
    if ($items.ContainsKey("ores")) {
        foreach ($ore in $items["ores"]) {
            $source = "$texturesPath/block/$ore.png"
            $dest = "$texturesPath/block/$mythology/ores/$ore.png"
            if (Move-Asset $source $dest) {
                $movedBlocks++
                Write-Host "  [OK] $ore.png -> $mythology/ores/" -ForegroundColor Gray
            }
        }
    }
    
    # Mover bloques especiales
    if ($items.ContainsKey("special")) {
        foreach ($special in $items["special"]) {
            $source = "$texturesPath/block/$special.png"
            $dest = "$texturesPath/block/$mythology/$special.png"
            if (Move-Asset $source $dest) {
                $movedBlocks++
                Write-Host "  [OK] $special.png -> $mythology/" -ForegroundColor Gray
            }
        }
    }
}

Write-Host "  [OK] $movedBlocks texturas de bloques movidas" -ForegroundColor Green
Write-Host ""

Write-Host "[5/6] Moviendo y actualizando modelos JSON..." -ForegroundColor Yellow
$updatedModels = 0

foreach ($mythology in $mythologies.Keys) {
    $items = $mythologies[$mythology]
    
    # Procesar armas
    if ($items.ContainsKey("weapons")) {
        foreach ($weapon in $items["weapons"]) {
            $sourceModel = "$modelsPath/item/$weapon.json"
            $destModel = "$modelsPath/item/$mythology/weapons/$weapon.json"
            
            if (Test-Path $sourceModel) {
                # Actualizar referencia de textura
                Update-JsonReferences $sourceModel "mythicalswords:item/$weapon" "mythicalswords:item/$mythology/weapons/$weapon"
                
                # Mover modelo
                if (Move-Asset $sourceModel $destModel) {
                    $updatedModels++
                    Write-Host "  [OK] $weapon.json -> $mythology/weapons/" -ForegroundColor Gray
                }
            }
        }
    }
    
    # Procesar materiales (ingots, raw, materials)
    $materialTypes = @("ingots", "raw", "materials")
    foreach ($type in $materialTypes) {
        if ($items.ContainsKey($type)) {
            foreach ($material in $items[$type]) {
                $sourceModel = "$modelsPath/item/$material.json"
                $destModel = "$modelsPath/item/$mythology/materials/$material.json"
                
                if (Test-Path $sourceModel) {
                    Update-JsonReferences $sourceModel "mythicalswords:item/$material" "mythicalswords:item/$mythology/materials/$material"
                    
                    if (Move-Asset $sourceModel $destModel) {
                        $updatedModels++
                        Write-Host "  [OK] $material.json -> $mythology/materials/" -ForegroundColor Gray
                    }
                }
            }
        }
    }
    
    # Procesar items especiales
    if ($items.ContainsKey("items")) {
        foreach ($item in $items["items"]) {
            $sourceModel = "$modelsPath/item/$item.json"
            $destModel = "$modelsPath/item/$mythology/items/$item.json"
            
            if (Test-Path $sourceModel) {
                Update-JsonReferences $sourceModel "mythicalswords:item/$item" "mythicalswords:item/$mythology/items/$item"
                
                if (Move-Asset $sourceModel $destModel) {
                    $updatedModels++
                    Write-Host "  [OK] $item.json -> $mythology/items/" -ForegroundColor Gray
                }
            }
        }
    }
    
    # Procesar ores (bloques)
    if ($items.ContainsKey("ores")) {
        foreach ($ore in $items["ores"]) {
            $sourceModel = "$modelsPath/block/$ore.json"
            $destModel = "$modelsPath/block/$mythology/ores/$ore.json"
            
            if (Test-Path $sourceModel) {
                Update-JsonReferences $sourceModel "mythicalswords:block/$ore" "mythicalswords:block/$mythology/ores/$ore"
                
                if (Move-Asset $sourceModel $destModel) {
                    $updatedModels++
                    Write-Host "  [OK] $ore.json (block) -> $mythology/ores/" -ForegroundColor Gray
                }
            }
            
            # Tambien mover el modelo de item del ore
            $sourceItemModel = "$modelsPath/item/$ore.json"
            $destItemModel = "$modelsPath/item/$mythology/materials/$ore.json"
            
            if (Test-Path $sourceItemModel) {
                Update-JsonReferences $sourceItemModel "mythicalswords:block/$ore" "mythicalswords:block/$mythology/ores/$ore"
                
                if (Move-Asset $sourceItemModel $destItemModel) {
                    $updatedModels++
                    Write-Host "  [OK] $ore.json (item) -> $mythology/materials/" -ForegroundColor Gray
                }
            }
        }
    }
    
    # Procesar test items
    if ($items.ContainsKey("test")) {
        foreach ($test in $items["test"]) {
            $sourceModel = "$modelsPath/item/$test.json"
            $destModel = "$modelsPath/item/$mythology/$test.json"
            
            if (Test-Path $sourceModel) {
                Update-JsonReferences $sourceModel "mythicalswords:item/$test" "mythicalswords:item/$mythology/$test"
                
                if (Move-Asset $sourceModel $destModel) {
                    $updatedModels++
                    Write-Host "  [OK] $test.json -> $mythology/" -ForegroundColor Gray
                }
            }
        }
    }
    
    # Procesar bloques especiales
    if ($items.ContainsKey("special")) {
        foreach ($special in $items["special"]) {
            $sourceModel = "$modelsPath/block/$special.json"
            $destModel = "$modelsPath/block/$mythology/$special.json"
            
            if (Test-Path $sourceModel) {
                Update-JsonReferences $sourceModel "mythicalswords:block/$special" "mythicalswords:block/$mythology/$special"
                
                if (Move-Asset $sourceModel $destModel) {
                    $updatedModels++
                    Write-Host "  [OK] $special.json -> $mythology/" -ForegroundColor Gray
                }
            }
        }
    }
}

Write-Host "  [OK] $updatedModels modelos movidos y actualizados" -ForegroundColor Green
Write-Host ""

Write-Host "[6/6] Actualizando blockstates..." -ForegroundColor Yellow
$blockstatesPath = "$assetsPath/blockstates"
$updatedBlockstates = 0

foreach ($mythology in $mythologies.Keys) {
    $items = $mythologies[$mythology]
    
    if ($items.ContainsKey("ores")) {
        foreach ($ore in $items["ores"]) {
            $blockstatePath = "$blockstatesPath/$ore.json"
            if (Test-Path $blockstatePath) {
                Update-JsonReferences $blockstatePath "mythicalswords:block/$ore" "mythicalswords:block/$mythology/ores/$ore"
                $updatedBlockstates++
                Write-Host "  [OK] $ore.json actualizado" -ForegroundColor Gray
            }
        }
    }
    
    if ($items.ContainsKey("special")) {
        foreach ($special in $items["special"]) {
            $blockstatePath = "$blockstatesPath/$special.json"
            if (Test-Path $blockstatePath) {
                Update-JsonReferences $blockstatePath "mythicalswords:block/$special" "mythicalswords:block/$mythology/$special"
                $updatedBlockstates++
                Write-Host "  [OK] $special.json actualizado" -ForegroundColor Gray
            }
        }
    }
}

Write-Host "  [OK] $updatedBlockstates blockstates actualizados" -ForegroundColor Green
Write-Host ""

# Resumen
Write-Host "========================================" -ForegroundColor Cyan
Write-Host "  MIGRACION COMPLETADA" -ForegroundColor Green
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""
Write-Host "Resumen:" -ForegroundColor Yellow
Write-Host "  * Texturas movidas: $movedTextures items + $movedBlocks bloques" -ForegroundColor White
Write-Host "  * Modelos actualizados: $updatedModels" -ForegroundColor White
Write-Host "  * Blockstates actualizados: $updatedBlockstates" -ForegroundColor White
Write-Host "  * Backup guardado en: $backupPath" -ForegroundColor White
Write-Host ""
Write-Host "Proximos pasos:" -ForegroundColor Yellow
Write-Host "  1. Compilar el mod: cd $projectRoot && ./gradlew build" -ForegroundColor Cyan
Write-Host "  2. Verificar que no hay errores" -ForegroundColor Cyan
Write-Host "  3. Probar en el juego" -ForegroundColor Cyan
Write-Host "  4. Si todo funciona, eliminar el backup" -ForegroundColor Cyan
Write-Host ""
Write-Host "Si algo sale mal, restaura desde: $backupPath" -ForegroundColor Red
Write-Host ""
