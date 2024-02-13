package net.mod.prymod.Renderer;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.sun.jna.platform.win32.OaIdl;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.block.ModelBlockRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.Tags;
import net.mod.prymod.ModBlock.ModBlock;
import net.mod.prymod.ModBlock.PRYBlock;
import net.mod.prymod.ModBlock.PRYBlockEntity;
import net.mod.prymod.ModBlock.PRYRadarEntity;
import net.mod.prymod.itemMod.custom.ProximityArrowEntity;
import net.mod.prymod.itemMod.networking.ModMessages;
import net.mod.prymod.itemMod.networking.packets.PRYBlockRendererC2SPacket;
import software.bernie.shadowed.eliotlash.mclib.math.functions.limit.Min;

import java.util.List;


public class PRYBlockEntityRenderer implements BlockEntityRenderer<PRYBlockEntity> {

    BlockEntityRendererProvider.Context context;

    public PRYBlockEntityRenderer(BlockEntityRendererProvider.Context context){
        this.context = context;
    }

    int progress = 0;
    @Override
    public void render(PRYBlockEntity entity, float partialTick, PoseStack stack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
        final BlockRenderDispatcher blockRenderDispatcher = this.context.getBlockRenderDispatcher();

        BlockState state = getBlockState(entity);
        int test_z = 0;
        LivingEntity target = null;
        AABB startEndBox = new AABB(
                Minecraft.getInstance().player.getX()-90,
                Minecraft.getInstance().player.getY()-200,
                Minecraft.getInstance().player.getZ()-90,
                Minecraft.getInstance().player.getX()+90,
                Minecraft.getInstance().player.getY()+90,
                Minecraft.getInstance().player.getZ()+90);
        List<Entity> list1 = Minecraft.getInstance().level.getEntities(null, startEndBox);
        if(list1.isEmpty()) return;
        for(Entity entity1 : list1){
            if(entity!=null && entity1 instanceof LivingEntity && entity1.getUUID().equals(entity.uuid)){
                target = (LivingEntity) entity1;
                break;
            }
        }

        Vec3 targetPos = null;

        if(entity.facing == -1){
            switch(entity.getBlockState().getValue(PRYBlock.FACING)){
                case NORTH -> {
                    entity.facing = 0;
                }
                case EAST -> {
                    entity.facing = 90;
                }
                case SOUTH -> {
                    entity.facing = 180;
                }
                case WEST -> {
                    entity.facing = 270;
                }
            }
        }

        if(target != null) {
            targetPos = target.getEyePosition();
        }
        else{
            entity.pointingAtTarget = false;
            float defaultAngle = 0;

            switch(entity.getBlockState().getValue(PRYBlock.FACING)){
                case NORTH -> {
                    defaultAngle = 0;
                }
                case EAST -> {
                    defaultAngle = 90;
                }
                case SOUTH -> {
                    defaultAngle = 180;
                }
                case WEST -> {
                    defaultAngle = 270;
                }
            }


            Vec3 facingVector = getVectorForRotation(0, entity.facing);
            Vec3 angleVector = getVectorForRotation(0, defaultAngle);
            Vec3 crossVector = facingVector.cross(angleVector);

            float angleBetween = angleBetween2Vectors(facingVector, angleVector);
            if(angleBetween > 1){
                //rotation needed
                if(crossVector.y > 0){
                    entity.facing -= 0.1;
                    stack.rotateAround(Axis.YN.rotationDegrees(entity.facing), 0.5F, 0, 0.5F);
                    if(entity.facing < 0) entity.facing = 359;
                }
                else{
                    entity.facing += 0.1;
                    stack.rotateAround(Axis.YN.rotationDegrees(entity.facing), 0.5F, 0, 0.5F);
                    if(entity.facing > 360) entity.facing = 1;
                }
                ModMessages.sendToServer(new PRYBlockRendererC2SPacket(entity.facing, entity.getBlockPos(), false));
            }
            else
            {
                entity.facing = defaultAngle;
                ModMessages.sendToServer(new PRYBlockRendererC2SPacket(entity.facing, entity.getBlockPos(), false));
                stack.rotateAround(Axis.YN.rotationDegrees(entity.facing), 0.5F, 0, 0.5F);
            }

            //stack.rotateAround(Axis.YN.rotationDegrees(facing), 0.5F, 0, 0.5F);
            blockRenderDispatcher.renderSingleBlock(state, stack, buffer, packedLight, packedOverlay);
            stack.pushPose();
            stack.popPose();
            return;
        }

        Vec3 currentPos = entity.getBlockPos().getCenter().add(new Vec3(-1, 0, 1));
        Vec3 resultantVector = new Vec3(targetPos.x - currentPos.x,
                targetPos.y - currentPos.y,
                targetPos.z - currentPos.z);
        Vec3 resultantVectorHorizontal = new Vec3(resultantVector.x, 0, resultantVector.z);
        Vec3 xVector = new Vec3(0, 0, 1);

        float angle = (float) (Math.acos(resultantVectorHorizontal.dot(xVector) / (resultantVectorHorizontal.length() * xVector.length())) * (180 / Math.PI));
        if (resultantVector.x > 0) {
            angle *= -1;
        }
        angle += 180;

        Vec3 facingVector = getVectorForRotation(0, entity.facing);
        Vec3 angleVector = getVectorForRotation(0, angle);
        Vec3 crossVector = facingVector.cross(angleVector);
        float angleBetween = angleBetween2Vectors(facingVector, angleVector);

        if(angleBetween > 1){
            //rotation needed
            entity.pointingAtTarget = false;
            if(crossVector.y > 0){
                entity.facing -= 0.2;
                //coordinate for stack.rotateAround = translate coordinate + 0.5
                stack.rotateAround(Axis.YN.rotationDegrees(entity.facing), -0.5F, 0, 1.5F);
                stack.translate(-1, 0, 1);
                if(entity.facing < 0) entity.facing = 359;
            }
            else{
                entity.facing += 0.2;
                //coordinate for stack.rotateAround = translate coordinate + 0.5
                stack.rotateAround(Axis.YN.rotationDegrees(entity.facing), -0.5F, 0, 1.5F);
                stack.translate(-1, 0, 1);
                if(entity.facing > 360) entity.facing = 1;
            }
            ModMessages.sendToServer(new PRYBlockRendererC2SPacket(entity.facing, entity.getBlockPos(), false));
        }
        else{
            entity.pointingAtTarget = true;
            entity.facing = angle;
            ModMessages.sendToServer(new PRYBlockRendererC2SPacket(entity.facing, entity.getBlockPos(), true));
            //coordinate for stack.rotateAround = translate coordinate + 0.5
            stack.rotateAround(Axis.YN.rotationDegrees(entity.facing), -0.5F, 0, 1.5F);
            stack.rotateAround(Axis.ZN.rotationDegrees(entity.facing), -0.5F, 0, 1.5F);
            stack.translate(-1, 0, 1);
        }

        blockRenderDispatcher.renderSingleBlock(state, stack, buffer, packedLight, packedOverlay);
        stack.pushPose();
        stack.popPose();
    }

