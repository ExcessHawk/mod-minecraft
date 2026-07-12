package com.mythicalswords.enchantments;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;

/**
 * Iaijutsu (japanese) — the untouched swordsman cuts deepest. Bonus magic
 * damage while the wielder is at full health.
 */
public class IaijutsuEnchantment extends ForgeExclusiveEnchantment {

    private static final float BONUS_PER_LEVEL = 2.5f;

    public IaijutsuEnchantment() {
        super(Rarity.RARE, 3);
    }

    @Override
    public void onTargetDamaged(LivingEntity user, Entity target, int level) {
        if (user.getWorld().isClient) return;
        if (!(target instanceof LivingEntity livingTarget)) return;

        if (user.getHealth() >= user.getMaxHealth()) {
            livingTarget.damage(user.getWorld().getDamageSources().magic(),
                    BONUS_PER_LEVEL * level);
        }
    }
}
