package com.mythicalswords.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.boss.BossBar;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;

/**
 * Quetzalcóatl — Mesoamerican feathered serpent of wind. Phase 2 hurls a gale
 * that flings players skyward; periodically dives at its target in a serpent
 * charge.
 */
public class QuetzalcoatlEntity extends MythicalBossEntity implements GeoEntity {

    private static final RawAnimation IDLE = RawAnimation.begin().thenLoop("idle");
    private final AnimatableInstanceCache geoCache = GeckoLibUtil.createInstanceCache(this);

    private int chargeCooldown = 100;

    public QuetzalcoatlEntity(EntityType<? extends HostileEntity> entityType, World world) {
        super(entityType, world);
        this.setNoGravity(true); // flying feathered serpent
        this.setBossBarColor(BossBar.Color.GREEN);
        this.setBossBarStyle(BossBar.Style.NOTCHED_12);
    }

    public static DefaultAttributeContainer.Builder createQuetzalcoatlAttributes() {
        return HostileEntity.createHostileAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 1200.0)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.45)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 18.0)
                .add(EntityAttributes.GENERIC_ARMOR, 35.0)
                .add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 1.0)
                .add(EntityAttributes.GENERIC_FLYING_SPEED, 0.6)
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 80.0);
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(1, new MeleeAttackGoal(this, 1.2, true));
        this.goalSelector.add(5, new WanderAroundFarGoal(this, 0.9));
        this.goalSelector.add(6, new LookAtEntityGoal(this, PlayerEntity.class, 12.0f));
        this.goalSelector.add(6, new LookAroundGoal(this));
        this.targetSelector.add(1, new RevengeGoal(this));
        this.targetSelector.add(2, new ActiveTargetGoal<>(this, PlayerEntity.class, true));
    }

    @Override
    protected void onPhaseTransition(int newPhase) {
        if (this.getWorld().isClient) return;
        if (newPhase == 2) {
            // Gale: fling everyone away and into the air
            if (this.getWorld() instanceof ServerWorld sw) {
                for (PlayerEntity p : sw.getPlayers()) {
                    if (p.squaredDistanceTo(this) <= 256) { // 16 blocks
                        Vec3d push = p.getPos().subtract(this.getPos()).normalize().multiply(1.5);
                        p.addVelocity(push.x, 0.8, push.z);
                        p.velocityModified = true;
                        p.addStatusEffect(new StatusEffectInstance(StatusEffects.LEVITATION, 40, 0));
                    }
                }
                sw.spawnParticles(ParticleTypes.CLOUD, getX(), getY() + 1, getZ(), 80, 2.0, 1.0, 2.0, 0.3);
            }
            summonMinions(com.mythicalswords.core.ModEntities.GUERRERO_JAGUAR, 2);
        } else if (newPhase == 3) {
            this.getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED).setBaseValue(0.55);
            this.addStatusEffect(new StatusEffectInstance(StatusEffects.STRENGTH, Integer.MAX_VALUE, 0));
        }
    }

    @Override
    public void mobTick() {
        super.mobTick();
        if (this.getWorld().isClient) return;
        if (chargeCooldown > 0) { chargeCooldown--; return; }

        LivingEntity target = this.getTarget();
        if (currentPhase < 2 || target == null) {
            chargeCooldown = 100;
            return;
        }
        // Serpent charge: dive at the target
        chargeCooldown = currentPhase == 3 ? 90 : 140;
        Vec3d dash = target.getPos().add(0, 0.5, 0).subtract(this.getPos()).normalize().multiply(1.6);
        this.setVelocity(dash.x, dash.y + 0.1, dash.z);
        this.velocityModified = true;
        triggerAttackAnim("special");
        if (this.getWorld() instanceof ServerWorld sw) {
            sw.spawnParticles(ParticleTypes.SWEEP_ATTACK, getX(), getY() + 0.5, getZ(), 6, 0.6, 0.4, 0.6, 0.0);
        }
        this.getWorld().playSound(null, getX(), getY(), getZ(),
                SoundEvents.ENTITY_PHANTOM_SWOOP, SoundCategory.HOSTILE, 1.4f, 0.8f);
    }

    @Override
    public boolean isPushedByFluids() { return false; }

    @Override
    public boolean handleFallDamage(float fallDistance, float damageMultiplier,
            net.minecraft.entity.damage.DamageSource damageSource) {
        return false; // flying serpent takes no fall damage
    }

    @Override
    public int getXpToDrop() { return 2200; }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "controller", 0, state -> state.setAndContinue(IDLE)));
        controllers.add(new AnimationController<>(this, "attack", 0, state -> software.bernie.geckolib.core.object.PlayState.STOP)
            .triggerableAnim("melee", RawAnimation.begin().thenPlay("attack_melee"))
            .triggerableAnim("special", RawAnimation.begin().thenPlay("attack_special")));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.geoCache;
    }
}
