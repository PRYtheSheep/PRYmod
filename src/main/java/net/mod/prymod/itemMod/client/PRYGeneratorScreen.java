package net.mod.prymod.itemMod.client;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.StonecutterScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.mod.prymod.ModBlock.PRYBlockContainer;
import net.mod.prymod.ModBlock.PRYBlockEntity;
import net.mod.prymod.ModBlock.PRYGeneratorContainer;
import net.mod.prymod.PRYmod;

public class PRYGeneratorScreen extends AbstractContainerScreen<PRYGeneratorContainer> {

    private static final int ENERGY_LEFT = 163;
    private static final int ENERGY_WIDTH = 4;
    private static final int ENERGY_TOP = 10;
    private static final int ENERGY_HEIGHT = 60;
    private final ResourceLocation GUI = new ResourceLocation(PRYmod.MODID, "textures/gui/prygeneratorgui.png");

    public PRYGeneratorScreen(PRYGeneratorContainer container, Inventory inventory, Component title) {
        super(container, inventory, title);
        this.inventoryLabelY = -999;
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTicks, int mouseX, int mouseY) {
        graphics.blit(GUI, leftPos, topPos, 0, 0, this.imageWidth, this.imageHeight);
        int energy = menu.getPower();
        int e = (int) ((energy / (float) PRYBlockEntity.CAPACITY) * ENERGY_HEIGHT);
        graphics.fill(leftPos + ENERGY_LEFT, topPos + ENERGY_TOP, leftPos + ENERGY_LEFT + ENERGY_WIDTH, topPos + ENERGY_TOP + ENERGY_HEIGHT - e, 0xff8b8b8b);
    }

    @Override
    public void render(GuiGraphics graphics, int mousex, int mousey, float partialTick) {
        super.render(graphics, mousex, mousey, partialTick);
        // Render tooltip with power if in the energy box
        if (mousex >= leftPos + ENERGY_LEFT && mousex < leftPos + ENERGY_LEFT + ENERGY_WIDTH && mousey >= topPos + ENERGY_TOP && mousey < topPos + ENERGY_TOP + ENERGY_HEIGHT) {
            int power = menu.getPower();
            graphics.renderTooltip(this.font, Component.literal(power + " RF"), mousex, mousey);
        }
    }

}
