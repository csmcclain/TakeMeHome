package com.csmcclain.takeMeHome.commands;

import com.csmcclain.takeMeHome.datastorage.UserHomeStore;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import org.bukkit.command.CommandExecutor;

public abstract class BaseCommand implements CommandExecutor {

    protected final ComponentLogger logger;
    protected final UserHomeStore userHomeStore;

    public BaseCommand(ComponentLogger logger, UserHomeStore userHomeStore) {
        this.logger = logger;
        this.userHomeStore = userHomeStore;
    }

}
