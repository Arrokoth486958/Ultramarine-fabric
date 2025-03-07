package com.voxelutopia.ultramarine.common.block.state;

import net.minecraft.util.StringRepresentable;

public enum ChiralBlockType implements StringRepresentable {
    LEFT("left", 1),
    RIGHT("right", 0),
    TOP("top", 3),
    BOTTOM("bottom", 2);

    public static final ChiralBlockType[] BY_ID = values();
    private final String name;
    private final int opposite;

    ChiralBlockType(String name, int opposite) {
        this.name = name;
        this.opposite = opposite;
    }

    public String getSerializedName() {
        return this.name;
    }

    public ChiralBlockType getOpposite() {
        return BY_ID[this.opposite];
    }
}
