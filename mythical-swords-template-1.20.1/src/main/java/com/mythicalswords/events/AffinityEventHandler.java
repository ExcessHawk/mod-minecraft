package com.mythicalswords.events;

import com.mythicalswords.weapons.ElementalAffinity;
import com.mythicalswords.weapons.MythicalWeaponItem;
import com.mythicalswords.systems.AffinityEffectSystem;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;

/**
 * Event handler for affinity effects
 * Uses Fabric's event system to apply affinity-based damage bonuses and effects
 */
public class AffinityEventHandler {
    
    /**
     * Register the affinity event handler
     */
    public static void register() {
        AttackEntityCallback.EVENT.register((player, world, hand, entity, hitResult) -> {
            // Only process on server
            if (world.isClient) {
                return ActionResult.PASS;
            }
            
            // Only process if target is a living entity
            if (!(entity instanceof LivingEntity target)) {
                return ActionResult.PASS;
            }
            
            // Get the weapon being used
            ItemStack weapon = player.getStackInHand(hand);
            if (weapon.isEmpty() || !(weapon.getItem() instanceof MythicalWeaponItem mythicalWeapon)) {
                return ActionResult.PASS;
            }
            
            // Get affinity and calculate bonus damage
            ElementalAffinity affinity = mythicalWeapon.getAffinity();
            float damageMultiplier = affinity.calculateBonus(world, target.getBlockPos(), target);
            
            // Apply bonus damage if multiplier is greater than 1.0
            if (damageMultiplier > 1.0f) {
                // Calculate bonus damage (base weapon damage * (multiplier - 1.0))
                float baseDamage = mythicalWeapon.getAttackDamage();
                float bonusDamage = baseDamage * (damageMultiplier - 1.0f);
                
                // Apply bonus damage separately to avoid infinite loops
                // In 1.20.1, we use the world's DamageSources registry
                target.damage(world.getDamageSources().playerAttack(player), bonusDamage);
                
                // Show enhanced visual/audio feedback for significant bonuses
                AffinityEffectSystem.applyAffinityProcEffect(target, player, affinity, world, damageMultiplier);
            }
            
            // Apply affinity effects (status effects and particles)
            AffinityEffectSystem.applyAffinityEffect(target, player, affinity, world);
            
            return ActionResult.PASS;
        });
    }
}
