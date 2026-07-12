package com.mythicalswords.entity;

import com.mythicalswords.core.ModItems;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
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
 * Oni Oscuro — dark Japanese demon wielding the cursed Muramasa. Phase 2
 * smothers the arena in darkness; his Dark Pulse periodically withers
 * everything close to him.
 */
public class OniOscuroEntity extends MythicalBossEntity implements GeoEntity {

    private static final RawAnimation IDLE = RawAnimation.begin().thenLoop("idle");
    private final AnimatableInstanceCache geoCache = GeckoLibUtil.createInstanceCache(this);

    private int pulseCooldown = 100;

    public OniOscuroEntity(EntityType<? extends HostileEntity> entityType, World world) {
        super(entityType, world);
        this.setBossBarColor(BossBar.Color.PURPLE);
        this.setBossBarStyle(BossBar.Style.NOTCHED_6);
        // Visual only: the loot table drops the pristine blade
        this.equipStack(EquipmentSlot.MAINHAND, new ItemStack(ModItems.MURAMASA));
        this.setEquipmentDropChance(EquipmentSlot.MAINHAND, 0.0f);
    }

    public static DefaultAttributeContainer.Builder createOniOscuroAttributes() {
        return HostileEntity.createHostileAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 700.0)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.35)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 15.0)
                .add(EntityAttributes.GENERIC_ARMOR, 24.0)
                .add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 0.9)
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 70.0);
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(0, new SwimGoal(this));
        this.goalSelector.add(1, new MeleeAttackGoal(this, 1.1, true));
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
            // The lights go out
            this.addStatusEffect(new StatusEffectInstance(StatusEffects.SPEED, 300, 0));
            if (this.getWorld() instanceof ServerWorld sw) {
                for (PlayerEntity p : sw.getPlayers()) {
                    if (p.squaredDistanceTo(this) <= 256) { // 16 blocks
                        p.addStatusEffect(new StatusEffectInstance(StatusEffects.DARKNESS, 100, 0));
                        p.addStatusEffect(new StatusEffectInstance(StatusEffects.WITHER, 60, 0));
                    }
                }
                sw.spawnParticles(ParticleTypes.LARGE_SMOKE, getX(), getY() + 1, getZ(), 60, 1.2, 1.0, 1.2, 0.05);
            }
        } else if (newPhase == 3) {
            this.getAttributeInstance(EntityAttributes.GENERIC_ATTACK_DAMAGE).setBaseValue(20.0);
            this.getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED).setBaseValue(0.40);
            this.addStatusEffect(new StatusEffectInstance(StatusEffects.STRENGTH, Integer.MAX_VALUE, 0));
        }
    }

    @Override
    public void mobTick() {
        super.mobTick();
        if (this.getWorld().isClient) return;
        if (pulseCooldown > 0) { pulseCooldown--; return; }

        if (currentPhase < 2 || this.getTarget() == null) {
            pulseCooldown = 100;
            return;
        }
        // Dark Pulse: withering burst around the oni
        pulseCooldown = currentPhase == 3 ? 80 : 130;
        if (this.getWorld() instanceof ServerWorld sw) {
            sw.spawnParticles(ParticleTypes.SOUL, getX(), getY() + 1, getZ(), 40, 2.0, 0.8, 2.0, 0.05);
            for (PlayerEntity p : sw.getPlayers()) {
                if (p.isAlive() && !p.isCreative() && !p.isSpectator()
                        && p.squaredDistanceTo(this) <= 36) { // 6 blocks
                    p.damage(this.getDamageSources().magic(), 5.0f);
                    p.addStatusEffect(new StatusEffectInstance(StatusEffects.WITHER, 60, 1), this);
                }
            }
        }
    }

    @Override
    public boolean isPushedByFluids() { return false; }

    @Override
    public int getXpToDrop() { return 1500; }

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
