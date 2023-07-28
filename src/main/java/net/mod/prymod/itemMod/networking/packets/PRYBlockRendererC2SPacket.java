package net.mod.prymod.itemMod.networking.packets;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.network.NetworkEvent;
import net.mod.prymod.ModBlock.PRYBlock;
import net.mod.prymod.ModBlock.PRYBlockEntity;

import javax.swing.text.html.parser.Entity;
import java.util.function.Supplier;

public class PRYBlockRendererC2SPacket {
    private final float facing;
    private BlockPos blockPos;
    private boolean pointingAtTarget;

    public PRYBlockRendererC2SPacket(FriendlyByteBuf buf){
        this.facing = buf.readFloat();
        this.blockPos = buf.readBlockPos();
        this.pointingAtTarget = buf.readBoolean();
    }

    public PRYBlockRendererC2SPacket(float facing, BlockPos blockPos, boolean pointingAtTarget){
        this.facing = facing;
        this.blockPos = blockPos;
        this.pointingAtTarget = pointingAtTarget;
    }

    public void toBytes(FriendlyByteBuf buf){
        buf.writeFloat(this.facing);
        buf.writeBlockPos(this.blockPos);
        buf.writeBoolean(this.pointingAtTarget);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier){
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            //SERVER SIDE
            ServerPlayer player = context.getSender();
            ServerLevel level = (ServerLevel) player.level();
            BlockEntity entity = level.getBlockEntity(blockPos);
            if(entity instanceof PRYBlockEntity entity1){
                entity1.facing = facing;
                entity1.pointingAtTarget = pointingAtTarget;
                entity1.setChanged();
            }
        });
        return true;
    }
}
