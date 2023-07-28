package net.mod.prymod.itemMod;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import net.mod.prymod.PRYmod;

public class ModCreativeModeTab {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TAB_DEFERRED_REGISTER = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, PRYmod.MODID);
    public static RegistryObject<CreativeModeTab> PRYMOD_TAB = CREATIVE_MODE_TAB_DEFERRED_REGISTER.register("prymod_tab",
            () -> CreativeModeTab.builder().icon(
                    () -> new ItemStack(itemClass.BLACK_OPAL.get())).title(Component.translatable("creativemodetab.prymod_tab")).build());
    public static void register(IEventBus eventBus){
        CREATIVE_MODE_TAB_DEFERRED_REGISTER.register(eventBus);
    }
}
