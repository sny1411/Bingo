package fr.sny1411.bingo.utils;

import fr.sny1411.bingo.Game;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Challenge {
    public enum Difficult {EASY, MEDIUM, HARD, EXTREME}

    // Static

    private static int maxEasy;
    private static int maxMedium;
    private static int maxHard;
    private static int maxExtreme;
    private static int nbEasy = 0;
    private static int nbMedium = 0;
    private static int nbHard = 0;
    private static int nbExtreme = 0;

    private static List<Challenge> challenges = new ArrayList<>();

    public static List<Challenge> getChallenges() {
        return challenges;
    }

    public static int getNbEasy() {
        return nbEasy;
    }

    public static int getNbMedium() {
        return nbMedium;
    }

    public static int getNbHard() {
        return nbHard;
    }

    public static int getNbExtreme() {
        return nbExtreme;
    }

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
        createChallenges();
    }

    private static void createChallenges() {
        try {
            URL resourceURL = Game.getBingoInstance().getClass().getResource("/challenges.csv");
            assert resourceURL != null;
            URLConnection connection = resourceURL.openConnection();
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] lineSplit = line.split("\\|");
                ItemStack item = createItem(lineSplit[0], lineSplit[1], lineSplit[3]);
                challenges.add(new Challenge(lineSplit[2], lineSplit[0], item));
            }
            Collections.shuffle(challenges);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static ItemStack createItem(String name, String description, String type) {
        ItemStack item = null;
        if (Character.isUpperCase(type.charAt(0))) {
            Material material = Material.valueOf(type);
            item = new ItemStack(material);
        } else {
            switch (type) {
                case "harming":
                    item = Items.Challenge.getHarming();
                    break;
                case "dolphin":
                    item = Items.Challenge.getDolphin();
                    break;
                case "fox":
                    item = Items.Challenge.getFox();
                    break;
                case "horse":
                    item = Items.Challenge.getHorse();
                    break;
                case "strider":
                    item = Items.Challenge.getStrider();
                    break;
                case "cavespider":
                    item = Items.Challenge.getCavespider();
                    break;
                case "llama":
                    item = Items.Challenge.getLlama();
                    break;
                case "parrot":
                    item = Items.Challenge.getParrot();
                    break;
                case "pillager":
                    item = Items.Challenge.getPillager();
                    break;
                case "leatherBoots":
                    item = Items.Challenge.getLeatherBoots();
                    break;
                case "piglin":
                    item = Items.Challenge.getPiglin();
                    break;
                case "ghast":
                    item = Items.Challenge.getGhast();
                    break;
                case "elderGuardian":
                    item = Items.Challenge.getElderGuardian();
                    break;
                case "ironGolem":
                    item = Items.Challenge.getIronGolem();
                    break;
                case "regen":
                    item = Items.Challenge.getRegen();
                    break;
                case "diamondChestplate":
                    item = Items.Challenge.getDiamondChestplate();
                    break;
                case "silverfish":
                    item = Items.Challenge.getSilverfish();
                    break;
                case "enderman":
                    item = Items.Challenge.getEnderman();
                    break;
                case "bowser":
                    item = Items.Challenge.getBowser();
                    break;
            }
        }
        assert item != null;
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.displayName(Component.text(name));
        itemMeta.lore(Text.divideString(description));
        item.setItemMeta(itemMeta);
        return item;
    }

    // Object
    private Difficult difficult;
    private String name;
    private ItemStack item;
    private Boolean realized;
    private Boolean validated;

    private Challenge(String difficult, String name, ItemStack item) {
        this.name = name;
        this.item = item;
        this.realized = false;
        this.validated = false;

        switch (Difficult.valueOf(difficult)) {
            case EASY:
                nbEasy++;
                this.difficult = Difficult.EASY;
                break;
            case MEDIUM:
                nbMedium++;
                this.difficult = Difficult.MEDIUM;
                break;
            case HARD:
                nbHard++;
                this.difficult = Difficult.HARD;
                break;
            case EXTREME:
                nbExtreme++;
                this.difficult = Difficult.EXTREME;
                break;
        }
    }

    public Difficult getDifficult() {
        return difficult;
    }

    public String getName() {
        return name;
    }

    public ItemStack getItem() {
        return item;
    }

    public Boolean getRealized() {
        return realized;
    }

    public Boolean getValidated() {
        return validated;
    }

    public void setRealized(Boolean realized) {
        this.realized = realized;
    }

    public void setValidated(Boolean validated) {
        this.validated = validated;
    }
}
