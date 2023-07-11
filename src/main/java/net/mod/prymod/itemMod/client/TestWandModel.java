package net.mod.prymod.itemMod.client;

import net.minecraft.resources.ResourceLocation;
import net.mod.prymod.PRYmod;
import net.mod.prymod.itemMod.custom.TestWand;
import software.bernie.geckolib.model.GeoModel;

public class TestWandModel extends GeoModel<TestWand> {
    @Override
    public ResourceLocation getModelResource(TestWand animatable) {
        return new ResourceLocation(PRYmod.MODID, "geo/test_wand.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(TestWand animatable) {
        return new ResourceLocation(PRYmod.MODID, "textures/item/test_wand.png");
    }

    @Override
    public ResourceLocation getAnimationResource(TestWand animatable) {
        return new ResourceLocation(PRYmod.MODID, "animations/test_wand.animation.json");
    }
}
