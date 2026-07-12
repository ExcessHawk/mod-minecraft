package com.mythicalswords.systems;

import com.mythicalswords.weapons.MythicalWeaponItem;
import com.mythicalswords.weapons.WeaponTier;
import net.minecraft.item.ItemStack;

/**
 * Handles weapon leveling logic, XP calculation, and damage bonuses.
 * Implements a 50-level progression system with exponential XP requirements.
 */
public class WeaponLevelingSystem {

    public static final int MAX_LEVEL = 50;

    /**
     * Calculate damage bonus based on weapon tier and level
     * 
     * @param weapon The weapon item
     * @param level  Current level
     * @return Damage bonus
     */
    public static float getDamageBonus(MythicalWeaponItem weapon, int level) {
        WeaponTier tier = weapon.getTier();
        return tier.getDamagePerLevel() * level;
    }

    /**
     * Calculate XP needed for the next level based on current level
     * Uses a three-phase exponential curve
     * 
     * @param currentLevel Current weapon level
     * @return XP needed to reach next level
     */
    public static int getXpForNextLevel(int currentLevel) {
        if (currentLevel >= MAX_LEVEL)
            return Integer.MAX_VALUE;

        if (currentLevel < 15) {
            // Phase 1: Fast progression (Levels 1-15)
            // Formula: 50 * (1.2^level)
            return (int) (50 * Math.pow(1.2, currentLevel));
        } else if (currentLevel < 35) {
            // Phase 2: Moderate progression (Levels 16-35)
            // Formula: 400 * (1.12^(level-15))
            return (int) (400 * Math.pow(1.12, currentLevel - 15));
        } else {
            // Phase 3: Slow endgame progression (Levels 36-50)
            // Formula: 2000 * (1.08^(level-35))
            return (int) (2000 * Math.pow(1.08, currentLevel - 35));
        }
    }

    /**
     * Add experience to a weapon and handle level ups
     * 
     * @param stack The weapon ItemStack
     * @param xp    Amount of XP to add
     * @return True if the weapon leveled up
     */
    public static boolean addExperience(ItemStack stack, int xp) {
        if (!(stack.getItem() instanceof MythicalWeaponItem weapon))
            return false;

        int currentLevel = weapon.getLevel(stack);
        if (currentLevel >= MAX_LEVEL)
            return false;

        int currentXP = weapon.getXP(stack);
        int newXP = currentXP + xp;
        weapon.setXP(stack, newXP);

        boolean leveledUp = false;

        // Handle multiple level ups if enough XP is gained
        while (tryLevelUp(stack)) {
            leveledUp = true;
        }

        return leveledUp;
    }

    /**
     * Attempt to level up the weapon if it has enough XP
     * 
     * @param stack The weapon ItemStack
     * @return True if leveled up
     */
    private static boolean tryLevelUp(ItemStack stack) {
        MythicalWeaponItem weapon = (MythicalWeaponItem) stack.getItem();
        int currentLevel = weapon.getLevel(stack);
        int currentXP = weapon.getXP(stack);

        if (currentLevel >= MAX_LEVEL)
            return false;

        int xpNeeded = getXpForNextLevel(currentLevel);
        if (currentXP >= xpNeeded) {
            weapon.setLevel(stack, currentLevel + 1);
            weapon.setXP(stack, currentXP - xpNeeded); // Carry over excess XP
            return true;
        }
        return false;
    }
}
