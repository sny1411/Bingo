package fr.sny1411.bingo.utils;

import io.papermc.lib.PaperLib;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.logging.Level;

public abstract class Spawn {

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
        PaperLib.teleportAsync(player, SPAWN_LOC).thenAccept(result -> {
            if (Boolean.TRUE.equals(result)) {
                Bukkit.getLogger().log(Level.INFO, String.format("%s teleport to spawn", player.getName()));
            } else {
                Bukkit.getLogger().log(Level.WARNING, String.format("%s teleport error", player.getName()));
            }
        });
    }
}
