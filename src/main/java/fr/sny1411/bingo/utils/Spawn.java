package fr.sny1411.bingo.utils;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;

public class Spawn {
    private static final World overworld = Bukkit.getWorlds().get(0);

    public static void create() {
        BlocksFill.changeArea(-20, 200, -20, 20, 200, 20, Material.WHITE_STAINED_GLASS, overworld);
        BlocksFill.changeArea(-19, 200, -19, 19, 200, 19, Material.BARRIER, overworld);
        BlocksFill.changeArea(-20, 201, -20, -20, 203, 20, Material.CYAN_STAINED_GLASS_PANE, overworld);
        BlocksFill.changeArea(-19, 201, -20, 20, 203, -20, Material.CYAN_STAINED_GLASS_PANE, overworld);
        BlocksFill.changeArea(-19, 201, 20, 20, 203, 20, Material.CYAN_STAINED_GLASS_PANE, overworld);
        BlocksFill.changeArea(20, 201, -19, 20, 203, 19, Material.CYAN_STAINED_GLASS_PANE, overworld);
    }

    public static void remove() {
        BlocksFill.changeArea(-20, 200, -20, 20, 203, 20, Material.AIR, overworld);
    }
}
