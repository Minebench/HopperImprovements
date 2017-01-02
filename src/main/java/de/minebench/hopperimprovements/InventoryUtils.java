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

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class InventoryUtils {

    /**
     * Checks whether or not at least one of an item stack can fit
     * into an inventory by checkingif there is a slot empty or an
     * item stack that has less than the max amount
     * @param inventory The inventory to search in
     * @param item The item to search
     * @return <tt>true</tt> if at least one item can fit; <tt>false</tt> if not
     */
    public static boolean atLeastOneFitsInInventory(Inventory inventory, ItemStack item) {
        if (inventory.firstEmpty() != -1) {
            return true;
        }
        for (ItemStack i : inventory.getStorageContents()) {
            if (i.isSimilar(item) && i.getAmount() < i.getMaxStackSize()) {
                return true;
            }
        }
        return false;
    }
}
