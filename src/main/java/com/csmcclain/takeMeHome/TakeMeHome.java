package com.csmcclain.takeMeHome;

import com.csmcclain.takeMeHome.commands.BackCommand;
import com.csmcclain.takeMeHome.commands.BaseCommand;
import com.csmcclain.takeMeHome.commands.HomeCommand;
import com.csmcclain.takeMeHome.commands.ListHomeCommand;
import com.csmcclain.takeMeHome.commands.RemoveDefaultHomeCommand;
import com.csmcclain.takeMeHome.commands.RemoveHomeCommand;
import com.csmcclain.takeMeHome.commands.SetDefaultHomeCommand;
import com.csmcclain.takeMeHome.commands.SetHomeCommand;
import com.csmcclain.takeMeHome.commands.UpdateHomeCommand;
import com.csmcclain.takeMeHome.datastorage.PlayerHome;
import com.csmcclain.takeMeHome.datastorage.PlayerStore;
import com.csmcclain.takeMeHome.datastorage.depricated.StoredLocation;
import com.csmcclain.takeMeHome.datastorage.UserHomeStore;
import com.csmcclain.takeMeHome.datastorage.depricated.UserLocationStore;
import com.csmcclain.takeMeHome.tabcompleters.EmptyTabCompleter;
import com.csmcclain.takeMeHome.tabcompleters.PlayerHomesTabCompleter;
import com.google.gson.Gson;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabCompleter;
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
        } catch (IOException e) {
            logger.error("Error Parsing data from datafile");
            throw new RuntimeException(e);
        }

        // Data file parsed, register commands
        logger.info("Registering commands");
        registerPlugin("home",
                new HomeCommand(logger, userHomeStore),
                new PlayerHomesTabCompleter(userHomeStore)
        );
        registerPlugin("sethome", new SetHomeCommand(logger, userHomeStore), new EmptyTabCompleter());
        registerPlugin("listhome", new ListHomeCommand(logger, userHomeStore), new EmptyTabCompleter());
        registerPlugin(
                "updatehome",
                new UpdateHomeCommand(logger, userHomeStore),
                new PlayerHomesTabCompleter(userHomeStore)
        );
        registerPlugin(
                "removehome",
                new RemoveHomeCommand(logger, userHomeStore),
                new PlayerHomesTabCompleter(userHomeStore)
        );
        registerPlugin(
                "setdefaulthome",
                new SetDefaultHomeCommand(logger, userHomeStore),
                new PlayerHomesTabCompleter(userHomeStore)
        );
        registerPlugin(
                "removedefaulthome",
                new RemoveDefaultHomeCommand(logger, userHomeStore),
                new EmptyTabCompleter()
        );
        registerPlugin("back", new BackCommand(logger, userHomeStore), new EmptyTabCompleter());
        logger.info("Commands registered");
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

    private void registerPlugin(String pluginName, BaseCommand command, TabCompleter completer) {
        String commandClassName = command.getClass().getSimpleName();
        String tabCompleterClassName = completer.getClass().getSimpleName();
        logger.info("Registering Command: {} with TabCompleter: {}", commandClassName, tabCompleterClassName);
        PluginCommand listHome = Objects.requireNonNull(this.getCommand(pluginName));
        listHome.setExecutor(command);
        listHome.setTabCompleter(completer);
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
                                        storedLocation.getWorldNameHome(),
                                        storedLocation.getxHome(),
                                        storedLocation.getyHome(),
                                        storedLocation.getzHome()

                                )
                        )),
                        new PlayerHome(
                                storedLocation.getWorldNameBack(),
                                storedLocation.getxBack(),
                                storedLocation.getyBack(),
                                storedLocation.getzBack()
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
