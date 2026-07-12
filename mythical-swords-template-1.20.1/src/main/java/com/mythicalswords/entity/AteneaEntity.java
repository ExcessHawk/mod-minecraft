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
 * Atenea (Athena) Boss Entity - Greek Goddess of Wisdom and War
 * 600 HP, 2 phases, drops Aegis Edge
 */
public class AteneaEntity extends MythicalBossEntity implements GeoEntity {

    private static final RawAnimation ATENEA_IDLE = RawAnimation.begin().thenLoop("idle");
    private final AnimatableInstanceCache geoCache = GeckoLibUtil.createInstanceCache(this);

    public AteneaEntity(EntityType<? extends HostileEntity> entityType, World world) {
        super(entityType, world);
        this.setBossBarColor(BossBar.Color.WHITE); // Wisdom and purity
        this.setBossBarStyle(BossBar.Style.NOTCHED_10);

        // Equip Aegis Edge in main hand
        this.equipStack(EquipmentSlot.MAINHAND, new ItemStack(ModItems.AEGIS_EDGE));

        // Set drop chances (100% for main hand so Aegis Edge always drops)
        this.setEquipmentDropChance(EquipmentSlot.MAINHAND, 1.0f);
    }

    public static DefaultAttributeContainer.Builder createAteneaAttributes() {
        return HostileEntity.createHostileAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 600.0) // 300 Hearts
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.35)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 13.0) // 6.5 Hearts
                .add(EntityAttributes.GENERIC_ARMOR, 22.0) // Heavy armor
                .add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 1.0) // Unstoppable
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

    private int spearCooldown = 100;

    @Override
    protected void onPhaseTransition(int newPhase) {
        if (this.getWorld().isClient) return;
        if (newPhase == 2) {
            // Strategy of war: shield herself, sap the mortals' strength
            this.addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, 400, 0));
            this.addStatusEffect(new StatusEffectInstance(StatusEffects.SPEED, 400, 0));
            if (this.getWorld() instanceof ServerWorld sw) {
                for (var p : sw.getPlayers()) {
                    if (p.squaredDistanceTo(this) <= 256) { // 16 blocks
                        p.addStatusEffect(new StatusEffectInstance(StatusEffects.WEAKNESS, 120, 0));
                    }
                }
                sw.spawnParticles(ParticleTypes.ENCHANT, getX(), getY() + 2, getZ(), 80, 1.0, 1.0, 1.0, 0.5);
            }
        } else if (newPhase == 3) {
            this.heal(40.0f);
            this.addStatusEffect(new StatusEffectInstance(StatusEffects.STRENGTH, Integer.MAX_VALUE, 1));
        }
    }

    @Override
    public void mobTick() {
        super.mobTick();
        if (this.getWorld().isClient) return;
        if (spearCooldown > 0) { spearCooldown--; return; }

        LivingEntity target = this.getTarget();
        if (currentPhase < 2 || target == null) {
            spearCooldown = 100;
            return;
        }
        // Spear of Light: marks and pierces the target from afar
        spearCooldown = currentPhase == 3 ? 80 : 120;
        if (this.getWorld() instanceof ServerWorld sw) {
            target.damage(this.getDamageSources().magic(), 7.0f);
            target.addStatusEffect(new StatusEffectInstance(StatusEffects.GLOWING, 80, 0), this);
            // Particle line from Athena to the target
            var from = this.getPos().add(0, 1.5, 0);
            var to = target.getPos().add(0, 1.0, 0);
            var step = to.subtract(from).multiply(1.0 / 10);
            for (int i = 0; i <= 10; i++) {
                var p = from.add(step.multiply(i));
                sw.spawnParticles(ParticleTypes.END_ROD, p.x, p.y, p.z, 1, 0.02, 0.02, 0.02, 0.0);
            }
        }
    }

    @Override
    public boolean isPushedByFluids() {
        return false; // Athena walks through water
    }

    @Override
    public int getXpToDrop() {
        return 1800; // Massive XP drop
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "controller", 0, state -> state.setAndContinue(ATENEA_IDLE)));
        controllers.add(new AnimationController<>(this, "attack", 0, state -> software.bernie.geckolib.core.object.PlayState.STOP)
            .triggerableAnim("melee", RawAnimation.begin().thenPlay("attack_melee"))
            .triggerableAnim("special", RawAnimation.begin().thenPlay("attack_special")));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.geoCache;
    }
}
