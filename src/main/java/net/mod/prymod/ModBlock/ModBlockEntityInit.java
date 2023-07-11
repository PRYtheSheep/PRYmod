package net.mod.prymod.ModBlock;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.mod.prymod.PRYmod;
import net.mod.prymod.itemMod.custom.ProximityArrowEntity;

public class ModBlockEntityInit {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, PRYmod.MODID);
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPE = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, PRYmod.MODID);

    public static final RegistryObject<BlockEntityType<PRYBlockEntity>> PRYBLOCKENTITY = BLOCK_ENTITY.register("pryblock",
            () -> BlockEntityType.Builder.of(PRYBlockEntity::new, ModBlock.PRYBLOCK.get())
                    .build(null));

    public static final RegistryObject<BlockEntityType<PRYRadarEntity>> PRYRADARENTITY = BLOCK_ENTITY.register("pryradar",
            () -> BlockEntityType.Builder.of(PRYRadarEntity::new, ModBlock.PRYRADAR.get())
                    .build(null));

    public static final RegistryObject<BlockEntityType<PRYGeneratorEntity>> PRYGENERATORENTITY = BLOCK_ENTITY.register("prygenerator",
            () -> BlockEntityType.Builder.of(PRYGeneratorEntity::new, ModBlock.PRYGENERATOR.get())
                    .build(null));

    public static final RegistryObject<BlockEntityType<CableEntity>> CABLEENTITY = BLOCK_ENTITY.register("cable",
            () -> BlockEntityType.Builder.of(CableEntity::new, ModBlock.CABLE.get())
                    .build(null));

    public static final RegistryObject<EntityType<ProximityArrowEntity>> PROXIMITY_ARROW_ENTITY = ENTITY_TYPE.register("proximity_arrow",
            () -> EntityType.Builder.of(ProximityArrowEntity::new, MobCategory.MISC)
                    .sized(0.5F, 0.5F)
                    .clientTrackingRange(4)
                    .updateInterval(1)
                    .build("proximity_arrow"));
}
