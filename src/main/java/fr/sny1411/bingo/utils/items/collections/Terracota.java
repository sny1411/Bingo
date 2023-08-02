package fr.sny1411.bingo.utils.items.collections;

import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public final class Terracota {
    private Terracota() {
        throw new IllegalStateException("Utility class");
    }
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
        for (ItemStack item : inventory) {
            if (item != null) {
                for (TerracotaType terracotaType : TerracotaType.values()) {
                    Material terracota = Material.valueOf(terracotaType.name());
                    if (item.getType() == terracota) {
                        nb++;
                    }
                }
            }
        }
        return nb;
    }

}
