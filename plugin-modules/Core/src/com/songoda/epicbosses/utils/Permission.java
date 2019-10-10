package com.songoda.epicbosses.utils;

import org.bukkit.command.CommandSender;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 04-Oct-18
 */
public enum Permission {

    admin("boss.admin"),
    create("boss.create"),
    debug("boss.debug"),
    edit("boss.edit"),
    give("boss.give"),
    help("boss.help"),
    nearby("boss.nearby"),
    reload("boss.reload"),
    shop("boss.shop"),
    time("boss.time");

    private String permission;

    Permission(String permission) {
        this.permission = permission;
    }

    public boolean hasPermission(CommandSender commandSender) {
        return commandSender.hasPermission(getPermission());
    }

    public String getPermission() {
        return this.permission;
    }
}
