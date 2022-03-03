package com.pdev.hivelook.api;

import java.io.File;
import java.util.List;

import com.pdev.hivelook.Main;

import org.bukkit.configuration.file.YamlConfiguration;

public class Config {
    private File file;
    private Main plugin;
    private YamlConfiguration config;

    public Config(Main plugin) {
        this.plugin = plugin;

        file = new File(plugin.getDataFolder(), "config.yml");

        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdir();
        }

        save();

        config = YamlConfiguration.loadConfiguration(file);
    }

    public void save() {
        if (!file.exists()) {
            plugin.saveResource("config.yml", false);
        }
    }

    public YamlConfiguration getConfiguration() {
        return config;
    }

    public String getHiveGuiTitle() {
        return config.getString("hive-gui-title");
    }

    public List<String> getBeeTextures() {
        return config.getStringList("bee-textures");
    }

    public List<String> getDisabledWorlds() {
        return config.getStringList("disabled-worlds");
    }

    public boolean getUsePermission() {
        return config.getBoolean("use-permission", true);
    }

    public String getPrefix() {
        return config.getString("prefix");
    }
}
