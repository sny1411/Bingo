package fr.sny1411.bingo.utils;

import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public abstract class Items {
    private static ItemStack settings;
    private static ItemStack teamSelector;
    private static ItemStack glassForGui;

    public static void init() {
        initSettings();
        initTeamSelector();
        initGlassForGui();
    }

    private static void initGlassForGui() {
        glassForGui = new ItemStack(Material.GRAY_STAINED_GLASS_PANE, 1);
        ItemMeta metaGlassGui = glassForGui.getItemMeta();
        metaGlassGui.displayName(Component.text(" "));
        glassForGui.setItemMeta(metaGlassGui);
    }

    private static void initSettings() {
        settings = new ItemStack(Material.COMPARATOR);
        ItemMeta metaSettings = settings.getItemMeta();
        metaSettings.displayName(Component.text("§2◈ §a§lParamètres §r§2◈"));
        settings.setItemMeta(metaSettings);
    }

    private static void initTeamSelector() {
        teamSelector = new ItemStack(Material.COMPASS);
        ItemMeta metaTeamsSelector = teamSelector.getItemMeta();
        metaTeamsSelector.displayName(Component.text("§2◈ §a§lTeams §r§2◈"));
        teamSelector.setItemMeta(metaTeamsSelector);
    }

    public static ItemStack getSettings() {
        return settings;
    }

    public static ItemStack getTeamSelector() {
        return teamSelector;
    }

    public static ItemStack getGlassForGui() {
        return glassForGui;
    }
}
