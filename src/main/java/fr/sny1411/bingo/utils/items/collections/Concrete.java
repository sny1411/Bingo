package fr.sny1411.bingo.utils.items.collections;

import org.bukkit.Material;

public class Concrete {
    private enum ConcreteType {
        WHITE_CONCRETE(Material.WHITE_CONCRETE),
        LIGHT_GRAY_CONCRETE(Material.LIGHT_GRAY_CONCRETE),
        GRAY_CONCRETE(Material.GRAY_CONCRETE),
        BLACK_CONCRETE(Material.BLACK_CONCRETE),
        BROWN_CONCRETE(Material.BROWN_CONCRETE),
        RED_CONCRETE(Material.RED_CONCRETE),
        ORANGE_CONCRETE(Material.ORANGE_CONCRETE),
        YELLOW_CONCRETE(Material.YELLOW_CONCRETE),
        LIME_CONCRETE(Material.LIME_CONCRETE),
        GREEN_CONCRETE(Material.GREEN_CONCRETE),
        CYAN_CONCRETE(Material.CYAN_CONCRETE),
        BLUE_CONCRETE(Material.BLUE_CONCRETE),
        LIGHT_BLUE_CONCRETE(Material.LIGHT_BLUE_CONCRETE),
        PURPLE_CONCRETE(Material.PURPLE_CONCRETE),
        MAGENTA_CONCRETE(Material.MAGENTA_CONCRETE),
        PINK_CONCRETE(Material.PINK_CONCRETE);

        private final Material concrete;
        ConcreteType(Material concrete) {
            this.concrete = concrete;
        }

        public Material getConcrete() {
            return concrete;
        }
    }

    public static boolean isConcrete(Material material) {
        for (ConcreteType concreteType : ConcreteType.values()) {
            if (concreteType.getConcrete() == material) {
                return true;
            }
        }
        return false;
    }
}
