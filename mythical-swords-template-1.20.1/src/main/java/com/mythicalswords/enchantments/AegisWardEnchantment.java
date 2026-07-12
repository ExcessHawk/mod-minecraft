package com.mythicalswords.enchantments;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;

/**
 * Aegis Ward (greek) — every strike raises Athena's shield: brief Resistance
 * for the wielder (amplifier scales with level).
 */
public class AegisWardEnchantment extends ForgeExclusiveEnchantment {

    public AegisWardEnchantment() {
        super(Rarity.RARE, 2);
    }

    @Override
    public void onTargetDamaged(LivingEntity user, Entity target, int level) {
        if (user.getWorld().isClient) return;
        user.addStatusEffect(new StatusEffectInstance(
                StatusEffects.RESISTANCE, 40, level - 1, false, false, true));
    }
}
