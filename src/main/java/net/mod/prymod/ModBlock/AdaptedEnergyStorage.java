package net.mod.prymod.ModBlock;

import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.energy.IEnergyStorage;

public class AdaptedEnergyStorage implements IEnergyStorage {

    public IEnergyStorage iEnergyStorage;

    public AdaptedEnergyStorage(EnergyStorage energyStorage){this.iEnergyStorage = energyStorage;}
    @Override
    public int receiveEnergy(int maxReceive, boolean simulate) {
        return 0;
    }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate) {
        return 0;
    }

    @Override
    public int getEnergyStored() {
        return 0;
    }

    @Override
    public int getMaxEnergyStored() {
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
}
