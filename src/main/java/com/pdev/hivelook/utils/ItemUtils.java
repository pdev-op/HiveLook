package com.pdev.hivelook.utils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.UUID;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;

import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

public class ItemUtils {

    private static Method metaSetProfileMethod;
    private static Field metaProfileField;

    public static ItemStack makeEnchantedItem(final Material material, final String name,
            final ArrayList<String> lore) {
        ItemStack item = new ItemStack(material);
        item.addUnsafeEnchantment(Enchantment.DURABILITY, 1);

        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(StringUtils.colorize(name));
        itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        itemMeta.setLore(lore);
        item.setItemMeta(itemMeta);

        return item;
    }

    public static ItemStack makeItem(final Material material, final String name, final ArrayList<String> lore) {
        ItemStack item = new ItemStack(material);

        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(StringUtils.colorize(name));
        itemMeta.setLore(lore);
        item.setItemMeta(itemMeta);

        return item;
    }

    public static ItemStack makeItemStack(final Material material, final int amount, final String name,
            final ArrayList<String> lore) {
        ItemStack item = new ItemStack(material, amount);

        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(StringUtils.colorize(name));
        itemMeta.setLore(lore);
        item.setItemMeta(itemMeta);

        return item;
    }

    public static ItemStack makePlayerSkull(final OfflinePlayer p, final String name, final ArrayList<String> lore) {
        ItemStack skull = new ItemStack(Material.PLAYER_HEAD);

        SkullMeta skullMeta = (SkullMeta) skull.getItemMeta();
        skullMeta.setOwningPlayer(p);
        skullMeta.setDisplayName(StringUtils.colorize(name));
        skullMeta.setLore(lore);
        skull.setItemMeta(skullMeta);

        return skull;
    }

    public static ItemStack makeCustomSkull(final String base64, final String name, final ArrayList<String> lore) {
        ItemStack skull = new ItemStack(Material.PLAYER_HEAD);

        SkullMeta skullMeta = (SkullMeta) skull.getItemMeta();
        skullMeta.setDisplayName(StringUtils.colorize(name));
        skullMeta.setLore(lore);
        mutateItemMeta(skullMeta, base64);
        skull.setItemMeta(skullMeta);

        return skull;
    }

    @SuppressWarnings("unchecked")
    private static GameProfile makeProfile(String b64) {
        // random uuid based on the b64 string
        UUID id = new UUID(
                b64.substring(b64.length() - 20).hashCode(),
                b64.substring(b64.length() - 10).hashCode());
        GameProfile profile = new GameProfile(id, "Player");

        // Still need to figure out how to fix this properly
        profile.getProperties().put("textures", new Property("textures", b64));

        return profile;
    }

    private static void mutateItemMeta(SkullMeta meta, String b64) {
        try {
            if (metaSetProfileMethod == null) {
                metaSetProfileMethod = meta.getClass().getDeclaredMethod("setProfile", GameProfile.class);
                metaSetProfileMethod.setAccessible(true);
            }

            metaSetProfileMethod.invoke(meta, makeProfile(b64));
        } catch (InvocationTargetException | IllegalAccessException | NoSuchMethodException
                | IllegalArgumentException ex) {
            try {
                if (metaProfileField == null) {
                    metaProfileField = meta.getClass().getDeclaredField("profile");
                    metaProfileField.setAccessible(true);
                }
                metaProfileField.set(meta, makeProfile(b64));

            } catch (NoSuchFieldException | IllegalAccessException ex2) {
                ex2.printStackTrace();
            }
        }
    }
}
