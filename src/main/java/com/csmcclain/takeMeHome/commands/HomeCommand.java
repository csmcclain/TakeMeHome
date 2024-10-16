package com.csmcclain.takeMeHome.commands;

import com.csmcclain.takeMeHome.datastorage.StoredLocation;
import com.csmcclain.takeMeHome.datastorage.UserLocationStore;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.UUID;

public class HomeCommand implements CommandExecutor {

    private final ComponentLogger logger;
    private final UserLocationStore locationStore;

    public HomeCommand(ComponentLogger logger, UserLocationStore locationStore) throws IOException {
        this.logger = logger;
        this.locationStore = locationStore;
    }


    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            return false;
        }
        UUID uuid = player.getUniqueId();
        StoredLocation storedLocation = locationStore.getUserLocationStore(uuid);

        if (args.length == 0) {
            // TP player to home if exists
            if (storedLocation == null) {
                logger.debug("{} does not have a home/back locations set", uuid);
                player.sendMessage(Component.text("You have not set a home/back yet", NamedTextColor.RED));
                return true;
            }
            storedLocation.setBack(player.getLocation());
            locationStore.setUserLocationStore(uuid, storedLocation);
            player.teleportAsync(storedLocation.getHome()).thenAccept(success -> {
                logger.info("{} was teleported successfully", player.getUniqueId());
                player.sendMessage(Component.text("You've been teleported home", NamedTextColor.GRAY));
            });
        } else if (args.length == 1 && args[0].equals("set")) {
            Location location = player.getLocation();
            if (storedLocation == null) {
                storedLocation = new StoredLocation(location, location);
            }
            storedLocation.setHome(location);
            player.sendMessage(Component.text("You have set home", NamedTextColor.GRAY));
            locationStore.setUserLocationStore(uuid, storedLocation);

        } else {
            return false;
        }

        return true;
    }
}
