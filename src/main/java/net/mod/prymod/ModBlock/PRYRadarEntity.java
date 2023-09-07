package net.mod.prymod.ModBlock;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ambient.Bat;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.network.PacketDistributor;
import net.mod.prymod.Renderer.RGB;
import net.mod.prymod.itemMod.networking.ModMessages;
import net.mod.prymod.itemMod.networking.packets.PRYRadarEntityS2C;
import net.mod.prymod.utils.DFSutils;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Predicate;

public class PRYRadarEntity extends BlockEntity {
    public PRYRadarEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntityInit.PRYRADARENTITY.get(), pos, state);
    }

    public static final String ENERGY_TAG = "energy_tag";

    public static final int CAPACITY = 10000;
    public static final int MAXRECEIVE = 1000;
    public static final int MAXTRANSFER = 1;
    public static final int POWER_DRAW = 1;

    private final EnergyStorage energy = createEnergyStorage();
    private final LazyOptional<IEnergyStorage> energyHandler = LazyOptional.of(() -> new AdaptedEnergyStorage(energy) {
        @Override
        public int receiveEnergy(int maxReceive, boolean simulate) {
            setChanged();
            return super.receiveEnergy(maxReceive, simulate);
        }

        @Override
        public int extractEnergy(int maxExtract, boolean simulate) {return 0;}

        @Override
        public boolean canExtract() {return false;}

        @Override
        public boolean canReceive() {
            return true;
        }
    });

    public LivingEntity radarTarget = null;
    public String entityName = null;
    public BlockPos entityPos = null;
    public Boolean trackedByLauncher = false;
    int progress = -1;

    public static LinkedHashMap<LivingEntity, RGB> livingEntityRGBHashMap = new LinkedHashMap<>();
    private final DFSutils utils = new DFSutils() {
        @Override
        public BlockPos getStartPos(BlockEntity blockEntity) {
            BlockPos currentPos = blockEntity.getBlockPos();
            switch(blockEntity.getBlockState().getValue(PRYBlock.FACING)){
                case NORTH -> {
                    return new BlockPos(currentPos.getX()+1, currentPos.getY(), currentPos.getZ());
                }
                case EAST -> {
                    return new BlockPos(currentPos.getX(), currentPos.getY(), currentPos.getZ()+1);
                }
                case SOUTH -> {
                    return new BlockPos(currentPos.getX()-1, currentPos.getY(), currentPos.getZ());
                }
                case WEST -> {
                    return new BlockPos(currentPos.getX(), currentPos.getY(), currentPos.getZ()-1);
                }
            }
            return null;
        }
    };

    public void tick(){
        //Increments progress counter each tick
        progress++;

        //Sends a packet to update data on client side every 40 ticks, packet is sent from server to client
        if(progress % 40 == 0) {
            if(radarTarget != null && radarTarget.getHealth() != 0){
                entityName = radarTarget.getDisplayName().getString();
                entityPos = radarTarget.getOnPos();
                //Checks if the target's RGB value in hashmap is cyan(1, 1, 0)
                //If cyan, sets trackedByLauncher to true, else set to false
                RGB rgb = livingEntityRGBHashMap.get(radarTarget);
                if(rgb.equals(new RGB(0, 1, 1))) trackedByLauncher = true;
                else trackedByLauncher = false;
            }
            else{
                //Name if target is null
                entityName = "null";
                //Block pos if the target is null
                entityPos = new BlockPos(0, -400, 0);

                trackedByLauncher = false;
            }
            System.out.println("trackedByLauncher is " + trackedByLauncher);
            ModMessages.INSTANCE.send(PacketDistributor.ALL.noArg(), new PRYRadarEntityS2C(this.getBlockPos(), entityPos, entityName, trackedByLauncher));
        }

        //Returns if on client side, all game logic is to run on server side only
        if(this.level.isClientSide) return;

        //TEST
        if(radarTarget != null){
            //System.out.println(radarTarget.getDisplayName().getString());
        }

        Predicate<Entity> predicate = (i) -> (i instanceof Player);
        Player player1 = this.level.getNearestPlayer(this.getBlockPos().getX(), this.getBlockPos().getY(), this.getBlockPos().getZ(), 2, predicate);
        if(player1 != null){
            player1.displayClientMessage(Component.literal(" energy " + energy.getEnergyStored()), true);
        }
        //TEST

        //Removes energy from storage each tick
        energy.extractEnergy(POWER_DRAW, false);

        //Checks if this block is connected to generator, returns if not connected
        //Sets all data to null and removes currently tracked target from the hashmap
        if(utils.isConnectedToBlockEntity(this, PRYGeneratorEntity.class) == null){
            livingEntityRGBHashMap.remove(radarTarget);
            radarTarget = null;
            entityPos = null;
            entityName = null;
            trackedByLauncher = false;
            return;
        }

        //Checks if this block is connected to a launcher(PRYBlock)
        //If not connected, changes the colour of tracking box to yellow
        if(utils.isConnectedToBlockEntity(this, PRYBlockEntity.class) == null && radarTarget != null){
            livingEntityRGBHashMap.put(radarTarget, new RGB(1, 1, 0));
        }

        //Checks if the currently tracked target is dead
        //If dead, it is removed from the hashmap and all data is set to null
        if(radarTarget != null && radarTarget.getHealth() == 0) {
            livingEntityRGBHashMap.remove(radarTarget);
            radarTarget = null;
            entityPos = null;
            entityName = null;
            trackedByLauncher = false;
        }

        //Checks if the closest player is within 128 blocks
        //If distance exceeds 128 blocks, currently tracked target is removed from hashmap and all data set to null
        if(Minecraft.getInstance().player != null && radarTarget != null){
            if(radarTarget.distanceTo(Minecraft.getInstance().player) > 128) {
                livingEntityRGBHashMap.remove(radarTarget);
                radarTarget = null;
                entityPos = null;
                entityName = null;
                trackedByLauncher = false;
            }
        }

        //If currently tracked target is not on hashmap, put it into the hashmap with colour of tracking box as yellow
        if(radarTarget != null && !livingEntityRGBHashMap.containsKey(radarTarget)){
            livingEntityRGBHashMap.put(radarTarget, new RGB(1, 1, 0));
        }

        //Checks if remaining energy is less than energy draw per tick
        //If is less, returns
        if(energy.getEnergyStored() < POWER_DRAW) {
            return;
        }

        AABB startEndBox = null;
        switch(this.getBlockState().getValue(PRYRadar.FACING)){
            case NORTH -> {
                startEndBox = new AABB(
                        this.getBlockPos().getX()-90,
                        this.getBlockPos().getY(),
                        this.getBlockPos().getZ()+1,
                        this.getBlockPos().getX()+90,
                        this.getBlockPos().getY()+90,
                        this.getBlockPos().getZ()+90);
            }
            case EAST -> {
                startEndBox = new AABB(
                        this.getBlockPos().getX()-1,
                        this.getBlockPos().getY(),
                        this.getBlockPos().getZ()-90,
                        this.getBlockPos().getX()-90,
                        this.getBlockPos().getY()+90,
                        this.getBlockPos().getZ()+90);
            }
            case SOUTH -> {
                startEndBox = new AABB(
                        this.getBlockPos().getX()-90,
                        this.getBlockPos().getY(),
                        this.getBlockPos().getZ()-1,
                        this.getBlockPos().getX()+90,
                        this.getBlockPos().getY()+90,
                        this.getBlockPos().getZ()-90);
            }
            case WEST -> {
                startEndBox = new AABB(
                        this.getBlockPos().getX()+1,
                        this.getBlockPos().getY(),
                        this.getBlockPos().getZ()-90,
                        this.getBlockPos().getX()+90,
                        this.getBlockPos().getY()+90,
                        this.getBlockPos().getZ()+90);
            }
        }
        if(radarTarget == null){
            List<Entity> list1 = this.level.getEntities(null, startEndBox);
            for(Entity entity : list1){
                if(entity instanceof Bat && ((Bat) entity).getHealth() != 0){
                    radarTarget = (LivingEntity) entity;
                    livingEntityRGBHashMap.put(radarTarget, new RGB(1, 1, 0));
                    break;
                }
            }
        }
    }

    public int getStoredPower() {
        return energy.getEnergyStored();
    }

    @Override
    protected void saveAdditional(CompoundTag nbt) {
        super.saveAdditional(nbt);
        nbt.put(ENERGY_TAG, energy.serializeNBT());
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        if(nbt.contains(ENERGY_TAG)) energy.deserializeNBT(nbt.get(ENERGY_TAG));
    }

    @NotNull
    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if(cap == ForgeCapabilities.ENERGY){
            return energyHandler.cast();
        }
        else {return super.getCapability(cap, side);}
    }

    @Nonnull
    private EnergyStorage createEnergyStorage() {
        return new EnergyStorage(CAPACITY, MAXRECEIVE, MAXTRANSFER);
    }

}
