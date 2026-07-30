package com.mythicalswords.entity;

import com.mythicalswords.core.ModEntities;
import com.mythicalswords.weapons.ElementalAffinity;
import com.mythicalswords.weapons.MythicalWeaponItem;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.boss.BossBar;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleEffect;
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
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

/**
 * Celestial Guardian — final boss of the Celestial dimension.
 *
 * Its signature mechanic is the Elemental Ward: at any moment one affinity is
 * warded, and mythical weapons of that affinity barely scratch it. The ward
 * rotates on a timer and on every phase change, so the fight demands an
 * arsenal rather than one favourite sword.
 */
public class CelestialGuardianEntity extends MythicalBossEntity implements GeoEntity {

    private static final RawAnimation IDLE = RawAnimation.begin().thenLoop("idle");
    private final AnimatableInstanceCache geoCache = GeckoLibUtil.createInstanceCache(this);

    /** Damage taken from a warded affinity is scaled by this. */
    private static final float WARD_REDUCTION = 0.15f;
    /** Damage taken from the ward's opposing affinity is amplified by this. */
    private static final float COUNTER_BONUS = 1.5f;
    private static final int WARD_ROTATION_TICKS = 400; // 20s

    private ElementalAffinity ward = ElementalAffinity.DIVINE;
    private int wardTimer = WARD_ROTATION_TICKS;
    private int judgmentCooldown = 200;
    private int summonCooldown = 300;

    public CelestialGuardianEntity(EntityType<? extends HostileEntity> entityType, World world) {
        super(entityType, world);
        this.setBossBarColor(BossBar.Color.WHITE);
        this.setBossBarStyle(BossBar.Style.NOTCHED_20);
        this.setNoGravity(true); // it hovers
    }

