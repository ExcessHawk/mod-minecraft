package com.mythicalswords.relics;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;

import java.util.List;

/**
 * Medusa's Eye - petrifies all nearby hostile creatures: heavy Slowness,
 * Weakness and Glowing, locking them in place briefly.
 */
public class MedusaEyeItem extends Item {

    private static final double RADIUS = 8.0;
    private static final int DURATION = 160; // 8s
    private static final int COOLDOWN = 500; // 25s

    public MedusaEyeItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        ItemStack stack = player.getStackInHand(hand);
        if (player.getItemCooldownManager().isCoolingDown(this)) {
            return TypedActionResult.fail(stack);
        }

        if (!world.isClient) {
            Box area = player.getBoundingBox().expand(RADIUS);
            List<LivingEntity> targets = world.getEntitiesByClass(LivingEntity.class, area,
                    e -> e != player && !(e instanceof PlayerEntity) && e.isAlive());
            for (LivingEntity e : targets) {
                e.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, DURATION, 5));
                e.addStatusEffect(new StatusEffectInstance(StatusEffects.WEAKNESS, DURATION, 2));
                e.addStatusEffect(new StatusEffectInstance(StatusEffects.MINING_FATIGUE, DURATION, 2));
                e.addStatusEffect(new StatusEffectInstance(StatusEffects.GLOWING, DURATION, 0));
                if (world instanceof ServerWorld sw) {
                    sw.spawnParticles(ParticleTypes.ASH, e.getX(), e.getY() + e.getHeight() / 2, e.getZ(),
                            20, 0.3, 0.5, 0.3, 0.02);
                }
            }
            if (world instanceof ServerWorld sw) {
                sw.spawnParticles(ParticleTypes.SCULK_SOUL, player.getX(), player.getY() + 1, player.getZ(),
                        30, 0.5, 0.5, 0.5, 0.05);
            }
            world.playSound(null, player.getX(), player.getY(), player.getZ(),
                    SoundEvents.ENTITY_ELDER_GUARDIAN_CURSE, SoundCategory.PLAYERS, 0.8f, 1.2f);
        }

        player.getItemCooldownManager().set(this, COOLDOWN);
        return TypedActionResult.success(stack, world.isClient());
    }
}
