package com.mythicalswords.weapons;

/**
 * Represents the tier/rarity of a mythical weapon.
 * Each tier has different durability and damage multiplier values.
 */
public enum WeaponTier {
    COMMON(1000, 1.0f, 0.06f),
    RARE(1500, 1.2f, 0.10f),
    EPIC(2000, 1.5f, 0.15f),
    LEGENDARY(5000, 2.0f, 0.20f);

    private final int durability;
    private final float damageMultiplier;
    private final float damagePerLevel;

    /**
     * Constructor for WeaponTier
     * 
     * @param durability       Maximum durability for weapons of this tier
     * @param damageMultiplier Damage multiplier for weapons of this tier
     * @param damagePerLevel   Damage bonus gained per level
     */
    WeaponTier(int durability, float damageMultiplier, float damagePerLevel) {
        this.durability = durability;
        this.damageMultiplier = damageMultiplier;
        this.damagePerLevel = damagePerLevel;
    }

    /**
     * Get the durability value for this tier
     * 
     * @return durability value
     */
    public int getDurability() {
        return durability;
    }

    /**
     * Get the damage multiplier for this tier
     * 
     * @return damage multiplier
     */
    public float getDamageMultiplier() {
        return damageMultiplier;
    }

    /**
     * Get the damage bonus per level for this tier
     * 
     * @return damage per level
     */
    public float getDamagePerLevel() {
        return damagePerLevel;
    }
}
