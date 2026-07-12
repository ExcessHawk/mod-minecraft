package com.mythicalswords.weapons;

import com.mythicalswords.abilities.SerpentStrikeAbility;
import com.mythicalswords.abilities.WeaponAbility;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterials;

/**
 * Xiuhcoatl - Fire Serpent of Huitzilopochtli
 * LEGENDARY tier weapon with FIRE affinity
 * Special Ability: Serpent Strike - Summons a fire serpent that seeks enemies
 */
public class XiuhcoatlItem extends MythicalWeaponItem {

    public XiuhcoatlItem() {
        super(
                ToolMaterials.NETHERITE,
                14, // Attack damage (very high)
                -2.8f, // Attack speed (slower but powerful)
                new Settings().maxDamage(5000).fireproof(),
                WeaponTier.LEGENDARY,
                ElementalAffinity.FIRE,
                "mesoamerican");
    }
    
    @Override
    public WeaponAbility getAbility() {
        return new SerpentStrikeAbility();
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        // Fire serpent: Set target on fire
        target.setOnFireFor(8);
        return super.postHit(stack, target, attacker);
    }
}
