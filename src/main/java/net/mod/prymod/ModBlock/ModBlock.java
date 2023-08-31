package net.mod.prymod.ModBlock;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.mod.prymod.PRYmod;
import net.mod.prymod.itemMod.itemClass;
import net.mod.prymod.world.tree.EbonyTreeGrower;

import java.util.function.Supplier;

public class ModBlock {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, PRYmod.MODID);

    public static final RegistryObject<Cable> CABLE = registerBlock("cable", () ->
            new Cable(BlockBehaviour
                    .Properties.copy(Blocks.IRON_BLOCK)
                    .strength(1f)
                    .noOcclusion()
            ));

    public static final RegistryObject<CableLCurve> CABLELCURVE = registerBlock("cable_lcurve", () ->
            new CableLCurve(BlockBehaviour
                    .Properties.copy(Blocks.IRON_BLOCK)
                    .strength(1f)
                    .noOcclusion()
            ));

    public static final RegistryObject<CableTCross> CABLETCROSS = registerBlock("cable_tcross", () ->
            new CableTCross(BlockBehaviour
                    .Properties.copy(Blocks.IRON_BLOCK)
                    .strength(1f)
                    .noOcclusion()
            ));

    public static final RegistryObject<CableRadar> CABLERADAR = registerBlock("cable_radar", () ->
            new CableRadar(BlockBehaviour
                    .Properties.copy(Blocks.IRON_BLOCK)
                    .strength(1f)
                    .noOcclusion()
            ));

    public static final RegistryObject<CableLCurve1Radar> CABLELCURVE1RADAR = registerBlock("cable_lcurve1_radar", () ->
            new CableLCurve1Radar(BlockBehaviour
                    .Properties.copy(Blocks.IRON_BLOCK)
                    .strength(1f)
                    .noOcclusion()
            ));

    public static final RegistryObject<CableLCurve2Radar> CABLELCURVE2RADAR = registerBlock("cable_lcurve2_radar", () ->
            new CableLCurve2Radar(BlockBehaviour
                    .Properties.copy(Blocks.IRON_BLOCK)
                    .strength(1f)
                    .noOcclusion()
            ));

    public static final RegistryObject<CableTCross1Radar> CABLETCROSS1RADAR = registerBlock("cable_tcross1_radar", () ->
            new CableTCross1Radar(BlockBehaviour
                    .Properties.copy(Blocks.IRON_BLOCK)
                    .strength(1f)
                    .noOcclusion()
            ));

    public static final RegistryObject<CableTCross2Radar> CABLETCROSS2RADAR = registerBlock("cable_tcross2_radar", () ->
            new CableTCross2Radar(BlockBehaviour
                    .Properties.copy(Blocks.IRON_BLOCK)
                    .strength(1f)
                    .noOcclusion()
            ));

    public static final RegistryObject<CableTCross3Radar> CABLETCROSS3RADAR = registerBlock("cable_tcross3_radar", () ->
            new CableTCross3Radar(BlockBehaviour
                    .Properties.copy(Blocks.IRON_BLOCK)
                    .strength(1f)
                    .noOcclusion()
            ));

    public static final RegistryObject<CableGenerator> CABLEGENERATOR = registerBlock("cable_generator", () ->
            new CableGenerator(BlockBehaviour
                    .Properties.copy(Blocks.IRON_BLOCK)
                    .strength(1f)
                    .noOcclusion()
            ));

    public static final RegistryObject<CableLCurve1Generator> CABLELCURVE1GENERATOR = registerBlock("cable_lcurve1_generator", () ->
            new CableLCurve1Generator(BlockBehaviour
                    .Properties.copy(Blocks.IRON_BLOCK)
                    .strength(1f)
                    .noOcclusion()
            ));

    public static final RegistryObject<CableLCurve2Generator> CABLELCURVE2GENERATOR = registerBlock("cable_lcurve2_generator", () ->
            new CableLCurve2Generator(BlockBehaviour
                    .Properties.copy(Blocks.IRON_BLOCK)
                    .strength(1f)
                    .noOcclusion()
            ));

    public static final RegistryObject<CableTCross1Generator> CABLETCROSS1GENERATOR = registerBlock("cable_tcross1_generator", () ->
            new CableTCross1Generator(BlockBehaviour
                    .Properties.copy(Blocks.IRON_BLOCK)
                    .strength(1f)
                    .noOcclusion()
            ));

    public static final RegistryObject<CableTCross2Generator> CABLETCROSS2GENERATOR = registerBlock("cable_tcross2_generator", () ->
            new CableTCross2Generator(BlockBehaviour
                    .Properties.copy(Blocks.IRON_BLOCK)
                    .strength(1f)
                    .noOcclusion()
            ));

    public static final RegistryObject<CableTCross3Generator> CABLETCROSS3GENERATOR = registerBlock("cable_tcross3_generator", () ->
            new CableTCross3Generator(BlockBehaviour
                    .Properties.copy(Blocks.IRON_BLOCK)
                    .strength(1f)
                    .noOcclusion()
            ));

    public static final RegistryObject<CableLauncher> CABLELAUNCHER = registerBlock("cable_launcher", () ->
            new CableLauncher(BlockBehaviour
                    .Properties.copy(Blocks.IRON_BLOCK)
                    .strength(1f)
                    .noOcclusion()
            ));

    public static final RegistryObject<CableLCurve1Launcher> CABLELCURVE1LAUNCHER = registerBlock("cable_lcurve1_launcher", () ->
            new CableLCurve1Launcher(BlockBehaviour
                    .Properties.copy(Blocks.IRON_BLOCK)
                    .strength(1f)
                    .noOcclusion()
            ));

    public static final RegistryObject<CableLCurve2Launcher> CABLELCURVE2LAUNCHER = registerBlock("cable_lcurve2_launcher", () ->
            new CableLCurve2Launcher(BlockBehaviour
                    .Properties.copy(Blocks.IRON_BLOCK)
                    .strength(1f)
                    .noOcclusion()
            ));

    public static final RegistryObject<CableTCross1Launcher> CABLETCROSS1LAUNCHER = registerBlock("cable_tcross1_launcher", () ->
            new CableTCross1Launcher(BlockBehaviour
                    .Properties.copy(Blocks.IRON_BLOCK)
                    .strength(1f)
                    .noOcclusion()
            ));

    public static final RegistryObject<CableTCross2Launcher> CABLETCROSS2LAUNCHER = registerBlock("cable_tcross2_launcher", () ->
            new CableTCross2Launcher(BlockBehaviour
                    .Properties.copy(Blocks.IRON_BLOCK)
                    .strength(1f)
                    .noOcclusion()
            ));

    public static final RegistryObject<CableTCross3Launcher> CABLETCROSS3LAUNCHER = registerBlock("cable_tcross3_launcher", () ->
            new CableTCross3Launcher(BlockBehaviour
                    .Properties.copy(Blocks.IRON_BLOCK)
                    .strength(1f)
                    .noOcclusion()
            ));

    public static final RegistryObject<PRYGenerator> PRYGENERATOR = registerBlock("prygenerator", () ->
            new PRYGenerator(BlockBehaviour
                    .Properties.copy(Blocks.IRON_BLOCK)
                    .strength(1f)
                    .noOcclusion()
            ));

    public static final RegistryObject<PRYBlock> PRYPROJECTILE = registerBlock("pryprojectile", () ->
            new PRYBlock(BlockBehaviour
                    .Properties.copy(Blocks.IRON_BLOCK)
                    .strength(1f)
                    .requiresCorrectToolForDrops()
                    .noOcclusion()
            ));

    public static final RegistryObject<PRYRadar> PRYRADAR = registerBlock("pryradar", () ->
            new PRYRadar(BlockBehaviour
                    .Properties.copy(Blocks.IRON_BLOCK)
                    .strength(1f)
                    .noOcclusion()
            ));

    public static final RegistryObject<PRYBlock> PRYBLOCK = registerBlock("pryblock", () ->
            new PRYBlock(BlockBehaviour
                    .Properties.copy(Blocks.IRON_BLOCK)
                    .strength(0.1f)
                    .noOcclusion()
            ));

    public static final RegistryObject<PRYLauncher_6> PRYLAUNCHER_0 = registerBlock("prylauncher_0", () ->
            new PRYLauncher_6(BlockBehaviour
                    .Properties.copy(Blocks.IRON_BLOCK)
                    .strength(1f)
                    .requiresCorrectToolForDrops()
                    .noOcclusion()
            ));

    public static final RegistryObject<PRYLauncher_6> PRYLAUNCHER_6 = registerBlock("prylauncher_6", () ->
            new PRYLauncher_6(BlockBehaviour
                    .Properties.copy(Blocks.IRON_BLOCK)
                    .strength(1f)
                    .requiresCorrectToolForDrops()
                    .noOcclusion()
            ));

    public static final RegistryObject<Block> BLACK_OPAL_BLOCK = registerBlock("black_opal_block", () ->
            new Block(BlockBehaviour
                    .Properties.copy(Blocks.IRON_BLOCK)
                    .strength(1f)
                    .requiresCorrectToolForDrops()
                    ));
    public static final RegistryObject<Block> BLACK_OPAL_ORE = registerBlock("black_opal_ore", () ->
            new DropExperienceBlock(BlockBehaviour
                    .Properties.copy(Blocks.STONE)
                    .strength(1f)
                    .requiresCorrectToolForDrops(),
                    UniformInt.of(2,6)));
    public static final RegistryObject<Block> DEEPSLATE_BLACK_OPAL_ORE = registerBlock("deepslate_black_opal_ore", () ->
            new DropExperienceBlock(BlockBehaviour
                    .Properties.copy(Blocks.STONE)
                    .strength(1f)
                    .requiresCorrectToolForDrops(),
                    UniformInt.of(2,6)));
    public static final RegistryObject<Block> NETHERRACK_BLACK_OPAL_ORE = registerBlock("netherrack_black_opal_ore", () ->
            new DropExperienceBlock(BlockBehaviour
                    .Properties.copy(Blocks.STONE)
                    .strength(1f)
                    .requiresCorrectToolForDrops(),
                    UniformInt.of(2,6)));
    public static final RegistryObject<Block> ENDSTONE_BLACK_OPAL_ORE = registerBlock("endstone_black_opal_ore", () ->
            new DropExperienceBlock(BlockBehaviour
                    .Properties.copy(Blocks.STONE)
                    .strength(1f)
                    .requiresCorrectToolForDrops(),
                    UniformInt.of(2,6)));

    public static final RegistryObject<Block> EBONY_LOG = registerBlock("ebony_log", () ->
            new ModFlammableRotatedPillarBlock(BlockBehaviour
                    .Properties.copy(Blocks.DARK_OAK_LOG)
                    .strength(5f)
                    ));
    public static final RegistryObject<Block> EBONY_WOOD = registerBlock("ebony_wood", () ->
            new ModFlammableRotatedPillarBlock(BlockBehaviour
                    .Properties.copy(Blocks.DARK_OAK_WOOD)
                    .strength(5f)
            ));
    public static final RegistryObject<Block> STRIPPED_EBONY_LOG = registerBlock("stripped_ebony_log", () ->
            new ModFlammableRotatedPillarBlock(BlockBehaviour
                    .Properties.copy(Blocks.STRIPPED_DARK_OAK_LOG)
                    .strength(5f)
            ));
    public static final RegistryObject<Block> STRIPPED_EBONY_WOOD = registerBlock("stripped_ebony_wood", () ->
            new ModFlammableRotatedPillarBlock(BlockBehaviour
                    .Properties.copy(Blocks.STRIPPED_DARK_OAK_WOOD)
                    .strength(5f)
            ));
    public static final RegistryObject<Block> EBONY_PLANKS = registerBlock("ebony_planks", () ->
            new Block(BlockBehaviour
                    .Properties.copy(Blocks.DARK_OAK_PLANKS)){
                @Override
                public boolean isFlammable(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
                    return true;
                }

                @Override
                public int getFlammability(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
                    return 5;
                }

                @Override
                public int getFireSpreadSpeed(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
                    return 5;
                }
            }
            );

    public static final RegistryObject<Block> EBONY_LEAVES = registerBlock("ebony_leaves", () ->
            new LeavesBlock(BlockBehaviour
                    .Properties.copy(Blocks.DARK_OAK_LEAVES)){
                @Override
                public boolean isFlammable(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
                    return true;
                }

                @Override
                public int getFlammability(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
                    return 30;
                }

                @Override
                public int getFireSpreadSpeed(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
                    return 60;
                }
            }
            );

    public static final RegistryObject<Block> EBONY_SAPLING = registerBlock("ebony_sapling", () ->
            new SaplingBlock(new EbonyTreeGrower(), BlockBehaviour
                    .Properties.copy(Blocks.DARK_OAK_SAPLING)
            ));
    private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block){
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block> RegistryObject<Item> registerBlockItem(String name, RegistryObject<T> block){
        return itemClass.ITEMS.register(name, () ->
                new BlockItem(block.get(), new Item.Properties()));
    }

    public static void register(IEventBus eventBus){
        BLOCKS.register(eventBus);
    }
}
