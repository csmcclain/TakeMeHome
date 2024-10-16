package com.csmcclain.takeMeHome;

import com.csmcclain.takeMeHome.commands.BackCommand;
import com.csmcclain.takeMeHome.commands.HomeCommand;
import com.csmcclain.takeMeHome.datastorage.UserLocationStore;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public final class TakeMeHome extends JavaPlugin {

    private UserLocationStore userLocationStore;


    @Override
    public void onEnable() {

        // Plugin startup logic
        ComponentLogger logger = getComponentLogger();

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
        File dataFile = new File(dataFolder + File.separator + "home.json");
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

        // Data file exists, Register Commands
        try {
            logger.info("Setting up User Location Store");
            userLocationStore = new UserLocationStore(dataFile);
            logger.info("Set up UserLocationStore");
            logger.info("Registering HomeCommand");
            Objects.requireNonNull(this.getCommand("home")).setExecutor(new HomeCommand(logger, userLocationStore));
            logger.info("HomeCommand Registered");
            logger.info("Registering BackCmd");
            Objects.requireNonNull(this.getCommand("back")).setExecutor(new BackCommand(logger, userLocationStore));
            logger.info("BackCommand Registered");
        } catch (IOException e) {
            logger.error("Error registering Commands/LocationStore");
            throw new RuntimeException(e);
        }

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        try {
            userLocationStore.saveUserStore();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
