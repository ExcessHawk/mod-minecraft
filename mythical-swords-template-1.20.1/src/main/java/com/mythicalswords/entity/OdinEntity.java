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
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;

public class OdinEntity extends MythicalBossEntity implements GeoEntity {

    private static final RawAnimation ODIN_IDLE = RawAnimation.begin().thenLoop("idle");
    private final AnimatableInstanceCache geoCache = GeckoLibUtil.createInstanceCache(this);

    private int specialAttackCooldown = 0;

    public OdinEntity(EntityType<? extends HostileEntity> entityType, World world) {
        super(entityType, world);
        this.setBossBarColor(BossBar.Color.BLUE);
        this.setBossBarStyle(BossBar.Style.NOTCHED_10);
        this.equipStack(EquipmentSlot.MAINHAND, new ItemStack(ModItems.GUNGNIR));
        this.setEquipmentDropChance(EquipmentSlot.MAINHAND, 1.0f);
    }

    public static DefaultAttributeContainer.Builder createOdinAttributes() {
        return HostileEntity.createHostileAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 800.0)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.30)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 15.0)
                .add(EntityAttributes.GENERIC_ARMOR, 25.0)
                .add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 1.0)
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 80.0);
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
            // The Allfather's wisdom: blinds nearby players, gains ranged awareness
            this.addStatusEffect(new StatusEffectInstance(StatusEffects.STRENGTH, 300, 1));
            this.addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, 300, 0));

            if (this.getWorld() instanceof ServerWorld sw) {
                // Blind nearby players for 5 seconds
                for (PlayerEntity p : sw.getPlayers()) {
                    if (p.squaredDistanceTo(this) <= 1024) {
                        p.addStatusEffect(new StatusEffectInstance(StatusEffects.BLINDNESS, 100, 0));
                        p.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 100, 1));
                    }
                }
                sw.spawnParticles(ParticleTypes.ELECTRIC_SPARK,
                    this.getX(), this.getY() + 1, this.getZ(), 60, 0.8, 1.0, 0.8, 0.3);
            }
            broadcastPhaseMessage("The Allfather unleashes his wisdom upon you!", Formatting.AQUA);

        } else if (newPhase == 3) {
            // Gungnir Fury: spear never misses, massive attack + call lightning
            this.getAttributeInstance(EntityAttributes.GENERIC_ATTACK_DAMAGE).setBaseValue(25.0);
            this.getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED).setBaseValue(0.40);
            this.addStatusEffect(new StatusEffectInstance(StatusEffects.STRENGTH, Integer.MAX_VALUE, 2));

            if (this.getWorld() instanceof ServerWorld sw) {
                sw.spawnParticles(ParticleTypes.TOTEM_OF_UNDYING,
                    this.getX(), this.getY() + 1, this.getZ(), 80, 0.8, 1.5, 0.8, 0.5);
                // Strike a lightning at Odin's position as dramatic effect
                LightningEntity lightning = EntityType.LIGHTNING_BOLT.create(sw);
                if (lightning != null) {
                    lightning.setPosition(this.getPos());
                    lightning.setCosmetic(true); // cosmetic only, won't burn
                    sw.spawnEntity(lightning);
                }
            }
            broadcastPhaseMessage("GUNGNIR WILL FIND YOU! NONE ESCAPE THE ALLFATHER!", Formatting.RED);
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

        specialAttackCooldown = currentPhase == 3 ? 60 : 100;

        if (this.getWorld() instanceof ServerWorld sw) {
            if (currentPhase == 2) {
                // Gungnir throw: lightning strike at target location
                strikeLightningAt(sw, target.getPos());
                this.getWorld().playSound(null, target.getX(), target.getY(), target.getZ(),
                    SoundEvents.ENTITY_LIGHTNING_BOLT_THUNDER, SoundCategory.HOSTILE, 1.2f, 1.0f);

            } else {
                // Phase 3: triple lightning burst
                for (int i = 0; i < 3; i++) {
                    double ox = (this.getRandom().nextDouble() - 0.5) * 4;
                    double oz = (this.getRandom().nextDouble() - 0.5) * 4;
                    strikeLightningAt(sw, target.getPos().add(ox, 0, oz));
                }
                // Also blind target briefly
                if (target instanceof PlayerEntity player) {
                    player.addStatusEffect(new StatusEffectInstance(StatusEffects.BLINDNESS, 40, 0));
                }
            }
        }
    }

    private void strikeLightningAt(ServerWorld world, Vec3d pos) {
        LightningEntity lightning = EntityType.LIGHTNING_BOLT.create(world);
        if (lightning != null) {
            lightning.setPosition(pos);
            lightning.setCosmetic(false);
            world.spawnEntity(lightning);
        }
    }

    private void broadcastPhaseMessage(String message, Formatting color) {
        Text msg = Text.literal("[Odin] ").formatted(Formatting.BLUE)
            .append(Text.literal(message).formatted(color));
        for (var player : this.getWorld().getPlayers()) {
            if (player.squaredDistanceTo(this) <= 6400) { // 80 blocks
                player.sendMessage(msg, false);
            }
        }
    }

    @Override
    public boolean isPushedByFluids() { return false; }

    @Override
    public int getXpToDrop() { return 2000; }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "controller", 0, state -> state.setAndContinue(ODIN_IDLE)));
        controllers.add(new AnimationController<>(this, "attack", 0, state -> software.bernie.geckolib.core.object.PlayState.STOP)
            .triggerableAnim("melee", RawAnimation.begin().thenPlay("attack_melee"))
            .triggerableAnim("special", RawAnimation.begin().thenPlay("attack_special")));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.geoCache;
    }
}
