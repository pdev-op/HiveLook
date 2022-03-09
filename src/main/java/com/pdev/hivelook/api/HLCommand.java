package com.pdev.hivelook.api;

import java.util.ArrayList;
import java.util.HashMap;

import com.pdev.hivelook.Main;

import org.bukkit.command.CommandSender;

public abstract class HLCommand {
    protected Main plugin;
    private ArrayList<String> aliases = new ArrayList<String>();
    private String usage;

    public HLCommand(Main plugin) {
        this.plugin = plugin;
    }

    public ArrayList<String> getAliases() {
        return this.aliases;
    }

    public void setAliases(ArrayList<String> a) {
        this.aliases = a;
    }

    public void addAlias(String a) {
        this.aliases.add(a);
    }

    public String getUsage() {
        return this.usage;
    }

    public void setUsage(String usage) {
        this.usage = usage;
    }

    public abstract HashMap<String, Integer> getSuggestions(CommandSender sender);

    public abstract boolean hasPermission(CommandSender sender);

    public abstract boolean execute(CommandSender sender, String args[]) throws Exception;
}
