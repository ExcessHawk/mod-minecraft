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
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;

public class ReyArturoEntity extends MythicalBossEntity implements GeoEntity {

    private static final RawAnimation REY_IDLE = RawAnimation.begin().thenLoop("idle");
    private final AnimatableInstanceCache geoCache = GeckoLibUtil.createInstanceCache(this);

    private int specialAttackCooldown = 0;

    public ReyArturoEntity(EntityType<? extends HostileEntity> entityType, World world) {
        super(entityType, world);
        this.setBossBarColor(BossBar.Color.YELLOW);
        this.setBossBarStyle(BossBar.Style.NOTCHED_10);
        this.equipStack(EquipmentSlot.MAINHAND, new ItemStack(ModItems.EXCALIBUR));
        this.setEquipmentDropChance(EquipmentSlot.MAINHAND, 1.0f);
    }

    public static DefaultAttributeContainer.Builder createReyArturoAttributes() {
        return HostileEntity.createHostileAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 600.0)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.30)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 12.0)
                .add(EntityAttributes.GENERIC_ARMOR, 20.0)
                .add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 1.0)
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 64.0);
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
    public void mobTick() {
        super.mobTick();
        if (!this.getWorld().isClient) {
            if (specialAttackCooldown > 0) specialAttackCooldown--;
            else performPhaseAbility();
        }
    }

    @Override
    protected void onPhaseTransition(int newPhase) {
        if (this.getWorld().isClient) return;

        if (newPhase == 2) {
            // Divine healing burst + speed
            this.heal(60.0f);
            this.addStatusEffect(new StatusEffectInstance(StatusEffects.SPEED, 400, 1));
            this.addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, 200, 0));

            if (this.getWorld() instanceof ServerWorld sw) {
                sw.spawnParticles(ParticleTypes.END_ROD,
                    this.getX(), this.getY() + 1, this.getZ(), 60, 0.5, 1.0, 0.5, 0.3);
            }
            broadcastPhaseMessage("The King rises! His wounds seal before your eyes!", Formatting.YELLOW);

        } else if (newPhase == 3) {
            // Enrage: double attack damage + permanent strength + regen
            this.getAttributeInstance(EntityAttributes.GENERIC_ATTACK_DAMAGE).setBaseValue(20.0);
            this.addStatusEffect(new StatusEffectInstance(StatusEffects.STRENGTH, Integer.MAX_VALUE, 1));
            this.addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, Integer.MAX_VALUE, 0));

            if (this.getWorld() instanceof ServerWorld sw) {
                sw.spawnParticles(ParticleTypes.TOTEM_OF_UNDYING,
                    this.getX(), this.getY() + 1, this.getZ(), 80, 0.6, 1.2, 0.6, 0.5);
            }
            broadcastPhaseMessage("FOR CAMELOT! FACE THE TRUE MIGHT OF KING ARTHUR!", Formatting.RED);
        }
    }

    private void performPhaseAbility() {
        if (currentPhase < 2) {
            specialAttackCooldown = 200;
            return;
        }
        LivingEntity target = this.getTarget();
        if (target == null) {
            specialAttackCooldown = 60;
            return;
        }

        // Royal Cleave: AOE sweep hitting all nearby players
        double radius = currentPhase == 3 ? 6.0 : 4.0;
        float damage = currentPhase == 3 ? 10.0f : 6.0f;
        specialAttackCooldown = currentPhase == 3 ? 80 : 120;

        if (this.getWorld() instanceof ServerWorld sw) {
            for (PlayerEntity player : sw.getPlayers()) {
                if (player.squaredDistanceTo(this) <= radius * radius && player.isAlive()) {
                    player.damage(sw.getDamageSources().mobAttack(this), damage);
                    player.takeKnockback(1.5, this.getX() - player.getX(), this.getZ() - player.getZ());
                }
            }
            sw.spawnParticles(ParticleTypes.SWEEP_ATTACK,
                this.getX(), this.getY() + 1, this.getZ(), 8, radius * 0.4, 0.2, radius * 0.4, 0);
            this.getWorld().playSound(null, this.getX(), this.getY(), this.getZ(),
                SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, SoundCategory.HOSTILE, 1.5f, 0.8f);
        }
    }

    private void broadcastPhaseMessage(String message, Formatting color) {
        Text msg = Text.literal("[Rey Arturo] ").formatted(Formatting.GOLD)
            .append(Text.literal(message).formatted(color));
        for (var player : this.getWorld().getPlayers()) {
            if (player.squaredDistanceTo(this) <= 4096) {
                player.sendMessage(msg, false);
            }
        }
    }

    @Override
    public boolean isPushedByFluids() { return false; }

    @Override
    public int getXpToDrop() { return 1500; }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "controller", 0, state -> state.setAndContinue(REY_IDLE)));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.geoCache;
    }
}
