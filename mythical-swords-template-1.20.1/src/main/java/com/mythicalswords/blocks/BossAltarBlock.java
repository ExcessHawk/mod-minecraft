package com.mythicalswords.blocks;

import com.mythicalswords.core.ModEntities;
import com.mythicalswords.entity.MythicalBossEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
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
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

/**
 * Boss Altar Block - NBT-driven boss summoning.
 * Each altar stores a "BossId" in its block entity so different structures
 * spawn different bosses when a player right-clicks with a Nether Star.
 */
public class BossAltarBlock extends BlockWithEntity {

    /**
     * Registry mapping boss ID strings to their EntityType + display name.
     */
    private record BossEntry(EntityType<?> type, String displayName, Formatting color) {}

    private static final Map<String, BossEntry> BOSS_REGISTRY = new HashMap<>();

    static {
        BOSS_REGISTRY.put("rey_arturo",   new BossEntry(ModEntities.REY_ARTURO,   "Rey Arturo",   Formatting.GOLD));
        BOSS_REGISTRY.put("odin",         new BossEntry(ModEntities.ODIN,         "Odín",         Formatting.AQUA));
        BOSS_REGISTRY.put("loki",         new BossEntry(ModEntities.LOKI,         "Loki",         Formatting.GREEN));
        BOSS_REGISTRY.put("atenea",       new BossEntry(ModEntities.ATENEA,       "Atenea",       Formatting.LIGHT_PURPLE));
        BOSS_REGISTRY.put("susanoo",      new BossEntry(ModEntities.SUSANOO,      "Susanoo",      Formatting.RED));
        BOSS_REGISTRY.put("oni_oscuro",   new BossEntry(ModEntities.ONI_OSCURO,   "Oni Oscuro",   Formatting.DARK_RED));
        BOSS_REGISTRY.put("izanagi",      new BossEntry(ModEntities.IZANAGI,      "Izanagi",      Formatting.WHITE));
        BOSS_REGISTRY.put("quetzalcoatl", new BossEntry(ModEntities.QUETZALCOATL, "Quetzalcóatl", Formatting.GREEN));
        BOSS_REGISTRY.put("anubis",       new BossEntry(ModEntities.ANUBIS,       "Anubis",       Formatting.DARK_PURPLE));
        BOSS_REGISTRY.put("ra",           new BossEntry(ModEntities.RA,           "Ra",           Formatting.YELLOW));
        BOSS_REGISTRY.put("sun_wukong",   new BossEntry(ModEntities.SUN_WUKONG,   "Sun Wukong",   Formatting.GOLD));
        BOSS_REGISTRY.put("celestial_guardian",
                new BossEntry(ModEntities.CELESTIAL_GUARDIAN, "Guardián Celestial", Formatting.AQUA));
    }

    public BossAltarBlock(Settings settings) {
        super(settings);
    }

    // ===== BlockWithEntity overrides =====

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new BossAltarBlockEntity(pos, state);
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        // Render as a normal block, not invisible like default BlockWithEntity
        return BlockRenderType.MODEL;
    }

    // ===== Interaction =====

    @Override
    @SuppressWarnings("deprecation")
    public ActionResult onUse(BlockState state, World world, BlockPos pos,
                              PlayerEntity player, Hand hand, BlockHitResult hit) {

        if (world.isClient) {
            return ActionResult.SUCCESS;
        }

        ItemStack heldItem = player.getStackInHand(hand);

        // Require a Nether Star
        if (!heldItem.isOf(Items.NETHER_STAR)) {
            String hint = getHintMessage(world, pos);
            player.sendMessage(Text.literal(hint).formatted(Formatting.GOLD), true);
            return ActionResult.PASS;
        }

        // Resolve which boss this altar should spawn
        String bossId = getBossId(world, pos);
        BossEntry entry = BOSS_REGISTRY.get(bossId);

        if (entry == null) {
            // Fallback: unknown or empty boss ID — default to Rey Arturo
            entry = BOSS_REGISTRY.get("rey_arturo");
            bossId = "rey_arturo";
        }

        // Check if that boss type already exists nearby
        if (isBossNearby(world, pos, entry.type)) {
            player.sendMessage(
                Text.literal("A powerful presence already exists nearby...")
                    .formatted(Formatting.RED),
                false
            );
            return ActionResult.FAIL;
        }

        // Consume the Nether Star
        if (!player.isCreative()) {
            heldItem.decrement(1);
        }

        // Spawn the boss
        spawnBoss((ServerWorld) world, pos, entry);

        return ActionResult.SUCCESS;
    }

    // ===== Helpers =====

    private String getBossId(World world, BlockPos pos) {
        if (world.getBlockEntity(pos) instanceof BossAltarBlockEntity altar) {
            return altar.getBossId();
        }
        return "";
    }

    private String getHintMessage(World world, BlockPos pos) {
        String bossId = getBossId(world, pos);
        BossEntry entry = BOSS_REGISTRY.get(bossId);
        if (entry != null) {
            return "Place a Nether Star upon the altar to summon " + entry.displayName + "...";
        }
        return "Place a Nether Star upon the altar to summon the guardian...";
    }

    private boolean isBossNearby(World world, BlockPos pos, EntityType<?> bossType) {
        return !world.getEntitiesByType(
            bossType,
            new net.minecraft.util.math.Box(pos).expand(50),
            entity -> true
        ).isEmpty();
    }

    @SuppressWarnings("unchecked")
    private void spawnBoss(ServerWorld serverWorld, BlockPos pos, BossEntry entry) {
        // Remove the altar block (it disappears when boss is summoned)
        serverWorld.removeBlock(pos, false);

        BlockPos spawnPos = pos.up();

        // Create the boss entity
        var boss = entry.type.create(serverWorld);
        if (boss == null) return;

        boss.refreshPositionAndAngles(
            spawnPos.getX() + 0.5,
            spawnPos.getY(),
            spawnPos.getZ() + 0.5,
            0, 0
        );

        // Set custom name
        boss.setCustomName(Text.literal(entry.displayName).formatted(entry.color, Formatting.BOLD));

        // Spawn the boss
        serverWorld.spawnEntity(boss);

        // Dramatic particle ring
        for (int i = 0; i < 100; i++) {
            double angle = (i / 100.0) * Math.PI * 2;
            double radius = 2.0;
            double x = spawnPos.getX() + 0.5 + Math.cos(angle) * radius;
            double z = spawnPos.getZ() + 0.5 + Math.sin(angle) * radius;

            serverWorld.spawnParticles(
                ParticleTypes.SOUL_FIRE_FLAME,
                x, spawnPos.getY(), z,
                1,
                0, 0.5, 0,
                0.05
            );
        }

        // Central explosion effect
        serverWorld.spawnParticles(
            ParticleTypes.EXPLOSION_EMITTER,
            spawnPos.getX() + 0.5,
            spawnPos.getY() + 1,
            spawnPos.getZ() + 0.5,
            1, 0, 0, 0, 0
        );

        // Sound
        serverWorld.playSound(
            null, pos,
            SoundEvents.ENTITY_WITHER_SPAWN,
            SoundCategory.HOSTILE,
            1.0f, 0.8f
        );

        // Announce to nearby players
        serverWorld.getPlayers().forEach(p -> {
            if (p.squaredDistanceTo(pos.getX(), pos.getY(), pos.getZ()) < 50 * 50) {
                p.sendMessage(
                    Text.literal(entry.displayName + " has been summoned!")
                        .formatted(Formatting.DARK_RED, Formatting.BOLD),
                    false
                );
            }
        });
    }
}
