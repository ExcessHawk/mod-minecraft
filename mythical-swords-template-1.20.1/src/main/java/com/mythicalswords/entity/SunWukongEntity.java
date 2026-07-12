package com.mythicalswords.entity;

import com.mythicalswords.core.ModItems;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.boss.BossBar;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;

/**
 * Sun Wukong - The Monkey King Boss Entity
 * Phase 4: Chinese Mythology
 * 
 * HARDEST BOSS IN MOD
 * 
 * Special Abilities:
 * - Creates 3 clones at 75% health
 * - Staff extend attack with massive knockback
 * - Cloud surfing (levitation/flight)
 * - 72 transformations (random effects)
 * - Invincibility phase at 50% HP
 * - Acrobatics (30% dodge chance)
 */
public class SunWukongEntity extends MythicalBossEntity implements GeoEntity {

    private static final RawAnimation WUKONG_IDLE = RawAnimation.begin().thenLoop("idle");
    private final AnimatableInstanceCache geoCache = GeckoLibUtil.createInstanceCache(this);

    private int cloneSummonCooldown = -1; // -1 = not summoned yet
    private int staffSlamCooldown = 0;
    private int transformCooldown = 0;
    private boolean hasUsedInvincibility = false;
    private int invincibilityTicks = 0;
    private int cloudSurfCooldown = 0;

    // Multi-phase tracking
    private boolean phase2Active = false; // 75-50% HP
    private boolean phase3Active = false; // <50% HP

    public SunWukongEntity(EntityType<? extends HostileEntity> entityType, World world) {
        super(entityType, world);
        this.setBossBarColor(BossBar.Color.RED); // Red/gold theme
        this.setBossBarStyle(BossBar.Style.NOTCHED_10);

        // Equip Ruyi Jingu Bang
        this.equipStack(EquipmentSlot.MAINHAND, new ItemStack(ModItems.RUYI_JINGU_BANG));
        this.setEquipmentDropChance(EquipmentSlot.MAINHAND, 0.03f); // 3% drop rate (hardest boss)
    }

