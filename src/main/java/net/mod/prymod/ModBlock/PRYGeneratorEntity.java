package net.mod.prymod.ModBlock;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;
import net.mod.prymod.utils.DFSutils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import java.util.function.Predicate;

public class PRYGeneratorEntity extends BlockEntity {
    public PRYGeneratorEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntityInit.PRYGENERATORENTITY.get(), pos, state);
    }

    public static final String INPUT_ITEMS_TAG = "input_items_tag";
    public static final String ENERGY_TAG = "energy_tag";
    public static final String BURNTIMER_TAG = "burntimer_tag";

    public static final int GENERATE = 50;
    public static final int MAXTRANSFER = 1000;
    public static final int CAPACITY = 10000;

    public static final int INPUT_SLOT = 0;
    public static final int FLUID_INPUT_SLOT = 1;
    public static final int INPUT_SLOT_COUNT = 2;
    public static final int SLOT_COUNT = 2;
    public int burnTime = 0;

    private PRYBlockEntity pryBlockEntity = null;
    private PRYRadarEntity pryRadarEntity = null;

    public final ItemStackHandler inputItems = createItemHandler(INPUT_SLOT_COUNT);
    private final LazyOptional<IItemHandler> itemHandler = LazyOptional.of(() -> new CombinedInvWrapper(inputItems));
    private final LazyOptional<IItemHandler> inputItemHandler = LazyOptional.of(() -> new AdaptedItemHandler(inputItems) {
        @Override
        public @NotNull ItemStack extractItem(int slot, int amount, boolean simulate) {return ItemStack.EMPTY;}

        @Override
        public boolean isItemValid(int slot, @NotNull ItemStack stack) {
            return switch(slot){
                case 0 -> stack.equals(new ItemStack(Items.COAL_BLOCK), false);
                case 1 -> stack.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM).isPresent();
                default -> super.isItemValid(slot, stack);
            };
        }
    });
    private final EnergyStorage energy = createEnergyStorage();
    private final LazyOptional<IEnergyStorage> energyHandler = LazyOptional.of(() -> new AdaptedEnergyStorage(energy) {
        @Override
        public int receiveEnergy(int maxReceive, boolean simulate) {return 0;}

        @Override
        public int extractEnergy(int maxExtract, boolean simulate) {return 0;}

        @Override
        public boolean canExtract() {return false;}

        @Override
        public boolean canReceive() {
            return false;
        }
    });

    private final FluidTank FLUID_TANK = new FluidTank(1000){
        @Override
        protected void onContentsChanged() {
            setChanged();
            //Sync to client later stuff
        }

        @Override
        public boolean isFluidValid(FluidStack stack) {
            return stack.getFluid() == Fluids.WATER;
        }
    };

    private LazyOptional<IFluidHandler> lazyFluidHandler = LazyOptional.empty();

    public void setFluid(FluidStack stack){this.FLUID_TANK.setFluid(stack);}

    public FluidStack getFluid(){return this.FLUID_TANK.getFluid();}

    private final DFSutils utils = new DFSutils() {
        @Override
        public BlockPos getStartPos(BlockEntity blockEntity) {
            BlockPos currentPos = blockEntity.getBlockPos();
            switch(blockEntity.getBlockState().getValue(PRYBlock.FACING)){
                case NORTH -> {
                    return new BlockPos(currentPos.getX()-1, currentPos.getY(), currentPos.getZ());
                }
                case EAST -> {
                    return new BlockPos(currentPos.getX(), currentPos.getY(), currentPos.getZ()-1);
                }
                case SOUTH -> {
                    return new BlockPos(currentPos.getX()+1, currentPos.getY(), currentPos.getZ()+1);
                }
                case WEST -> {
                    return new BlockPos(currentPos.getX(), currentPos.getY(), currentPos.getZ()+1);
                }
            }
            return null;
        }
    };

    public void tick(){
        if(this.level.isClientSide) {return;}
        Predicate<Entity> predicate = (i) -> (i instanceof Player);
        Player player = this.level.getNearestPlayer(this.getBlockPos().getX(), this.getBlockPos().getY(), this.getBlockPos().getZ(), 2, predicate);
        if(player != null){
            player.displayClientMessage(Component.literal("generator burntime " + burnTime + " energy " + energy.getEnergyStored() + " fluid " + FLUID_TANK.getFluidAmount()), true);
        }

        BlockPos launcherPos = utils.isConnectedToBlockEntity(this, PRYBlockEntity.class);
        if(launcherPos != null) pryBlockEntity = (PRYBlockEntity) this.level.getBlockEntity(launcherPos);
        else pryBlockEntity = null;
        BlockPos radarPos = utils.isConnectedToBlockEntity(this, PRYRadarEntity.class);
        if(radarPos != null) pryRadarEntity = (PRYRadarEntity) this.level.getBlockEntity(radarPos);
        else pryRadarEntity = null;

        if(hasFluidItemInSlot()){
            transferFluidFromItemIntoTank();
        }
        generateEnergy();
        distributeEnergy(pryBlockEntity);
        distributeEnergy(pryRadarEntity);
    }

    private void transferFluidFromItemIntoTank() {
        this.inputItems.getStackInSlot(FLUID_INPUT_SLOT).getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM).ifPresent(handler ->{
            int drainAmount = Math.min(this.FLUID_TANK.getSpace(), 1000);
            FluidStack stack = handler.drain(drainAmount, IFluidHandler.FluidAction.SIMULATE);
            if(this.FLUID_TANK.isFluidValid(stack)){
                //fluid in tank is valid (water)
                stack = handler.drain(drainAmount, IFluidHandler.FluidAction.EXECUTE);
                fillTankWithFluid(stack, handler.getContainer());
            }
        });
    }

    private void fillTankWithFluid(FluidStack stack, ItemStack container) {
        this.FLUID_TANK.fill(stack, IFluidHandler.FluidAction.EXECUTE);
        this.inputItems.extractItem(FLUID_INPUT_SLOT, 1, false);
        this.inputItems.insertItem(FLUID_INPUT_SLOT, container, false);
    }

    private boolean hasFluidItemInSlot() {
        ItemStack itemStack = this.inputItems.getStackInSlot(FLUID_INPUT_SLOT);
        return itemStack.getCount() > 0;
    }

    public ItemStackHandler getItems() {
        return inputItems;
    }

    public int getStoredPower() {
        return energy.getEnergyStored();
    }

    @Override
    protected void saveAdditional(CompoundTag nbt) {
        super.saveAdditional(nbt);
        nbt.put(INPUT_ITEMS_TAG, inputItems.serializeNBT());
        nbt.put(ENERGY_TAG, energy.serializeNBT());
        nbt.putInt(BURNTIMER_TAG, burnTime);
        nbt = FLUID_TANK.writeToNBT(nbt);
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        if (nbt.contains(INPUT_ITEMS_TAG)) {
            inputItems.deserializeNBT(nbt.getCompound(INPUT_ITEMS_TAG));
        }
        if (nbt.contains(ENERGY_TAG)) {
            energy.deserializeNBT(nbt.get(ENERGY_TAG));
        }
        this.burnTime = nbt.getInt(BURNTIMER_TAG);
        FLUID_TANK.readFromNBT(nbt);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        lazyFluidHandler = LazyOptional.of(() -> FLUID_TANK);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        lazyFluidHandler.invalidate();
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

    @Nonnull
    private EnergyStorage createEnergyStorage() {
        return new EnergyStorage(CAPACITY, MAXTRANSFER, MAXTRANSFER);
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.ITEM_HANDLER) {
            if (side == null) {
                return itemHandler.cast();
            } else {
                return inputItemHandler.cast();
            }
        } else if (cap == ForgeCapabilities.ENERGY) {
            return energyHandler.cast();
        } else if (cap == ForgeCapabilities.FLUID_HANDLER) {
            return lazyFluidHandler.cast();
        } else {
            return super.getCapability(cap, side);
        }
        
    }

    public void generateEnergy(){
        if(energy.getEnergyStored() >= energy.getMaxEnergyStored()) return;

        if(burnTime > 0){
            int difference = energy.getMaxEnergyStored() - energy.getEnergyStored();
            if(difference > GENERATE) energy.receiveEnergy(GENERATE, false);
            else energy.receiveEnergy(difference, false);
            burnTime -= 1000;
            if(burnTime < 0) burnTime = 0;
        }

        ItemStack itemStack = inputItems.getStackInSlot(INPUT_SLOT);
        if(!itemStack.isEmpty() && itemStack.is(Items.COAL_BLOCK)){
            int additionalBurnTime = ForgeHooks.getBurnTime(itemStack, RecipeType.SMELTING);
            burnTime += additionalBurnTime;
            inputItems.extractItem(INPUT_SLOT, 1, false);
        }
        setChanged();
    }

    public void distributeEnergy(BlockEntity blockEntity){
        if(energy.getEnergyStored() <= 0 || blockEntity == null) return;

        blockEntity.getCapability(ForgeCapabilities.ENERGY).map(e -> {
            if(e.canReceive()){
                int received = e.receiveEnergy(Math.min(energy.getEnergyStored(), MAXTRANSFER), false);
                energy.extractEnergy(received, false);
                setChanged();
                return received;
            }
            return 0;
        });
    }
}
