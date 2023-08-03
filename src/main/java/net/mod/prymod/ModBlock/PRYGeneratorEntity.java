package net.mod.prymod.ModBlock;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;

public class PRYGeneratorEntity extends BlockEntity {
    public PRYGeneratorEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntityInit.PRYGENERATORENTITY.get(), pos, state);
    }

    public static final String INPUT_ITEMS_TAG = "input_items_tag";
    public static final String ENERGY_TAG = "energy_tag";
    public static final String BURNTIMER_TAG = "burntimer_tag"

    public static final int GENERATE = 50;
    public static final int MAXTRANSFER = 1000;
    public static final int CAPACITY = 10000;

    public static final int INPUT_SLOT = 0;
    public static final int INPUT_SLOT_COUNT = 1;
    public static final int SLOT_COUNT = 0;
    public int burnTime = 0;

    public final ItemStackHandler inputItems = createItemHandler(INPUT_SLOT_COUNT);
    private final LazyOptional<IItemHandler> itemHandler = LazyOptional.of(() -> new CombinedInvWrapper(inputItems));
    private final LazyOptional<IItemHandler> inputItemHandler = LazyOptional.of(() -> new AdaptedItemHandler(inputItems) {
        @Override
        public @NotNull ItemStack extractItem(int slot, int amount, boolean simulate) {
            return ItemStack.EMPTY;
        }
    });
    private final EnergyStorage energy = createEnergyStorage();
    private final LazyOptional<IEnergyStorage> energyHandler = LazyOptional.of(() -> new AdaptedEnergyStorage(energy) {
        @Override
        public int receiveEnergy(int maxReceive, boolean simulate) {
            return 0;
        }

        @Override
        public int extractEnergy(int maxExtract, boolean simulate) {
            return 0;
        }

        @Override
        public boolean canExtract() {
            return false;
        }

        @Override
        public boolean canReceive() {
            return false;
        }
    });

    public void tick(){
        if(this.level.isClientSide) return;

        generateEnergy(); //check input item, if is fuel, increase the burn timer. If burn timer > 0 and not at capacity, make energy
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
            return itemHandler.cast();
        } else if (cap == ForgeCapabilities.ENERGY) {
            return energyHandler.cast();
        } else {
            return super.getCapability(cap, side);
        }
    }

    public void generateEnergy(){
        if(energy.getEnergyStored() >= energy.getMaxEnergyStored()) return;

        setBurnTime();
    }

    public void setBurnTime(){
        //function to set the burn timer;
        ItemStack item = inputItems.getStackInSlot(INPUT_SLOT);
        if(item.is(Items.COAL)){
            
        }
    }
}
