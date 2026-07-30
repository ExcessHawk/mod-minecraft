package com.mythicalswords.blocks;

import com.mythicalswords.MythicalSwords;
import com.mythicalswords.core.ModBlocks;
import net.fabricmc.fabric.api.dimension.v1.FabricDimensions;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionTypes;
import net.minecraft.world.TeleportTarget;

/**
 * The gate itself. Walking in sends you to the Celestial dimension (or back to
 * the Overworld), building a small landing platform on arrival so nobody drops
 * straight into the void.
 */
public class CelestialPortalBlock extends Block {

    public static final RegistryKey<World> CELESTIAL_WORLD = RegistryKey.of(
            RegistryKeys.WORLD, new Identifier(MythicalSwords.MOD_ID, "celestial"));

    /** Cooldown so the player doesn't ping-pong between dimensions. */
    private static final String NBT_COOLDOWN = "MythicalPortalCooldown";

    public CelestialPortalBlock(Settings settings) {
        super(settings);
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return VoxelShapes.empty(); // walk straight through
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return VoxelShapes.fullCube();
    }

    @Override
    public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
        if (world.isClient || !(world instanceof ServerWorld serverWorld)) return;
        if (!(entity instanceof PlayerEntity player)) return;
        if (player.hasPortalCooldown()) return;

        ServerWorld target = resolveTarget(serverWorld);
        if (target == null) return;

        player.resetPortalCooldown();
        BlockPos landing = findLanding(target, player.getBlockPos());
        FabricDimensions.teleport(player, target,
                new TeleportTarget(new Vec3d(landing.getX() + 0.5, landing.getY(), landing.getZ() + 0.5),
                        Vec3d.ZERO, player.getYaw(), player.getPitch()));
    }

    /** Celestial <-> Overworld. From anywhere else, go to Celestial. */
    private ServerWorld resolveTarget(ServerWorld from) {
        var server = from.getServer();
        if (from.getRegistryKey().equals(CELESTIAL_WORLD)) {
            return server.getWorld(World.OVERWORLD);
        }
        return server.getWorld(CELESTIAL_WORLD);
    }

    /**
     * Finds solid ground near the destination; if there is none (floating
     * islands leave plenty of empty sky), lay a small celestial platform.
     */
    private BlockPos findLanding(ServerWorld world, BlockPos origin) {
        BlockPos scan = new BlockPos(origin.getX(), 0, origin.getZ());
        int top = world.getTopY();
        for (int y = top - 1; y > world.getBottomY(); y--) {
            BlockPos ground = new BlockPos(scan.getX(), y, scan.getZ());
            if (!world.getBlockState(ground).isAir()
                    && world.getBlockState(ground.up()).isAir()
                    && world.getBlockState(ground.up(2)).isAir()) {
                return ground.up();
            }
        }

        // Nothing to stand on — build a 3x3 pad at a safe height
        int padY = Math.min(top - 4, 64);
        BlockPos pad = new BlockPos(scan.getX(), padY, scan.getZ());
        for (int dx = -1; dx <= 1; dx++) {
            for (int dz = -1; dz <= 1; dz++) {
                world.setBlockState(pad.add(dx, 0, dz), ModBlocks.CELESTIAL_BRICKS.getDefaultState());
            }
        }
        return pad.up();
    }

    @Override
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        if (random.nextInt(2) != 0) return;
        world.addParticle(ParticleTypes.PORTAL,
                pos.getX() + random.nextDouble(),
                pos.getY() + random.nextDouble(),
                pos.getZ() + random.nextDouble(),
                (random.nextDouble() - 0.5) * 0.4, random.nextDouble() * 0.3,
                (random.nextDouble() - 0.5) * 0.4);
        if (random.nextInt(6) == 0) {
            world.addParticle(ParticleTypes.END_ROD,
                    pos.getX() + random.nextDouble(), pos.getY() + random.nextDouble(),
                    pos.getZ() + random.nextDouble(), 0.0, 0.02, 0.0);
        }
    }
}
