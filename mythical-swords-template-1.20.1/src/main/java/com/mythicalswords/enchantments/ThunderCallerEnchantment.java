package com.mythicalswords.enchantments;

import com.mythicalswords.weapons.MythicalWeaponItem;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.LightningEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;

public class ThunderCallerEnchantment extends Enchantment {

    private static final float LIGHTNING_CHANCE = 0.10f; // 10% per level

    public ThunderCallerEnchantment() {
        super(Rarity.VERY_RARE, EnchantmentTarget.WEAPON, new EquipmentSlot[]{EquipmentSlot.MAINHAND});
    }

    @Override
    public int getMinPower(int level) {
        return 25;
    }

    @Override
    public int getMaxPower(int level) {
        return 50;
    }

    @Override
    public int getMaxLevel() {
        return 1;
    }

    @Override
    public boolean isAvailableForEnchantedBookOffer() {
        return true;
    }

    @Override
    public boolean canAccept(Enchantment other) {
        return super.canAccept(other);
    }

    @Override
    public void onTargetDamaged(LivingEntity user, Entity target, int level) {
        if (user.getWorld().isClient) return;

        // Only applies to mythical weapons
        ItemStack mainHand = user.getMainHandStack();
        if (!(mainHand.getItem() instanceof MythicalWeaponItem)) return;

        float chance = LIGHTNING_CHANCE * level;
        if (user.getRandom().nextFloat() < chance) {
            if (user.getWorld() instanceof ServerWorld serverWorld) {
                LightningEntity lightning = EntityType.LIGHTNING_BOLT.create(serverWorld);
                if (lightning != null) {
                    lightning.setPosition(target.getPos());
                    lightning.setCosmetic(false);
                    serverWorld.spawnEntity(lightning);
                }
            }
        }
    }
}
