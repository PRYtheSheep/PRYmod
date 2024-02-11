package net.mod.prymod.ModBlock;

import net.minecraft.util.StringRepresentable;

public enum TestMultiBlockPartIndex implements StringRepresentable {
    UP,
    LOW;

    public String toString() {
        return this.getSerializedName();
    }

    public String getSerializedName() {
        return this == UP ? "up" : "low";
    }
}
