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
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.plugin.RegisteredListener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public final class HopperImprovements extends JavaPlugin {

    private InventoryMoveItemListener inventoryMoveItemListener;
    private boolean debug;
    private final List<RegisteredListener> stoppedListener = new ArrayList<>();

    @Override
    public void onEnable() {
        loadConfig();
        getCommand("hopperimprovements").setExecutor(this);
    }

    public void loadConfig() {
        saveDefaultConfig();
        reloadConfig();
        debug = getConfig().getBoolean("debug");

        getLogger().log(Level.INFO, "Config:");
        for (String key : getConfig().getKeys(true)) {
            getLogger().log(Level.INFO, " " + key + ": " + getConfig().get(key));
        }

        if (getConfig().getBoolean("disable-move-event")) {
            for (RegisteredListener listener : InventoryMoveItemEvent.getHandlerList().getRegisteredListeners()) {
                if (listener.getPlugin() != this) {
                    InventoryMoveItemEvent.getHandlerList().unregister(listener);
                    stoppedListener.add(listener);
                    getLogger().log(Level.INFO, "Unregistered InventoryMoveItemEvent listener by " + listener.getPlugin().getName());
                }
            }
        } else {
            InventoryMoveItemEvent.getHandlerList().registerAll(stoppedListener);
            getLogger().log(Level.INFO, "Registered " + stoppedListener.size() + " stopped InventoryMoveItemEvent listeners!");
            stoppedListener.clear();
        }

        if (inventoryMoveItemListener != null) {
            inventoryMoveItemListener.unregister();
        }
        getServer().getPluginManager().registerEvents(inventoryMoveItemListener = new InventoryMoveItemListener(this), this);

    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length > 0) {
            if ("reload".equalsIgnoreCase(args[0]) && sender.hasPermission("hopperimprovements.command.reload")) {
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
