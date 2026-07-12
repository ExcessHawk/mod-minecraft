package com.mythicalswords.enchantments;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;

/**
 * Monkey's Step (chinese) — Sun Wukong's restless footwork: each strike
 * grants a burst of Speed (amplifier scales with level).
 */
public class MonkeyStepEnchantment extends ForgeExclusiveEnchantment {

    public MonkeyStepEnchantment() {
        super(Rarity.RARE, 2);
    }

    @Override
    public void onTargetDamaged(LivingEntity user, Entity target, int level) {
        if (user.getWorld().isClient) return;
        user.addStatusEffect(new StatusEffectInstance(
                StatusEffects.SPEED, 60, level - 1, false, false, true));
    }
}
