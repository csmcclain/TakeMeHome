package com.csmcclain.takeMeHome.datastorage;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Deprecated
public class UserLocationStore {

    private final Map<UUID, StoredLocation> locationStore;
    private final File homeStorageFile;

    public UserLocationStore(File homeStorageFile) throws IOException {
        this.homeStorageFile = homeStorageFile;
        String storageFileJson = Files.readString(homeStorageFile.toPath());
        Type type = new TypeToken<Map<UUID, StoredLocation>>() {}.getType();
        Map<UUID, StoredLocation> store = new Gson().fromJson(storageFileJson, type);
        this.locationStore = store == null ? new HashMap<>() : store;
    }

    public void setUserLocationStore(UUID uuid, StoredLocation store) {
        locationStore.put(uuid, store);
    }

    public StoredLocation getUserLocationStore(UUID uuid) {
        return locationStore.get(uuid);
    }

    public Map<UUID, StoredLocation> getLocationStore() {
        return locationStore;
    }

    public void saveUserStore() throws IOException {
        String json = new Gson().toJson(locationStore);
        Files.write(homeStorageFile.toPath(), json.getBytes());
    }

}
