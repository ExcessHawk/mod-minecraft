package com.mythicalswords.relics;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.UseAction;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

import java.util.List;
import java.util.function.Supplier;

/**
 * A drinkable relic that heals and applies a set of status effects on use,
 * then goes on cooldown and is consumed (unless in creative).
 */
public class ConsumableRelicItem extends Item {

    private final float healAmount;
    private final int cooldownTicks;
    private final List<Supplier<StatusEffectInstance>> effects;
    private final net.minecraft.particle.ParticleEffect particle;

    public ConsumableRelicItem(Settings settings, float healAmount, int cooldownTicks,
            net.minecraft.particle.ParticleEffect particle, List<Supplier<StatusEffectInstance>> effects) {
        super(settings);
        this.healAmount = healAmount;
        this.cooldownTicks = cooldownTicks;
        this.particle = particle;
        this.effects = effects;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        ItemStack stack = player.getStackInHand(hand);

        if (player.getItemCooldownManager().isCoolingDown(this)) {
            return TypedActionResult.fail(stack);
        }

        if (!world.isClient) {
            if (healAmount > 0) {
                player.heal(healAmount);
            }
            for (Supplier<StatusEffectInstance> effect : effects) {
                player.addStatusEffect(effect.get());
            }
            if (world instanceof ServerWorld serverWorld && particle != null) {
                serverWorld.spawnParticles(particle,
                        player.getX(), player.getY() + 1.0, player.getZ(),
                        30, 0.4, 0.6, 0.4, 0.1);
            }
        }

        world.playSound(null, player.getX(), player.getY(), player.getZ(),
                SoundEvents.ENTITY_GENERIC_DRINK, SoundCategory.PLAYERS, 1.0f, 1.0f);
        world.playSound(null, player.getX(), player.getY(), player.getZ(),
                SoundEvents.BLOCK_BEACON_ACTIVATE, SoundCategory.PLAYERS, 0.4f, 1.6f);

        player.getItemCooldownManager().set(this, cooldownTicks);
        if (!player.isCreative()) {
            stack.decrement(1);
        }
        return TypedActionResult.success(stack, world.isClient());
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.DRINK;
    }
}
