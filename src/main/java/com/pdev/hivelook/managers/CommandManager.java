package com.pdev.hivelook.managers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.pdev.hivelook.Main;
import com.pdev.hivelook.api.HLCommand;
import com.pdev.hivelook.commands.HiveLook;
import com.pdev.hivelook.utils.StringUtils;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

public class CommandManager implements CommandExecutor, TabCompleter {
    private Main plugin;
    private ArrayList<HLCommand> commands;

    public CommandManager(Main plugin) {
        // Plugin
        this.plugin = plugin;

        // Commands List
        commands = new ArrayList<HLCommand>();

        // Main Command
        commands.add(new HiveLook(plugin));

        // Register the commands
        registerCommands();
    }

    public void registerCommands() {
        for (HLCommand c : commands) {
            for (String s : c.getAliases()) {
                if (plugin.getCommand(s) != null) {
                    plugin.getCommand(s).setExecutor(this);
                    plugin.getCommand(s).setTabCompleter(this);
                }
            }
        }
    }

    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        ArrayList<String> suggestions = new ArrayList<String>();

        for (HLCommand c : commands) {
            for (String al : c.getAliases()) {
                if (al.equalsIgnoreCase(alias) && c.hasPermission(sender)) {
                    HashMap<String, Integer> suggestionsMap = c.getSuggestions(sender);

                    for (String s : suggestionsMap.keySet()) {
                        if (args.length == suggestionsMap.get(s)) {
                            if (!s.equalsIgnoreCase("players")) {
                                suggestions.add(s);
                            } else {
                                for (Player player : Bukkit.getOnlinePlayers()) {
                                    suggestions.add(player.getName());
                                }
                            }
                        }
                    }
                }
            }
        }

        return suggestions;
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        for (HLCommand c : commands) {
            if (c.getAliases().contains(label.toLowerCase())) {

                String prefix = plugin.getConfigFile().getPrefix();

                // Check Permissions
                if (!c.hasPermission(sender)) {
                    if (!(sender instanceof Player)) {
                        sender.sendMessage(StringUtils.colorize(prefix + "&7This command cannot be run from the console."));
                    } else {
                        sender.sendMessage(StringUtils.colorize(prefix + "&cInsufficient permissions."));
                    }

                    return false;
                }

                // Send it
                try {
                    c.execute(sender, args);
                } catch (Exception e) {
                    if (e.getMessage().equalsIgnoreCase("insufficient-permissions")) {
                        sender.sendMessage(StringUtils.colorize(prefix + "&cInsufficient permissions."));
                    } else if (e.getMessage().equalsIgnoreCase("usage")) {
                        sender.sendMessage(
                                StringUtils.colorize(prefix + "&7Incorrect usage (try &e" + c.getUsage() + ")."));
                    } else if (e.getMessage().equalsIgnoreCase("no-player")) {
                        sender.sendMessage(StringUtils.colorize(prefix + "&7Player not found."));
                    } else {
                        sender.sendMessage(StringUtils.colorize("&cAn internal error has occured, please contact an admin. We are sorry for the inconvenience!"));

                        e.printStackTrace();
                    }
                }

                return true;
            }
        }

        return false;
    }
}
