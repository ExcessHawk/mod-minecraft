package com.mythicalswords.relics;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;

/**
 * Void Pearl - blink forward through the void up to 16 blocks (stops at walls),
 * granting brief fall immunity. No fall damage, no mob aggro reset abuse.
 */
public class VoidPearlItem extends Item {

    private static final double RANGE = 16.0;
    private static final int COOLDOWN = 120; // 6s

    public VoidPearlItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        ItemStack stack = player.getStackInHand(hand);

        if (player.getItemCooldownManager().isCoolingDown(this)) {
            return TypedActionResult.fail(stack);
        }

        if (!world.isClient) {
            Vec3d start = player.getEyePos();
            Vec3d look = player.getRotationVec(1.0f);
            Vec3d end = start.add(look.multiply(RANGE));
            HitResult hit = world.raycast(new RaycastContext(start, end,
                    RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.NONE, player));
            Vec3d dest = hit.getPos().subtract(look.multiply(1.0)); // back off from wall

            Vec3d from = player.getPos();
            player.requestTeleport(dest.x, dest.y - player.getStandingEyeHeight() + 0.2, dest.z);
            player.fallDistance = 0.0f;

            if (world instanceof ServerWorld serverWorld) {
                serverWorld.spawnParticles(ParticleTypes.REVERSE_PORTAL, from.x, from.y + 1, from.z,
                        40, 0.3, 0.6, 0.3, 0.4);
                serverWorld.spawnParticles(ParticleTypes.PORTAL, player.getX(), player.getY() + 1, player.getZ(),
                        40, 0.3, 0.6, 0.3, 0.4);
            }
            world.playSound(null, player.getX(), player.getY(), player.getZ(),
                    SoundEvents.ENTITY_ENDERMAN_TELEPORT, SoundCategory.PLAYERS, 1.0f, 0.8f);
        }

        player.getItemCooldownManager().set(this, COOLDOWN);
        return TypedActionResult.success(stack, world.isClient());
    }
}
