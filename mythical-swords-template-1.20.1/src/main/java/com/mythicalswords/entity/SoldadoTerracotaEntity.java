package com.mythicalswords.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.world.World;

/**
 * Soldado de Terracota — chinese clay soldier. The first time it would
 * shatter, the clay reassembles at half health.
 */
public class SoldadoTerracotaEntity extends MythicalMinionEntity {

    private boolean reassembled = false;

    public SoldadoTerracotaEntity(EntityType<? extends HostileEntity> entityType, World world) {
        super(entityType, world);
    }

    public static DefaultAttributeContainer.Builder createSoldadoTerracotaAttributes() {
        return HostileEntity.createHostileAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 28.0)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.27)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 5.0)
                .add(EntityAttributes.GENERIC_ARMOR, 10.0);
    }

    @Override
    public boolean damage(DamageSource source, float amount) {
        if (!this.getWorld().isClient && !reassembled
                && this.getHealth() - amount <= 0.0f && amount < 100.0f) {
            reassembled = true;
            this.setHealth(this.getMaxHealth() * 0.5f);
            if (this.getWorld() instanceof ServerWorld sw) {
                sw.spawnParticles(ParticleTypes.ITEM_SLIME,
                        getX(), getY() + 1, getZ(), 25, 0.4, 0.6, 0.4, 0.05);
            }
            this.getWorld().playSound(null, getX(), getY(), getZ(),
                    SoundEvents.BLOCK_DECORATED_POT_SHATTER, SoundCategory.HOSTILE, 1.0f, 0.7f);
            return false;
        }
        return super.damage(source, amount);
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putBoolean("Reassembled", reassembled);
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        reassembled = nbt.getBoolean("Reassembled");
    }

    @Override
    public String getMinionId() { return "soldado_terracota"; }
}
