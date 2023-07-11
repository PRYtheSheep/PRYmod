package net.mod.prymod.itemMod;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.CreativeModeTabEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.mod.prymod.PRYmod;

@Mod.EventBusSubscriber(modid = PRYmod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModCreativeModeTab {
    public static CreativeModeTab PRYMOD_TAB;

    @SubscribeEvent
    public static void registerCreativeModeTab(CreativeModeTabEvent.Register event){
        PRYMOD_TAB = event.registerCreativeModeTab(new ResourceLocation(PRYmod.MODID, "prymod_tab"),
                (builder) -> builder.icon(
                        () -> new ItemStack(itemClass.BLACK_OPAL.get())).title(Component.translatable("creativemodetab.prymod_tab")));
    }
}
