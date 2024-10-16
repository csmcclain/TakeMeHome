package com.csmcclain.takeMeHome.datastorage;

import java.util.Map;
import java.util.UUID;

public class UserHomeStore {
    private final String version;
    private final Map<UUID, PlayerStore> playerStores;

    public UserHomeStore(String version, Map<UUID, PlayerStore> playerStores) {
        this.version = version;
        this.playerStores = playerStores;
    }

    public String getVersion() {
        return version;
    }

    public PlayerStore getPlayerStore(UUID uuid) {
        return playerStores.get(uuid);
    }

    public void createPlayerStore(UUID uuid, PlayerStore playerStore) {
        playerStores.put(uuid, playerStore);
  ;  }
}
