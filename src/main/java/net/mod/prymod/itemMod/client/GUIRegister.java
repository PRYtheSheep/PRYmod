package net.mod.prymod.itemMod.client;

import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.mod.prymod.ModBlock.PRYBlockContainer;
import net.mod.prymod.ModBlock.PRYGeneratorContainer;
import net.mod.prymod.ModBlock.PRYRadarContainer;
import net.mod.prymod.PRYmod;

public class GUIRegister {

    public static final DeferredRegister<MenuType<?>> MENU_TYPES = DeferredRegister.create(ForgeRegistries.MENU_TYPES, PRYmod.MODID);

    public static final RegistryObject<MenuType<PRYBlockContainer>> PRYBLOCK_CONTAINER = MENU_TYPES.register("pryblock",
            () -> IForgeMenuType.create((windowId, inv, data) -> new PRYBlockContainer(windowId, inv.player, data.readBlockPos())));

    public static final RegistryObject<MenuType<PRYGeneratorContainer>> PRYGENERATOR_CONTAINER = MENU_TYPES.register("prygenerator",
            () -> IForgeMenuType.create((windowId, inv, data) -> new PRYGeneratorContainer(windowId, inv.player, data.readBlockPos())));

    public static final RegistryObject<MenuType<PRYRadarContainer>> PRYRADAR_CONTAINER = MENU_TYPES.register("pryradar",
            () -> IForgeMenuType.create((windowId, inv, data) -> new PRYRadarContainer(windowId, inv.player, data)));

    public static void init(IEventBus modEventBus) {
        MENU_TYPES.register(modEventBus);
    }

}
