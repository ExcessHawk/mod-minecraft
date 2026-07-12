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
import net.minecraft.entity.mob.SilverfishEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.SmallFireballEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;
import net.minecraft.world.World;

/**
 * Ra - Egyptian Sun God Boss Entity
 * Phase 4: Egyptian Mythology
 * 
 * Special Abilities:
 * - Solar Beam: Ranged fire attack
 * - Heals in sunlight during day
 * - Burning Aura: Sets nearby entities on fire
 * - Summons scarab swarms
 * - Day/Night Modifier: 150% damage during day, 50% at night
 */
public class RaEntity extends MythicalBossEntity implements GeoEntity {

    private static final RawAnimation IDLE = RawAnimation.begin().thenLoop("idle");
    private final AnimatableInstanceCache geoCache = GeckoLibUtil.createInstanceCache(this);

    private int scarabSummonCooldown = 0;
    private int solarBeamCooldown = 0;
    private int auraTickCounter = 0;
    private int healTickCounter = 0;

    public RaEntity(EntityType<? extends HostileEntity> entityType, World world) {
        super(entityType, world);
        this.setBossBarColor(BossBar.Color.YELLOW); // Gold/sun theme
        this.setBossBarStyle(BossBar.Style.NOTCHED_10);

        // Equip Was Scepter
        this.equipStack(EquipmentSlot.MAINHAND, new ItemStack(ModItems.WAS_SCEPTER));
        this.setEquipmentDropChance(EquipmentSlot.MAINHAND, 0.05f); // 5% drop rate

        // Flying boss
        this.setNoGravity(true);
    }

