package net.mod.prymod.itemMod.client;

import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.mod.prymod.ModBlock.ModBlockEntityInit;
import net.mod.prymod.ModBlock.PRYRadarContainer;
import net.mod.prymod.PRYmod;
import net.mod.prymod.Renderer.PRYBlockEntityRenderer;
import net.mod.prymod.Renderer.ProximityArrowRenderer;
import net.mod.prymod.itemMod.custom.ProximityArrowModel;

public class ClientStartup {
    @Mod.EventBusSubscriber(modid = PRYmod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public class ClientSetup {
        @SubscribeEvent
        public static void doSetup(FMLClientSetupEvent event) {
            EntityRenderers.register(ModBlockEntityInit.PROXIMITY_ARROW_ENTITY.get(), ProximityArrowRenderer::new);
            event.enqueueWork(() -> {
                MenuScreens.register(GUIRegister.PRYBLOCK_CONTAINER.get(), PRYBlockScreen::new);
                MenuScreens.register(GUIRegister.PRYGENERATOR_CONTAINER.get(), PRYGeneratorScreen::new);
                MenuScreens.register(GUIRegister.PRYRADAR_CONTAINER.get(), PRYRadarScreen::new);
            });
        }

        @SubscribeEvent
        public static void registerRender(final EntityRenderersEvent.RegisterRenderers event){
            event.registerBlockEntityRenderer(ModBlockEntityInit.PRYBLOCKENTITY.get(), PRYBlockEntityRenderer::new);
        }

        @SubscribeEvent
        public static void registerAdditional(ModelEvent.RegisterAdditional event){
            event.register(new ResourceLocation(PRYmod.MODID, "textures/block/prylauncher_0.png"));
            event.register(new ResourceLocation(PRYmod.MODID, "textures/block/prylauncher_6.png"));
        }

        @SubscribeEvent
        public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
            event.registerLayerDefinition(ProximityArrowModel.LAYER_LOCATION, ProximityArrowModel::createBodyLayer);
        }


    }
}
