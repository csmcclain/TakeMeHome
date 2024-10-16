package com.csmcclain.takeMeHome.datastorage;

public class PlayerHome {
    private final double x;
    private final double y;
    private final double z;
    private final String worldNameHome;

    public PlayerHome(double x, double y, double z, String worldNameHome) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.worldNameHome = worldNameHome;
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

    public String getWorldNameHome() {
        return worldNameHome;
    }
}
