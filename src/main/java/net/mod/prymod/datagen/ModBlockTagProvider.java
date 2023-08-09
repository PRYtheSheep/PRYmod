package net.mod.prymod.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.mod.prymod.ModBlock.ModBlock;
import net.mod.prymod.PRYmod;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ModBlockTagProvider extends BlockTagsProvider {

    public ModBlockTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, PRYmod.MODID, existingFileHelper);

    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(BlockTags.MINEABLE_WITH_PICKAXE)
                .add(ModBlock.PRYBLOCK.get(), ModBlock.PRYRADAR.get(), ModBlock.PRYGENERATOR.get(), ModBlock.CABLE.get());
        tag(BlockTags.NEEDS_IRON_TOOL)
                .add(ModBlock.PRYBLOCK.get(), ModBlock.PRYRADAR.get(), ModBlock.PRYGENERATOR.get(), ModBlock.CABLE.get());
    }
}
