package com.mythicalswords.enchantments;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;

/**
 * Odin's Rune (norse) — executes weakened enemies. If the target drops below
 * 15%/25% health (level I/II) after the hit, it is struck down outright.
 */
public class OdinRuneEnchantment extends ForgeExclusiveEnchantment {

    public OdinRuneEnchantment() {
        super(Rarity.VERY_RARE, 2);
    }

    @Override
    public void onTargetDamaged(LivingEntity user, Entity target, int level) {
        if (user.getWorld().isClient) return;
        if (!(target instanceof LivingEntity livingTarget)) return;
        if (livingTarget.isDead()) return;

        float threshold = 0.05f + 0.10f * level;
        if (livingTarget.getHealth() / livingTarget.getMaxHealth() < threshold) {
            livingTarget.damage(user.getWorld().getDamageSources().magic(),
                    livingTarget.getHealth() + 100.0f);
        }
    }
}