    private Vec3 getVectorForRotation(float pitch, float yaw) {
        float f = (float) Math.cos(-yaw * 0.017453292F - (float)Math.PI);
        float f1 = (float) Math.sin(-yaw * 0.017453292F - (float)Math.PI);
        float f2 = (float) -Math.cos(-pitch * 0.017453292F);
        float f3 = (float) Math.sin(-pitch * 0.017453292F);
        return new Vec3(f1 * f2 * -1, f3, f * f2 * -1);
    }
    
    private float angleBetween2Vectors(Vec3 v1, Vec3 v2) {
        return (float) (Math.acos(v1.dot(v2) / (v1.length() * v2.length())) * (180 / Math.PI));
    }

    private BlockState getBlockState(PRYBlockEntity entity){
        int num = entity.numberOfMissiles;
        if(num == 0) return ModBlock.PRYLAUNCHER_0.get().defaultBlockState();
        else if(num >= 1 && num <= 10) return ModBlock.PRYLAUNCHER_1.get().defaultBlockState();
        else if(num >= 11 && num <= 20) return ModBlock.PRYLAUNCHER_2.get().defaultBlockState();
        else if(num >= 21 && num <= 30) return ModBlock.PRYLAUNCHER_3.get().defaultBlockState();
        else if(num >= 31 && num <= 40) return ModBlock.PRYLAUNCHER_4.get().defaultBlockState();
        else if(num >= 41 && num <= 50) return ModBlock.PRYLAUNCHER_5.get().defaultBlockState();
        else return ModBlock.PRYLAUNCHER_6.get().defaultBlockState();
    }
}
