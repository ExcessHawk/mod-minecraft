package com.mythicalswords.screen;

import com.mythicalswords.core.ModScreenHandlers;
import com.mythicalswords.systems.MythicalForgeSystem;
import com.mythicalswords.weapons.MythicalWeaponItem;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.math.BlockPos;

public class MythicalForgeScreenHandler extends ScreenHandler {

    private final Inventory inventory;

    // Client-side constructor (called from network packet)
    public MythicalForgeScreenHandler(int syncId, PlayerInventory playerInventory, PacketByteBuf buf) {
        this(syncId, playerInventory, new SimpleInventory(4));
        buf.readBlockPos(); // consume pos from packet
    }

    // Server-side constructor
    public MythicalForgeScreenHandler(int syncId, PlayerInventory playerInventory, Inventory inventory) {
        super(ModScreenHandlers.MYTHICAL_FORGE, syncId);
        checkSize(inventory, 4);
        this.inventory = inventory;
        inventory.onOpen(playerInventory.player);

        // Forge slots (diamond layout, matches the 176x196 GUI texture)
        this.addSlot(new WeaponSlot(inventory, 0, 44, 48));       // weapon
        this.addSlot(new Slot(inventory, 1, 80, 26));              // material 1
        this.addSlot(new Slot(inventory, 2, 80, 70));              // material 2
        this.addSlot(new ForgeOutputSlot(inventory, 3, 116, 48)); // output

        // Player inventory (rows)
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++) {
                this.addSlot(new Slot(playerInventory,
                    col + row * 9 + 9,
                    8 + col * 18,
                    114 + row * 18));
            }
        }

        // Player hotbar
        for (int col = 0; col < 9; col++) {
            this.addSlot(new Slot(playerInventory, col, 8 + col * 18, 172));
        }
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return this.inventory.canPlayerUse(player);
    }

    public void craft(PlayerEntity player) {
        MythicalForgeSystem.process(this.inventory, player);
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int invSlot) {
        ItemStack result = ItemStack.EMPTY;
        Slot slot = this.slots.get(invSlot);

        if (!slot.hasStack()) return result;

        ItemStack stack = slot.getStack();
        result = stack.copy();

        if (invSlot < 4) {
            // Move from forge to player inventory
            if (!this.insertItem(stack, 4, this.slots.size(), true)) {
                return ItemStack.EMPTY;
            }
        } else {
            // Move from player inventory to forge input slots (0-2)
            if (!this.insertItem(stack, 0, 3, false)) {
                return ItemStack.EMPTY;
            }
        }

        if (stack.isEmpty()) {
            slot.setStack(ItemStack.EMPTY);
        } else {
            slot.markDirty();
        }

        return result;
    }

    // Only mythical weapons go in slot 0
    private static class WeaponSlot extends Slot {
        public WeaponSlot(Inventory inventory, int index, int x, int y) {
            super(inventory, index, x, y);
        }

        @Override
        public boolean canInsert(ItemStack stack) {
            return stack.getItem() instanceof MythicalWeaponItem;
        }
    }

    // Output slot - no manual insertion
    private static class ForgeOutputSlot extends Slot {
        public ForgeOutputSlot(Inventory inventory, int index, int x, int y) {
            super(inventory, index, x, y);
        }

        @Override
        public boolean canInsert(ItemStack stack) {
            return false;
        }
    }
}
