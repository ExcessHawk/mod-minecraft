package com.mythicalswords.weapons;

import com.mythicalswords.abilities.WeaponAbility;
import com.mythicalswords.abilities.WindBladeAbility;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterials;

/**
 * Kusanagi-no-Tsurugi - Legendary grass-cutting sword of Susanoo
 * LEGENDARY tier weapon with WIND affinity
 * Special Ability: Wind Blade - Launches a cutting wind projectile
 */
public class KusanagiItem extends MythicalWeaponItem {

    public KusanagiItem() {
        super(
                ToolMaterials.NETHERITE,
                12, // Attack damage
                -2.4f, // Attack speed
                new Settings().maxDamage(5000),
                WeaponTier.LEGENDARY,
                ElementalAffinity.NATURE, // Wind/Storm = Nature
                "japanese");
    }
    
    @Override
    public WeaponAbility getAbility() {
        return new WindBladeAbility();
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        // Storm effect: Levitation and Slowness
        target.addStatusEffect(new StatusEffectInstance(StatusEffects.LEVITATION, 20, 1), attacker);
        target.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 60, 1), attacker);
        return super.postHit(stack, target, attacker);
    }
}
