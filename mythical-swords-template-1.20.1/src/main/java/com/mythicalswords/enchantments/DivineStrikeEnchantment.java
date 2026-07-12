package com.mythicalswords.enchantments;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.server.world.ServerWorld;

public class DivineStrikeEnchantment extends Enchantment {

    private static final float BONUS_DAMAGE_PER_LEVEL = 2.5f;

    public DivineStrikeEnchantment() {
        super(Rarity.UNCOMMON, EnchantmentTarget.WEAPON, new EquipmentSlot[]{EquipmentSlot.MAINHAND});
    }

    @Override
    public int getMinPower(int level) {
        return 5 + (level - 1) * 8;
    }

    @Override
    public int getMaxPower(int level) {
        return getMinPower(level) + 20;
    }

    @Override
    public int getMaxLevel() {
        return 3;
    }

    @Override
    public void onTargetDamaged(LivingEntity user, Entity target, int level) {
        if (user.getWorld().isClient) return;
        if (!(target instanceof LivingEntity livingTarget)) return;

        // Bonus damage vs undead
        String typeName = livingTarget.getType().toString().toLowerCase();
        boolean isUndead = typeName.contains("zombie") || typeName.contains("skeleton")
            || typeName.contains("phantom") || typeName.contains("wither")
            || typeName.contains("drowned") || typeName.contains("husk")
            || typeName.contains("stray") || typeName.contains("revenant");

        if (isUndead) {
            float bonus = BONUS_DAMAGE_PER_LEVEL * level;
            DamageSource source = user.getWorld().getDamageSources().magic();
            livingTarget.damage(source, bonus);
        }
    }
}
