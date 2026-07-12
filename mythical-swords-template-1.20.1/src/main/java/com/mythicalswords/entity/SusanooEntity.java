package com.mythicalswords.entity;

import com.mythicalswords.core.ModItems;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LightningEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.boss.BossBar;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;

/**
 * Susanoo — Japanese god of storms and sea. Phase 2 unleashes the tempest:
 * slows everyone caught in the rain and calls lightning down on every player
 * near him.
 */
public class SusanooEntity extends MythicalBossEntity implements GeoEntity {

    private static final RawAnimation IDLE = RawAnimation.begin().thenLoop("idle");
    private final AnimatableInstanceCache geoCache = GeckoLibUtil.createInstanceCache(this);

    private int stormCooldown = 100;

    public SusanooEntity(EntityType<? extends HostileEntity> entityType, World world) {
        super(entityType, world);
        this.setBossBarColor(BossBar.Color.BLUE);
        this.setBossBarStyle(BossBar.Style.NOTCHED_10);
        // Visual only: the loot table drops the pristine blade
        this.equipStack(EquipmentSlot.MAINHAND, new ItemStack(ModItems.KUSANAGI_NO_TSURUGI));
        this.setEquipmentDropChance(EquipmentSlot.MAINHAND, 0.0f);
    }

    public static DefaultAttributeContainer.Builder createSusanooAttributes() {
        return HostileEntity.createHostileAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 900.0)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.35)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 16.0)
                .add(EntityAttributes.GENERIC_ARMOR, 28.0)
                .add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 1.0)
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 70.0);
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(0, new SwimGoal(this));
        this.goalSelector.add(1, new MeleeAttackGoal(this, 1.0, true));
        this.goalSelector.add(5, new WanderAroundFarGoal(this, 0.8));
        this.goalSelector.add(6, new LookAtEntityGoal(this, PlayerEntity.class, 8.0f));
        this.goalSelector.add(6, new LookAroundGoal(this));
        this.targetSelector.add(1, new RevengeGoal(this));
        this.targetSelector.add(2, new ActiveTargetGoal<>(this, PlayerEntity.class, true));
    }

    @Override
    protected void onPhaseTransition(int newPhase) {
        if (this.getWorld().isClient) return;
        if (newPhase == 2) {
            this.addStatusEffect(new StatusEffectInstance(StatusEffects.SPEED, 300, 0));
            if (this.getWorld() instanceof ServerWorld sw) {
                for (PlayerEntity p : sw.getPlayers()) {
                    if (p.squaredDistanceTo(this) <= 576) { // 24 blocks
                        p.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 100, 0));
                    }
                }
                sw.spawnParticles(ParticleTypes.CLOUD, getX(), getY() + 2, getZ(), 50, 1.5, 1.0, 1.5, 0.1);
                sw.spawnParticles(ParticleTypes.ELECTRIC_SPARK, getX(), getY() + 1, getZ(), 40, 1.0, 1.0, 1.0, 0.3);
            }
        } else if (newPhase == 3) {
            this.getAttributeInstance(EntityAttributes.GENERIC_ATTACK_DAMAGE).setBaseValue(22.0);
            this.getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED).setBaseValue(0.42);
            this.addStatusEffect(new StatusEffectInstance(StatusEffects.STRENGTH, Integer.MAX_VALUE, 0));
        }
    }

    @Override
    public void mobTick() {
        super.mobTick();
        if (this.getWorld().isClient) return;
        if (stormCooldown > 0) { stormCooldown--; return; }

        if (currentPhase < 2 || this.getTarget() == null) {
            stormCooldown = 100;
            return;
        }
        // Tempest: lightning on every player caught in the storm (up to 3)
        stormCooldown = currentPhase == 3 ? 100 : 160;
        if (this.getWorld() instanceof ServerWorld sw) {
            int struck = 0;
            for (PlayerEntity p : sw.getPlayers()) {
                if (struck >= 3) break;
                if (p.isAlive() && !p.isCreative() && !p.isSpectator()
                        && p.squaredDistanceTo(this) <= 400) { // 20 blocks
                    LightningEntity bolt = EntityType.LIGHTNING_BOLT.create(sw);
                    if (bolt != null) {
                        bolt.setPosition(p.getPos());
                        sw.spawnEntity(bolt);
                        struck++;
                    }
                }
            }
        }
    }

    @Override
    public boolean isPushedByFluids() { return false; }

    @Override
    public int getXpToDrop() { return 1800; }

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
