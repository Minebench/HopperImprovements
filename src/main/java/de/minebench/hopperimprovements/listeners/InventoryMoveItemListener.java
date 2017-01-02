package de.minebench.hopperimprovements.listeners;

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

import de.minebench.hopperimprovements.HopperImprovements;
import de.minebench.hopperimprovements.InventoryUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.plugin.RegisteredListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;

public class InventoryMoveItemListener implements Listener {
    private final HopperImprovements plugin;
    private final boolean disableMoveEvent;
    private final boolean cancelMoveEventIfFull;
    private final boolean stopMoveEventIfFull;
    private final List<RegisteredListener> stoppedListener = new ArrayList<>();

    public InventoryMoveItemListener(HopperImprovements plugin) {
        this.plugin = plugin;
        disableMoveEvent = plugin.getConfig().getBoolean("disable-move-event");
        cancelMoveEventIfFull = plugin.getConfig().getBoolean("cancel-move-event-if-full");
        stopMoveEventIfFull = plugin.getConfig().getBoolean("stop-move-event-if-full");
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onInventoryMoveLow(InventoryMoveItemEvent event) {
        if (disableMoveEvent) {
            for (RegisteredListener listener : event.getHandlers().getRegisteredListeners()) {
                if (listener.getPlugin() != plugin) {
                    event.getHandlers().unregister(listener);
                    plugin.getLogger().log(Level.INFO, "Unregistered InventoryMoveItemEvent listener by " + listener.getPlugin().getName());
                }
            }
            return;
        }

        if (!cancelMoveEventIfFull && !stopMoveEventIfFull) {
            // Should not edit event
            return;
        }

        if (InventoryUtils.atLeastOneFitsInInventory(event.getDestination(), event.getItem())) {
            if (plugin.isDebug()) {
                plugin.getLogger().log(Level.INFO, "At least one of the item stack fits into the inventory -> a move event will occur");
            }
            return;
        }

        if (stopMoveEventIfFull) {
            for (RegisteredListener listener : event.getHandlers().getRegisteredListeners()) {
                if (listener.getPlugin() != plugin) {
                    event.getHandlers().unregister(listener);
                    stoppedListener.add(listener);
                }
            }
            if (plugin.isDebug()) {
                plugin.getLogger().log(Level.INFO, "Stopped " + stoppedListener.size() + " other listeners!");
            }
        } else if (cancelMoveEventIfFull) {
            event.setCancelled(true);
            if (plugin.isDebug()) {
                plugin.getLogger().log(Level.INFO, "Canceled the event. This way other plugins can ignore it as the destination is full");
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onInventoryMoveMonitor(InventoryMoveItemEvent event) {
        // Register the stopped listeners
        if (stoppedListener.size() > 0) {
            if (plugin.isDebug()) {
                plugin.getLogger().log(Level.INFO, "Re-registering " + stoppedListener.size() + " listeners!");
            }
            Iterator<RegisteredListener> listeners = stoppedListener.iterator();
            while (listeners.hasNext()) {
                RegisteredListener listener = listeners.next();
                listeners.remove();
                event.getHandlers().register(listener);
            }
        }
    }

    public void unregister() {
        InventoryMoveItemEvent.getHandlerList().unregister(this);
    }
}
