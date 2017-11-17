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

import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Directional;

public class InventoryUtils {

    /**
     * Checks whether or not at least one of an item stack can fit
     * into an inventory by checking if there is a slot empty or an
     * item stack that has less than the max amount
     *
     * @param source
     * @param inventory The inventory to search in
     * @param item The item to search
     * @return <tt>true</tt> if at least one item can fit; <tt>false</tt> if not
     */
    public static boolean atLeastOneFitsInInventory(Inventory source, Inventory inventory, ItemStack item) {
        if (hasFreeSlotForItem(source, inventory, item)) {
            return true;
        }
        for (ItemStack i : inventory.getStorageContents()) {
            if (i != null && i.isSimilar(item) && i.getAmount() < i.getMaxStackSize()) {
                return true;
            }
        }
        return false;
    }

    public static boolean hasFreeSlotForItem(Inventory source, Inventory inventory, ItemStack item) {
        int firstEmpty = inventory.firstEmpty();
        if (firstEmpty == -1) {
            return false;
        }

        BlockFace facing = null;
        if (source.getHolder() instanceof BlockState) {
            if (((BlockState) source.getHolder()).getData() instanceof Directional) {
                facing = ((Directional) ((BlockState) source.getHolder()).getData()).getFacing();
            }
        }

        ItemStack[] items = inventory.getStorageContents();
        switch (inventory.getType()) {
            case BREWING:
                switch (item.getType()) {
                    case POTION:
                    case SPLASH_POTION:
                    case LINGERING_POTION:
                        if (facing != BlockFace.DOWN && firstEmpty < 3) {
                            return true;
                        }
                        break;
                    case BLAZE_POWDER:
                        if (facing != BlockFace.DOWN && items[4] == null) {
                            return true;
                        }
                    case NETHER_STALK:
                    case SPIDER_EYE:
                    case SUGAR:
                    case GHAST_TEAR:
                    case FERMENTED_SPIDER_EYE:
                    case MAGMA_CREAM:
                    case GOLDEN_CARROT:
                    case SPECKLED_MELON:
                    case DRAGONS_BREATH:
                    case RABBIT_FOOT:
                    case REDSTONE:
                    case SULPHUR:
                    case GLOWSTONE_DUST:
                        if (facing == BlockFace.DOWN && items[3] == null) {
                            return true;
                        }
                        break;
                    case RAW_FISH:
                        if (facing == BlockFace.DOWN && items[3] == null && item.getData().getData() == 3) {
                            return true;
                        }
                        break;
                    default:
                        return false;
                }
                break;
            case BEACON:
                switch (item.getType()) {
                    case DIAMOND:
                    case EMERALD:
                    case GOLD_INGOT:
                    case IRON_INGOT:
                        return true;
                    default:
                        return false;
                }
            default:
                return true;
        }
        return false;
    }
}
