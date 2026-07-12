package com.mythicalswords.enchantments;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;

/**
 * Blood Thirst (mesoamerican) — the sacrifice feeds the wielder: killing
 * blows restore health (1 heart per level).
 */
public class BloodThirstEnchantment extends ForgeExclusiveEnchantment {

    private static final float HEAL_PER_LEVEL = 2.0f;

    public BloodThirstEnchantment() {
        super(Rarity.RARE, 3);
    }

    @Override
    public void onTargetDamaged(LivingEntity user, Entity target, int level) {
        if (user.getWorld().isClient) return;
        if (!(target instanceof LivingEntity livingTarget)) return;

        if (livingTarget.isDead() || livingTarget.getHealth() <= 0.0f) {
            user.heal(HEAL_PER_LEVEL * level);
        }
    }
}
