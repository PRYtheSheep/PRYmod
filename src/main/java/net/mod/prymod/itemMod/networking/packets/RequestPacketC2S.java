package net.mod.prymod.itemMod.networking.packets;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import net.mod.prymod.ModBlock.Cable;
import net.mod.prymod.ModBlock.CableEntity;
import net.mod.prymod.ModBlock.PRYBlockEntity;
import net.mod.prymod.ModBlock.PRYRadarEntity;
import net.mod.prymod.itemMod.networking.ModMessages;

import java.util.function.Supplier;

public class RequestPacketC2S {


    public RequestPacketC2S(FriendlyByteBuf buf){

    }

    public RequestPacketC2S(){

    }

    public void toBytes(FriendlyByteBuf buf){

    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier){
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            //SERVER SIDE
            ServerPlayer player = context.getSender();

        });
        return true;
    }
}
