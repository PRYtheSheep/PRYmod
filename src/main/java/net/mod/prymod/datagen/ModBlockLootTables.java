package net.mod.prymod.datagen;


import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.RegistryObject;
import net.mod.prymod.ModBlock.ModBlock;
import net.mod.prymod.itemMod.itemClass;

import java.util.Set;

public class ModBlockLootTables extends BlockLootSubProvider {
    public ModBlockLootTables() {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags());
    }

    @Override
    protected void generate() {
        dropSelf(ModBlock.BLACK_OPAL_BLOCK.get());

        add(ModBlock.BLACK_OPAL_ORE.get(),
                (block) -> createOreDrop(ModBlock.BLACK_OPAL_ORE.get(), itemClass.RAW_BLACK_OPAL.get()));
        add(ModBlock.DEEPSLATE_BLACK_OPAL_ORE.get(),
                (block) -> createOreDrop(ModBlock.DEEPSLATE_BLACK_OPAL_ORE.get(), itemClass.RAW_BLACK_OPAL.get()));
        add(ModBlock.NETHERRACK_BLACK_OPAL_ORE.get(),
                (block) -> createOreDrop(ModBlock.NETHERRACK_BLACK_OPAL_ORE.get(), itemClass.RAW_BLACK_OPAL.get()));
        add(ModBlock.ENDSTONE_BLACK_OPAL_ORE.get(),
                (block) -> createOreDrop(ModBlock.ENDSTONE_BLACK_OPAL_ORE.get(), itemClass.RAW_BLACK_OPAL.get()));

        this.dropSelf(ModBlock.EBONY_LOG.get());
        this.dropSelf(ModBlock.EBONY_WOOD.get());
        this.dropSelf(ModBlock.STRIPPED_EBONY_LOG.get());
        this.dropSelf(ModBlock.STRIPPED_EBONY_WOOD.get());
        this.dropSelf(ModBlock.EBONY_PLANKS.get());
        this.dropSelf(ModBlock.EBONY_SAPLING.get());

        this.add(ModBlock.EBONY_LEAVES.get(), (block ->
                createLeavesDrops(block, ModBlock.EBONY_LEAVES.get(), NORMAL_LEAVES_SAPLING_CHANCES)));

        dropSelf(ModBlock.CABLE.get());
        dropSelf(ModBlock.CABLEGENERATOR.get());
        dropSelf(ModBlock.CABLELAUNCHER.get());
        dropSelf(ModBlock.CABLERADAR.get());
        dropSelf(ModBlock.CABLELCURVE.get());
        dropSelf(ModBlock.CABLETCROSS.get());
        dropSelf(ModBlock.CABLELCURVE1GENERATOR.get());
        dropSelf(ModBlock.CABLELCURVE2GENERATOR.get());
        dropSelf(ModBlock.CABLELCURVE1LAUNCHER.get());
        dropSelf(ModBlock.CABLELCURVE2LAUNCHER.get());
        dropSelf(ModBlock.CABLELCURVE1GENERATOR.get());
        dropSelf(ModBlock.CABLELCURVE2GENERATOR.get());
        dropSelf(ModBlock.CABLETCROSS1GENERATOR.get());
        dropSelf(ModBlock.CABLETCROSS2GENERATOR.get());
        dropSelf(ModBlock.CABLETCROSS3GENERATOR.get());
        dropSelf(ModBlock.CABLETCROSS1LAUNCHER.get());
        dropSelf(ModBlock.CABLETCROSS2LAUNCHER.get());
        dropSelf(ModBlock.CABLETCROSS3LAUNCHER.get());
        dropSelf(ModBlock.CABLETCROSS1RADAR.get());
        dropSelf(ModBlock.CABLETCROSS2RADAR.get());
        dropSelf(ModBlock.CABLETCROSS3RADAR.get());
        dropSelf(ModBlock.CABLELCURVE1RADAR.get());
        dropSelf(ModBlock.CABLELCURVE2RADAR.get());
        dropSelf(ModBlock.PRYBLOCK.get());
        dropSelf(ModBlock.PRYGENERATOR.get());
        dropSelf(ModBlock.PRYRADAR.get());
        dropOther(ModBlock.PRYPROJECTILE.get(), Items.AIR);
        dropOther(ModBlock.PRYLAUNCHER.get(), Items.AIR);
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return ModBlock.BLOCKS.getEntries().stream().map(RegistryObject::get)::iterator;
    }
}
