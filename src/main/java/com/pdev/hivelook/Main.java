package com.pdev.hivelook;

import com.pdev.hivelook.api.Config;
import com.pdev.hivelook.listeners.HiveGuiClick;
import com.pdev.hivelook.listeners.HiveInteract;
import com.pdev.hivelook.managers.CommandManager;
import com.pdev.hivelook.utils.StringUtils;
import com.pdev.hivelook.utils.bStats;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
    private static Main instance;

    private CommandManager commandManager;

    private Config config;

    public Main() {
        instance = this;
    }

    @Override
    public void onEnable() {
        // Timings
        long start = System.currentTimeMillis();

        // Logging
        Bukkit.getConsoleSender().sendMessage("§eHiveLook §7v" + getDescription().getVersion() + " by pdev enabling...");

        // Config
        config = new Config(this);

        // Managers
        commandManager = new CommandManager(this);

        // Listeners
        getServer().getPluginManager().registerEvents(new HiveGuiClick(), this);
        getServer().getPluginManager().registerEvents(new HiveInteract(this), this);

        // Metrics
        new bStats(this, 14515);

        // Log load time
        Bukkit.getConsoleSender().sendMessage("§aEnabled §7in " + (System.currentTimeMillis() - start) + "ms");
    }

    @Override
    public void onDisable() {
        // Close Inventories to prevent glitching
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.closeInventory();
        }

        // Logging
        Bukkit.getConsoleSender().sendMessage("§eHiveLook §7v" + getDescription().getVersion() + " by pdev disabled.");
    }

    public Main getInstance() {
        return instance;
    }

    public Config getConfigFile() {
        return config;
    }

    public CommandManager getCommandManager() {
        return commandManager;
    }

    public void reloadPlugin(CommandSender sender) {
        // Save Config
        config.save();

        // Config
        config = new Config(this);

        // Logging
        sender.sendMessage(StringUtils.colorize("&e&lHive&6&lLook &r&8| &7Plugin has been reloaded"));
    }
}
