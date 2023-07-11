package net.mod.prymod.Renderer;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ambient.Bat;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.mod.prymod.ModBlock.PRYBlockEntity;
import net.mod.prymod.ModBlock.PRYRadarEntity;
import net.mod.prymod.PRYmod;
import net.mod.prymod.itemMod.networking.ModMessages;
import net.mod.prymod.itemMod.networking.packets.RequestPacketC2S;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import software.bernie.shadowed.eliotlash.mclib.math.functions.limit.Min;

import java.util.List;
import java.util.function.Predicate;
import java.util.logging.Level;

@Mod.EventBusSubscriber(modid = PRYmod.MODID, value = Dist.CLIENT)
public class TestRenderer {

    static double previousX;
    static double previousY;
    static double previousZ;
    public static String colour;
    public static Vector3f vec = null;

    @SubscribeEvent
    public static void onWorldRenderLast(RenderLevelStageEvent event){

        if(event.getStage() != RenderLevelStageEvent.Stage.AFTER_TRANSLUCENT_BLOCKS) return;
        if(PRYRadarEntity.livingEntityLinkedList.isEmpty()) return;

        if(Minecraft.getInstance().player == null) return;

        for(LivingEntity livingEntity : PRYRadarEntity.livingEntityLinkedList){
            AABB startEndBox = new AABB(
                    Minecraft.getInstance().player.getX()-90,
                    Minecraft.getInstance().player.getY()-200,
                    Minecraft.getInstance().player.getZ()-90,
                    Minecraft.getInstance().player.getX()+90,
                    Minecraft.getInstance().player.getY()+90,
                    Minecraft.getInstance().player.getZ()+90);
            List<Entity> list1 = Minecraft.getInstance().level.getEntities(null, startEndBox);
            Entity target = null;
            for(Entity entity : list1){
                if(entity!=null && entity.getStringUUID().equals(livingEntity.getStringUUID())){
                    target = entity;
                    break;
                }
            }
            if(target == null){
                return;
            }

            RenderSystem.disableDepthTest();
            PoseStack stack = event.getPoseStack();

            AABB aabb = target.getBoundingBox().move(-target.getX(), -target.getY(), -target.getZ());


            RenderSystem.depthMask(true);
            VertexConsumer vertexConsumer = Minecraft.getInstance().renderBuffers().bufferSource().getBuffer(RenderType.lines());

            double px = Mth.lerp(event.getPartialTick(), previousX, target.getX());
            double py = Mth.lerp(event.getPartialTick(), previousY, target.getY());
            double pz = Mth.lerp(event.getPartialTick(), previousZ, target.getZ());

            previousX = target.getX();
            previousY = target.getY();
            previousZ = target.getZ();

            Vec3 camvec = Minecraft.getInstance().gameRenderer.getMainCamera().getPosition();
            double s0 = camvec.x;
            double s1 = camvec.y;
            double s2 = camvec.z;

            stack.pushPose();
            stack.translate(px - s0, py - s1, pz - s2);
            //stack.translate(-target.getX(), -target.getY(), -target.getZ());
            //stack.translate(-target.getDeltaMovement().x, -target.getDeltaMovement().y, -target.getDeltaMovement().z);
            if(colour.equals("yellow")){
                LevelRenderer.renderLineBox(stack, vertexConsumer, aabb, 1, 1, 0, 1);
            }
            else if(colour.equals("red")){
                LevelRenderer.renderLineBox(stack, vertexConsumer, aabb, 1, 0, 0, 1);
            }
            else{
                LevelRenderer.renderLineBox(stack, vertexConsumer, aabb, 0, 1, 0, 1);
            }
            stack.popPose();
        }
        //event.getPoseStack().popPose();
        RenderSystem.enableDepthTest();
    }

    private static void renderHitbox(PoseStack p_114442_, VertexConsumer p_114443_, Entity p_114444_, float p_114445_) {
        AABB aabb = p_114444_.getBoundingBox().move(-p_114444_.getX(), -p_114444_.getY(), -p_114444_.getZ());
        LevelRenderer.renderLineBox(p_114442_, p_114443_, aabb, 0, 1.0F, 0, 1.0F);
    }

    private static Vec3 getVectorForRotation(float pitch, float yaw)
    {
        float f = (float) Math.cos(-yaw * 0.017453292F - (float)Math.PI);
        float f1 = (float) Math.sin(-yaw * 0.017453292F - (float)Math.PI);
        float f2 = (float) -Math.cos(-pitch * 0.017453292F);
        float f3 = (float) Math.sin(-pitch * 0.017453292F);
        return new Vec3(f1 * f2 * -1, f3, f * f2 * -1);
    }

}
