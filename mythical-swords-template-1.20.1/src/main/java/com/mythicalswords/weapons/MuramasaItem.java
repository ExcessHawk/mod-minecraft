package com.mythicalswords.weapons;

import com.mythicalswords.abilities.BloodFrenzyAbility;
import com.mythicalswords.abilities.WeaponAbility;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterials;

/**
 * Muramasa - Cursed demon blade
 * LEGENDARY tier weapon with DARK affinity
 * Special Ability: Blood Frenzy - Massive damage boost but drains health
 */
public class MuramasaItem extends MythicalWeaponItem {

    public MuramasaItem() {
        super(
                ToolMaterials.NETHERITE,
                11, // Attack damage
                -2.2f, // Attack speed (slightly faster)
                new Settings().maxDamage(4500),
                WeaponTier.LEGENDARY,
                ElementalAffinity.DARK,
                "japanese");
    }
    
    @Override
    public WeaponAbility getAbility() {
        return new BloodFrenzyAbility();
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        // Cursed blade: Wither effect and Lifesteal (Regeneration for attacker)
        target.addStatusEffect(new StatusEffectInstance(StatusEffects.WITHER, 100, 1), attacker);
        attacker.addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 40, 0), attacker);
        return super.postHit(stack, target, attacker);
    }
}
