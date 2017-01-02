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

import java.util.logging.Level;

public class InventoryMoveItemListener implements Listener {
    private final HopperImprovements plugin;

    public InventoryMoveItemListener(HopperImprovements plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onInventoryMove(InventoryMoveItemEvent event) {
        if (InventoryUtils.atLeastOneFitsInInventory(event.getDestination(), event.getItem())) {
            if (plugin.isDebug()) {
                plugin.getLogger().log(Level.INFO, "At least one of the item stack fits into the inventory -> a move event will occur");
            }
            return;
        }

        // Cancel the event. This way other plugins can ignore it as the destination is full
        event.setCancelled(true);
    }

    public void unregister() {
        InventoryMoveItemEvent.getHandlerList().unregister(this);
    }
}
