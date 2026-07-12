package com.mythicalswords.relics;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;

import java.util.List;

/**
 * Phoenix Feather - a passive relic. While carried in the inventory it will be
 * consumed to cheat death once, fully reviving the player in a burst of flame.
 * The actual revive is handled by {@link com.mythicalswords.events.RelicEventHandler}.
 */
public class PhoenixFeatherItem extends Item {

    public PhoenixFeatherItem(Settings settings) {
        super(settings);
    }

    @Override
    public void appendTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(Text.literal("Cheats death once when carried.").formatted(Formatting.GOLD));
        tooltip.add(Text.literal("Consumed on revival.").formatted(Formatting.GRAY));
    }
}
