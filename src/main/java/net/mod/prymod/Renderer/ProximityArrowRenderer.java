package net.mod.prymod.Renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.model.TridentModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import net.mod.prymod.PRYmod;
import net.mod.prymod.itemMod.custom.ProximityArrowEntity;
import net.mod.prymod.itemMod.custom.ProximityArrowModel;


public class ProximityArrowRenderer extends EntityRenderer<ProximityArrowEntity> {

    public static final ResourceLocation TEXTURE = new ResourceLocation(PRYmod.MODID, "textures/block/pryprojectile.png");

    private final ProximityArrowModel model;

    public ProximityArrowRenderer(EntityRendererProvider.Context manager){
        super(manager);
        this.model = new ProximityArrowModel(manager.bakeLayer(ProximityArrowModel.LAYER_LOCATION));
    }

    @Override
    public void render(ProximityArrowEntity entity, float yaw, float pitch, PoseStack stack, MultiBufferSource bufferSource, int light) {
        stack.pushPose();
        stack.translate(0, -0.1, 0);

        stack.mulPose(Axis.YP.rotationDegrees(Mth.lerp(pitch, entity.yRotO, entity.getYRot()) - 90.0F));
        stack.mulPose(Axis.ZP.rotationDegrees(Mth.lerp(pitch, entity.xRotO, entity.getXRot()) + 90.0F));


        VertexConsumer vertexconsumer = ItemRenderer.getFoilBufferDirect(bufferSource, this.model.renderType(this.getTextureLocation(entity)), false, entity.isFoil());
        this.model.renderToBuffer(stack, vertexconsumer, light, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        stack.popPose();
        super.render(entity, yaw, pitch, stack, bufferSource, light);
    }

    @Override
    public ResourceLocation getTextureLocation(ProximityArrowEntity p_114482_) {
        return TEXTURE;
    }
}
