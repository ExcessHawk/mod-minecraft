package com.mythicalswords.abilities;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Centralized cooldown tracking for weapon abilities
 * Manages cooldowns per player using UUID-based mapping
 */
public class CooldownManager {
    
    private static final CooldownManager INSTANCE = new CooldownManager();
    
    // Map of player UUID -> ability name -> cooldown end time
    private final Map<UUID, Map<String, Long>> cooldowns = new HashMap<>();
    
    private CooldownManager() {}
    
    public static CooldownManager getInstance() {
        return INSTANCE;
    }
    
    /**
     * Check if an ability is on cooldown for a player
     * 
     * @param playerUuid Player's UUID
     * @param abilityName Name of the ability
     * @return true if the ability is on cooldown
     */
    public boolean isOnCooldown(UUID playerUuid, String abilityName) {
        Map<String, Long> playerCooldowns = cooldowns.get(playerUuid);
        if (playerCooldowns == null) {
            return false;
        }
        
        Long endTime = playerCooldowns.get(abilityName);
        if (endTime == null) {
            return false;
        }
        
        long currentTime = System.currentTimeMillis();
        if (currentTime >= endTime) {
            // Cooldown expired, remove it
            playerCooldowns.remove(abilityName);
            return false;
        }
        
        return true;
    }
    
    /**
     * Set a cooldown for an ability
     * 
     * @param playerUuid Player's UUID
     * @param abilityName Name of the ability
     * @param cooldownTicks Cooldown duration in ticks (20 ticks = 1 second)
     */
    public void setCooldown(UUID playerUuid, String abilityName, int cooldownTicks) {
        Map<String, Long> playerCooldowns = cooldowns.computeIfAbsent(playerUuid, k -> new HashMap<>());
        long endTime = System.currentTimeMillis() + (cooldownTicks * 50); // 50ms per tick
        playerCooldowns.put(abilityName, endTime);
    }
    
    /**
     * Get remaining cooldown time in ticks
     * 
     * @param playerUuid Player's UUID
     * @param abilityName Name of the ability
     * @return remaining cooldown in ticks, or 0 if not on cooldown
     */
    public int getRemainingCooldown(UUID playerUuid, String abilityName) {
        Map<String, Long> playerCooldowns = cooldowns.get(playerUuid);
        if (playerCooldowns == null) {
            return 0;
        }
        
        Long endTime = playerCooldowns.get(abilityName);
        if (endTime == null) {
            return 0;
        }
        
        long currentTime = System.currentTimeMillis();
        if (currentTime >= endTime) {
            return 0;
        }
        
        long remainingMs = endTime - currentTime;
        return (int) (remainingMs / 50); // Convert ms to ticks
    }
    
    /**
     * Clear all cooldowns for a player (called when player logs out)
     * 
     * @param playerUuid Player's UUID
     */
    public void clearPlayerCooldowns(UUID playerUuid) {
        cooldowns.remove(playerUuid);
    }
    
    /**
     * Cleanup expired cooldowns for all players
     * Should be called periodically to prevent memory leaks
     */
    public void cleanup() {
        long currentTime = System.currentTimeMillis();
        cooldowns.values().forEach(playerCooldowns -> 
            playerCooldowns.entrySet().removeIf(entry -> currentTime >= entry.getValue())
        );
        cooldowns.entrySet().removeIf(entry -> entry.getValue().isEmpty());
    }
}
