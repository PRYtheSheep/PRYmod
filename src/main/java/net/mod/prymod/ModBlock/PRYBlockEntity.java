package net.mod.prymod.ModBlock;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fml.ISystemReportExtender;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;
import net.minecraftforge.network.PacketDistributor;
import net.mod.prymod.Renderer.RGB;
import net.mod.prymod.Renderer.TestRenderer;
import net.mod.prymod.itemMod.custom.ProximityArrowEntity;
import net.mod.prymod.itemMod.itemClass;
import net.mod.prymod.itemMod.networking.ModMessages;
import net.mod.prymod.itemMod.networking.packets.PRYBlockEntityS2C;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.reflect.Constructor;
import java.util.*;
import java.util.function.Predicate;

public class PRYBlockEntity extends BlockEntity {

    public PRYBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntityInit.PRYBLOCKENTITY.get(), pos, state);
    }

    int progress = 0;
    public float facing = -1;
    public UUID uuid;
    public LivingEntity target = null;
    public PRYRadarEntity radarEntity = null;
    public boolean pointingAtTarget = false;
    public boolean inflight = false;
    public int progressCount = -1;

    public static final String FACING = "facing";
    public static final String INPUT_ITEMS_TAG = "input_items_tag";

    public static final int INPUT_SLOT = 0;
    public static final int INPUT_SLOT_COUNT = 1;
    public static final int SLOT_COUNT = 0;

    public final ItemStackHandler inputItems = createItemHandler(INPUT_SLOT_COUNT);
    private final LazyOptional<IItemHandler> itemHandler = LazyOptional.of(() -> new CombinedInvWrapper(inputItems));
    private final LazyOptional<IItemHandler> inputItemHandler = LazyOptional.of(() -> new AdaptedItemHandler(inputItems) {
        @Override
        public @NotNull ItemStack extractItem(int slot, int amount, boolean simulate) {
            return ItemStack.EMPTY;
        }
    });

    public void tick() {
        if(this.level.isClientSide) return;

        ItemStack itemStack = inputItems.getStackInSlot(INPUT_SLOT);
        if(itemStack.is(itemClass.BLACK_OPAL.get())){
            inputItems.extractItem(INPUT_SLOT, 1, false);
        }

        if(!isConnectedToGenerator(this.getBlockPos())) return;
        if(target != null && target.getHealth() == 0){
            target = null;
            inflight = false;
            return;
        }
        if(isConnectedToRadar(this.getBlockPos()) != null){
            BlockPos radarPos = isConnectedToRadar(this.getBlockPos());
            radarEntity = (PRYRadarEntity) this.level.getBlockEntity(radarPos);
        }
        else return;
        Predicate<Entity> predicate = (i) -> (i instanceof Player);
        if(radarEntity.radarTarget != target) progressCount = -1;
        //Spawn projectile if target is found
        if(radarEntity.radarTarget != null){
            Vec3 test = new Vec3((radarEntity.radarTarget.getX() - this.getBlockPos().getX()),
                    (radarEntity.radarTarget.getY() - this.getBlockPos().getY()),
                    (radarEntity.radarTarget.getZ() - this.getBlockPos().getZ()));
            if(test.length() > 90) return;

            target = radarEntity.radarTarget;
            ModMessages.INSTANCE.send(PacketDistributor.ALL.noArg(), new PRYBlockEntityS2C(target.getUUID(), this.getBlockPos()));

            if(progress %2 == 0 && progressCount == -1) progressCount = progress;
            if(progress != -1 && progress - progressCount <= 100){
                RGB currentRGB = PRYRadarEntity.livingEntityRGBHashMap.get(target);
                if(progress %3 == 0 &&currentRGB.equals(new RGB(0, 1, 1))) PRYRadarEntity.livingEntityRGBHashMap.put(target, new RGB(1, 1, 0));
                else if(progress %3 == 0 && currentRGB.equals(new RGB(1, 1, 0))) PRYRadarEntity.livingEntityRGBHashMap.put(target, new RGB(0, 1, 1));
                if(progress-progressCount == 100) PRYRadarEntity.livingEntityRGBHashMap.put(target, new RGB(0, 1, 1));

                this.inflight = false;
                progress++;
                return;
            }
            else if(progress % 50 == 0 && pointingAtTarget){
                ProximityArrowEntity proxyArrow = new ProximityArrowEntity(ModBlockEntityInit.PROXIMITY_ARROW_ENTITY.get(), this.level);
                proxyArrow.setEntityOwner(this);
                proxyArrow.setPos(this.getBlockPos().getX() + 0.5, this.getBlockPos().getY() + 1.2, this.getBlockPos().getZ() + 0.5);
                Vec3 resultantVector = new Vec3((target.getX() - this.getBlockPos().getX()),
                        1,
                        (target.getZ() - this.getBlockPos().getZ()));
                if(!inflight){
                    proxyArrow.setDeltaMovement(resultantVector);
                    if(proxyArrow.getDeltaMovement().length() > 1){
                        proxyArrow.setDeltaMovement(proxyArrow.getDeltaMovement().multiply(
                                1/proxyArrow.getDeltaMovement().length(),
                                1/proxyArrow.getDeltaMovement().length(),
                                1/proxyArrow.getDeltaMovement().length()
                        ));
                    }
                    proxyArrow.target = target;
                    this.level.addFreshEntity(proxyArrow);
                    inflight = true;
                    Player player = this.level.getNearestPlayer(this.getBlockPos().getX(), this.getBlockPos().getY(), this.getBlockPos().getZ(), 10000, predicate);
                    if(player != null){
                        player.displayClientMessage(Component.literal("§aMissile launched"), true);
                    }
                }
            }
        }
        progress++;
        setChanged();
    }

    @Override
    protected void saveAdditional(CompoundTag nbt) {
        super.saveAdditional(nbt);
        //nbt.putInt("progress", this.progress);
        nbt.putFloat(FACING, this.facing);
        nbt.put(INPUT_ITEMS_TAG, inputItems.serializeNBT());
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        //this.progress = nbt.getInt("progress");
        this.facing = nbt.getFloat("facing");
        if (nbt.contains(INPUT_ITEMS_TAG)) {
            inputItems.deserializeNBT(nbt.getCompound(INPUT_ITEMS_TAG));
        }
    }

    @NotNull
    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.ITEM_HANDLER) {
            if (side == null) {
                return itemHandler.cast();
            } else {
                return inputItemHandler.cast();
            }
        } else {
            return super.getCapability(cap, side);
        }
    }

    @Nonnull
    private ItemStackHandler createItemHandler(int slots) {
        return new ItemStackHandler(slots) {
            @Override
            protected void onContentsChanged(int slot) {
                setChanged();
            }
        };
    }

    public ItemStackHandler getInputItems(){
        return inputItems;
    }

    private boolean isConnectedToGenerator(BlockPos startPos){
        if(level==null) return false;
        //Depth first search using a stack
        //Search order positive x, positive z, negative x, negative z
        //Discovered positions are stored in a LinkedList

        //Initialise the discovered linkedlist and stack
        LinkedList<BlockPos> discovered = new LinkedList<>();
        Stack<BlockPos> stack = new Stack<>();

        //Push in the start position into stack and set it as discovered
        stack.push(startPos);

        //Depth first search loop
        while(!stack.isEmpty()){
            BlockPos currentPos = stack.peek();
            BlockPos nextPos = null;

            //Exit condition
            if(level.getBlockEntity(currentPos) instanceof PRYGeneratorEntity){
                return true;
            }

            //Cable must connect to the designated port of the block, so check if the current position is the start
            //position
            if(currentPos.equals(startPos)){
                switch(this.getBlockState().getValue(PRYRadar.FACING)){
                    case NORTH -> {
                        nextPos = new BlockPos(currentPos.getX(), currentPos.getY(), currentPos.getZ()-1);
                    }
                    case EAST -> {
                        nextPos = new BlockPos(currentPos.getX()+1, currentPos.getY(), currentPos.getZ());
                    }
                    case SOUTH -> {
                        nextPos = new BlockPos(currentPos.getX(), currentPos.getY(), currentPos.getZ()+1);
                    }
                    case WEST -> {
                        nextPos = new BlockPos(currentPos.getX()-1, currentPos.getY(), currentPos.getZ());
                    }
                }
                Block block = level.getBlockState(nextPos).getBlock();
                if(block instanceof Cable && !contains(discovered, nextPos)){
                    stack.push(nextPos);
                    discovered.add(nextPos);
                    discovered.add(startPos);
                }
                else stack.pop();
                continue;
            }

            //Try positive x first
            nextPos = new BlockPos(currentPos.getX()+1, currentPos.getY(), currentPos.getZ());
            if(!contains(discovered, nextPos) && (level.getBlockState(nextPos).getBlock() instanceof Cable || level.getBlockState(nextPos).getBlock() instanceof PRYGenerator)){
                stack.push(nextPos);
                discovered.add(nextPos);
                continue;
            }

            //Try positive z next
            nextPos = new BlockPos(currentPos.getX(), currentPos.getY(), currentPos.getZ()+1);
            if(!contains(discovered, nextPos) && (level.getBlockState(nextPos).getBlock() instanceof Cable || level.getBlockState(nextPos).getBlock() instanceof PRYGenerator)){
                stack.push(nextPos);
                discovered.add(nextPos);
                continue;
            }

            //Try negative x next
            nextPos = new BlockPos(currentPos.getX()-1, currentPos.getY(), currentPos.getZ());
            if(!contains(discovered, nextPos) && (level.getBlockState(nextPos).getBlock() instanceof Cable || level.getBlockState(nextPos).getBlock() instanceof PRYGenerator)){
                stack.push(nextPos);
                discovered.add(nextPos);
                continue;
            }

            //Try negative z next
            nextPos = new BlockPos(currentPos.getX(), currentPos.getY(), currentPos.getZ()-1);
            if(!contains(discovered, nextPos) && (level.getBlockState(nextPos).getBlock() instanceof Cable || level.getBlockState(nextPos).getBlock() instanceof PRYGenerator)){
                stack.push(nextPos);
                discovered.add(nextPos);
                continue;
            }

            //No positions added to stack, pop the top one
            stack.pop();
        }
        return false;
    }

    private BlockPos isConnectedToRadar(BlockPos startPos){
        if(level==null) return null;
        //Depth first search using a stack
        //Search order positive x, positive z, negative x, negative z
        //Discovered positions are stored in a LinkedList

        //Initialise the discovered linkedlist and stack
        LinkedList<BlockPos> discovered = new LinkedList<>();
        Stack<BlockPos> stack = new Stack<>();

        //Push in the start position into stack and set it as discovered
        stack.push(startPos);

        //Depth first search loop
        while(!stack.isEmpty()){
            BlockPos currentPos = stack.peek();
            BlockPos nextPos = null;

            //Exit condition
            if(level.getBlockEntity(currentPos) instanceof PRYRadarEntity){
                return currentPos;
            }

            //Cable must connect to the designated port of the block, so check if the current position is the start
            //position
            if(currentPos.equals(startPos)){
                switch(this.getBlockState().getValue(PRYRadar.FACING)){
                    case NORTH -> {
                        nextPos = new BlockPos(currentPos.getX(), currentPos.getY(), currentPos.getZ()-1);
                    }
                    case EAST -> {
                        nextPos = new BlockPos(currentPos.getX()+1, currentPos.getY(), currentPos.getZ());
                    }
                    case SOUTH -> {
                        nextPos = new BlockPos(currentPos.getX(), currentPos.getY(), currentPos.getZ()+1);
                    }
                    case WEST -> {
                        nextPos = new BlockPos(currentPos.getX()-1, currentPos.getY(), currentPos.getZ());
                    }
                }
                Block block = level.getBlockState(nextPos).getBlock();
                if(block instanceof Cable && !contains(discovered, nextPos)){
                    stack.push(nextPos);
                    discovered.add(nextPos);
                    discovered.add(startPos);
                }
                else stack.pop();
                continue;
            }

            //Try positive x first
            nextPos = new BlockPos(currentPos.getX()+1, currentPos.getY(), currentPos.getZ());
            if(!contains(discovered, nextPos) && (level.getBlockState(nextPos).getBlock() instanceof Cable || level.getBlockState(nextPos).getBlock() instanceof PRYRadar)){
                stack.push(nextPos);
                discovered.add(nextPos);
                continue;
            }

            //Try positive z next
            nextPos = new BlockPos(currentPos.getX(), currentPos.getY(), currentPos.getZ()+1);
            if(!contains(discovered, nextPos) && (level.getBlockState(nextPos).getBlock() instanceof Cable || level.getBlockState(nextPos).getBlock() instanceof PRYRadar)){
                stack.push(nextPos);
                discovered.add(nextPos);
                continue;
            }

            //Try negative x next
            nextPos = new BlockPos(currentPos.getX()-1, currentPos.getY(), currentPos.getZ());
            if(!contains(discovered, nextPos) && (level.getBlockState(nextPos).getBlock() instanceof Cable || level.getBlockState(nextPos).getBlock() instanceof PRYRadar)){
                stack.push(nextPos);
                discovered.add(nextPos);
                continue;
            }

            //Try negative z next
            nextPos = new BlockPos(currentPos.getX(), currentPos.getY(), currentPos.getZ()-1);
            if(!contains(discovered, nextPos) && (level.getBlockState(nextPos).getBlock() instanceof Cable || level.getBlockState(nextPos).getBlock() instanceof PRYRadar)){
                stack.push(nextPos);
                discovered.add(nextPos);
                continue;
            }

            //No positions added to stack, pop the top one
            stack.pop();
        }
        return null;
    }

    private boolean contains(LinkedList<BlockPos> ll, BlockPos pos){
        for(BlockPos position : ll){
            if(pos.equals(position)){
                return true;
            }
        }
        return false;
    }
}
