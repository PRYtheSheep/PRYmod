package net.mod.prymod.ModBlock;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.PacketDistributor;
import net.mod.prymod.itemMod.client.GUIRegister;
import net.mod.prymod.itemMod.networking.ModMessages;
import net.mod.prymod.itemMod.networking.packets.PRYRadarEntityS2C;

public class PRYRadarContainer extends AbstractContainerMenu {

    private final BlockPos pos;
    private int power;

    public PRYRadarContainer(int windowId, Player player, FriendlyByteBuf extraData) {
        //client side shenanigans
        super(GUIRegister.PRYRADAR_CONTAINER.get(), windowId);
        this.pos = extraData.readBlockPos();
        if (player.level().getBlockEntity(pos) instanceof PRYRadarEntity radar) {
            addDataSlot(new DataSlot() {
                @Override
                public int get() {
                    return radar.getStoredPower() & 0xffff;
                }

                @Override
                public void set(int pValue) {
                    PRYRadarContainer.this.power = (PRYRadarContainer.this.power & 0xffff0000) | (pValue & 0xffff);
                }
            });
            addDataSlot(new DataSlot() {
                @Override
                public int get() {return (radar.getStoredPower() >> 16) & 0xffff;}

                @Override
                public void set(int pValue) {
                    PRYRadarContainer.this.power = (PRYRadarContainer.this.power & 0xffff) | ((pValue & 0xffff) << 16);
                }
            });
        }
        layoutPlayerInventorySlots(player.getInventory(), 8, 82);
    }

    public PRYRadarContainer(int windowId, Player player, BlockPos pos) {
        //server side shenanigans
        super(GUIRegister.PRYRADAR_CONTAINER.get(), windowId);
        this.pos = pos;
        if (player.level().getBlockEntity(pos) instanceof PRYRadarEntity radar) {
            addDataSlot(new DataSlot() {
                @Override
                public int get() {
                    return radar.getStoredPower() & 0xffff;
                }

                @Override
                public void set(int pValue) {
                    PRYRadarContainer.this.power = (PRYRadarContainer.this.power & 0xffff0000) | (pValue & 0xffff);
                }
            });
            addDataSlot(new DataSlot() {
                @Override
                public int get() {return (radar.getStoredPower() >> 16) & 0xffff;}

                @Override
                public void set(int pValue) {
                    PRYRadarContainer.this.power = (PRYRadarContainer.this.power & 0xffff) | ((pValue & 0xffff) << 16);
                }
            });
        }
        layoutPlayerInventorySlots(player.getInventory(), 8, 82);
    }

    public int getPower() {return power;}
    public BlockPos getPos() {return pos;}

    private int addSlotRange(Container playerInventory, int index, int x, int y, int amount, int dx) {
        for (int i = 0 ; i < amount ; i++) {
            addSlot(new Slot(playerInventory, index, x, y));
            x += dx;
            index++;
        }
        return index;
    }

    private int addSlotBox(Container playerInventory, int index, int x, int y, int horAmount, int dx, int verAmount, int dy) {
        for (int j = 0 ; j < verAmount ; j++) {
            index = addSlotRange(playerInventory, index, x, y, horAmount, dx);
            y += dy;
        }
        return index;
    }

    private void layoutPlayerInventorySlots(Inventory playerInventory, int leftCol, int topRow) {
        // Player inventory
        addSlotBox(playerInventory, 9, leftCol, topRow, 9, 18, 3, 18);

        // Hotbar
        topRow += 58;
        addSlotRange(playerInventory, 0, leftCol, topRow, 9, 18);
    }

    @Override
    public ItemStack quickMoveStack(Player p_38941_, int p_38942_) {
        return null;
    }

    @Override
    public boolean stillValid(Player player) {
        return stillValid(ContainerLevelAccess.create(player.level(), pos), player, ModBlock.PRYRADAR.get());
    }

}
