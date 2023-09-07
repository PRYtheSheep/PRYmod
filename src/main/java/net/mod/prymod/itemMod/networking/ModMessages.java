package net.mod.prymod.itemMod.networking;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;
import net.mod.prymod.PRYmod;
import net.mod.prymod.itemMod.networking.packets.PRYBlockEntityS2C;
import net.mod.prymod.itemMod.networking.packets.PRYBlockRendererC2SPacket;
import net.mod.prymod.itemMod.networking.packets.PRYRadarEntityS2C;
import net.mod.prymod.itemMod.networking.packets.RequestPacketC2S;

public class ModMessages {
    public static SimpleChannel INSTANCE;
    private static int packetId = 0;
    private static int id(){
        return packetId++;
    }

    public static void register(){
        SimpleChannel net = NetworkRegistry.ChannelBuilder
                .named(new ResourceLocation(PRYmod.MODID, "main"))
                .networkProtocolVersion(() -> "1.0")
                .clientAcceptedVersions(s -> true)
                .serverAcceptedVersions(s -> true)
                .simpleChannel();

        INSTANCE = net;

        net.messageBuilder(PRYBlockRendererC2SPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(PRYBlockRendererC2SPacket::new)
                .encoder(PRYBlockRendererC2SPacket::toBytes)
                .consumerMainThread(PRYBlockRendererC2SPacket::handle)
                .add();

        net.messageBuilder(RequestPacketC2S.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(RequestPacketC2S::new)
                .encoder(RequestPacketC2S::toBytes)
                .consumerMainThread(RequestPacketC2S::handle)
                .add();

        net.messageBuilder(PRYBlockEntityS2C.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(PRYBlockEntityS2C::new)
                .encoder(PRYBlockEntityS2C::toBytes)
                .consumerMainThread(PRYBlockEntityS2C::handle)
                .add();

        net.messageBuilder(PRYRadarEntityS2C.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(PRYRadarEntityS2C::new)
                .encoder(PRYRadarEntityS2C::toBytes)
                .consumerMainThread(PRYRadarEntityS2C::handle)
                .add();
    }

    public static <MSG> void sendToServer(MSG message){
        INSTANCE.sendToServer(message);
    }

    public static <MSG> void sendToPlayer(MSG message, ServerPlayer player){
        INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), message);
    }
}
