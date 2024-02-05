package net.mod.prymod.itemMod.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.mod.prymod.ModBlock.PRYBlockEntity;
import net.mod.prymod.ModBlock.PRYRadarContainer;
import net.mod.prymod.ModBlock.PRYRadarEntity;
import net.mod.prymod.PRYmod;

public class PRYRadarScreen extends AbstractContainerScreen<PRYRadarContainer> {

    private static final int ENERGY_LEFT = 163;
    private static final int ENERGY_WIDTH = 4;
    private static final int ENERGY_TOP = 10;
    private static final int ENERGY_HEIGHT = 60;
    private final ResourceLocation GUI = new ResourceLocation(PRYmod.MODID, "textures/gui/pryradargui.png");

    public PRYRadarScreen(PRYRadarContainer container, Inventory inventory, Component title) {
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

        //Display static text on GUI
        graphics.drawString(this.font, "Entity:", leftPos + 10, topPos + 11, 0xff00ff00);
        graphics.drawString(this.font, "Coor:", leftPos + 10, topPos + 20, 0xff00ff00);

        PRYRadarEntity radarEntity = (PRYRadarEntity) Minecraft.getInstance().level.getBlockEntity(menu.getPos());
        if(radarEntity != null){
            if(!radarEntity.entityName.equals("null")) {
                //Radar is tracking some entity
                //Name
                graphics.drawString(this.font, radarEntity.entityName, leftPos + 41, topPos + 11, 0xff00ff00);

                //Pos
                BlockPos pos = radarEntity.entityPos;
                String posString = pos.getX() + " " + pos.getY() + " " + pos.getZ();
                graphics.drawString(this.font, posString, leftPos + 37, topPos + 20, 0xff00ff00);
            }
            else{
                graphics.drawString(this.font, "Null", leftPos + 41, topPos + 11, 0xff00ff00);
                graphics.drawString(this.font, "Null", leftPos + 37, topPos + 20, 0xff00ff00);
            }
        }
    }

}
