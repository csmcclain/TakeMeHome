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

import java.util.Set;

@SuppressWarnings("ExtractMethodRecommender")
public class ListHomeCommand extends BaseCommand {


    public ListHomeCommand(ComponentLogger logger, UserHomeStore userHomeStore) {
        super(logger, userHomeStore);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            return false;
        }

        PlayerStore playerStore = userHomeStore.getPlayerStore(player.getUniqueId());
        Set<String> houseNames = playerStore.getHomes();

        if (houseNames.isEmpty()) {
            player.sendMessage(Component.text("You do not have any set homes", NamedTextColor.GRAY));
        } else {
            Component finalMessage = Component.text(
                    "You have set the following homes: ", NamedTextColor.GRAY
            );

            String defaultHouseName = playerStore.getDefaultHomeName();

            for (String houseName : houseNames) {
                if (!playerStore.getHome(houseName).isVisible()) {
                    continue;
                }

                finalMessage = finalMessage.appendNewline().append(
                        Component.text("- " + houseName, NamedTextColor.GRAY)
                );

                if (houseName.equals(defaultHouseName)) {
                    finalMessage = finalMessage.append(
                            Component.text(" [default]", NamedTextColor.GRAY)
                    );
                }
            }

            player.sendMessage(finalMessage);
        }

        return true;
    }
}
