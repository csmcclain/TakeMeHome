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

public class UpdateHomeCommand extends BaseCommand {

    public UpdateHomeCommand(ComponentLogger logger, UserHomeStore userHomeStore) {
        super(logger, userHomeStore);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player) || args.length != 1) {
            return false;
        }

        String homeName = args[0];
        PlayerStore playerStore = userHomeStore.getPlayerStore(player.getUniqueId());
        PlayerHome playerHome = playerStore.getHome(homeName);
        if (playerHome == null) {
            player.sendMessage(
                    Component.text("You do not have a saved home named: " + homeName, NamedTextColor.RED)
            );
        } else {
            playerStore.saveHome(homeName, new PlayerHome(
                    player.getWorld().getName(), player.getX(), player.getY(), player.getZ()
            ));
            player.sendMessage(
                    Component.text("Successfully updated: " + homeName, NamedTextColor.GRAY)
            );
        }

        return true;
    }
}
