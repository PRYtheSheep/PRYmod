package net.mod.prymod.itemMod.networking.packets;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import net.mod.prymod.ModBlock.PRYBlockEntity;

import java.util.UUID;
import java.util.function.Supplier;

public class PRYBlockEntityS2C {

    BlockPos pos;
    UUID uuid;
    public PRYBlockEntityS2C(FriendlyByteBuf buf){
        this.uuid = buf.readUUID();
        this.pos = buf.readBlockPos();
    }

    public PRYBlockEntityS2C(UUID uuid, BlockPos pos){
        this.uuid = uuid;
        this.pos = pos;
    }

    public void toBytes(FriendlyByteBuf buf){
        buf.writeUUID(uuid);
        buf.writeBlockPos(pos);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier){
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            //CLIENT SIDE
            PRYBlockEntity entity = (PRYBlockEntity) Minecraft.getInstance().level.getBlockEntity(pos);
            entity.uuid = uuid;

        });
        return true;
    }
}