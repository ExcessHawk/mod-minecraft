package com.mythicalswords.entity;

import com.mythicalswords.core.ModItems;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.boss.BossBar;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;

/**
 * Loki Boss Entity - Norse Trickster God
 * 1000 HP, 3 phases + enrage, drops Laevateinn
 */
public class LokiEntity extends MythicalBossEntity implements GeoEntity {

    private static final RawAnimation LOKI_IDLE = RawAnimation.begin().thenLoop("idle");
    private final AnimatableInstanceCache geoCache = GeckoLibUtil.createInstanceCache(this);

    public LokiEntity(EntityType<? extends HostileEntity> entityType, World world) {
        super(entityType, world);
        this.setBossBarColor(BossBar.Color.GREEN); // Trickery and chaos
        this.setBossBarStyle(BossBar.Style.NOTCHED_12);

        // Equip Laevateinn in main hand
        this.equipStack(EquipmentSlot.MAINHAND, new ItemStack(ModItems.LAEVATEINN));

        // Set drop chances (100% for main hand so Laevateinn always drops)
        this.setEquipmentDropChance(EquipmentSlot.MAINHAND, 1.0f);
    }

    public static DefaultAttributeContainer.Builder createLokiAttributes() {
        return HostileEntity.createHostileAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 1000.0) // 500 Hearts
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.40) // Fast and agile
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 14.0) // 7 Hearts
                .add(EntityAttributes.GENERIC_ARMOR, 20.0) // Moderate armor
                .add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 0.8) // Mostly unstoppable
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 70.0);
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(0, new SwimGoal(this));
        this.goalSelector.add(1, new MeleeAttackGoal(this, 1.2, true)); // Faster attacks
        this.goalSelector.add(5, new WanderAroundFarGoal(this, 1.0)); // More mobile
        this.goalSelector.add(6, new LookAtEntityGoal(this, PlayerEntity.class, 8.0f));
        this.goalSelector.add(6, new LookAroundGoal(this));

        this.targetSelector.add(1, new RevengeGoal(this));
        this.targetSelector.add(2, new ActiveTargetGoal<>(this, PlayerEntity.class, true));
    }

    private int trickCooldown = 100;

    @Override
    protected void onPhaseTransition(int newPhase) {
        if (this.getWorld().isClient) return;
        if (newPhase == 2) {
            // Vanishing act
            this.addStatusEffect(new StatusEffectInstance(StatusEffects.INVISIBILITY, 100, 0));
            this.addStatusEffect(new StatusEffectInstance(StatusEffects.SPEED, 400, 1));
            if (this.getWorld() instanceof ServerWorld sw) {
                sw.spawnParticles(ParticleTypes.LARGE_SMOKE, getX(), getY() + 1, getZ(), 50, 0.8, 1.0, 0.8, 0.05);
            }
        } else if (newPhase == 3) {
            this.getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED).setBaseValue(0.48);
            this.addStatusEffect(new StatusEffectInstance(StatusEffects.STRENGTH, Integer.MAX_VALUE, 0));
        }
    }

    @Override
    public void mobTick() {
        super.mobTick();
        if (this.getWorld().isClient) return;
        if (trickCooldown > 0) { trickCooldown--; return; }

        LivingEntity target = this.getTarget();
        if (currentPhase < 2 || target == null) {
            trickCooldown = 100;
            return;
        }
        // Trickery: blink behind the target and poison them
        trickCooldown = currentPhase == 3 ? 90 : 140;
        if (this.getWorld() instanceof ServerWorld sw) {
            sw.spawnParticles(ParticleTypes.PORTAL, getX(), getY() + 1, getZ(), 30, 0.5, 1.0, 0.5, 0.5);
            var behind = target.getPos().subtract(
                    target.getRotationVector().multiply(2.0, 0.0, 2.0));
            this.teleport(behind.x, target.getY(), behind.z);
            sw.spawnParticles(ParticleTypes.PORTAL, getX(), getY() + 1, getZ(), 30, 0.5, 1.0, 0.5, 0.5);
            this.getWorld().playSound(null, getX(), getY(), getZ(),
                    SoundEvents.ENTITY_ENDERMAN_TELEPORT, SoundCategory.HOSTILE, 1.0f, 1.2f);
            target.addStatusEffect(new StatusEffectInstance(StatusEffects.POISON, 80, 1), this);
        }
    }

    @Override
    public boolean isPushedByFluids() {
        return false; // Loki walks through water
    }

    @Override
    public int getXpToDrop() {
        return 2500; // Massive XP drop (more than Odin due to difficulty)
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "controller", 0, state -> state.setAndContinue(LOKI_IDLE)));
        controllers.add(new AnimationController<>(this, "attack", 0, state -> software.bernie.geckolib.core.object.PlayState.STOP)
            .triggerableAnim("melee", RawAnimation.begin().thenPlay("attack_melee"))
            .triggerableAnim("special", RawAnimation.begin().thenPlay("attack_special")));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.geoCache;
    }
}
