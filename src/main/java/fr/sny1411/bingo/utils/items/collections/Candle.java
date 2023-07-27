package fr.sny1411.bingo.utils.items.collections;

import org.bukkit.Material;

public class Candle {
    private enum CandleType {
        NO_COLOR(Material.CANDLE),
        WHITE_CANDLE(Material.WHITE_CANDLE),
        LIGHT_GRAY_CANDLE(Material.LIGHT_GRAY_CANDLE),
        GRAY_CANDLE(Material.GRAY_CANDLE),
        BLACK_CANDLE(Material.BLACK_CANDLE),
        BROWN_CANDLE(Material.BROWN_CANDLE),
        RED_CANDLE(Material.RED_CANDLE),
        ORANGE_CANDLE(Material.ORANGE_CANDLE),
        YELLOW_CANDLE(Material.YELLOW_CANDLE),
        LIME_CANDLE(Material.LIME_CANDLE),
        GREEN_CANDLE(Material.GREEN_CANDLE),
        CYAN_CANDLE(Material.CYAN_CANDLE),
        LIGHT_BLUE_CANDLE(Material.LIGHT_BLUE_CANDLE),
        BLUE_CANDLE(Material.BLUE_CANDLE),
        PURPLE_CANDLE(Material.PURPLE_CANDLE),
        MAGENTA_CANDLE(Material.MAGENTA_CANDLE),
        PINK_CANDLE(Material.PINK_CANDLE);

        private final Material materialCandle;

        CandleType(Material materialCandle) {
            this.materialCandle = materialCandle;
        }

        public Material getMaterialCandle() {
            return materialCandle;
        }
    }

    ;

    public static boolean isCandleItem(Material material) {
        for (CandleType candleType : CandleType.values()) {
            if (candleType.getMaterialCandle().equals(material)) {
                return true;
            }
        }
        return false;
    }
}
