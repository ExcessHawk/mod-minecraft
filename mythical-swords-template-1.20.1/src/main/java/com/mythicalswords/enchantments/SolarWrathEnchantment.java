package com.mythicalswords.enchantments;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;

/**
 * Wrath of Ra (egyptian) — under the open daytime sun, strikes ignite the
 * target and deal bonus fire damage.
 */
public class SolarWrathEnchantment extends ForgeExclusiveEnchantment {

    private static final float BONUS_PER_LEVEL = 1.5f;

    public SolarWrathEnchantment() {
        super(Rarity.RARE, 2);
    }

    @Override
    public void onTargetDamaged(LivingEntity user, Entity target, int level) {
        if (user.getWorld().isClient) return;
        if (!(target instanceof LivingEntity livingTarget)) return;
        if (!user.getWorld().isDay()) return;

        livingTarget.setOnFireFor(3 * level);
        livingTarget.damage(user.getWorld().getDamageSources().onFire(),
                BONUS_PER_LEVEL * level);
    }
}
