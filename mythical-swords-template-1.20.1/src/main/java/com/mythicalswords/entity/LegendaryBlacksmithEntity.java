package com.mythicalswords.entity;

import com.mythicalswords.core.ModItems;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.boss.BossBar;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.world.World;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;

/**
 * Legendary Blacksmith Mini-Boss Entity
 * 300 HP, drops Soul of the Swordsmith
 * Master craftsman who forged legendary weapons
 */
public class LegendaryBlacksmithEntity extends MythicalBossEntity implements GeoEntity {

    private static final RawAnimation SMITH_IDLE = RawAnimation.begin().thenLoop("idle");
    private final AnimatableInstanceCache geoCache = GeckoLibUtil.createInstanceCache(this);

    public LegendaryBlacksmithEntity(EntityType<? extends HostileEntity> entityType, World world) {
        super(entityType, world);
        this.setBossBarColor(BossBar.Color.RED); // Fire and forge
        this.setBossBarStyle(BossBar.Style.NOTCHED_6); // Mini-boss style
        
        // Equip the blacksmith's own hammer
        this.equipStack(EquipmentSlot.MAINHAND, new ItemStack(com.mythicalswords.core.ModItems.BLACKSMITH_HAMMER));
        
        // Set drop chances (100% for main hand)
        this.setEquipmentDropChance(EquipmentSlot.MAINHAND, 1.0f);
    }

    public static DefaultAttributeContainer.Builder createLegendaryBlacksmithAttributes() {
        return HostileEntity.createHostileAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 300.0) // 150 Hearts
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.30)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 10.0) // 5 Hearts
                .add(EntityAttributes.GENERIC_ARMOR, 15.0) // Medium armor
                .add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 0.8) // High resistance
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 50.0);
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
    public boolean isPushedByFluids() {
        return false; // Legendary Blacksmith is sturdy
    }

    @Override
    public int getXpToDrop() {
        return 500; // Mini-boss XP drop
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "controller", 0, state -> state.setAndContinue(SMITH_IDLE)));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.geoCache;
    }
}
