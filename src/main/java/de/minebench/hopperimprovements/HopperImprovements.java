package de.minebench.hopperimprovements;

/*
 * Copyright 2017 Max Lee (https://github.com/Phoenix616/)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the Mozilla Public License as published by
 * the Mozilla Foundation, version 2.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * Mozilla Public License v2.0 for more details.
 *
 * You should have received a copy of the Mozilla Public License v2.0
 * along with this program. If not, see <http://mozilla.org/MPL/2.0/>.
 */

import de.minebench.hopperimprovements.listeners.InventoryMoveItemListener;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public final class HopperImprovements extends JavaPlugin {

    private boolean cancelMoveEventIfFull = false;
    private InventoryMoveItemListener inventoryMoveItemListener;
    private boolean debug;

    @Override
    public void onEnable() {
        loadConfig();
        getCommand("hopperimprovements").setExecutor(this);
    }

    public void loadConfig() {
        saveDefaultConfig();
        reloadConfig();
        if (cancelMoveEventIfFull != getConfig().getBoolean("cancel-move-event-if-full")) {
            cancelMoveEventIfFull = getConfig().getBoolean("cancel-move-event-if-full");
            if (cancelMoveEventIfFull) {
                getServer().getPluginManager().registerEvents(inventoryMoveItemListener = new InventoryMoveItemListener(this), this);
            } else if (inventoryMoveItemListener != null) {
                inventoryMoveItemListener.unregister();
            }
        }
        debug = getConfig().getBoolean("debug");
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length > 0) {
            if ("reload".equalsIgnoreCase(args[0]) && sender.hasPermission("pluginname.command.reload")) {
                loadConfig();
                sender.sendMessage(ChatColor.YELLOW + "Config reloaded!");
                return true;
            }
        }
        return false;
    }

    public boolean isDebug() {
        return debug;
    }
}
