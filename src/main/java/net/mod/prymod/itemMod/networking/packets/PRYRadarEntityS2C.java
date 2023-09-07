package net.mod.prymod.itemMod.networking.packets;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import net.mod.prymod.ModBlock.PRYBlockEntity;
import net.mod.prymod.ModBlock.PRYRadarEntity;

import java.util.UUID;
import java.util.function.Supplier;

public class PRYRadarEntityS2C {

    BlockPos blockPos;
    BlockPos entityPos;
    String entityName;
    Boolean trackedByLancher;


    public PRYRadarEntityS2C(FriendlyByteBuf buf){
        this.blockPos = buf.readBlockPos();
        this.entityPos = buf.readBlockPos();
        this.entityName = buf.readUtf();
        this.trackedByLancher = buf.readBoolean();
    }

    public PRYRadarEntityS2C(BlockPos blockPos, BlockPos entityPos, String entityName, Boolean trackedByLancher){
        this.blockPos = blockPos;
        this.entityPos = entityPos;
        this.entityName = entityName;
        this.trackedByLancher = trackedByLancher;
    }

    public void toBytes(FriendlyByteBuf buf){
        buf.writeBlockPos(blockPos);
        buf.writeBlockPos(entityPos);
        buf.writeUtf(entityName);
        buf.writeBoolean(trackedByLancher);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier){
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            //CLIENT SIDE
            PRYRadarEntity radarEntity = (PRYRadarEntity) Minecraft.getInstance().level.getBlockEntity(blockPos);
            if(radarEntity != null){
                radarEntity.entityName = entityName;
                radarEntity.entityPos = entityPos;
                radarEntity.trackedByLauncher = trackedByLancher;
            }
        });
        return true;
    }
}