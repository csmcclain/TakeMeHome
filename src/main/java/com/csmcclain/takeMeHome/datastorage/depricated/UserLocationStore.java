package com.csmcclain.takeMeHome.datastorage.depricated;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class UserLocationStore {

    private final Map<UUID, StoredLocation> locationStore;

    public UserLocationStore(File homeStorageFile) throws IOException {
        String storageFileJson = Files.readString(homeStorageFile.toPath());
        Type type = new TypeToken<Map<UUID, StoredLocation>>() {}.getType();
        Map<UUID, StoredLocation> store = new Gson().fromJson(storageFileJson, type);
        this.locationStore = store == null ? new HashMap<>() : store;
    }

    public Map<UUID, StoredLocation> getLocationStore() {
        return locationStore;
    }

}
