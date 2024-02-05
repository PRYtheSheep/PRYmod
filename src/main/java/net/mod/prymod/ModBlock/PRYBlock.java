package net.mod.prymod.ModBlock;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.*;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.Nullable;

public class PRYBlock extends Block implements EntityBlock {

    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

    private static final VoxelShape SHAPE = Block.box(0, 0, 0, 16, 8, 16);
    public PRYBlock(Properties properties) {
        super(properties);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return ModBlockEntityInit.PRYBLOCKENTITY.get().create(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return level.isClientSide ? null : ($0, pos, $1, blockEntity) -> {
            if(blockEntity instanceof PRYBlockEntity pryBlockEntity){
                pryBlockEntity.tick();
            }
        };
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult trace) {
        if (!level.isClientSide) {
            BlockEntity be = level.getBlockEntity(pos);
            if (be instanceof PRYBlockEntity) {
                MenuProvider containerProvider = new MenuProvider() {
                    @Override
                    public Component getDisplayName() {
                        return Component.translatable("PRYBlock");
                    }

                    @Override
                    public AbstractContainerMenu createMenu(int windowId, Inventory playerInventory, Player playerEntity) {
                        return new PRYBlockContainer(windowId, playerEntity, pos);
                    }
                };
                NetworkHooks.openScreen((ServerPlayer) player, containerProvider, be.getBlockPos());
            } else {
                throw new IllegalStateException("Our named container provider is missing!");
            }
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if(!state.is(newState.getBlock())){
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if(blockEntity instanceof PRYBlockEntity){
                if(level instanceof ServerLevel){
                    ItemStack itemStack = ((PRYBlockEntity) blockEntity).inputItems.getStackInSlot(PRYBlockEntity.INPUT_SLOT);
                    ItemEntity item = new ItemEntity(level, pos.getX(), pos.getY(), pos.getZ(), itemStack);
                    level.addFreshEntity(item);
                }
            }
        }
        super.onRemove(state, level, pos, newState, isMoving);
    }

    @Override
    public RenderShape getRenderShape(BlockState p_49232_) {
        return RenderShape.MODEL;
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateBuilder) {
        stateBuilder.add(FACING);
    }

    @Override
    public VoxelShape getShape(BlockState p_60555_, BlockGetter p_60556_, BlockPos p_60557_, CollisionContext p_60558_) {
        return makeShape();
    }

    public VoxelShape makeShape(){
        VoxelShape shape = Shapes.empty();
        shape = Shapes.join(shape, Shapes.box(0.1875, 0.125, 0.0625, 0.8125, 0.1875, 0.9375), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.75, 0.08125, 0.0625, 0.8125, 0.125, 0.125), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.75, 0.0875, 0.13125, 0.80625, 0.125, 0.25), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.6, 0.125, 0.037500000000000006, 0.625, 0.375, 0.10625000000000001), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.3724904557574047, 0.13693284357489002, 0.037500000000000006, 0.3974904557574047, 0.38693284357489, 0.10625), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.3912404557574044, 0.36818284357488984, 0.037500000000000006, 0.6412404557574042, 0.39318284357488986, 0.10625), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.7499999999999999, 0, 0.2749999999999999, 0.79375, 0.31875, 0.31875), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.7000000000000001, 0.26874999999999993, 0.9312499999999997, 0.7562500000000001, 0.28749999999999987, 0.9874999999999993), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.25000000000000006, 0, 0.9374999999999998, 0.29375000000000007, 0.31875, 0.9812499999999993), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.2437500000000001, 0.22499999999999998, 0.9312499999999997, 0.30000000000000004, 0.24374999999999997, 0.9874999999999993), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.2437500000000001, 0.26874999999999993, 0.9312499999999997, 0.30000000000000004, 0.28749999999999987, 0.9874999999999993), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.7000000000000001, 0.22499999999999998, 0.9312499999999997, 0.7562500000000001, 0.24374999999999997, 0.9874999999999993), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.70625, 0, 0.9374999999999998, 0.7500000000000001, 0.31875, 0.9812499999999993), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.7437499999999999, 0.22499999999999998, 0.26874999999999993, 0.7999999999999999, 0.24374999999999997, 0.32499999999999996), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.7437499999999999, 0.26874999999999993, 0.26874999999999993, 0.7999999999999999, 0.28749999999999987, 0.32499999999999996), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.2062499999999999, 0.22499999999999998, 0.26874999999999993, 0.26250000000000007, 0.24374999999999997, 0.32499999999999996), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.21249999999999988, 0, 0.2749999999999999, 0.25625, 0.31875, 0.31875), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.2062499999999999, 0.26874999999999993, 0.26874999999999993, 0.26250000000000007, 0.28749999999999987, 0.32499999999999996), BooleanOp.OR);

        return shape;
    }

}
