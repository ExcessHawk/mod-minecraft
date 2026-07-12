package com.mythicalswords.relics;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LightningEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
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
 * Storm Vial - calls down a lightning bolt where the player is looking
 * (up to 40 blocks), in any weather. Thor in a bottle.
 */
public class StormVialItem extends Item {

    private static final double RANGE = 40.0;
    private static final int COOLDOWN = 200; // 10s

    public StormVialItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        ItemStack stack = player.getStackInHand(hand);
        if (player.getItemCooldownManager().isCoolingDown(this)) {
            return TypedActionResult.fail(stack);
        }

        if (!world.isClient && world instanceof ServerWorld serverWorld) {
            Vec3d start = player.getEyePos();
            Vec3d look = player.getRotationVec(1.0f);
            Vec3d end = start.add(look.multiply(RANGE));
            HitResult hit = world.raycast(new RaycastContext(start, end,
                    RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.ANY, player));
            Vec3d pos = hit.getPos();

            LightningEntity bolt = EntityType.LIGHTNING_BOLT.create(serverWorld);
            if (bolt != null) {
                bolt.refreshPositionAfterTeleport(pos.x, pos.y, pos.z);
                bolt.setCosmetic(false);
                serverWorld.spawnEntity(bolt);
            }
            world.playSound(null, player.getX(), player.getY(), player.getZ(),
                    SoundEvents.ENTITY_LIGHTNING_BOLT_THUNDER, SoundCategory.PLAYERS, 1.0f, 1.0f);
        }

        player.getItemCooldownManager().set(this, COOLDOWN);
        return TypedActionResult.success(stack, world.isClient());
    }
}
