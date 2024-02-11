package net.mod.prymod.ModBlock;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class TestBlock extends Block implements EntityBlock {

    public static final EnumProperty<TestMultiBlockPartIndex> PART = EnumProperty.create("part", TestMultiBlockPartIndex.class);

    public TestBlock(Properties p_49795_) {
        super(p_49795_);
        StateDefinition.Builder<Block, BlockState> builder = new StateDefinition.Builder<>(this);
        this.createBlockStateDefinition(builder);
        this.registerDefaultState(this.getStateDefinition().any().setValue(PART, TestMultiBlockPartIndex.LOW));
    }

    @Override
    public VoxelShape getShape(BlockState p_60555_, BlockGetter p_60556_, BlockPos p_60557_, CollisionContext p_60558_) {
        return Block.box(0, 0, 0, 16, 16, 16);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(PART, TestMultiBlockPartIndex.LOW);

    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos p_153215_, BlockState p_153216_) {
        return null;
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity livingEntity, ItemStack stack) {
        level.setBlock(pos.above(), state.setValue(PART, TestMultiBlockPartIndex.UP), 3);
    }

    @Override
    public InteractionResult use(BlockState p_60503_, Level p_60504_, BlockPos p_60505_, Player p_60506_, InteractionHand p_60507_, BlockHitResult p_60508_) {
        System.out.println("right clicked on test block");
        return InteractionResult.SUCCESS;
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> blockBlockStateBuilder) {
        blockBlockStateBuilder.add(PART);
    }

    protected boolean canSurvive(BlockState state, Level level, BlockPos pos){
        if(state.getValue(PART) == TestMultiBlockPartIndex.UP){
            BlockPos targetPos = pos.subtract(new Vec3i(0, 1, 0));
            return level.getBlockState(targetPos).getValue(PART) == TestMultiBlockPartIndex.LOW;
        }
        else{
            BlockPos targetPos = pos.subtract(new Vec3i(0, -1, 0));
            return level.getBlockState(targetPos).getValue(PART) == TestMultiBlockPartIndex.UP;
        }
    }

    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos wallPos, boolean p_60514_) {
        super.neighborChanged(state, level, pos, block, wallPos, p_60514_);
    }
}
