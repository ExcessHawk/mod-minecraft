package com.mythicalswords.item.custom;

import com.mythicalswords.structures.ModStructures;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.CompassItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.GlobalPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Mythical Compass — extends vanilla CompassItem so the needle physically
 * points toward the target structure using the lodestone mechanic.
 *
 * The compass always appears enchanted and shows the structure name,
 * coordinates, distance, and direction in the tooltip.
 * Right-click prints distance + direction in chat.
 */
public class MythicalCompassItem extends CompassItem {

    private final String structureType;
    private final String displayName;
    private final Formatting color;

    public MythicalCompassItem(Settings settings, String structureType, String displayName, Formatting color) {
        super(settings);
        this.structureType = structureType;
        this.displayName = displayName;
        this.color = color;
    }

    // ===== Enchanted glint =====

    @Override
    public boolean hasGlint(ItemStack stack) {
        return true;
    }

    // ===== Right-click: show distance + direction in chat =====

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);

        if (!world.isClient && world instanceof ServerWorld sw) {
            ServerWorld overworld = sw.getServer().getWorld(World.OVERWORLD);
            BlockPos target = overworld != null
                ? ModStructures.getStructurePos(overworld, user.getBlockPos(), structureType) : null;

            if (target != null) {
                int dist = (int) Math.sqrt(user.getBlockPos().getSquaredDistance(target));
                String dir = getDirectionLabel(
                    user.getX(), user.getZ(), target.getX(), target.getZ());
                user.sendMessage(
                    Text.literal(displayName + " is " + dist + " blocks to the " + dir + "!")
                        .formatted(color), true);
            } else {
                user.sendMessage(
                    Text.literal(displayName + " has not been revealed yet...")
                        .formatted(Formatting.RED), true);
            }
        }

        return TypedActionResult.success(stack, world.isClient());
    }

    // ===== Tick: keep LodestonPos updated so the needle points correctly =====

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        if (world.isClient || !(entity instanceof PlayerEntity)) return;

        // Update every 100 ticks (5 seconds)
        if (world.getTime() % 100 != 0) return;

        ServerWorld overworld = ((ServerWorld) world).getServer().getWorld(World.OVERWORLD);
        if (overworld == null) return;

        BlockPos target = ModStructures.getStructurePos(overworld, entity.getBlockPos(), structureType);
        if (target != null) {
            // Write lodestone NBT so vanilla compass rendering points the needle
            NbtCompound nbt = stack.getOrCreateNbt();
            nbt.put("LodestonePos", NbtHelper.fromBlockPos(target));
            nbt.putString("LodestoneDimension", "minecraft:overworld");
            nbt.putBoolean("LodestoneTracked", false);

            // Extra data for our tooltip
            nbt.putBoolean("HasTarget", true);
            nbt.putBoolean("IsBuilt", ModStructures.isStructureBuilt(overworld, structureType));
        }
    }

    // ===== Tooltip =====

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(Text.literal("Points to: " + displayName).formatted(color));

        NbtCompound nbt = stack.getNbt();
        if (nbt != null && nbt.getBoolean("HasTarget") && nbt.contains("LodestonePos")) {
            BlockPos target = NbtHelper.toBlockPos(nbt.getCompound("LodestonePos"));
            tooltip.add(Text.literal("Location: " + target.getX() + ", " + target.getZ())
                .formatted(Formatting.GRAY));

            boolean built = nbt.getBoolean("IsBuilt");
            tooltip.add(built
                ? Text.literal("Structure: BUILT").formatted(Formatting.GREEN)
                : Text.literal("Structure: Not yet generated — travel there!")
                    .formatted(Formatting.YELLOW));
        } else {
            tooltip.add(Text.literal("Hold in hand to locate").formatted(Formatting.DARK_GRAY));
        }
    }

    // ===== Direction helper =====

    public static String getDirectionLabel(double playerX, double playerZ, double targetX, double targetZ) {
        double dx = targetX - playerX;
        double dz = targetZ - playerZ;
        double angle = Math.toDegrees(Math.atan2(dz, dx));
        double bearing = (angle + 90 + 360) % 360;
        String[] dirs = { "N", "NE", "E", "SE", "S", "SW", "W", "NW" };
        int idx = (int) Math.round(bearing / 45.0) % 8;
        return dirs[idx];
    }
}
