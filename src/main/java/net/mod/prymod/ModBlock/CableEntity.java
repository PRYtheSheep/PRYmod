package net.mod.prymod.ModBlock;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class CableEntity extends BlockEntity {
    public CableEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntityInit.CABLEENTITY.get(), pos, state);
    }

    public boolean nextToGenerator = false;
    public void tick(){
    }
}
