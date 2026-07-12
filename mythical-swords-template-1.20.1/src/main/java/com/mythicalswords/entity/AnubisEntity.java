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
import net.minecraft.world.World;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;

/**
 * Anubis - Egyptian God of Death Boss Entity
 * Phase 4: Egyptian Mythology
 * 
 * Special Abilities:
 * - Death Touch: Applies Wither II on hit
 * - Summons mummy minions every 30 seconds
 * - Life Drain: Steals 3 HP per hit
 * - Decay Aura: Nearby players get Hunger effect
 * - Night Buff: 25% stronger at night
 */
public class AnubisEntity extends MythicalBossEntity implements GeoEntity {

    private static final RawAnimation IDLE = RawAnimation.begin().thenLoop("idle");

    private final AnimatableInstanceCache geoCache = GeckoLibUtil.createInstanceCache(this);
    private int minionSummonCooldown = 0;
    private int auraTickCounter = 0;

    public AnubisEntity(EntityType<? extends HostileEntity> entityType, World world) {
        super(entityType, world);
        this.setBossBarColor(BossBar.Color.PURPLE); // Dark/death theme
        this.setBossBarStyle(BossBar.Style.NOTCHED_10);

        // Equip Khopesh
        this.equipStack(EquipmentSlot.MAINHAND, new ItemStack(ModItems.KHOPESH));
        this.setEquipmentDropChance(EquipmentSlot.MAINHAND, 0.05f); // 5% drop rate
    }

    public static DefaultAttributeContainer.Builder createAnubisAttributes() {
        return HostileEntity.createHostileAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 700.0) // 350 Hearts
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.35)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 14.0) // 7 Hearts base
                .add(EntityAttributes.GENERIC_ARMOR, 22.0) // Heavy armor
                .add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 1.0) // Unstoppable
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
    public void tick() {
        super.tick();

        if (!this.getWorld().isClient) {
            // Summon minions every 600 ticks (30 seconds)
            if (minionSummonCooldown <= 0) {
                summonMummies();
                minionSummonCooldown = 600;
            } else {
                minionSummonCooldown--;
            }

            // Decay aura every 40 ticks (2 seconds)
            if (auraTickCounter <= 0) {
                applyDecayAura();
                auraTickCounter = 40;
            } else {
                auraTickCounter--;
            }
        }
    }

    @Override
    public boolean tryAttack(Entity target) {
        boolean success = super.tryAttack(target);

        if (success && target instanceof LivingEntity livingTarget) {
            // Death Touch: Apply Wither II
            livingTarget.addStatusEffect(new StatusEffectInstance(StatusEffects.WITHER, 100, 1), this);

            // Life Drain: Heal self for 3 HP
            float healthToHeal = 3.0f;
            this.heal(healthToHeal);

            // Night buff: Extra damage at night
            if (!this.getWorld().isDay()) {
                livingTarget.damage(this.getDamageSources().mobAttack(this), 3.5f); // +25% damage
            }
        }

        return success;
    }

    /**
     * Summons 2-3 zombie minions (mummies) around Anubis
     */
    private void summonMummies() {
        if (!(this.getWorld() instanceof ServerWorld serverWorld))
            return;

        int mummyCount = 2 + this.random.nextInt(2); // 2-3 mummies

        for (int i = 0; i < mummyCount; i++) {
            ZombieEntity mummy = EntityType.ZOMBIE.create(serverWorld);
            if (mummy != null) {
                // Spawn near Anubis
                double offsetX = (this.random.nextDouble() - 0.5) * 4;
                double offsetZ = (this.random.nextDouble() - 0.5) * 4;

                mummy.refreshPositionAndAngles(
                        this.getX() + offsetX,
                        this.getY(),
                        this.getZ() + offsetZ,
                        this.random.nextFloat() * 360.0F,
                        0.0F);

                // Give mummy some buffs
                mummy.addStatusEffect(new StatusEffectInstance(StatusEffects.SPEED, 1200, 0));
                mummy.addStatusEffect(new StatusEffectInstance(StatusEffects.STRENGTH, 1200, 0));

                serverWorld.spawnEntity(mummy);
            }
        }
    }

    /**
     * Applies Hunger effect to nearby players (decay aura)
     */
    private void applyDecayAura() {
        for (PlayerEntity player : this.getWorld().getPlayers()) {
            if (player.squaredDistanceTo(this) < 64.0) { // 8 block radius
                player.addStatusEffect(new StatusEffectInstance(StatusEffects.HUNGER, 60, 1));
            }
        }
    }

    @Override
    public boolean isPushedByFluids() {
        return false; // Gods walk on water
    }

    @Override
    protected void onPhaseTransition(int newPhase) {
        if (this.getWorld().isClient) return;
        if (newPhase == 2) {
            // The scales tip: the judge of the dead hardens
            this.addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, 400, 0));
            if (this.getWorld() instanceof net.minecraft.server.world.ServerWorld sw) {
                sw.spawnParticles(net.minecraft.particle.ParticleTypes.SOUL,
                        getX(), getY() + 1, getZ(), 60, 1.2, 1.0, 1.2, 0.05);
            }
        } else if (newPhase == 3) {
            this.getAttributeInstance(net.minecraft.entity.attribute.EntityAttributes.GENERIC_MOVEMENT_SPEED)
                    .setBaseValue(0.40);
            this.addStatusEffect(new StatusEffectInstance(StatusEffects.STRENGTH, Integer.MAX_VALUE, 0));
        }
    }

    @Override
    public int getXpToDrop() {
        return 2000; // Massive XP for Phase 4 boss
    }

    // ===== GeckoLib =====

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
