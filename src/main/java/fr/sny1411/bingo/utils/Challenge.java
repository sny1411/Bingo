package fr.sny1411.bingo.utils;

import org.bukkit.inventory.ItemStack;

public class Challenge {
    public enum Difficult {EASY, MEDIUM, HARD, EXTREME}

    // Static

    private static int maxEasy;
    private static int maxMedium;
    private static int maxHard;
    private static int maxExtreme;

    public static int getMaxEasy() {
        return maxEasy;
    }

    public static void setMaxEasy(int maxEasy) {
        Challenge.maxEasy = maxEasy;
    }

    public static int getMaxMedium() {
        return maxMedium;
    }

    public static void setMaxMedium(int maxMedium) {
        Challenge.maxMedium = maxMedium;
    }

    public static int getMaxHard() {
        return maxHard;
    }

    public static void setMaxHard(int maxHard) {
        Challenge.maxHard = maxHard;
    }

    public static int getMaxExtreme() {
        return maxExtreme;
    }

    public static void setMaxExtreme(int maxExtreme) {
        Challenge.maxExtreme = maxExtreme;
    }

    private static void setPreset(int maxEasy, int maxMedium, int maxHard, int maxExtreme) {
        setMaxEasy(maxEasy);
        setMaxMedium(maxMedium);
        setMaxHard(maxHard);
        setMaxExtreme(maxExtreme);
    }

    public static void presetOnePlayer() {
        setPreset(13, 8, 4, 0);
    }

    public static void presetTwoPlayers() {
        setPreset(10, 10, 5, 0);
    }

    public static void presetThreePlayers() {
        setPreset(6, 13, 6, 0);
    }

    public static void presetFourPlayers() {
        setPreset(2, 10, 12, 1);
    }

    public static void init() {
        presetThreePlayers();
    }

    // Object
    private Difficult difficult;
    private String name;
    private String description;
    private ItemStack item;
    private Boolean realized;
    private Boolean validated;


}
