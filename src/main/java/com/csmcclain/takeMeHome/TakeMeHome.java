package com.csmcclain.takeMeHome;

import com.csmcclain.takeMeHome.commands.BackCommand;
import com.csmcclain.takeMeHome.commands.EmptyTabCompleter;
import com.csmcclain.takeMeHome.commands.HomeCommand;
import com.csmcclain.takeMeHome.commands.SetHomeCommand;
import com.csmcclain.takeMeHome.datastorage.*;
import com.google.gson.Gson;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public final class TakeMeHome extends JavaPlugin {

    private UserHomeStore userHomeStore;
    private File dataFile;
    private ComponentLogger logger;


    @Override
    public void onEnable() {

        // Plugin startup logic
        logger = getComponentLogger();

        // Check if Data Folder exists
        File dataFolder = this.getDataFolder();
        if (!dataFolder.exists()) {
            // Data Folder doesn't exist, create it
            logger.debug("Creating data folder {}", dataFolder);
            if (dataFolder.mkdir()) {
                logger.debug("Created data folder {}", dataFolder);
            }
        }

        // Check if Home json file exists
        dataFile = new File(dataFolder + File.separator + "home.json");
        if (!dataFile.exists()) {
            // Data File doesn't exist, create it
            logger.debug("Creating data file {}", dataFile);
            try {
                if (dataFile.createNewFile()) {
                    logger.debug("Created data file {}", dataFile);
                }
            } catch (IOException e) {
                logger.error("Error creating {}", dataFile);
                throw new RuntimeException(e);
            }
        }

        // Data file exists, Parse it
        try {
            String storageFileJson = Files.readString(dataFile.toPath());
            logger.info("Loading user data");
            userHomeStore = new Gson().fromJson(storageFileJson, UserHomeStore.class);
            if (userHomeStore.getVersion() == null) {
                logger.info("Migrating user data from UserLocationStore to UserHomeStore");
                userHomeStore = migrateFromUserLocationStore(dataFile);
                logger.info("Migration complete");
            }
            logger.info("Loaded user data into UserHomeStore");

            logger.info("Registering SetHomeCommand");
            PluginCommand setHome = Objects.requireNonNull(this.getCommand("sethome"));
            setHome.setExecutor(new SetHomeCommand(logger, userHomeStore));
            setHome.setTabCompleter(new EmptyTabCompleter());
            logger.info("SetHomeCommand Registered");

//            logger.info("Registering HomeCommand");
//            Objects.requireNonNull(this.getCommand("home")).setExecutor(new HomeCommand(logger, userLocationStore));
//            logger.info("HomeCommand Registered");
//            logger.info("Registering BackCmd");
//            Objects.requireNonNull(this.getCommand("back")).setExecutor(new BackCommand(logger, userLocationStore));
//            logger.info("BackCommand Registered");
        } catch (IOException e) {
            logger.error("Error registering Commands/LocationStore");
            throw new RuntimeException(e);
        }

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        try {
            logger.info("Saving player home data");
            String json = new Gson().toJson(userHomeStore);
            Files.write(dataFile.toPath(), json.getBytes());
            logger.info("Player home data saved");
        } catch (IOException e) {
            logger.info("Error saving player home data");
            throw new RuntimeException(e);
        }
    }

    private UserHomeStore migrateFromUserLocationStore(File dataFile) {
        Map<UUID, PlayerStore> newSave = new HashMap<>();
        String version = "1.0";
        try {
            Map<UUID, StoredLocation> previousSave = new UserLocationStore(dataFile).getLocationStore();

            for (UUID uuid : previousSave.keySet()) {
                StoredLocation storedLocation = previousSave.get(uuid);
                newSave.put(uuid, new PlayerStore(
                        Map.ofEntries(Map.entry("home",
                                new PlayerHome(
                                        storedLocation.getxHome(),
                                        storedLocation.getyHome(),
                                        storedLocation.getzHome(),
                                        storedLocation.getWorldNameHome()
                                )
                        )),
                        new PlayerHome(
                                storedLocation.getxBack(),
                                storedLocation.getyBack(),
                                storedLocation.getzBack(),
                                storedLocation.getWorldNameBack()
                        ),
                        "home"
                ));
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return new UserHomeStore(version, newSave);
    }
}
