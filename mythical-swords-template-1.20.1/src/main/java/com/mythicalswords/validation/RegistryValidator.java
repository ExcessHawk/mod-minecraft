package com.mythicalswords.validation;

import com.mythicalswords.MythicalSwords;
import com.mythicalswords.core.ModBlocks;
import com.mythicalswords.core.ModEntities;
import com.mythicalswords.core.ModItems;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

/**
 * Validates that all mod content is properly registered
 * Runs on mod initialization to catch registration issues early
 */
public class RegistryValidator {
    
    private static int errors = 0;
    private static int warnings = 0;
    
    /**
     * Run all validation checks
     */
    public static void validate() {
        MythicalSwords.LOGGER.info("Running registry validation...");
        
        errors = 0;
        warnings = 0;
        
        validateItems();
        validateBlocks();
        validateEntities();
        
        if (errors > 0) {
            MythicalSwords.LOGGER.error("Registry validation FAILED with " + errors + " errors and " + warnings + " warnings!");
        } else if (warnings > 0) {
            MythicalSwords.LOGGER.warn("Registry validation completed with " + warnings + " warnings");
        } else {
            MythicalSwords.LOGGER.info("Registry validation PASSED - All content registered successfully!");
        }
    }
    
    /**
     * Validate all items are registered
     */
    private static void validateItems() {
        MythicalSwords.LOGGER.info("Validating items...");
        
        // Core materials
        validateItem("mythril_ingot", "Mythril Ingot");
        validateItem("northsteel_ingot", "Northsteel Ingot");
        validateItem("sun_blessed_alloy", "Sun-Blessed Alloy");
        validateItem("dragon_fang_fragment", "Dragon Fang Fragment");
        
        // Weapons
        validateItem("gram", "Gram");
        validateItem("excalibur", "Excalibur");
        validateItem("gungnir", "Gungnir");
        validateItem("laevateinn", "Laevateinn");
        
        // Block items
        validateItem("mythril_ore", "Mythril Ore (Item)");
        validateItem("northsteel_ore", "Northsteel Ore (Item)");
        validateItem("boss_altar", "Boss Altar (Item)");
        
        MythicalSwords.LOGGER.info("Item validation complete");
    }
    
    /**
     * Validate all blocks are registered
     */
    private static void validateBlocks() {
        MythicalSwords.LOGGER.info("Validating blocks...");
        
        validateBlock("mythril_ore", "Mythril Ore");
        validateBlock("northsteel_ore", "Northsteel Ore");
        validateBlock("boss_altar", "Boss Altar");
        
        MythicalSwords.LOGGER.info("Block validation complete");
    }
    
    /**
     * Validate all entities are registered
     */
    private static void validateEntities() {
        MythicalSwords.LOGGER.info("Validating entities...");
        
        validateEntity("rey_arturo", "Rey Arturo Boss");
        
        MythicalSwords.LOGGER.info("Entity validation complete");
    }
    
    /**
     * Validate a single item is registered
     */
    private static void validateItem(String id, String name) {
        Identifier identifier = new Identifier(MythicalSwords.MOD_ID, id);
        
        if (!Registries.ITEM.containsId(identifier)) {
            MythicalSwords.LOGGER.error("MISSING ITEM: " + name + " (" + id + ")");
            errors++;
        } else {
            var item = Registries.ITEM.get(identifier);
            if (item == null) {
                MythicalSwords.LOGGER.error("NULL ITEM: " + name + " (" + id + ")");
                errors++;
            } else {
                MythicalSwords.LOGGER.debug("✓ Item registered: " + name);
            }
        }
    }
    
    /**
     * Validate a single block is registered
     */
    private static void validateBlock(String id, String name) {
        Identifier identifier = new Identifier(MythicalSwords.MOD_ID, id);
        
        if (!Registries.BLOCK.containsId(identifier)) {
            MythicalSwords.LOGGER.error("MISSING BLOCK: " + name + " (" + id + ")");
            errors++;
        } else {
            var block = Registries.BLOCK.get(identifier);
            if (block == null) {
                MythicalSwords.LOGGER.error("NULL BLOCK: " + name + " (" + id + ")");
                errors++;
            } else {
                MythicalSwords.LOGGER.debug("✓ Block registered: " + name);
            }
        }
    }
    
    /**
     * Validate a single entity is registered
     */
    private static void validateEntity(String id, String name) {
        Identifier identifier = new Identifier(MythicalSwords.MOD_ID, id);
        
        if (!Registries.ENTITY_TYPE.containsId(identifier)) {
            MythicalSwords.LOGGER.error("MISSING ENTITY: " + name + " (" + id + ")");
            errors++;
        } else {
            var entityType = Registries.ENTITY_TYPE.get(identifier);
            if (entityType == null) {
                MythicalSwords.LOGGER.error("NULL ENTITY: " + name + " (" + id + ")");
                errors++;
            } else {
                MythicalSwords.LOGGER.debug("✓ Entity registered: " + name);
            }
        }
    }
    
    /**
     * Get the number of validation errors
     */
    public static int getErrors() {
        return errors;
    }
    
    /**
     * Get the number of validation warnings
     */
    public static int getWarnings() {
        return warnings;
    }
}
