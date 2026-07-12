package com.mythicalswords.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.world.World;

/** Hoplita Espectral — ghost of a greek phalanx soldier. Raises its shield when struck. */
public class HoplitaEspectralEntity extends MythicalMinionEntity {

    public HoplitaEspectralEntity(EntityType<? extends HostileEntity> entityType, World world) {
        super(entityType, world);
    }

    public static DefaultAttributeContainer.Builder createHoplitaAttributes() {
        return HostileEntity.createHostileAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 30.0)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.28)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 5.0)
                .add(EntityAttributes.GENERIC_ARMOR, 12.0)
                .add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 0.8);
    }

    @Override
    public boolean damage(DamageSource source, float amount) {
        boolean damaged = super.damage(source, amount);
        if (damaged && !this.getWorld().isClient && this.getRandom().nextFloat() < 0.3f) {
            // Phalanx discipline: brief shield wall after taking a hit
            this.addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, 40, 1));
        }
        return damaged;
    }

    @Override
    public String getMinionId() { return "hoplita_espectral"; }
}
