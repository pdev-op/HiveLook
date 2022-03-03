package com.pdev.hivelook.listeners;

import com.pdev.hivelook.gui.HiveGui;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryHolder;

public class HiveGuiClick implements Listener {
    @EventHandler(priority = EventPriority.HIGH)
    public void onClick(InventoryClickEvent e) {
        InventoryHolder holder = e.getInventory().getHolder();

        if (holder instanceof HiveGui) {
            e.setCancelled(true);

            if (e.getCurrentItem() != null) {
                HiveGui gui = (HiveGui) holder;
                gui.handleGuiClick(e);
            }
        }
    }
}
