package com.mythicalswords.weapons;

import com.mythicalswords.abilities.SoulSealAbility;
import com.mythicalswords.abilities.WeaponAbility;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterials;

/**
 * Totsuka-no-Tsurugi - Divine sealing blade of Izanagi
 * LEGENDARY tier weapon with DIVINE affinity
 * Special Ability: Soul Seal - Seals weakened enemies
 */
public class TotsukaItem extends MythicalWeaponItem {

    public TotsukaItem() {
        super(
                ToolMaterials.NETHERITE,
                13, // Attack damage (highest)
                -2.4f, // Attack speed
                new Settings().maxDamage(5500),
                WeaponTier.LEGENDARY,
                ElementalAffinity.DIVINE,
                "japanese");
    }
    
    @Override
    public WeaponAbility getAbility() {
        return new SoulSealAbility();
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        // Sealing power: Weakness and Slowness
        target.addStatusEffect(new StatusEffectInstance(StatusEffects.WEAKNESS, 100, 2), attacker);
        target.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 100, 2), attacker);
        return super.postHit(stack, target, attacker);
    }
}
