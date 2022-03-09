package com.pdev.hivelook.api;

import java.io.File;
import java.util.List;

import com.pdev.hivelook.Main;
import com.pdev.hivelook.utils.StringUtils;

import org.bukkit.Material;
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

    public boolean getUseEject() {
        return config.getBoolean("use-eject-button", true);
    }

    public Material getHoneyMaterial() {
        Material material = Material.getMaterial(config.getString("honey-material", " "));

        return material != null ? material : Material.ORANGE_STAINED_GLASS_PANE;
    }

    public String getHoneyName() {
        return StringUtils.colorize(config.getString("honey-name", "&6Honey"));
    }

    public boolean getUseEmptyItem() {
        return config.getBoolean("item-in-empty-slot", true);
    }

    public Material getEmptyMaterial() {
        String empty = config.getString("empty-material", " ");
        Material material = Material.getMaterial(empty);

        if (empty.equalsIgnoreCase("none")) {
            return null;
        }

        return material != null ? material : Material.GRAY_STAINED_GLASS_PANE;
    }

    public String getEmptyName() {
        return StringUtils.colorize(config.getString("empty-name", " "));
    }

    public String getBeeName() {
        return config.getString("bee-name", "&eBee");
    }
}
