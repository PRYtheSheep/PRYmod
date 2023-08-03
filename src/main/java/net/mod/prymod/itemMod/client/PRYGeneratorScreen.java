package net.mod.prymod.itemMod.client;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.mod.prymod.ModBlock.PRYBlockContainer;
import net.mod.prymod.ModBlock.PRYGeneratorContainer;
import net.mod.prymod.PRYmod;

public class PRYGeneratorScreen extends AbstractContainerScreen<PRYGeneratorContainer> {

    private final ResourceLocation GUI = new ResourceLocation(PRYmod.MODID, "textures/gui/prygeneratorgui.png");

    public PRYGeneratorScreen(PRYGeneratorContainer container, Inventory inventory, Component title) {
        super(container, inventory, title);
        this.inventoryLabelY = this.imageHeight - 110;
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTicks, int mouseX, int mouseY) {
        int relX = (this.width - this.imageWidth) / 2;
        int relY = (this.height - this.imageHeight) / 2;
        graphics.blit(GUI, relX, relY, 0, 0, this.imageWidth, this.imageHeight);
    }

}
