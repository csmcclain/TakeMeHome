package com.csmcclain.takeMeHome.datastorage;

import org.bukkit.Bukkit;
import org.bukkit.Location;

public class PlayerHome {

    private final String worldName;
    private final double x;
    private final double y;
    private final double z;
    private boolean isVisible = true;


    public PlayerHome(String worldName, double x, double y, double z) {
        this.worldName = worldName;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Location toLocation() {
        return new Location(
                Bukkit.getWorld(worldName),
                x,
                y,
                z
        );
    }

    public boolean isVisible() {
        return this.isVisible;
    }

    public void toggleVisibility() {
        this.isVisible = !this.isVisible;
    }
}
