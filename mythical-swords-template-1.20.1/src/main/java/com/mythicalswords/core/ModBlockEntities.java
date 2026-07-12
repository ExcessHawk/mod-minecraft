package com.mythicalswords.core;

import com.mythicalswords.MythicalSwords;
import com.mythicalswords.blocks.BossAltarBlockEntity;
import com.mythicalswords.blocks.MythicalForgeBlockEntity;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModBlockEntities {

    public static final BlockEntityType<MythicalForgeBlockEntity> MYTHICAL_FORGE = Registry.register(
        Registries.BLOCK_ENTITY_TYPE,
        new Identifier(MythicalSwords.MOD_ID, "mythical_forge"),
        FabricBlockEntityTypeBuilder.create(MythicalForgeBlockEntity::new, ModBlocks.MYTHICAL_FORGE).build()
    );

    public static final BlockEntityType<BossAltarBlockEntity> BOSS_ALTAR = Registry.register(
        Registries.BLOCK_ENTITY_TYPE,
        new Identifier(MythicalSwords.MOD_ID, "boss_altar"),
        FabricBlockEntityTypeBuilder.create(BossAltarBlockEntity::new, ModBlocks.BOSS_ALTAR).build()
    );

    public static void register() {
        MythicalSwords.LOGGER.info("Registering ModBlockEntities for " + MythicalSwords.MOD_ID);
    }
}
