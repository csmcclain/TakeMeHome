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

public class RemoveHomeCommand extends BaseCommand {

    public RemoveHomeCommand(ComponentLogger logger, UserHomeStore userHomeStore) {
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
                    Component.text("You do not have a home set with name: " + homeName, NamedTextColor.RED)
            );
            return true;
        }

        playerStore.removeHome(homeName);

        Component finalMessage = Component.text(
                "Successfully removed home with name: " + homeName, NamedTextColor.GRAY
        );

        if (homeName.equals(playerStore.getDefaultHomeName())) {
            playerStore.setDefaultHomeName("");
            finalMessage = finalMessage.appendNewline().append(
                    Component.text("You default has been reset", NamedTextColor.GRAY)
            );
        }

        player.sendMessage(finalMessage);

        return true;
    }
}
