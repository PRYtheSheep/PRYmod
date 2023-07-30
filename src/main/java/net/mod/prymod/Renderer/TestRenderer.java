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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.logging.Level;

import static net.mod.prymod.ModBlock.PRYRadarEntity.livingEntityRGBHashMap;

@Mod.EventBusSubscriber(modid = PRYmod.MODID, value = Dist.CLIENT)
public class TestRenderer {

    static double previousX;
    static double previousY;
    static double previousZ;
    public static ArrayList<Vector3f> previousPos = new ArrayList<>();
    public static int currentTick = 0;

    @SubscribeEvent
    public static void onWorldRenderLast(RenderLevelStageEvent event){

        if(event.getStage() != RenderLevelStageEvent.Stage.AFTER_TRANSLUCENT_BLOCKS) return;
        //if(PRYRadarEntity.livingEntityLinkedList.isEmpty()) return;
        if(livingEntityRGBHashMap.isEmpty()) return;

        if(Minecraft.getInstance().player == null) return;
        if(event.getRenderTick() %10 == 0) return;
        Player player = Minecraft.getInstance().player;
        PoseStack stack = event.getPoseStack();

        //Get the list of mobs from Mc.getinstance.level, check with PRYRadarEntity
        //Store the entities to render a hitbox for inside some array
        //Above to be done in a for loop

        //In a separate for loop, interate through the list of AABB generated above and render
        //Use 1 colour for now
        /*
        AABB startEndBox = new AABB(
                Minecraft.getInstance().player.getX()-90,
                Minecraft.getInstance().player.getY()-200,
                Minecraft.getInstance().player.getZ()-90,
                Minecraft.getInstance().player.getX()+90,
                Minecraft.getInstance().player.getY()+90,
                Minecraft.getInstance().player.getZ()+90);
        List<Entity> list1 = Minecraft.getInstance().level.getEntities(null, startEndBox);

        for(Entity entity1 : PRYRadarEntity.livingEntityLinkedList){
            if(toRenderList.size() == 1) break;
            for(Entity entity2 : list1){
                if(entity1!=null && entity2!=null && entity1.getStringUUID().equals(entity2.getStringUUID())){
                    //create a vec3 to store previous pos in previousPos array
                    Vec3 vec = Vec3.ZERO;
                    if(!toRenderList.contains(entity1)) {
                        previousPos.add(vec);
                        toRenderList.add(entity1);
                    }
                }
            }
        }

        System.out.println(previousPos.size());

        //System.out.println(PRYRadarEntity.livingEntityLinkedList.size() + " " + previousPos.size());

        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(true);
        VertexConsumer vertexConsumer = Minecraft.getInstance().renderBuffers().bufferSource().getBuffer(RenderType.lines());

        Vec3 camvec = Minecraft.getInstance().gameRenderer.getMainCamera().getPosition();
        double s0 = camvec.x;
        double s1 = camvec.y;
        double s2 = camvec.z;
        System.out.println(toRenderList.size());
        int i = 0;
        for(Entity entity : toRenderList){
            stack.pushPose();
            AABB aabb = entity.getBoundingBox().move(-entity.getX(), -entity.getY(), -entity.getZ());
            double px = Mth.lerp(event.getPartialTick(), previousPos.get(i).x, entity.getX());
            double py = Mth.lerp(event.getPartialTick(), previousPos.get(i).y, entity.getY());
            double pz = Mth.lerp(event.getPartialTick(), previousPos.get(i).z, entity.getZ());

            Vec3 vec3 = new Vec3(entity.getX(), entity.getY(), entity.getZ());
            previousPos.set(i, vec3);
            i++;
            stack.translate(px - s0, py - s1, pz - s2);
            LevelRenderer.renderLineBox(stack, vertexConsumer, aabb, 1, 1, 0, 1);
            stack.popPose();
        }
        RenderSystem.enableDepthTest();*/
        int i = 0;
        int j = 0;
        //for(LivingEntity livingEntity : PRYRadarEntity.livingEntityLinkedList){
        for(Map.Entry<LivingEntity, RGB> set : livingEntityRGBHashMap.entrySet()){
            AABB startEndBox = new AABB(
                    Minecraft.getInstance().player.getX()-90,
                    Minecraft.getInstance().player.getY()-200,
                    Minecraft.getInstance().player.getZ()-90,
                    Minecraft.getInstance().player.getX()+90,
                    Minecraft.getInstance().player.getY()+90,
                    Minecraft.getInstance().player.getZ()+90);
            List<Entity> list1 = Minecraft.getInstance().level.getEntities(null, startEndBox);
            Entity target = null;
            if(set.getKey() == null) return;
            for(Entity entity : list1){
                if(entity!=null && entity.getStringUUID().equals(set.getKey().getStringUUID())){
                    target = entity;
                    try{
                        previousPos.get(j++);
                    }
                    catch(Exception e){
                        previousPos.add( previousPos.size(), new Vector3f(0 ,0 ,0));
                    }
                }
            }
            if(target == null){
                return;
            }

            RenderSystem.disableDepthTest();

            AABB aabb = target.getBoundingBox().move(-target.getX(), -target.getY(), -target.getZ());

            RenderSystem.depthMask(true);
            VertexConsumer vertexConsumer = Minecraft.getInstance().renderBuffers().bufferSource().getBuffer(RenderType.lines());

            double px = Mth.lerp(event.getPartialTick(), previousPos.get(i).x, target.getX());
            double py = Mth.lerp(event.getPartialTick(), previousPos.get(i).y, target.getY());
            double pz = Mth.lerp(event.getPartialTick(), previousPos.get(i).z, target.getZ());

            previousX = target.getX();
            previousY = target.getY();
            previousZ = target.getZ();
            previousPos.set(i, new Vector3f((float) previousX, (float) previousY, (float) previousZ));
            i++;
            Vec3 camvec = Minecraft.getInstance().gameRenderer.getMainCamera().getPosition();
            double s0 = camvec.x;
            double s1 = camvec.y;
            double s2 = camvec.z;

            stack.pushPose();
            stack.translate(px - s0, py - s1, pz - s2);

            RGB rgbValues = set.getValue();
            LevelRenderer.renderLineBox(stack, vertexConsumer, aabb, rgbValues.r, rgbValues.g, rgbValues.b, 1);

            stack.popPose();
        }
        //event.getPoseStack().popPose();
        RenderSystem.enableDepthTest();
    }
}
