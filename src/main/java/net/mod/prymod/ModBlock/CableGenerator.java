package net.mod.prymod.ModBlock;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import org.jetbrains.annotations.Nullable;

public class CableGenerator extends Cable{

    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final BooleanProperty NORTHCONNECTED = BooleanProperty.create("northconnected");
    public static final BooleanProperty EASTCONNECTED = BooleanProperty.create("eastconnected");
    public static final BooleanProperty SOUTHCONNECTED = BooleanProperty.create("southconnected");
    public static final BooleanProperty WESTCONNECTED = BooleanProperty.create("westconnected");
    public CableGenerator(Properties p_49795_) {
        super(p_49795_);
        this.registerDefaultState(
                this.stateDefinition.any().setValue(NORTHCONNECTED, false).setValue(EASTCONNECTED, false).setValue(SOUTHCONNECTED, false).setValue(WESTCONNECTED, false)
        );
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {

        Level level = context.getLevel();

        BlockPos currentPos = context.getClickedPos();
        BlockPos eastpos = new BlockPos(currentPos.getX()+1, currentPos.getY(), currentPos.getZ());
        BlockPos southpos = new BlockPos(currentPos.getX(), currentPos.getY(), currentPos.getZ()+1);
        BlockPos westpos = new BlockPos(currentPos.getX()-1, currentPos.getY(), currentPos.getZ());
        BlockPos northpos = new BlockPos(currentPos.getX(), currentPos.getY(), currentPos.getZ()-1);

        boolean east = level.getBlockEntity(eastpos) instanceof CableEntity;
        boolean south = level.getBlockEntity(southpos) instanceof CableEntity;
        boolean west = level.getBlockEntity(westpos) instanceof CableEntity;
        boolean north = level.getBlockEntity(northpos) instanceof CableEntity;

        //only 1 side connected
        if(east && !south && !west && !north) return this.defaultBlockState().setValue(EASTCONNECTED, true).setValue(FACING, context.getHorizontalDirection());
        if(!east && south && !west && !north) return this.defaultBlockState().setValue(SOUTHCONNECTED, true).setValue(FACING, context.getHorizontalDirection());
        if(!east && !south && west && !north) return this.defaultBlockState().setValue(WESTCONNECTED, true).setValue(FACING, context.getHorizontalDirection());
        if(!east && !south && !west && north) return this.defaultBlockState().setValue(NORTHCONNECTED, true).setValue(FACING, context.getHorizontalDirection());

        //2 opposite sides connected
        if(east && !south && west && !north) return this.defaultBlockState().setValue(EASTCONNECTED, true).setValue(WESTCONNECTED, true).setValue(FACING, context.getHorizontalDirection());
        if(!east && south && !west && north) return this.defaultBlockState().setValue(SOUTHCONNECTED, true).setValue(NORTHCONNECTED, true).setValue(FACING, context.getHorizontalDirection());

        //2 adjacent sides connected
        if(east && !south && !west && north) return this.defaultBlockState().setValue(EASTCONNECTED, true).setValue(NORTHCONNECTED, true).setValue(FACING, context.getHorizontalDirection());
        if(east && south && !west && !north) return this.defaultBlockState().setValue(EASTCONNECTED, true).setValue(SOUTHCONNECTED, true).setValue(FACING, context.getHorizontalDirection());
        if(!east && south && west && !north) return this.defaultBlockState().setValue(SOUTHCONNECTED, true).setValue(WESTCONNECTED, true).setValue(FACING, context.getHorizontalDirection());
        if(!east && !south && west && north) return this.defaultBlockState().setValue(WESTCONNECTED, true).setValue(NORTHCONNECTED, true).setValue(FACING, context.getHorizontalDirection());


        //default return
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection());
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateBuilder) {
        stateBuilder.add(NORTHCONNECTED).add(SOUTHCONNECTED).add(EASTCONNECTED).add(WESTCONNECTED).add(FACING);
    }

}
