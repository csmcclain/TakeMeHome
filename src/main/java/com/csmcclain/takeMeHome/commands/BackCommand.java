package com.csmcclain.takeMeHome.commands;

import com.csmcclain.takeMeHome.datastorage.PlayerHome;
import com.csmcclain.takeMeHome.datastorage.UserHomeStore;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class BackCommand extends BaseCommand {

    public BackCommand(ComponentLogger logger, UserHomeStore userHomeStore) {
        super(logger, userHomeStore);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player) || args.length != 0) {
            return false;
        }

        PlayerHome back = userHomeStore.getPlayerStore(player.getUniqueId()).getPreviousLocation();

        if (back == null) {
            player.sendMessage(Component.text("You have not set a home/back yet", NamedTextColor.RED));
        } else {
            player.teleportAsync(back.toLocation()).thenAccept(success ->
                player.sendMessage(Component.text("You've been teleported back", NamedTextColor.GRAY))
            );
        }

        return true;
    }
}
