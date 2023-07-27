package net.mod.prymod.itemMod.custom;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.mod.prymod.ModBlock.ModBlockEntityInit;
import net.mod.prymod.ModBlock.PRYBlockEntity;
import org.jetbrains.annotations.Nullable;

public class ProximityArrowItem extends ArrowItem {



    public ProximityArrowItem(Properties p_40512_) {
        super(p_40512_);
    }

    @Override
    public AbstractArrow createArrow(Level level, ItemStack p_40514_, LivingEntity shooter) {
        return new ProximityArrowEntity(ModBlockEntityInit.PROXIMITY_ARROW_ENTITY.get(), shooter, level);
    }



}
