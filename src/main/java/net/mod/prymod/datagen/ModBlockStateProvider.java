package net.mod.prymod.datagen;

import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.SaplingBlock;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.mod.prymod.ModBlock.ModBlock;
import net.mod.prymod.PRYmod;

public class ModBlockStateProvider extends BlockStateProvider {
    public ModBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, PRYmod.MODID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        blockWithItem(ModBlock.BLACK_OPAL_BLOCK);
        blockWithItem(ModBlock.BLACK_OPAL_ORE);
        blockWithItem(ModBlock.DEEPSLATE_BLACK_OPAL_ORE);
        blockWithItem(ModBlock.ENDSTONE_BLACK_OPAL_ORE);
        blockWithItem(ModBlock.NETHERRACK_BLACK_OPAL_ORE);

        logBlock(((RotatedPillarBlock) ModBlock.EBONY_LOG.get()));
        axisBlock((RotatedPillarBlock) ModBlock.EBONY_WOOD.get(), blockTexture(ModBlock.EBONY_LOG.get()), blockTexture(ModBlock.EBONY_LOG.get()));
        axisBlock((RotatedPillarBlock) ModBlock.STRIPPED_EBONY_LOG.get(), new ResourceLocation(PRYmod.MODID, "block/stripped_ebony_log"),
                new ResourceLocation(PRYmod.MODID, "block/stripped_ebony_log_top"));
        axisBlock((RotatedPillarBlock) ModBlock.STRIPPED_EBONY_WOOD.get(), new ResourceLocation(PRYmod.MODID, "block/stripped_ebony_log"),
                new ResourceLocation(PRYmod.MODID, "block/stripped_ebony_log"));

        blockWithItem(ModBlock.EBONY_PLANKS);
        blockWithItem(ModBlock.EBONY_LEAVES);
        saplingBlock(ModBlock.EBONY_SAPLING);

        simpleBlockItem(ModBlock.EBONY_LOG.get(), models().withExistingParent("prymod:ebony_log", "minecraft:block/cube_column"));
        simpleBlockItem(ModBlock.EBONY_WOOD.get(), models().withExistingParent("prymod:ebony_wood", "minecraft:block/cube_column"));
        simpleBlockItem(ModBlock.STRIPPED_EBONY_LOG.get(), models().withExistingParent("prymod:stripped_ebony_log", "minecraft:block/cube_column"));
        simpleBlockItem(ModBlock.STRIPPED_EBONY_WOOD.get(), models().withExistingParent("prymod:stripped_ebony_wood", "minecraft:block/cube_column"));
    }

    private void blockWithItem(RegistryObject<Block> blockRegistryObject) {
        simpleBlockWithItem(blockRegistryObject.get(), cubeAll(blockRegistryObject.get()));
    }

    private void saplingBlock(RegistryObject<Block> blockRegistryObject) {
        simpleBlock(blockRegistryObject.get(),
                models().cross(ForgeRegistries.BLOCKS.getKey(blockRegistryObject.get()).getPath(), blockTexture(blockRegistryObject.get())).renderType("cutout"));
    }

}
