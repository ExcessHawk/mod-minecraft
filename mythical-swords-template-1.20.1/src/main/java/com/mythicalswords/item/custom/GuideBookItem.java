package com.mythicalswords.item.custom;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

/**
 * Guía Mítica — opens the in-game guide screen. Content lives in the lang
 * files (guide.mythicalswords.page.N.*), so translators can localize it and
 * adding a page is just adding lang keys + bumping GuideScreen.PAGE_COUNT.
 */
public class GuideBookItem extends Item {

    public GuideBookItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (world.isClient) {
            openGuide();
        }
        return TypedActionResult.success(user.getStackInHand(hand), world.isClient());
    }

    @Environment(EnvType.CLIENT)
    private void openGuide() {
        net.minecraft.client.MinecraftClient.getInstance()
                .setScreen(new com.mythicalswords.client.screen.GuideScreen());
    }
}
