package fr.sny1411.bingo.utils;

import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class Terracota {
    private enum TerracotaType {
        WHITE_TERRACOTTA,
        ORANGE_TERRACOTTA,
        PINK_TERRACOTTA,
        MAGENTA_TERRACOTTA,
        LIGHT_BLUE_TERRACOTTA,
        YELLOW_TERRACOTTA,
        LIME_TERRACOTTA,
        GRAY_TERRACOTTA,
        LIGHT_GRAY_TERRACOTTA,
        CYAN_TERRACOTTA,
        PURPLE_TERRACOTTA,
        BLUE_TERRACOTTA,
        BROWN_TERRACOTTA,
        RED_TERRACOTTA,
        BLACK_TERRACOTTA,
        GREEN_TERRACOTTA
    }

    public static int getNbTerracota(Inventory inventory) {
        int nb = 0;
        for (TerracotaType terracota : TerracotaType.values()) {
            Material material = Material.valueOf(terracota.name());
            if (inventory.containsAtLeast(new ItemStack(material), 1)) {
                nb++;
            }
        }
        return nb;
    }

}
