package com.csmcclain.takeMeHome.tabcompleters;

import com.csmcclain.takeMeHome.datastorage.PlayerStore;
import com.csmcclain.takeMeHome.datastorage.UserHomeStore;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class PlayerHomesTabCompleter implements TabCompleter {

    private final UserHomeStore userHomeStore;

    public PlayerHomesTabCompleter(UserHomeStore userHomeStore) {
        this.userHomeStore = userHomeStore;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            return List.of();
        }

        String currentSelection = args.length == 1 ? args[0] : "";
        List<String> completions = new ArrayList<>();
        PlayerStore playerStore = userHomeStore.getPlayerStore(player.getUniqueId());
        Set<String> homes = playerStore.getHomes();

        StringUtil.copyPartialMatches(currentSelection, homes, completions);

        return completions.stream().filter(home -> playerStore.getHome(home).isVisible()).toList();
    }
}
