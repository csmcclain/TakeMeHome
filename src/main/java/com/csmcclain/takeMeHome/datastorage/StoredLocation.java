package com.csmcclain.takeMeHome.datastorage;

import org.bukkit.Bukkit;
import org.bukkit.Location;

@Deprecated
public class StoredLocation {
    private double xHome;
    private double yHome;
    private double zHome;
    private String worldNameHome;

    private double xBack;
    private double yBack;
    private double zBack;
    private String worldNameBack;

    public double getxHome() {
        return xHome;
    }

    public double getyHome() {
        return yHome;
    }

    public double getzHome() {
        return zHome;
    }

    public String getWorldNameHome() {
        return worldNameHome;
    }

    public double getxBack() {
        return xBack;
    }

    public double getyBack() {
        return yBack;
    }

    public double getzBack() {
        return zBack;
    }

    public String getWorldNameBack() {
        return worldNameBack;
    }

    public StoredLocation(Location home, Location back) {
        this.setHome(home);
        this.setBack(back);
    }

    public Location getHome() {
        return new Location(Bukkit.getWorld(worldNameHome), xHome, yHome, zHome);
    }

    public Location getBack() {
        return new Location(Bukkit.getWorld(worldNameBack), xBack, yBack, zBack);
    }

    public void setHome(Location home) {
        this.xHome = home.getX();
        this.yHome = home.getY();
        this.zHome = home.getZ();
        this.worldNameHome = home.getWorld().getName();
    }

    public void setBack(Location back) {
        this.xBack = back.getX();
        this.yBack = back.getY();
        this.zBack = back.getZ();
        this.worldNameBack = back.getWorld().getName();
    }

}
