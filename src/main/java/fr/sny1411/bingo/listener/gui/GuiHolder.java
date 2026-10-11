package fr.sny1411.bingo.listener.gui;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;

/**
 * Tells which GUI of the plugin an inventory is, since its title depends on the player's language.
 */
public final class GuiHolder implements InventoryHolder {
    public enum Type { SETTINGS, SETTINGS_GRID, SETTINGS_VICTORY, SETTINGS_TEAMS, BINGO, TEAMS }

    private final Type type;
    private Inventory inventory;

    private GuiHolder(Type type) {
        this.type = type;
    }

    public static Inventory createInventory(Type type, int size, Component title) {
        GuiHolder holder = new GuiHolder(type);
        holder.inventory = Bukkit.createInventory(holder, size, title);
        return holder.inventory;
    }

    // The type of the GUI, or null if the inventory is not a GUI of the plugin
    public static Type typeOf(Inventory inventory) {
        if (inventory != null && inventory.getHolder(false) instanceof GuiHolder holder) {
            return holder.type;
        }
        return null;
    }

    @Override
    public @NotNull Inventory getInventory() {
        return inventory;
    }
}
