package net.mod.prymod.ModBlock;

import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.energy.IEnergyStorage;

public class AdaptedEnergyStorage implements IEnergyStorage {

    public IEnergyStorage iEnergyStorage;

    public AdaptedEnergyStorage(EnergyStorage energyStorage){this.iEnergyStorage = energyStorage;}
    @Override
    public int receiveEnergy(int maxReceive, boolean simulate) {
        return iEnergyStorage.receiveEnergy(maxReceive, simulate);
    }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate) {
        return iEnergyStorage.extractEnergy(maxExtract, simulate);
    }

    @Override
    public int getEnergyStored() {
        return iEnergyStorage.getEnergyStored();
    }

    @Override
    public int getMaxEnergyStored() {
        return iEnergyStorage.getMaxEnergyStored();
    }

    @Override
    public boolean canExtract() {
        return iEnergyStorage.canExtract();
    }

    @Override
    public boolean canReceive() {
        return iEnergyStorage.canReceive();
    }
}
