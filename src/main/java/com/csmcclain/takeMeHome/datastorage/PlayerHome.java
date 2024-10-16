package com.csmcclain.takeMeHome.datastorage;

public class PlayerHome {

    private final String worldName;
    private final double x;
    private final double y;
    private final double z;


    public PlayerHome(String worldName, double x, double y, double z) {
        this.worldName = worldName;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public String getWorldName() {
        return worldName;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }
}
