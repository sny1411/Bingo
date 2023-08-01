package fr.sny1411.bingo.utils.items.collections;

import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class Shulker {
    private enum ShulkerType {
        SHULKER(Material.SHULKER_BOX),
        WHITE_SHULKER_BOX(Material.WHITE_SHULKER_BOX),
        LIGHT_BLUE_SHULKER_BOX(Material.LIGHT_BLUE_SHULKER_BOX),
        GRAY_SHULKER_BOX(Material.GRAY_SHULKER_BOX),
        BLACK_SHULKER_BOX(Material.BLACK_SHULKER_BOX),
        BROWN_SHULKER_BOX(Material.BROWN_SHULKER_BOX),
        RED_SHULKER_BOX(Material.RED_SHULKER_BOX),
        ORANGE_SHULKER_BOX(Material.ORANGE_SHULKER_BOX),
        YELLOW_SHULKER_BOX(Material.YELLOW_SHULKER_BOX),
        LIME_SHULKER_BOX(Material.LIME_SHULKER_BOX),
        GREEN_SHULKER_BOX(Material.GREEN_SHULKER_BOX),
        CYAN_SHULKER_BOX(Material.CYAN_SHULKER_BOX),
        BLUE_SHULKER_BOX(Material.BLUE_SHULKER_BOX),
        PURPLE_SHULKER_BOX(Material.PURPLE_SHULKER_BOX),
        MAGENTA_SHULKER_BOX(Material.MAGENTA_SHULKER_BOX),
        PINK_SHULKER_BOX(Material.PINK_SHULKER_BOX),
        LIGHT_GRAY_SHULKER_BOX(Material.LIGHT_GRAY_SHULKER_BOX);

        private final Material material;

        ShulkerType(Material material) {
            this.material = material;
        }

        public Material getMaterial() {
            return material;
        }
    }

    public static boolean isShulker(Material material) {
        for (ShulkerType shulkerType : ShulkerType.values()) {
            if (shulkerType.getMaterial() == material) {
                return true;
            }
        }
        return false;
    }

    public static boolean isInInventory(Inventory inventory) {
        for (ItemStack itemStack : inventory.getContents()) {
            if (itemStack != null && isShulker(itemStack.getType())) return true;
        }
        return false;
    }
}
