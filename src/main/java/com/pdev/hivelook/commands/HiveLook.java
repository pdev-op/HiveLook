package com.pdev.hivelook.commands;

import java.util.HashMap;

import com.pdev.hivelook.Main;
import com.pdev.hivelook.api.HLCommand;
import com.pdev.hivelook.utils.StringUtils;

import org.bukkit.command.CommandSender;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;

public class HiveLook extends HLCommand {
    public HiveLook(Main plugin) {
        super(plugin);

        this.addAlias("hivelook");
        this.addAlias("hl");
        this.setUsage("/hivelook [reload]");
    }

    @Override
    public HashMap<String, Integer> getSuggestions(CommandSender sender) {
        HashMap<String, Integer> suggestions = new HashMap<>();

        suggestions.put("reload", 1);

        return suggestions;
    }

    @Override
    public boolean hasPermission(CommandSender sender) {
        return sender.hasPermission("hivelook.admin");
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) throws Exception {
        if (args.length == 0) {
            sendHelpMessage(sender);
        } else if (args.length == 1) {
            if (args[0].equalsIgnoreCase("reload")) {
                plugin.reloadPlugin(sender);
            } else {
                throw new Exception("usage");
            }
        } else {
            throw new Exception("usage");
        }

        return true;
    }

    private void sendHelpMessage(CommandSender sender) {
        // Spacer
        sender.sendMessage(" ");

        // Title
        TextComponent title = new TextComponent(StringUtils.colorize("&e&lHive&6&lLook &rHelp Page"));
        title.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, ""));
        title.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
            new Text(StringUtils.colorize("&e&lHive&6&lLook\n&7Version: &f" + plugin.getDescription().getVersion() + "\n&7Written by &dpdev\n\n&7➥ &eClick to go to plugin page"))));
        sender.spigot().sendMessage(title);

        // Reload
        TextComponent reload = new TextComponent(StringUtils.colorize("&8- &7/hivelook &nreload"));
        reload.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/hivelook reload"));
        reload.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(StringUtils.colorize("&7Description: &fReload the plugin & config.\n&7Permission: &fhivelook.admin"))));

        sender.spigot().sendMessage(reload);

        // Spacer
        sender.sendMessage(" ");
    }
}
