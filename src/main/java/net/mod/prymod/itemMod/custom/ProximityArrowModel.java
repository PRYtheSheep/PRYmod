package net.mod.prymod.itemMod.custom;// Made with Blockbench 4.7.4
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.mod.prymod.PRYmod;

public class ProximityArrowModel extends EntityModel<ProximityArrowEntity> {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(PRYmod.MODID, "pryprojectile"), "main");
    private final ModelPart body;
    private final ModelPart nose;
    private final ModelPart fin1;
    private final ModelPart fin2;
    private final ModelPart fin3;
    private final ModelPart fin4;
    private final ModelPart fin5;
    private final ModelPart fin6;
    private final ModelPart fin7;
    private final ModelPart fin8;

    public ProximityArrowModel(ModelPart root) {
        this.body = root.getChild("body");
        this.nose = root.getChild("nose");
        this.fin1 = root.getChild("fin1");
        this.fin2 = root.getChild("fin2");
        this.fin3 = root.getChild("fin3");
        this.fin4 = root.getChild("fin4");
        this.fin5 = root.getChild("fin5");
        this.fin6 = root.getChild("fin6");
        this.fin7 = root.getChild("fin7");
        this.fin8 = root.getChild("fin8");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(28, 0).addBox(-1.5F, -28.5F, -0.5F, 1.0F, 29.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(16, 0).addBox(0.5F, -28.5F, -0.5F, 1.0F, 29.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(24, 0).addBox(-0.5F, -28.5F, -1.5F, 1.0F, 29.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(20, 0).addBox(-0.5F, -28.5F, 0.5F, 1.0F, 29.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.5F, 23.5F, 0.5F));

        PartDefinition cube_r1 = body.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -28.5F, -0.7F, 1.0F, 29.0F, 1.4F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.0F, 0.0F, 1.0F, 0.0F, -0.7854F, 0.0F));

        PartDefinition cube_r2 = body.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(4, 0).addBox(0.0F, -28.5F, -0.7F, 1.0F, 29.0F, 1.4F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.0F, 0.0F, -1.0F, 0.0F, -0.7854F, 0.0F));

        PartDefinition cube_r3 = body.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(12, 0).addBox(-0.7F, -28.5F, 0.0F, 1.4F, 29.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.0F, 0.0F, -1.0F, 0.0F, -0.7854F, 0.0F));

        PartDefinition cube_r4 = body.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(8, 0).addBox(-0.7F, -28.5F, -1.0F, 1.4F, 29.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.0F, 0.0F, 1.0F, 0.0F, -0.7854F, 0.0F));

        PartDefinition nose = partdefinition.addOrReplaceChild("nose", CubeListBuilder.create().texOffs(36, 25).addBox(0.0F, -33.0F, 0.0F, 1.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition cube_r5 = nose.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(36, 5).addBox(-0.5F, -1.8855F, -0.7426F, 1.0F, 4.1F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.5F, -30.9145F, 0.7426F, 2.8972F, 0.0F, -3.1416F));

        PartDefinition cube_r6 = nose.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(36, 15).addBox(-0.5F, -2.05F, 0.0F, 1.0F, 4.1F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.5F, -30.95F, -0.5F, -0.2443F, 0.0F, 0.0F));

        PartDefinition cube_r7 = nose.addOrReplaceChild("cube_r7", CubeListBuilder.create().texOffs(36, 20).addBox(-0.5F, -1.8826F, -0.7257F, 1.0F, 4.1F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.7468F, -30.9174F, 0.5F, 0.0F, -1.5708F, -0.2443F));

        PartDefinition cube_r8 = nose.addOrReplaceChild("cube_r8", CubeListBuilder.create().texOffs(36, 10).addBox(-0.5F, -1.7531F, 0.0F, 1.0F, 4.1F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.4189F, -31.2469F, 0.5F, 0.0F, 1.5708F, 0.2443F));

        PartDefinition fin1 = partdefinition.addOrReplaceChild("fin1", CubeListBuilder.create().texOffs(32, 18).addBox(-2.0F, -5.3F, 0.0F, 1.0F, 5.3F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(20, 37).addBox(-3.0F, -2.9F, 0.0F, 1.0F, 2.9F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition cube_r9 = fin1.addOrReplaceChild("cube_r9", CubeListBuilder.create().texOffs(12, 30).addBox(-0.5F, -8.5F, -0.5F, 1.0F, 8.6F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.5F, -0.5F, 0.5F, 0.0F, 0.0F, 0.3927F));

        PartDefinition fin2 = partdefinition.addOrReplaceChild("fin2", CubeListBuilder.create().texOffs(32, 12).addBox(-2.0F, -5.3F, 4.0F, 1.0F, 5.3F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(16, 37).addBox(-3.0F, -2.9F, 4.0F, 1.0F, 2.9F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.0F, 24.0F, 1.0F, 0.0F, 1.5708F, 0.0F));

        PartDefinition cube_r10 = fin2.addOrReplaceChild("cube_r10", CubeListBuilder.create().texOffs(8, 30).addBox(-0.5F, -8.5F, -0.5F, 1.0F, 8.6F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.5F, -0.5F, 4.5F, 0.0F, 0.0F, 0.3927F));

        PartDefinition fin3 = partdefinition.addOrReplaceChild("fin3", CubeListBuilder.create().texOffs(32, 6).addBox(4.0F, -5.3F, 4.0F, 1.0F, 5.3F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(36, 34).addBox(3.0F, -2.9F, 4.0F, 1.0F, 2.9F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(5.0F, 24.0F, -6.0F, 0.0F, -1.5708F, 0.0F));

        PartDefinition cube_r11 = fin3.addOrReplaceChild("cube_r11", CubeListBuilder.create().texOffs(4, 30).addBox(-0.5F, -8.5F, -0.5F, 1.0F, 8.6F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.5F, -0.5F, 4.5F, 0.0F, 0.0F, 0.3927F));

        PartDefinition fin4 = partdefinition.addOrReplaceChild("fin4", CubeListBuilder.create().texOffs(32, 0).addBox(4.0F, -5.3F, 4.0F, 1.0F, 5.3F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(36, 30).addBox(3.0F, -2.9F, 4.0F, 1.0F, 2.9F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(7.0F, 24.0F, 5.0F, 0.0F, 3.1416F, 0.0F));

        PartDefinition cube_r12 = fin4.addOrReplaceChild("cube_r12", CubeListBuilder.create().texOffs(0, 30).addBox(-0.5F, -8.5F, -0.5F, 1.0F, 8.6F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.5F, -0.5F, 4.5F, 0.0F, 0.0F, 0.3927F));

        PartDefinition fin5 = partdefinition.addOrReplaceChild("fin5", CubeListBuilder.create().texOffs(32, 34).addBox(4.0F, -7.0F, 4.0F, 1.0F, 4.2F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(39, 37).addBox(3.0F, -5.3F, 4.0F, 1.0F, 2.5F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(7.0F, 9.5F, 5.0F, 0.0F, 3.1416F, 0.0F));

        PartDefinition cube_r13 = fin5.addOrReplaceChild("cube_r13", CubeListBuilder.create().texOffs(28, 30).addBox(-2.0F, -8.5F, -0.5F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.5F, -0.5F, 4.5F, 0.0F, 0.0F, 0.5236F));

        PartDefinition fin6 = partdefinition.addOrReplaceChild("fin6", CubeListBuilder.create().texOffs(36, 0).addBox(0.0163F, -2.0123F, -0.5F, 1.0F, 4.2F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(35, 38).addBox(-0.9837F, -0.3123F, -0.5F, 1.0F, 2.5F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.9837F, 4.5123F, 0.5F));

        PartDefinition cube_r14 = fin6.addOrReplaceChild("cube_r14", CubeListBuilder.create().texOffs(24, 30).addBox(-2.0F, -8.5F, -0.5F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.4837F, 4.4877F, 0.0F, 0.0F, 0.0F, 0.5236F));

        PartDefinition fin7 = partdefinition.addOrReplaceChild("fin7", CubeListBuilder.create().texOffs(32, 29).addBox(0.0163F, -2.0123F, -0.5F, 1.0F, 4.2F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(28, 37).addBox(-0.9837F, -0.3123F, -0.5F, 1.0F, 2.5F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.6163F, 4.5123F, -2.0F, 0.0F, -1.5708F, 0.0F));

        PartDefinition cube_r15 = fin7.addOrReplaceChild("cube_r15", CubeListBuilder.create().texOffs(20, 30).addBox(-2.0F, -8.5F, -0.5F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.4837F, 4.4877F, 0.0F, 0.0F, 0.0F, 0.5236F));

        PartDefinition fin8 = partdefinition.addOrReplaceChild("fin8", CubeListBuilder.create().texOffs(32, 24).addBox(0.0163F, -2.0123F, -0.5F, 1.0F, 4.2F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(24, 37).addBox(-0.9837F, -0.3123F, -0.5F, 1.0F, 2.5F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.6163F, 4.5123F, 3.0F, 0.0F, 1.5708F, 0.0F));

        PartDefinition cube_r16 = fin8.addOrReplaceChild("cube_r16", CubeListBuilder.create().texOffs(16, 30).addBox(-2.0F, -8.5F, -0.5F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.4837F, 4.4877F, 0.0F, 0.0F, 0.0F, 0.5236F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }


    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        body.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        nose.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        fin1.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        fin2.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        fin3.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        fin4.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        fin5.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        fin6.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        fin7.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        fin8.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }

    @Override
    public void setupAnim(ProximityArrowEntity p_102618_, float p_102619_, float p_102620_, float p_102621_, float p_102622_, float p_102623_) {

    }
}