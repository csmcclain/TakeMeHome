package com.csmcclain.takeMeHome.datastorage;

import java.util.List;
import java.util.Map;
import java.util.Set;

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

    public Set<String> getHomes() {
        return savedHomes.keySet();
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
