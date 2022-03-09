package com.pdev.hivelook.gui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import com.pdev.hivelook.Main;
import com.pdev.hivelook.api.Config;
import com.pdev.hivelook.utils.ItemUtils;
import com.pdev.hivelook.utils.StringUtils;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Beehive;
import org.bukkit.block.EntityBlockStorage;
import org.bukkit.entity.Bee;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class HiveGui implements InventoryHolder {
    private static Random random;

    private Main plugin;
    private Player player;
    private Beehive beehive;
    private Inventory inventory;
    private int bees;
    private int honey;

    public HiveGui(Main plugin, Player player, Beehive beehive) {
        this.plugin = plugin;
        this.player = player;
        this.beehive = beehive;
        this.bees = beehive.getEntityCount();
        this.honey = ((org.bukkit.block.data.type.Beehive) beehive.getBlockData()).getHoneyLevel();
    }

    public String getName() {
        return StringUtils.colorize(plugin.getConfigFile().getHiveGuiTitle()
                .replaceAll("%bees%", Integer.toString(bees))
                .replaceAll("%honey%", Integer.toString(honey)));
    }

    public void setItems() {
        // Config file
        Config cf = plugin.getConfigFile();

        // Random
        ArrayList<Integer> slots = new ArrayList<>(Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8));

        // Move Item
        if (cf.getUseEject()) {
            ItemStack move = new ItemStack(Material.BARRIER);
            ItemMeta moveMeta = move.getItemMeta();
            moveMeta.setDisplayName(StringUtils.colorize("&c&lForce Bees Out"));
            move.setItemMeta(moveMeta);

            inventory.setItem(0, move);
            slots.remove(0);
        }

        // Bees
        for (int i = 0; i < bees; i++) {
            int randomSlot = random.nextInt(slots.size());
            int slot = slots.get(randomSlot);
            ItemStack bee = getRandomBeeHead();

            inventory.setItem(slot, bee);
            slots.remove(randomSlot);
        }

        // Honey
        for (int i = 0; i < honey; i++) {
            if (!slots.isEmpty()) {
                int randomSlot = random.nextInt(slots.size());
                int slot = slots.get(randomSlot);
                ItemStack hon = getHoneyItem();

                inventory.setItem(slot, hon);
                slots.remove(randomSlot);
            }
        }

        // Empty Slots
        if (cf.getUseEmptyItem()) {
            for (Integer slot : slots) {
                ItemStack empty = getEmptyItem();

                inventory.setItem(slot, empty);
            }
        }
    }

    public void handleGuiClick(InventoryClickEvent e) {
        int slot = e.getSlot();

        // Move bees
        if (slot == 0 && plugin.getConfigFile().getUseEject()) {
            EntityBlockStorage<Bee> beeStorage = (EntityBlockStorage<Bee>) beehive;
            List<Bee> bees = (List<Bee>) beeStorage.releaseEntities();

            for (Bee bee : bees) {
                bee.setCannotEnterHiveTicks(300);
                bee.setHive((Location) null);
            }

            player.closeInventory();
        }
    }

    public void open() {
        inventory = plugin.getServer().createInventory(
                this,
                InventoryType.DISPENSER,
                StringUtils.colorize(plugin.getConfigFile().getHiveGuiTitle()
                        .replaceAll("%bees%", Integer.toString(bees))
                        .replaceAll("%honey%", Integer.toString(honey))));

        setItems();

        player.openInventory(inventory);
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }

    private ItemStack getRandomBeeHead() {
        List<String> beeHeads = plugin.getConfigFile().getBeeTextures();
        String b64 = beeHeads.get(random.nextInt(beeHeads.size()));

        return ItemUtils.makeCustomSkull(b64, plugin.getConfigFile().getBeeName(), new ArrayList<String>());
    }

    private ItemStack getHoneyItem() {
        Config cf = plugin.getConfigFile();
        ItemStack hon = new ItemStack(cf.getHoneyMaterial());
        ItemMeta honeyMeta = hon.getItemMeta();

        honeyMeta.setDisplayName(cf.getHoneyName());
        hon.setItemMeta(honeyMeta);

        return hon;
    }

    private ItemStack getEmptyItem() {
        Config cf = plugin.getConfigFile();
        ItemStack empty = new ItemStack(cf.getEmptyMaterial());
        ItemMeta emptyMeta = empty.getItemMeta();

        emptyMeta.setDisplayName(cf.getEmptyName());
        empty.setItemMeta(emptyMeta);

        return empty;
    }

    static {
        random = new Random();
    }
}