    public static DefaultAttributeContainer.Builder createCelestialGuardianAttributes() {
        return HostileEntity.createHostileAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 2000.0)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.32)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 22.0)
                .add(EntityAttributes.GENERIC_ARMOR, 40.0)
                .add(EntityAttributes.GENERIC_ARMOR_TOUGHNESS, 12.0)
                .add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 1.0)
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 90.0);
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(1, new MeleeAttackGoal(this, 1.0, true));
        this.goalSelector.add(5, new WanderAroundFarGoal(this, 0.8));
        this.goalSelector.add(6, new LookAtEntityGoal(this, PlayerEntity.class, 16.0f));
        this.goalSelector.add(6, new LookAroundGoal(this));
        this.targetSelector.add(1, new RevengeGoal(this));
        this.targetSelector.add(2, new ActiveTargetGoal<>(this, PlayerEntity.class, true));
    }

    // ===== Elemental Ward =====

    public ElementalAffinity getWard() {
        return ward;
    }

    /** The affinity that counters the current ward (weakness to exploit). */
    private ElementalAffinity counterOf(ElementalAffinity warded) {
        return switch (warded) {
            case FIRE -> ElementalAffinity.ICE;
            case ICE -> ElementalAffinity.FIRE;
            case LIGHTNING -> ElementalAffinity.NATURE;
            case NATURE -> ElementalAffinity.LIGHTNING;
            case DIVINE -> ElementalAffinity.DARK;
            case DARK -> ElementalAffinity.DIVINE;
        };
    }

    private ParticleEffect wardParticle(ElementalAffinity affinity) {
        return switch (affinity) {
            case FIRE -> ParticleTypes.FLAME;
            case ICE -> ParticleTypes.SNOWFLAKE;
            case LIGHTNING -> ParticleTypes.ELECTRIC_SPARK;
            case DIVINE -> ParticleTypes.END_ROD;
            case DARK -> ParticleTypes.SOUL;
            case NATURE -> ParticleTypes.HAPPY_VILLAGER;
        };
    }

    private Formatting wardColor(ElementalAffinity affinity) {
        return switch (affinity) {
            case FIRE -> Formatting.RED;
            case ICE -> Formatting.AQUA;
            case LIGHTNING -> Formatting.YELLOW;
            case DIVINE -> Formatting.WHITE;
            case DARK -> Formatting.DARK_PURPLE;
            case NATURE -> Formatting.GREEN;
        };
    }

    private void rotateWard() {
        ElementalAffinity[] all = ElementalAffinity.values();
        ElementalAffinity next;
        do {
            next = all[this.getRandom().nextInt(all.length)];
        } while (next == ward && all.length > 1);
        ward = next;
        wardTimer = WARD_ROTATION_TICKS;

        this.triggerAnim("attack", "ward");
        this.getWorld().playSound(null, getX(), getY(), getZ(),
                SoundEvents.BLOCK_BEACON_POWER_SELECT, SoundCategory.HOSTILE, 1.4f, 1.0f);

        if (this.getWorld() instanceof ServerWorld sw) {
            sw.spawnParticles(wardParticle(ward), getX(), getY() + 2.0, getZ(), 80, 1.5, 1.5, 1.5, 0.1);
            Text msg = Text.translatable("message.mythicalswords.guardian.ward",
                    Text.translatable("affinity.mythicalswords." + ward.name())
                            .formatted(wardColor(ward)));
            for (PlayerEntity p : sw.getPlayers()) {
                if (p.squaredDistanceTo(this) <= 4096) { // 64 blocks
                    p.sendMessage(msg, true);
                }
            }
        }
    }

    @Override
    public boolean damage(DamageSource source, float amount) {
        if (!this.getWorld().isClient && source.getAttacker() instanceof LivingEntity attacker) {
            var weapon = attacker.getMainHandStack().getItem();
            if (weapon instanceof MythicalWeaponItem mythical) {
                ElementalAffinity used = mythical.getAffinity();
                if (used == ward) {
                    amount *= WARD_REDUCTION;
                    if (this.getWorld() instanceof ServerWorld sw) {
                        sw.spawnParticles(ParticleTypes.CRIT, getX(), getY() + 2, getZ(), 8, 0.6, 0.6, 0.6, 0.0);
                    }
                    if (attacker instanceof PlayerEntity p) {
                        p.sendMessage(Text.translatable("message.mythicalswords.guardian.warded")
                                .formatted(Formatting.GRAY), true);
                    }
                } else if (used == counterOf(ward)) {
                    amount *= COUNTER_BONUS;
                    if (this.getWorld() instanceof ServerWorld sw) {
                        sw.spawnParticles(wardParticle(used), getX(), getY() + 2, getZ(), 20, 0.8, 0.8, 0.8, 0.1);
                    }
                }
            }
        }
        return super.damage(source, amount);
    }

    // ===== Phases and abilities =====

    @Override
    protected void onPhaseTransition(int newPhase) {
        if (this.getWorld().isClient) return;
        rotateWard();

        if (newPhase == 2) {
            // Judgment of the heavens: blind and push everyone back
            if (this.getWorld() instanceof ServerWorld sw) {
                for (PlayerEntity p : sw.getPlayers()) {
                    if (p.squaredDistanceTo(this) <= 400) { // 20 blocks
                        p.addStatusEffect(new StatusEffectInstance(StatusEffects.BLINDNESS, 80, 0));
                        var push = p.getPos().subtract(this.getPos()).normalize().multiply(1.4);
                        p.addVelocity(push.x, 0.5, push.z);
                        p.velocityModified = true;
                    }
                }
                sw.spawnParticles(ParticleTypes.FLASH, getX(), getY() + 2, getZ(), 4, 1.0, 1.0, 1.0, 0.0);
            }
            this.addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, 400, 0));
            summonAllPantheons();
        } else if (newPhase == 3) {
            this.getAttributeInstance(EntityAttributes.GENERIC_ATTACK_DAMAGE).setBaseValue(30.0);
            this.getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED).setBaseValue(0.42);
            this.addStatusEffect(new StatusEffectInstance(StatusEffects.STRENGTH, Integer.MAX_VALUE, 1));
            summonAllPantheons();
        }
    }

    /** Calls one minion from every mythology at once. */
    private void summonAllPantheons() {
        summonMinions(ModEntities.DRAUGR, 1);
        summonMinions(ModEntities.ONI_MENOR, 1);
        summonMinions(ModEntities.MOMIA_SIRVIENTE, 1);
        summonMinions(ModEntities.GUERRERO_JAGUAR, 1);
        summonMinions(ModEntities.HOPLITA_ESPECTRAL, 1);
        summonMinions(ModEntities.SOLDADO_TERRACOTA, 1);
    }

    @Override
    public void mobTick() {
        super.mobTick();
        if (this.getWorld().isClient) return;

        // Ward rotation
        if (--wardTimer <= 0) {
            rotateWard();
        }
        // Ambient ward aura so players can read the current element
        if (this.age % 10 == 0 && this.getWorld() instanceof ServerWorld sw) {
            sw.spawnParticles(wardParticle(ward), getX(), getY() + 2.2, getZ(), 3, 1.0, 0.8, 1.0, 0.02);
        }

        LivingEntity target = this.getTarget();

        // Celestial Judgment: pillar of light on the target
        if (judgmentCooldown > 0) {
            judgmentCooldown--;
        } else if (target != null && currentPhase >= 2) {
            judgmentCooldown = currentPhase == 3 ? 120 : 200;
            triggerAttackAnim("special");
            if (this.getWorld() instanceof ServerWorld sw) {
                target.damage(this.getDamageSources().magic(), currentPhase == 3 ? 14.0f : 10.0f);
                for (int i = 0; i < 20; i++) {
                    sw.spawnParticles(ParticleTypes.END_ROD,
                            target.getX(), target.getY() + i * 0.6, target.getZ(), 3, 0.25, 0.1, 0.25, 0.01);
                }
                this.getWorld().playSound(null, target.getX(), target.getY(), target.getZ(),
                        SoundEvents.ENTITY_ILLUSIONER_CAST_SPELL, SoundCategory.HOSTILE, 1.5f, 0.7f);
            }
        }

        // Keeps calling reinforcements while enraged
        if (summonCooldown > 0) {
            summonCooldown--;
        } else if (target != null && currentPhase >= 3) {
            summonCooldown = 400;
            summonAllPantheons();
        }
    }

    /** Themed heavy attack: a shockwave of the currently warded element. */
    @Override
    protected void executeHeavyAttack() {
        if (!(this.getWorld() instanceof ServerWorld world)) return;
        double radius = heavyAttackRange();
        float damage = (float) this.getAttributeValue(EntityAttributes.GENERIC_ATTACK_DAMAGE)
                * heavyAttackDamageMultiplier();

        world.spawnParticles(wardParticle(ward), getX(), getY() + 0.5, getZ(),
                60, radius / 2, 0.4, radius / 2, 0.15);
        this.getWorld().playSound(null, getX(), getY(), getZ(),
                SoundEvents.ENTITY_WITHER_BREAK_BLOCK, SoundCategory.HOSTILE, 1.2f, 1.4f);

        for (PlayerEntity player : world.getPlayers()) {
            if (player.isAlive() && !player.isCreative() && !player.isSpectator()
                    && player.squaredDistanceTo(this) <= radius * radius) {
                player.damage(this.getDamageSources().mobAttack(this), damage);
                var push = player.getPos().subtract(this.getPos()).normalize().multiply(1.5).add(0, 0.6, 0);
                player.addVelocity(push.x, push.y, push.z);
                player.velocityModified = true;
                // The ward's element leaves its mark
                switch (ward) {
                    case FIRE -> player.setOnFireFor(5);
                    case ICE -> player.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 100, 2));
                    case LIGHTNING -> player.addStatusEffect(new StatusEffectInstance(StatusEffects.WEAKNESS, 120, 1));
                    case DARK -> player.addStatusEffect(new StatusEffectInstance(StatusEffects.BLINDNESS, 80, 0));
                    case NATURE -> player.addStatusEffect(new StatusEffectInstance(StatusEffects.POISON, 100, 1));
                    case DIVINE -> player.addStatusEffect(new StatusEffectInstance(StatusEffects.GLOWING, 200, 0));
                }
            }
        }
    }

    @Override
    protected double heavyAttackRange() { return 7.0; }

    @Override
    protected float heavyAttackDamageMultiplier() { return 1.3f; }

    @Override
    public boolean isPushedByFluids() { return false; }

    @Override
    public boolean isFireImmune() { return true; }

    @Override
    public boolean handleFallDamage(float fallDistance, float damageMultiplier, DamageSource damageSource) {
        return false;
    }

    @Override
    public int getXpToDrop() { return 5000; }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putString("Ward", ward.name());
        nbt.putInt("WardTimer", wardTimer);
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        if (nbt.contains("Ward")) {
            try {
                ward = ElementalAffinity.valueOf(nbt.getString("Ward"));
            } catch (IllegalArgumentException ignored) {
                ward = ElementalAffinity.DIVINE;
            }
        }
        wardTimer = nbt.contains("WardTimer") ? nbt.getInt("WardTimer") : WARD_ROTATION_TICKS;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "controller", 0, state -> state.setAndContinue(IDLE)));
        controllers.add(new AnimationController<>(this, "attack", 0, state -> PlayState.STOP)
                .triggerableAnim("melee", RawAnimation.begin().thenPlay("attack_melee"))
                .triggerableAnim("special", RawAnimation.begin().thenPlay("attack_special"))
                .triggerableAnim("ward", RawAnimation.begin().thenPlay("ward_shift")));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.geoCache;
    }
}
