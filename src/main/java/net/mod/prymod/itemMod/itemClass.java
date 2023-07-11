package net.mod.prymod.itemMod;

import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.mod.prymod.PRYmod;
import net.mod.prymod.itemMod.custom.ProximityArrowItem;
import net.mod.prymod.itemMod.custom.TestWand;

public class itemClass {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, PRYmod.MODID);

        public static final RegistryObject<Item> BLACK_OPAL =
            ITEMS.register("black_opal",
                    () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> RAW_BLACK_OPAL =
            ITEMS.register("raw_black_opal",
                    () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> TEST_WAND =
            ITEMS.register("test_wand",
                    () -> new TestWand(new Item.Properties()));

    public static final RegistryObject<Item> PROXIMITY_ARROW =
            ITEMS.register("proximity_arrow",
                    () -> new ProximityArrowItem(new Item.Properties()));

    public static void register(IEventBus eventBus){
        ITEMS.register(eventBus);
    }


}
