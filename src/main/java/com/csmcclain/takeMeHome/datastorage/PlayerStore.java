package com.csmcclain.takeMeHome.datastorage;

import java.util.Map;

public class PlayerStore {
    private final Map<String, PlayerHome> savedHomes;
    private PlayerHome previousLocation;
    private String defaultHomeName;

    public PlayerStore(Map<String, PlayerHome> savedHomes, PlayerHome previousLocation, String defaultHomeName) {
        this.savedHomes = savedHomes;
        this.previousLocation = previousLocation;
        this.defaultHomeName = defaultHomeName;
    }

    public PlayerHome getHome(String houseName) {
        return savedHomes.get(houseName);
    }

    public void saveHome(String houseName, PlayerHome home) {
        savedHomes.put(houseName, home);
    }

    public PlayerHome getPreviousLocation() {
        return previousLocation;
    }

    public void setPreviousLocation(PlayerHome previousLocation) {
        this.previousLocation = previousLocation;
    }

    public String getDefaultHomeName() {
        return defaultHomeName;
    }

    public void setDefaultHomeName(String defaultHomeName) {
        this.defaultHomeName = defaultHomeName;
    }
}
