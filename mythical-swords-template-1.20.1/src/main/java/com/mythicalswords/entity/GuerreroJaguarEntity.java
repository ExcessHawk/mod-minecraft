package com.mythicalswords.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

/** Guerrero Jaguar — mesoamerican warrior. Pounces at distant prey. */
public class GuerreroJaguarEntity extends MythicalMinionEntity {

    private int pounceCooldown = 40;

    public GuerreroJaguarEntity(EntityType<? extends HostileEntity> entityType, World world) {
        super(entityType, world);
    }

    public static DefaultAttributeContainer.Builder createGuerreroJaguarAttributes() {
        return HostileEntity.createHostileAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 26.0)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.33)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 5.0)
                .add(EntityAttributes.GENERIC_ARMOR, 3.0);
    }

    @Override
    public void mobTick() {
        super.mobTick();
        if (this.getWorld().isClient) return;
        if (pounceCooldown > 0) { pounceCooldown--; return; }

        LivingEntity target = this.getTarget();
        if (target != null && this.isOnGround()) {
            double dist = this.squaredDistanceTo(target);
            if (dist > 9 && dist < 36) { // pounce from 3-6 blocks
                Vec3d leap = target.getPos().subtract(this.getPos()).normalize().multiply(0.9);
                this.setVelocity(leap.x, 0.45, leap.z);
                this.velocityModified = true;
                pounceCooldown = 60;
            }
        }
    }

    @Override
    public String getMinionId() { return "guerrero_jaguar"; }
}
