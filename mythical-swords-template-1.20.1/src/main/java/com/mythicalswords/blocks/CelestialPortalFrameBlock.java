package com.mythicalswords.blocks;

import com.mythicalswords.core.ModBlocks;
import com.mythicalswords.core.ModItems;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Celestial Portal Frame — build a 3x3 platform of these, then right-click the
 * centre with a Celestial Ingot to open the gate to the Celestial dimension.
 * The ingot is consumed; the portal appears in the air above the centre.
 */
public class CelestialPortalFrameBlock extends Block {

    public CelestialPortalFrameBlock(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos,
                              PlayerEntity player, Hand hand, BlockHitResult hit) {
        ItemStack held = player.getStackInHand(hand);
        if (!held.isOf(ModItems.CELESTIAL_INGOT)) {
            if (!world.isClient) {
                player.sendMessage(Text.translatable("message.mythicalswords.portal.needs_ingot")
                        .formatted(Formatting.AQUA), true);
            }
            return ActionResult.PASS;
        }

        if (!isCompletePlatform(world, pos)) {
            if (!world.isClient) {
                player.sendMessage(Text.translatable("message.mythicalswords.portal.incomplete")
                        .formatted(Formatting.RED), true);
            }
            return ActionResult.PASS;
        }

        if (world.isClient) {
            return ActionResult.SUCCESS;
        }

        // Light the gate: a portal column rises above the platform centre
        BlockPos portalPos = pos.up();
        if (!world.getBlockState(portalPos).isAir()) {
            player.sendMessage(Text.translatable("message.mythicalswords.portal.blocked")
                    .formatted(Formatting.RED), true);
            return ActionResult.PASS;
        }

        if (!player.isCreative()) {
            held.decrement(1);
        }
        world.setBlockState(portalPos, ModBlocks.CELESTIAL_PORTAL.getDefaultState());
        world.setBlockState(portalPos.up(), ModBlocks.CELESTIAL_PORTAL.getDefaultState());

        world.playSound(null, pos, SoundEvents.BLOCK_END_PORTAL_SPAWN, SoundCategory.BLOCKS, 0.8f, 1.4f);
        if (world instanceof ServerWorld sw) {
            sw.spawnParticles(ParticleTypes.END_ROD,
                    pos.getX() + 0.5, pos.getY() + 1.5, pos.getZ() + 0.5, 80, 0.8, 1.2, 0.8, 0.2);
        }
        player.sendMessage(Text.translatable("message.mythicalswords.portal.opened")
                .formatted(Formatting.LIGHT_PURPLE), true);
        return ActionResult.SUCCESS;
    }

    /** True when pos is the centre of a full 3x3 of frame blocks. */
    private boolean isCompletePlatform(World world, BlockPos centre) {
        for (int dx = -1; dx <= 1; dx++) {
            for (int dz = -1; dz <= 1; dz++) {
                if (!world.getBlockState(centre.add(dx, 0, dz)).isOf(this)) {
                    return false;
                }
            }
        }
        return true;
    }
}