    public static DefaultAttributeContainer.Builder createRaAttributes() {
        return HostileEntity.createHostileAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 750.0) // 375 Hearts
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.30)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 16.0) // 8 Hearts base
                .add(EntityAttributes.GENERIC_ARMOR, 24.0) // Very heavy armor
                .add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 1.0)
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 80.0) // Longer range for ranged attacks
                .add(EntityAttributes.GENERIC_FLYING_SPEED, 0.5);
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(0, new SwimGoal(this));
        this.goalSelector.add(1, new MeleeAttackGoal(this, 1.0, true));
        this.goalSelector.add(5, new WanderAroundFarGoal(this, 0.7));
        this.goalSelector.add(6, new LookAtEntityGoal(this, PlayerEntity.class, 12.0f));
        this.goalSelector.add(6, new LookAroundGoal(this));

        this.targetSelector.add(1, new RevengeGoal(this));
        this.targetSelector.add(2, new ActiveTargetGoal<>(this, PlayerEntity.class, true));
    }

    @Override
    public void tick() {
        super.tick();

        if (!this.getWorld().isClient) {
            // Summon scarabs every 800 ticks (40 seconds)
            if (scarabSummonCooldown <= 0) {
                summonScarabs();
                scarabSummonCooldown = 800;
            } else {
                scarabSummonCooldown--;
            }

            // Solar beam every 200 ticks (10 seconds)
            if (solarBeamCooldown <= 0 && this.getTarget() != null) {
                fireSolarBeam();
                solarBeamCooldown = 200;
            } else {
                solarBeamCooldown--;
            }

            // Burning aura every 40 ticks (2 seconds)
            if (auraTickCounter <= 0) {
                applyBurningAura();
                auraTickCounter = 40;
            } else {
                auraTickCounter--;
            }

            // Heal in sunlight every 20 ticks (1 second)
            if (healTickCounter <= 0) {
                healInSunlight();
                healTickCounter = 20;
            } else {
                healTickCounter--;
            }

            // Hover movement (maintain height)
            if (!this.isOnGround()) {
                Vec3d velocity = this.getVelocity();
                this.setVelocity(velocity.x, 0.02, velocity.z); // Gentle hover
            }
        }
    }

    @Override
    public boolean tryAttack(Entity target) {
        boolean success = super.tryAttack(target);

        if (success && target instanceof LivingEntity livingTarget) {
            // Set target on fire
            livingTarget.setOnFireFor(8);

            // Day/Night damage modifier
            if (this.getWorld().isDay()) {
                // 150% damage during day (+50% bonus)
                livingTarget.damage(this.getDamageSources().mobAttack(this), 8.0f);
            } else {
                // 50% damage at night (-50% penalty)
                // Base damage is applied, so this does nothing extra
            }
        }

        return success;
    }

    /**
     * Fires a solar beam (fireball) at current target
     */
    private void fireSolarBeam() {
        if (!(this.getWorld() instanceof ServerWorld serverWorld))
            return;
        if (this.getTarget() == null)
            return;

        Vec3d targetPos = this.getTarget().getPos();
        Vec3d myPos = this.getPos().add(0, 1.5, 0); // From head

        Vec3d direction = targetPos.subtract(myPos).normalize();

        SmallFireballEntity fireball = new SmallFireballEntity(
                serverWorld, this,
                direction.x, direction.y, direction.z);

        fireball.setPosition(myPos.x, myPos.y, myPos.z);
        serverWorld.spawnEntity(fireball);
    }

    /**
     * Summons 4-6 silverfish (scarabs) around Ra
     */
    private void summonScarabs() {
        if (!(this.getWorld() instanceof ServerWorld serverWorld))
            return;

        int scarabCount = 4 + this.random.nextInt(3); // 4-6 scarabs

        for (int i = 0; i < scarabCount; i++) {
            SilverfishEntity scarab = EntityType.SILVERFISH.create(serverWorld);
            if (scarab != null) {
                double offsetX = (this.random.nextDouble() - 0.5) * 5;
                double offsetZ = (this.random.nextDouble() - 0.5) * 5;

                scarab.refreshPositionAndAngles(
                        this.getX() + offsetX,
                        this.getY(),
                        this.getZ() + offsetZ,
                        this.random.nextFloat() * 360.0F,
                        0.0F);

                serverWorld.spawnEntity(scarab);
            }
        }
    }

    /**
     * Sets nearby entities on fire (burning aura)
     */
    private void applyBurningAura() {
        for (Entity entity : this.getWorld().getOtherEntities(this, this.getBoundingBox().expand(6.0))) {
            if (entity instanceof LivingEntity livingEntity && !(entity instanceof RaEntity)) {
                livingEntity.setOnFireFor(3);
            }
        }
    }

    /**
     * Heals Ra when in direct sunlight during day
     */
    private void healInSunlight() {
        if (this.getWorld().isDay() && this.getWorld().isSkyVisible(this.getBlockPos())) {
            this.heal(1.0f); // 1 HP per second during day
        }
    }

    @Override
    public boolean isPushedByFluids() {
        return false; // Gods are above physics
    }

    @Override
    protected void onPhaseTransition(int newPhase) {
        if (this.getWorld().isClient) return;
        if (newPhase == 2) {
            // Solar flare: everything near the sun god catches fire
            if (this.getWorld() instanceof net.minecraft.server.world.ServerWorld sw) {
                for (PlayerEntity p : sw.getPlayers()) {
                    if (p.squaredDistanceTo(this) <= 144) { // 12 blocks
                        p.setOnFireFor(5);
                    }
                }
                sw.spawnParticles(net.minecraft.particle.ParticleTypes.FLAME,
                        getX(), getY() + 1.5, getZ(), 100, 1.5, 1.2, 1.5, 0.15);
            }
        } else if (newPhase == 3) {
            // The dying sun burns brightest
            this.getAttributeInstance(net.minecraft.entity.attribute.EntityAttributes.GENERIC_ATTACK_DAMAGE)
                    .setBaseValue(24.0);
            this.addStatusEffect(new StatusEffectInstance(StatusEffects.STRENGTH, Integer.MAX_VALUE, 0));
            this.addStatusEffect(new StatusEffectInstance(StatusEffects.SPEED, Integer.MAX_VALUE, 0));
        }
    }

    @Override
    public int getXpToDrop() {
        return 2500; // Highest XP for Phase 4 Egyptian boss
    }

    @Override
    public boolean isFireImmune() {
        return true; // Sun god is immune to fire
    }

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
