package com.mythicalswords.compat.rei;

import com.mythicalswords.MythicalSwords;
import com.mythicalswords.core.ModBlocks;
import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.gui.Renderer;
import me.shedaniel.rei.api.client.gui.widgets.Widget;
import me.shedaniel.rei.api.client.gui.widgets.Widgets;
import me.shedaniel.rei.api.client.registry.display.DisplayCategory;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;

public class MythicalForgeCategory implements DisplayCategory<MythicalForgeDisplay> {

    public static final CategoryIdentifier<MythicalForgeDisplay> ID =
            CategoryIdentifier.of(MythicalSwords.MOD_ID, "forge");

    @Override
    public CategoryIdentifier<? extends MythicalForgeDisplay> getCategoryIdentifier() {
        return ID;
    }

    @Override
    public Text getTitle() {
        return Text.translatable("category.mythicalswords.forge");
    }

    @Override
    public Renderer getIcon() {
        return EntryStacks.of(ModBlocks.MYTHICAL_FORGE);
    }

    @Override
    public List<Widget> setupDisplay(MythicalForgeDisplay display, Rectangle bounds) {
        List<Widget> widgets = new ArrayList<>();
        widgets.add(Widgets.createRecipeBase(bounds));
        widgets.add(Widgets.createLabel(new Point(bounds.getCenterX(), bounds.y + 6),
                display.getOperation()).noShadow().color(0xFF404040, 0xFFBBBBBB).centered());

        List<EntryIngredient> inputs = display.getInputEntries();
        int y = bounds.y + 20;
        int x = bounds.x + 8;
        for (int i = 0; i < inputs.size(); i++) {
            widgets.add(Widgets.createSlot(new Point(x + i * 20, y)).entries(inputs.get(i)).markInput());
        }
        widgets.add(Widgets.createArrow(new Point(x + inputs.size() * 20 + 6, y)));
        Point outPoint = new Point(bounds.getMaxX() - 28, y);
        widgets.add(Widgets.createResultSlotBackground(outPoint));
        widgets.add(Widgets.createSlot(outPoint)
                .entries(display.getOutputEntries().get(0)).disableBackground().markOutput());
        return widgets;
    }

    @Override
    public int getDisplayHeight() {
        return 44;
    }
}
