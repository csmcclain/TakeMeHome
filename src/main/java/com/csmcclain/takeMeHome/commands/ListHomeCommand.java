package com.csmcclain.takeMeHome.commands;

import com.csmcclain.takeMeHome.datastorage.UserHomeStore;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class ListHomeCommand extends BaseCommand {


    public ListHomeCommand(ComponentLogger logger, UserHomeStore userHomeStore) {
        super(logger, userHomeStore);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            return false;
        }

        Set<String> houseNames = userHomeStore.getPlayerStore(player.getUniqueId()).getHomes();

        if (houseNames.isEmpty()) {
            player.sendMessage(Component.text("You do not have any set homes", NamedTextColor.GRAY));
        } else {
            player.sendMessage(
                    Component.text("You have set the following homes: " + houseNames, NamedTextColor.GRAY)
            );
        }

        return true;
    }
}
