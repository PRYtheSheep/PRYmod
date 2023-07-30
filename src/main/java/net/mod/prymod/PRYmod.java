package net.mod.prymod;

import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.mod.prymod.ModBlock.ModBlock;
import net.mod.prymod.ModBlock.ModBlockEntityInit;
import net.mod.prymod.itemMod.ModCreativeModeTab;
import net.mod.prymod.itemMod.client.ClientStartup;
import net.mod.prymod.itemMod.client.GUIRegister;
import net.mod.prymod.itemMod.custom.TestWand;
import net.mod.prymod.itemMod.itemClass;
import net.mod.prymod.itemMod.networking.ModMessages;
import net.mod.prymod.sound.ModSounds;
import org.slf4j.Logger;



// The value here should match an entry in the META-INF/mods.toml file
@Mod(PRYmod.MODID)
public class PRYmod
{
    // Define mod id in a common place for everything to reference
    public static final String MODID = "prymod";
    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();
    // Create a Deferred Register to hold Blocks which will all be registered under the "examplemod" namespace

    public PRYmod()
    {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        ModCreativeModeTab.register(modEventBus);
        itemClass.register(modEventBus);
        ModBlock.register(modEventBus);
        MinecraftForge.EVENT_BUS.register(new EventHandlingTest());
        MinecraftForge.EVENT_BUS.register(new ClientStartup());
        ModSounds.register(modEventBus);
        ModBlockEntityInit.BLOCK_ENTITY.register(modEventBus);
        ModBlockEntityInit.ENTITY_TYPE.register(modEventBus);
        GUIRegister.MENU_TYPES.register(modEventBus);

        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);

        // Register the item to a creative tab
        modEventBus.addListener(this::addCreative);
    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {
        ModMessages.register();
    }

    private void addCreative(BuildCreativeModeTabContentsEvent event)
    {
        if(event.getTabKey() == CreativeModeTabs.INGREDIENTS){
            event.accept(itemClass.BLACK_OPAL);
            event.accept(itemClass.RAW_BLACK_OPAL);
        }
        if(event.getTab() == ModCreativeModeTab.PRYMOD_TAB.get()){
            event.accept(itemClass.BLACK_OPAL);
            event.accept(itemClass.RAW_BLACK_OPAL);
            event.accept(ModBlock.BLACK_OPAL_BLOCK);
            event.accept(ModBlock.BLACK_OPAL_ORE);
            event.accept(ModBlock.DEEPSLATE_BLACK_OPAL_ORE);
            event.accept(ModBlock.NETHERRACK_BLACK_OPAL_ORE);
            event.accept(ModBlock.ENDSTONE_BLACK_OPAL_ORE);

            event.accept(ModBlock.EBONY_LOG);
            event.accept(ModBlock.EBONY_WOOD);
            event.accept(ModBlock.STRIPPED_EBONY_LOG);
            event.accept(ModBlock.STRIPPED_EBONY_WOOD);
            event.accept(ModBlock.EBONY_PLANKS);
            event.accept(ModBlock.EBONY_LEAVES);
            event.accept(ModBlock.EBONY_SAPLING);

            event.accept(itemClass.TEST_WAND);
            event.accept(ModBlock.PRYBLOCK);
            event.accept(itemClass.PROXIMITY_ARROW);
            event.accept(ModBlock.PRYRADAR);
            event.accept(ModBlock.PRYGENERATOR);
            event.accept(ModBlock.CABLE);
            event.accept(ModBlock.CABLELCURVE);
            event.accept(ModBlock.CABLETCROSS);

            event.accept(ModBlock.CABLERADAR);
            event.accept(ModBlock.CABLELCURVE1RADAR);
            event.accept(ModBlock.CABLELCURVE2RADAR);
            event.accept(ModBlock.CABLETCROSS1RADAR);
            event.accept(ModBlock.CABLETCROSS2RADAR);
            event.accept(ModBlock.CABLETCROSS3RADAR);

            event.accept(ModBlock.CABLEGENERATOR);
            event.accept(ModBlock.CABLELCURVE1GENERATOR);
            event.accept(ModBlock.CABLELCURVE2GENERATOR);
            event.accept(ModBlock.CABLETCROSS1GENERATOR);
            event.accept(ModBlock.CABLETCROSS2GENERATOR);
            event.accept(ModBlock.CABLETCROSS3GENERATOR);


            event.accept(ModBlock.CABLELAUNCHER);
            event.accept(ModBlock.CABLELCURVE1LAUNCHER);
            event.accept(ModBlock.CABLELCURVE2LAUNCHER);
            event.accept(ModBlock.CABLETCROSS1LAUNCHER);
            event.accept(ModBlock.CABLETCROSS2LAUNCHER);
            event.accept(ModBlock.CABLETCROSS3LAUNCHER);
        }
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {
        // Do something when the server starts
        LOGGER.info("HELLO from server starting");
    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {

        }
    }
}

