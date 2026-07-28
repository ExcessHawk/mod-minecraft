package com.mythicalswords.client.screen;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

/**
 * Paginated in-game guide. Every page is a pair of lang keys
 * (guide.mythicalswords.page.N.title / .body); the body uses \n as the line
 * separator so the whole guide is translatable.
 */
public class GuideScreen extends Screen {

    public static final int PAGE_COUNT = 7;
    private static final int PANEL_WIDTH = 300;

    private int page = 0;

    public GuideScreen() {
        super(Text.translatable("item.mythicalswords.guide_book"));
    }

    @Override
    protected void init() {
        this.addDrawableChild(ButtonWidget.builder(Text.literal("<"),
                b -> page = Math.max(0, page - 1))
                .dimensions(this.width / 2 - 70, this.height - 36, 40, 20).build());
        this.addDrawableChild(ButtonWidget.builder(Text.literal(">"),
                b -> page = Math.min(PAGE_COUNT - 1, page + 1))
                .dimensions(this.width / 2 + 30, this.height - 36, 40, 20).build());
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context);

        int left = (this.width - PANEL_WIDTH) / 2;
        int top = 24;
        context.fill(left - 8, top - 8, left + PANEL_WIDTH + 8, this.height - 48, 0xC0140d04);
        context.fill(left - 8, top - 8, left + PANEL_WIDTH + 8, top + 14, 0xFF462e0c);

        Text title = Text.translatable("guide.mythicalswords.page." + (page + 1) + ".title");
        context.drawCenteredTextWithShadow(this.textRenderer, title,
                this.width / 2, top, 0xFFFFDD44);

        String body = Text.translatable("guide.mythicalswords.page." + (page + 1) + ".body").getString();
        int y = top + 22;
        for (String line : body.split("\\n")) {
            context.drawTextWithShadow(this.textRenderer, line, left, y, 0xFFE8E0D0);
            y += 11;
        }

        context.drawCenteredTextWithShadow(this.textRenderer,
                Text.literal((page + 1) + " / " + PAGE_COUNT),
                this.width / 2, this.height - 30, 0xFF999999);

        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    public boolean shouldPause() {
        return false;
    }
}
