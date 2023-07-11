package net.mod.prymod.itemMod.client;

import net.mod.prymod.itemMod.custom.TestWand;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class TestWandRenderer extends GeoItemRenderer<TestWand> {
    public TestWandRenderer() {
        super(new TestWandModel());
    }
}
