package com.csmcclain.takeMeHome.commands;

import com.csmcclain.takeMeHome.datastorage.PlayerHome;
import com.csmcclain.takeMeHome.datastorage.PlayerStore;
import com.csmcclain.takeMeHome.datastorage.UserHomeStore;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class HomeCommand extends BaseCommand {

    public HomeCommand(ComponentLogger logger, UserHomeStore userHomeStore) {
        super(logger, userHomeStore);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player) || args.length > 1) {
            return false;
        }

        PlayerStore playerstore = userHomeStore.getPlayerStore(player.getUniqueId());
        String locationName;
        String errorText;
        PlayerHome locationToTeleportTo;

        if (args.length == 0) {
            locationName = playerstore.getDefaultHomeName();
            errorText = "No default home set.\nPlease set one using /setdefaulthome.";
        } else {
            locationName = args[0];
            errorText = "No home with name " + args[0] + " found.";
        }

        locationToTeleportTo = playerstore.getHome(locationName);
        PlayerHome currentLocation = new PlayerHome(
                player.getWorld().getName(),
                player.getX(),
                player.getY(),
                player.getZ()
        );

        if (locationToTeleportTo == null) {
            player.sendMessage(Component.text(errorText, NamedTextColor.RED));
        } else {
            player.teleportAsync(
                    locationToTeleportTo.toLocation()
            ).thenAccept(success -> {
                if (success) {
                    playerstore.setPreviousLocation(currentLocation);
                    player.sendMessage(
                            Component.text("You have been teleported to: " + locationName, NamedTextColor.GRAY)
                    );
                }
            });
        }

        return true;
    }
}
