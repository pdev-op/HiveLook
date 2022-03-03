package com.pdev.hivelook.listeners;

import java.util.List;

import com.pdev.hivelook.Main;
import com.pdev.hivelook.api.Config;
import com.pdev.hivelook.gui.HiveGui;

import org.bukkit.Material;
import org.bukkit.block.Beehive;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class HiveInteract implements Listener {
    private Main plugin;

    public HiveInteract(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInteract(PlayerInteractEvent e) {
        // Not a beehive clicked
        if (!e.hasBlock()) {
            return;
        }

        Action action = e.getAction();

        if (!action.equals(Action.RIGHT_CLICK_BLOCK)) {
            return;
        }

        Block block = e.getClickedBlock();

        if (!block.getType().equals(Material.BEE_NEST) && !block.getType().equals(Material.BEEHIVE)) {
            return;
        }

        Player player = e.getPlayer();
        Config config = plugin.getConfigFile();

        // Disabled worlds
        List<String> disabledWorlds = config.getDisabledWorlds();

        if (disabledWorlds.contains(player.getWorld().getName())) {
            return;
        }

        // Permissions
        if (config.getUsePermission() && !player.hasPermission("hivelook.use")) {
            return;
        }

        // Make the GUI & open it
        HiveGui gui = new HiveGui(plugin, player, (Beehive) block.getState());
        gui.open();
    }
}
