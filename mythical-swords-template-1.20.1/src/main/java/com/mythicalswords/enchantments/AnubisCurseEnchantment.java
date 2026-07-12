package com.mythicalswords.enchantments;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;

/**
 * Curse of Anubis (egyptian) — wounds fester with the judgment of the dead:
 * applies Wither to the target (duration and amplifier scale with level).
 */
public class AnubisCurseEnchantment extends ForgeExclusiveEnchantment {

    public AnubisCurseEnchantment() {
        super(Rarity.VERY_RARE, 2);
    }

    @Override
    public void onTargetDamaged(LivingEntity user, Entity target, int level) {
        if (user.getWorld().isClient) return;
        if (!(target instanceof LivingEntity livingTarget)) return;
        livingTarget.addStatusEffect(new StatusEffectInstance(
                StatusEffects.WITHER, 60 * level, level - 1), user);
    }
}
