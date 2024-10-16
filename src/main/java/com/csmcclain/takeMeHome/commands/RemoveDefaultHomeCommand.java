package com.csmcclain.takeMeHome.commands;

import com.csmcclain.takeMeHome.datastorage.PlayerStore;
import com.csmcclain.takeMeHome.datastorage.UserHomeStore;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class RemoveDefaultHomeCommand extends BaseCommand {

    public RemoveDefaultHomeCommand(ComponentLogger logger, UserHomeStore userHomeStore) {
        super(logger, userHomeStore);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            return false;
        }

        PlayerStore playerStore = userHomeStore.getPlayerStore(player.getUniqueId());
        String playerHome = playerStore.getDefaultHomeName();

        Component finalMessage;

        if (playerHome == null || playerHome.isEmpty()) {
            finalMessage = Component.text("You do not have a default home set", NamedTextColor.RED);
        } else {
            playerStore.setDefaultHomeName("");
            finalMessage = Component.text("Successfully removed default home", NamedTextColor.GRAY);
        }

        player.sendMessage(finalMessage);

        return true;
    }
}
