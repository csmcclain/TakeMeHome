package com.csmcclain.takeMeHome;

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

public class BackCommand implements CommandExecutor {

    private final ComponentLogger logger;
    private final UserLocationStore locationStore;

    public BackCommand(ComponentLogger logger, UserLocationStore locationStore) throws IOException {
        this.logger = logger;
        this.locationStore = locationStore;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            return false;
        }
        UUID uuid = player.getUniqueId();
        Location location = player.getLocation();
        StoredLocation storedLocation = locationStore.getUserLocationStore(uuid);

        if (args.length != 0) {
            logger.error("Bad Number of back logs user {}", uuid);

            return false;
        }

        if (storedLocation == null) {
            player.sendMessage(Component.text("You have not set a home/back yet", NamedTextColor.RED));
        } else {
            player.teleportAsync(storedLocation.getBack()).thenAccept(success -> {
                logger.info("{} was teleported successfully", player.getUniqueId());
                player.sendMessage(Component.text("You've been teleported back", NamedTextColor.GRAY));
            });
        }

        return true;
    }
}
