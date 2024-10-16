package com.csmcclain.takeMeHome.commands;

import com.csmcclain.takeMeHome.datastorage.PlayerHome;
import com.csmcclain.takeMeHome.datastorage.PlayerStore;
import com.csmcclain.takeMeHome.datastorage.UserHomeStore;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class SetHomeCommand extends BaseCommand {

    public SetHomeCommand(ComponentLogger logger, UserHomeStore userHomeStore) {
        super(logger, userHomeStore);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player) || (args.length != 1)) {
            return false;
        }

        PlayerStore playerStore = userHomeStore.getPlayerStore(player.getUniqueId());

        if (playerStore.getHome(args[0]) != null) {
            player.sendMessage(Component.text(
                    "Home with name " + args[0] +  " already exists.\n" +
                    "Either update home using /updatehome or remove home with /removehome",
                    NamedTextColor.RED
            ));
        } else {
            playerStore.saveHome(
                    args[0],
                    new PlayerHome(
                            player.getX(),
                            player.getY(),
                            player.getZ(),
                            player.getWorld().getName()
                    )
            );

            player.sendMessage(Component.text("Successfully set home named: " + args[0], NamedTextColor.GRAY));
        }



        return true;
    }
}
