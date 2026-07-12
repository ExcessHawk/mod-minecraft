package com.mythicalswords.client.screen;

import com.mythicalswords.screen.MythicalForgeScreenHandler;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class MythicalForgeScreen extends HandledScreen<MythicalForgeScreenHandler> {

    private static final Identifier BACKGROUND =
        new Identifier("mythicalswords", "textures/gui/mythical_forge.png");

    public MythicalForgeScreen(MythicalForgeScreenHandler handler,
                                PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
        this.backgroundWidth = 176;
        this.backgroundHeight = 196; // taller forge area so labels/button have room
        this.playerInventoryTitleY = this.backgroundHeight - 94;
    }

    @Override
    protected void init() {
        super.init();
        this.titleX = (this.backgroundWidth - this.textRenderer.getWidth(this.title)) / 2;

        // "Forge" button — triggers the forge operation server-side
        this.addDrawableChild(ButtonWidget.builder(
            Text.translatable("screen.mythicalswords.forge.button"),
            btn -> sendForgePacket()
        ).dimensions(this.x + 118, this.y + 88, 50, 16).build());
    }

    @Override
    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
        context.drawTexture(BACKGROUND, this.x, this.y, 0, 0,
                this.backgroundWidth, this.backgroundHeight);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context);
        super.render(context, mouseX, mouseY, delta);
        this.drawMouseoverTooltip(context, mouseX, mouseY);

        // Slot labels (localized). Left-side labels are right-aligned against
        // their slot so longer translations grow away from it.
        Text weaponLabel = Text.translatable("screen.mythicalswords.forge.weapon");
        Text materialLabel = Text.translatable("screen.mythicalswords.forge.material");
        Text outputLabel = Text.translatable("screen.mythicalswords.forge.output");

        context.drawText(this.textRenderer, weaponLabel,
                this.x + 40 - this.textRenderer.getWidth(weaponLabel), this.y + 52, 0xFFFFC933, false);
        context.drawText(this.textRenderer, materialLabel,
                this.x + 76 - this.textRenderer.getWidth(materialLabel), this.y + 30, 0xFFBBAA88, false);
        context.drawText(this.textRenderer, materialLabel,
                this.x + 76 - this.textRenderer.getWidth(materialLabel), this.y + 74, 0xFFBBAA88, false);
        context.drawText(this.textRenderer, outputLabel,
                this.x + 138, this.y + 52, 0xFF33FF77, false);

        // Upgrade info if weapon has upgrades
        var weaponSlot = this.handler.slots.get(0);
        if (weaponSlot.hasStack()) {
            var nbt = weaponSlot.getStack().getNbt();
            if (nbt != null && nbt.contains("ForgeUpgrades")) {
                int upgrades = nbt.getInt("ForgeUpgrades");
                int max = com.mythicalswords.config.ModConfig.get().forgeMaxUpgrades;
                context.drawText(this.textRenderer,
                    "Upgrades: " + upgrades + "/" + max,
                    this.x + 8, this.y + 92, 0xFFFFDD00, false);
            }
        }
    }

    private void sendForgePacket() {
        // Ask the server to run the forge on the real container inventory; the
        // result syncs back to the client automatically.
        net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking.send(
            com.mythicalswords.core.ModNetworking.FORGE_CRAFT,
            net.fabricmc.fabric.api.networking.v1.PacketByteBufs.create());
    }

    @Override
    protected void drawForeground(DrawContext context, int mouseX, int mouseY) {
        context.drawText(this.textRenderer, this.title, this.titleX, this.titleY, 0xFFFFDD44, false);
        context.drawText(this.textRenderer, this.playerInventoryTitle,
            this.playerInventoryTitleX, this.playerInventoryTitleY, 0xFFCCCCCC, false);
    }
}
