package com.mythicalswords.blocks;

import com.mythicalswords.core.ModBlockEntities;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

/**
 * Block entity for the Boss Altar.
 * Stores which boss should be summoned when a player activates this altar.
 */
public class BossAltarBlockEntity extends BlockEntity {

    private String bossId = "";

    public BossAltarBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.BOSS_ALTAR, pos, state);
    }

    public String getBossId() {
        return bossId;
    }

    public void setBossId(String bossId) {
        this.bossId = bossId != null ? bossId : "";
        markDirty();
    }

    // ===== NBT persistence =====

    @Override
    public void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        nbt.putString("BossId", bossId);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        bossId = nbt.getString("BossId");
    }

    // ===== Sync to client (for any future client-side rendering) =====

    @Nullable
    @Override
    public Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt() {
        return createNbt();
    }
}
