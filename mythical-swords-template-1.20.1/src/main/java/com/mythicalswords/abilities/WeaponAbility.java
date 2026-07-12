package com.mythicalswords.abilities;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

/**
 * Interface for weapon special abilities
 * Each mythical weapon can have a unique ability that can be activated
 */
public interface WeaponAbility {
    
    /**
     * Activate the weapon ability
     * 
     * @param world The world where the ability is activated
     * @param player The player activating the ability
     * @param weapon The weapon item stack
     * @return true if the ability was successfully activated
     */
    boolean activate(World world, PlayerEntity player, ItemStack weapon);
    
    /**
     * Get the cooldown time in ticks for this ability
     * 
     * @return cooldown in ticks (20 ticks = 1 second)
     */
    int getCooldownTicks();
    
    /**
     * Check if the ability can be used
     * 
     * @param world The world
     * @param player The player
     * @param weapon The weapon item stack
     * @return true if the ability can be used
     */
    default boolean canUse(World world, PlayerEntity player, ItemStack weapon) {
        return !player.getItemCooldownManager().isCoolingDown(weapon.getItem());
    }
    
    /**
     * Get the name of this ability
     * 
     * @return ability name
     */
    String getName();
}