    public static DefaultAttributeContainer.Builder createSunWukongAttributes() {
        return HostileEntity.createHostileAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 800.0) // 400 Hearts - HIGHEST IN MOD
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.45) // Very fast
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 18.0) // 9 Hearts - massive damage
                .add(EntityAttributes.GENERIC_ARMOR, 25.0) // Maximum armor
                .add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 1.0)
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 80.0)
                .add(EntityAttributes.GENERIC_ATTACK_KNOCKBACK, 3.0); // Massive knockback
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(0, new SwimGoal(this));
        this.goalSelector.add(1, new MeleeAttackGoal(this, 1.2, true)); // Faster attack
        this.goalSelector.add(5, new WanderAroundFarGoal(this, 1.0)); // Fast movement
        this.goalSelector.add(6, new LookAtEntityGoal(this, PlayerEntity.class, 12.0f));
        this.goalSelector.add(6, new LookAroundGoal(this));

        this.targetSelector.add(1, new RevengeGoal(this));
        this.targetSelector.add(2, new ActiveTargetGoal<>(this, PlayerEntity.class, true));
    }

    @Override
    public void tick() {
        super.tick();

        if (!this.getWorld().isClient) {
            float healthPercent = this.getHealth() / this.getMaxHealth();

            // Phase transitions
            if (healthPercent <= 0.75 && !phase2Active) {
                enterPhase2();
            }
            if (healthPercent <= 0.50 && !phase3Active) {
                enterPhase3();
            }

            // Invincibility phase countdown
            if (invincibilityTicks > 0) {
                invincibilityTicks--;
                this.addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, 10, 4)); // Resistance V
                this.addStatusEffect(new StatusEffectInstance(StatusEffects.GLOWING, 10, 0));
            }

            // Staff slam cooldown
            if (staffSlamCooldown <= 0 && this.getTarget() != null) {
                performStaffSlam();
                staffSlamCooldown = 150; // 7.5 seconds
            } else {
                staffSlamCooldown--;
            }

            // Transformation ability
            if (transformCooldown <= 0 && phase2Active) {
                performTransformation();
                transformCooldown = 300; // 15 seconds
            } else {
                transformCooldown--;
            }

            // Cloud surf ability
            if (cloudSurfCooldown <= 0 && phase2Active) {
                cloudSurf();
                cloudSurfCooldown = 400; // 20 seconds
            } else {
                cloudSurfCooldown--;
            }
        }
    }

    /**
     * Phase 2: 75-50% HP - Summon clones
     */
    private void enterPhase2() {
        phase2Active = true;
        summonClones();
        this.addStatusEffect(new StatusEffectInstance(StatusEffects.SPEED, 1000000, 1)); // Permanent Speed II
    }

    /**
     * Phase 3: <50% HP - Invincibility + chaos
     */
    private void enterPhase3() {
        phase3Active = true;

        if (!hasUsedInvincibility) {
            invincibilityTicks = 100; // 5 seconds of invincibility
            hasUsedInvincibility = true;

            // Summon more clones
            summonClones();
        }

        this.addStatusEffect(new StatusEffectInstance(StatusEffects.STRENGTH, 1000000, 1)); // Permanent Strength II
    }

    /**
     * Summons 3 zombie clones that look like Sun Wukong
     */
    private void summonClones() {
        if (!(this.getWorld() instanceof ServerWorld serverWorld))
            return;

        for (int i = 0; i < 3; i++) {
            ZombieEntity clone = EntityType.ZOMBIE.create(serverWorld);
            if (clone != null) {
                double angle = (i / 3.0) * Math.PI * 2;
                double offsetX = Math.cos(angle) * 3;
                double offsetZ = Math.sin(angle) * 3;

                clone.refreshPositionAndAngles(
                        this.getX() + offsetX,
                        this.getY(),
                        this.getZ() + offsetZ,
                        this.random.nextFloat() * 360.0F,
                        0.0F);

                // Buff clones to 30% of Sun Wukong's power
                clone.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH).setBaseValue(240.0); // 30% of 800
                clone.setHealth(240.0f);
                clone.addStatusEffect(new StatusEffectInstance(StatusEffects.SPEED, 1000000, 1));
                clone.addStatusEffect(new StatusEffectInstance(StatusEffects.STRENGTH, 1000000, 0));
                clone.addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, 1000000, 0));

                // TODO: Give clone Sun Wukong texture

                serverWorld.spawnEntity(clone);
            }
        }
    }

    /**
     * Staff slam with AoE knockback
     */
    private void performStaffSlam() {
        // Massive knockback to all entities in 5 block radius
        for (Entity entity : this.getWorld().getOtherEntities(this, this.getBoundingBox().expand(5.0))) {
            if (entity instanceof LivingEntity livingEntity) {
                Vec3d knockback = entity.getPos().subtract(this.getPos()).normalize().multiply(2.5);
                livingEntity.addVelocity(knockback.x, 0.8, knockback.z); // Upward + outward
                livingEntity.velocityModified = true;

                // Bonus damage
                livingEntity.damage(this.getDamageSources().mobAttack(this), 6.0f);
            }
        }
    }

    /**
     * Random transformation effects (cosmetic + confusion)
     */
    private void performTransformation() {
        int transformType = this.random.nextInt(4);

        switch (transformType) {
            case 0: // Speed burst
                this.addStatusEffect(new StatusEffectInstance(StatusEffects.SPEED, 100, 3));
                break;
            case 1: // Invisibility
                this.addStatusEffect(new StatusEffectInstance(StatusEffects.INVISIBILITY, 60, 0));
                break;
            case 2: // Fire resistance + flame
                this.addStatusEffect(new StatusEffectInstance(StatusEffects.FIRE_RESISTANCE, 200, 0));
                this.setOnFireFor(10);
                break;
            case 3: // Jump boost (parkour)
                this.addStatusEffect(new StatusEffectInstance(StatusEffects.JUMP_BOOST, 100, 3));
                break;
        }
    }

    /**
     * Cloud surfing - brief flight
     */
    private void cloudSurf() {
        this.addStatusEffect(new StatusEffectInstance(StatusEffects.LEVITATION, 80, 1)); // 4 seconds
        this.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOW_FALLING, 120, 0)); // 6 seconds
    }

    @Override
    public boolean tryAttack(Entity target) {
        // 30% chance to dodge (acrobatics)
        if (this.random.nextFloat() < 0.30) {
            // Dodge: teleport behind target
            if (target instanceof LivingEntity livingTarget) {
                Vec3d behindPos = livingTarget.getPos().subtract(livingTarget.getRotationVector().multiply(2));
                this.teleport(behindPos.x, behindPos.y, behindPos.z);
            }
        }

        return super.tryAttack(target);
    }

    @Override
    public boolean damage(net.minecraft.entity.damage.DamageSource source, float amount) {
        // Can't be damaged during invincibility
        if (invincibilityTicks > 0) {
            return false;
        }

        // 30% dodge chance
        if (this.random.nextFloat() < 0.30) {
            return false; // Dodged!
        }

        return super.damage(source, amount);
    }

    @Override
    public boolean isPushedByFluids() {
        return false;
    }

    @Override
    public int getXpToDrop() {
        return 5000; // MASSIVE XP - hardest boss
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "controller", 0, state -> state.setAndContinue(WUKONG_IDLE)));
        controllers.add(new AnimationController<>(this, "attack", 0, state -> software.bernie.geckolib.core.object.PlayState.STOP)
            .triggerableAnim("melee", RawAnimation.begin().thenPlay("attack_melee"))
            .triggerableAnim("special", RawAnimation.begin().thenPlay("attack_special")));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.geoCache;
    }
}
