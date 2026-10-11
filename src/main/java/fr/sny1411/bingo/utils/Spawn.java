package fr.sny1411.bingo.utils;

import fr.sny1411.bingo.Bingo;
import fr.sny1411.bingo.Game;
import fr.sny1411.bingo.listener.gui.SettingsGui;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.PlayerInventory;

import java.util.List;
import java.util.Locale;
import java.util.logging.Level;

public final class Spawn {
    private Spawn() {
        throw new IllegalStateException("Utility class");
    }

    public static final String SETTINGS_PERMISSION = "bingo.settings";

    private static final List<World> WORLDS = Bukkit.getWorlds();
    private static final World OVERWORLD = WORLDS.get(0);
    private static final Location SPAWN_LOC = new Location(OVERWORLD, 0, 204, 0);

    public static void create() {
        BlocksFill.changeArea(-20, 200, -20, 20, 200, 20, Material.WHITE_STAINED_GLASS, OVERWORLD);
        BlocksFill.changeArea(-19, 200, -19, 19, 200, 19, Material.BARRIER, OVERWORLD);
        BlocksFill.changeArea(-20, 201, -20, -20, 203, 20, Material.CYAN_STAINED_GLASS_PANE, OVERWORLD);
        BlocksFill.changeArea(-19, 201, -20, 20, 203, -20, Material.CYAN_STAINED_GLASS_PANE, OVERWORLD);
        BlocksFill.changeArea(-19, 201, 20, 20, 203, 20, Material.CYAN_STAINED_GLASS_PANE, OVERWORLD);
        BlocksFill.changeArea(20, 201, -19, 20, 203, 19, Material.CYAN_STAINED_GLASS_PANE, OVERWORLD);
    }

    public static void remove() {
        BlocksFill.changeArea(-20, 200, -20, 20, 203, 20, Material.AIR, OVERWORLD);
    }

    public static void teleportPlayers() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            teleportPlayer(player);
        }
    }

    public static void teleportPlayer(Player player) {
        player.teleportAsync(SPAWN_LOC).thenAccept(result -> {
            if (Boolean.TRUE.equals(result)) {
                Bukkit.getLogger().log(Level.INFO, String.format("%s teleport to spawn", player.getName()));
            } else {
                Bukkit.getLogger().log(Level.WARNING, String.format("%s teleport error", player.getName()));
            }
        });
    }

    public static void giveItemsPlayers() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            giveItemsPlayer(player);
        }
    }

    public static void giveItemsPlayer(Player player) {
        giveItemsPlayer(player, player.locale());
    }

    // The locale is given when it changes, since the player's locale is only updated after the event
    public static void giveItemsPlayer(Player player, Locale locale) {
        Inventory playerInventory = player.getInventory();
        playerInventory.clear();
        if (player.hasPermission(SETTINGS_PERMISSION)) {
            playerInventory.setItem(0, Items.getSettings(locale));
        }
        playerInventory.setItem(4, Items.getTeamSelector(locale));
    }

    public static void updateSettingsItems() {
        if (Bingo.getGame().getEtat() != Game.Etat.SETUP) {
            return;
        }
        for (Player player : Bukkit.getOnlinePlayers()) {
            PlayerInventory playerInventory = player.getInventory();
            int settingsSlot = -1;
            for (int slot = 0; slot < playerInventory.getSize(); slot++) {
                if (Items.isSettings(playerInventory.getItem(slot))) {
                    settingsSlot = slot;
                }
            }
            if (player.hasPermission(SETTINGS_PERMISSION)) {
                if (settingsSlot == -1) {
                    playerInventory.setItem(0, Items.getSettings(player.locale()));
                }
            } else {
                if (settingsSlot != -1) {
                    playerInventory.setItem(settingsSlot, null);
                }
                if (SettingsGui.isSettingsGui(player.getOpenInventory().getTopInventory())) {
                    player.closeInventory();
                }
            }
        }
    }
}
