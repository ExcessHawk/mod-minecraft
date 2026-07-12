package com.mythicalswords.entity;

import com.mythicalswords.core.ModItems;
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
 * Izanagi — Japanese creator deity. Phase 2 purifies himself (big heal) and
 * weakens mortals in his presence; his Divine Judgment periodically smites the
 * target with holy light.
 */
public class IzanagiEntity extends MythicalBossEntity implements GeoEntity {

    private static final RawAnimation IDLE = RawAnimation.begin().thenLoop("idle");
    private final AnimatableInstanceCache geoCache = GeckoLibUtil.createInstanceCache(this);

    private int judgmentCooldown = 100;

    public IzanagiEntity(EntityType<? extends HostileEntity> entityType, World world) {
        super(entityType, world);
        this.setBossBarColor(BossBar.Color.WHITE);
        this.setBossBarStyle(BossBar.Style.NOTCHED_10);
        // Visual only: the loot table drops the pristine blade
        this.equipStack(EquipmentSlot.MAINHAND, new ItemStack(ModItems.TOTSUKA_NO_TSURUGI));
        this.setEquipmentDropChance(EquipmentSlot.MAINHAND, 0.0f);
    }

    public static DefaultAttributeContainer.Builder createIzanagiAttributes() {
        return HostileEntity.createHostileAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 1100.0)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.4)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 14.0)
                .add(EntityAttributes.GENERIC_ARMOR, 30.0)
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
            // Purification: heal and shrug off harm, mortals feel their weakness
            this.heal(80.0f);
            this.addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, 300, 0));
            if (this.getWorld() instanceof ServerWorld sw) {
                for (PlayerEntity p : sw.getPlayers()) {
                    if (p.squaredDistanceTo(this) <= 144) { // 12 blocks
                        p.addStatusEffect(new StatusEffectInstance(StatusEffects.WEAKNESS, 100, 0));
                    }
                }
                sw.spawnParticles(ParticleTypes.END_ROD, getX(), getY() + 1.5, getZ(), 60, 1.0, 1.2, 1.0, 0.15);
            }
        } else if (newPhase == 3) {
            this.getAttributeInstance(EntityAttributes.GENERIC_ATTACK_DAMAGE).setBaseValue(20.0);
            this.addStatusEffect(new StatusEffectInstance(StatusEffects.STRENGTH, Integer.MAX_VALUE, 1));
            this.addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, Integer.MAX_VALUE, 0));
        }
    }

    @Override
    public void mobTick() {
        super.mobTick();
        if (this.getWorld().isClient) return;
        if (judgmentCooldown > 0) { judgmentCooldown--; return; }

        LivingEntity target = this.getTarget();
        if (currentPhase < 2 || target == null) {
            judgmentCooldown = 100;
            return;
        }
        // Divine Judgment: column of light smites the target
        judgmentCooldown = currentPhase == 3 ? 100 : 150;
        if (this.getWorld() instanceof ServerWorld sw) {
            target.damage(this.getDamageSources().magic(), 8.0f);
            target.addStatusEffect(new StatusEffectInstance(StatusEffects.GLOWING, 60, 0), this);
            for (int i = 0; i < 12; i++) {
                sw.spawnParticles(ParticleTypes.END_ROD,
                        target.getX(), target.getY() + i * 0.5, target.getZ(), 3, 0.2, 0.1, 0.2, 0.02);
            }
        }
    }

    @Override
    public boolean isPushedByFluids() { return false; }

    @Override
    public int getXpToDrop() { return 2000; }

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
