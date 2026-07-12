package com.mythicalswords.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.ai.goal.LookAroundGoal;
import net.minecraft.entity.ai.goal.LookAtEntityGoal;
import net.minecraft.entity.ai.goal.WanderAroundGoal;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;

/**
 * Rideable flying dragon mount (custom, not the vanilla Ender Dragon).
 * - Right-click with a Saddle to saddle it, then right-click to ride.
 * - While ridden: WASD steers in the direction you look, look up/down to
 *   climb/dive while holding forward, Sneak descends.
 */
public class RideableDragonEntity extends PathAwareEntity implements GeoEntity {

    private static final TrackedData<Boolean> SADDLED =
            DataTracker.registerData(RideableDragonEntity.class, TrackedDataHandlerRegistry.BOOLEAN);

    private static final RawAnimation FLY = RawAnimation.begin().thenLoop("fly");
    private static final RawAnimation IDLE = RawAnimation.begin().thenLoop("idle");

    private final AnimatableInstanceCache geoCache = GeckoLibUtil.createInstanceCache(this);

    public RideableDragonEntity(EntityType<? extends PathAwareEntity> entityType, World world) {
        super(entityType, world);
        this.setNoGravity(true);
    }

    public static DefaultAttributeContainer.Builder createDragonAttributes() {
        return MobEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 200.0)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.3)
                .add(EntityAttributes.GENERIC_FLYING_SPEED, 0.8)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 12.0)
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 48.0);
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(2, new WanderAroundGoal(this, 0.8));
        this.goalSelector.add(6, new LookAtEntityGoal(this, PlayerEntity.class, 10.0f));
        this.goalSelector.add(7, new LookAroundGoal(this));
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(SADDLED, false);
    }

    public boolean isSaddled() {
        return this.dataTracker.get(SADDLED);
    }

    public void setSaddled(boolean saddled) {
        this.dataTracker.set(SADDLED, saddled);
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putBoolean("Saddled", this.isSaddled());
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        this.setSaddled(nbt.getBoolean("Saddled"));
    }

    @Override
    public ActionResult interactMob(PlayerEntity player, Hand hand) {
        ItemStack stack = player.getStackInHand(hand);

        if (!this.isSaddled() && stack.isOf(Items.SADDLE)) {
            if (!player.getAbilities().creativeMode) stack.decrement(1);
            this.setSaddled(true);
            this.getWorld().playSound(null, this.getX(), this.getY(), this.getZ(),
                    SoundEvents.ENTITY_HORSE_SADDLE, this.getSoundCategory(), 1.0f, 1.0f);
            return ActionResult.success(this.getWorld().isClient);
        }

        if (this.isSaddled() && !player.shouldCancelInteraction() && this.getPassengerList().isEmpty()) {
            if (!this.getWorld().isClient) {
                player.startRiding(this);
            }
            return ActionResult.success(this.getWorld().isClient);
        }

        return super.interactMob(player, hand);
    }

    @Nullable
    @Override
    public LivingEntity getControllingPassenger() {
        if (this.isSaddled() && this.getFirstPassenger() instanceof PlayerEntity player) {
            return player;
        }
        return null;
    }

    @Override
    protected void updatePassengerPosition(Entity passenger, Entity.PositionUpdater positionUpdater) {
        if (this.hasPassenger(passenger)) {
            double y = this.getY() + this.getMountedHeightOffset() + passenger.getHeightOffset();
            positionUpdater.accept(passenger, this.getX(), y, this.getZ());
        }
    }

    @Override
    public double getMountedHeightOffset() {
        return this.getHeight() * 0.7;
    }

    @Override
    public void travel(Vec3d movementInput) {
        if (this.isLogicalSideForUpdatingMovement()
                && this.getControllingPassenger() instanceof PlayerEntity player) {
            // Steer to rider's facing
            this.setYaw(player.getYaw());
            this.setPitch(player.getPitch() * 0.5f);
            this.bodyYaw = this.headYaw = this.getYaw();

            float speed = (float) this.getAttributeValue(EntityAttributes.GENERIC_FLYING_SPEED);
            Vec3d look = player.getRotationVector();
            double forward = player.forwardSpeed;
            double strafe = player.sidewaysSpeed * 0.5;

            Vec3d vel = Vec3d.ZERO;
            if (forward != 0) {
                // full 3D movement along where the rider looks
                vel = vel.add(look.multiply(forward));
            }
            if (strafe != 0) {
                vel = vel.add(look.crossProduct(new Vec3d(0, 1, 0)).normalize().multiply(strafe));
            }
            if (player.isSneaking()) {
                vel = vel.add(0, -1, 0);
            }
            vel = vel.multiply(speed);
            this.setVelocity(vel);
            this.move(MovementType.SELF, this.getVelocity());
            this.setVelocity(this.getVelocity().multiply(0.88));
            this.updateLimbs(false);
        } else {
            super.travel(movementInput);
        }
    }

    @Override
    public boolean isPushedByFluids() {
        return false;
    }

    @Override
    protected boolean isImmobile() {
        return false;
    }

    // ===== GeckoLib =====
    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "controller", 5, state -> {
            if (this.getVelocity().horizontalLengthSquared() > 0.0015 || this.getControllingPassenger() != null) {
                return state.setAndContinue(FLY);
            }
            return state.setAndContinue(IDLE);
        }));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.geoCache;
    }
}
