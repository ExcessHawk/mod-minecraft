package com.mythicalswords.enchantments;

import com.mythicalswords.systems.AffinityEffectSystem;
import com.mythicalswords.weapons.ElementalAffinity;
import com.mythicalswords.weapons.MythicalWeaponItem;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;

public class ElementalEdgeEnchantment extends Enchantment {

    public ElementalEdgeEnchantment() {
        super(Rarity.RARE, EnchantmentTarget.WEAPON, new EquipmentSlot[]{EquipmentSlot.MAINHAND});
    }

    @Override
    public int getMinPower(int level) {
        return 10 + (level - 1) * 8;
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
    public boolean isAvailableForEnchantedBookOffer() {
        return true;
    }

    @Override
    public void onTargetDamaged(LivingEntity user, Entity target, int level) {
        if (user.getWorld().isClient) return;
        if (!(target instanceof LivingEntity livingTarget)) return;

        ItemStack mainHand = user.getMainHandStack();
        if (!(mainHand.getItem() instanceof MythicalWeaponItem weapon)) return;

        ElementalAffinity affinity = weapon.getAffinity();
        // Level scales effect strength
        for (int i = 0; i < level; i++) {
            AffinityEffectSystem.applyAffinityEffect(livingTarget, user, affinity, user.getWorld());
        }
    }
}
