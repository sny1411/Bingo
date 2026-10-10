package fr.sny1411.bingo.utils;

import fr.sny1411.bingo.Game;
import io.papermc.paper.datacomponent.DataComponentType;
import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.TooltipDisplay;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;

public class Challenge {
    public enum Difficult {
        EASY(1, "§aFacile"), MEDIUM(3, "§6Moyen"), HARD(9, "§cDifficile"), EXTREME(27, "§8Extreme");

        private final int points;
        private final String textDifficult;

        Difficult(int points, String textDifficult) {
            this.points = points;
            this.textDifficult = textDifficult;
        }

        public int getPoints() {
            return points;
        }

        public String getTextDifficult() {
            return textDifficult;
        }
    }

    public static List<Challenge> loadChallenges() {
        List<Challenge> challenges = new ArrayList<>();
        try {
            URL resourceURL = Game.getBingoInstance().getClass().getResource("/challenges.csv");
            assert resourceURL != null;
            URLConnection connection = resourceURL.openConnection();
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));
            Set<ChallengeId> loadedIds = EnumSet.noneOf(ChallengeId.class);
            String line;
            while ((line = reader.readLine()) != null) {
                String[] lineSplit = line.split("\\|");
                ChallengeId id = ChallengeId.fromString(lineSplit[0]);
                if (id == null) {
                    Bukkit.getLogger().log(Level.SEVERE, String.format("challenges.csv: unknown challenge id %s, add it to ChallengeId", lineSplit[0]));
                } else if (!loadedIds.add(id)) {
                    Bukkit.getLogger().log(Level.SEVERE, String.format("challenges.csv: duplicate challenge id %s", id));
                } else {
                    ItemStack item = createItem(id, lineSplit[1], lineSplit[2], lineSplit[4], lineSplit[3]);
                    challenges.add(new Challenge(id, lineSplit[3], lineSplit[1], item));
                }
            }
            for (ChallengeId id : ChallengeId.values()) {
                if (!loadedIds.contains(id)) {
                    Bukkit.getLogger().log(Level.SEVERE, String.format("challenges.csv: missing challenge id %s", id));
                }
            }
            Collections.shuffle(challenges);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return challenges;
    }

    private static ItemStack createItem(ChallengeId id, String name, String description, String type, String difficult) {
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
                case "grenouille":
                    item = Items.Challenge.getGrenouille();
                    break;
                case "dromadaire":
                    item = Items.Challenge.getDromadaire();
                    break;
            }
        }
        assert item != null;
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.displayName(Component.text(name));
        List<Component> lore = Text.divideString(description);
        lore.add(Component.text("Difficulté : " + loreDifficultBuilder(difficult)));
        itemMeta.lore(lore);
        itemMeta.getPersistentDataContainer().set(idKey(), PersistentDataType.STRING, id.name());
        item.setItemMeta(itemMeta);
        // The icon only shows its name and lore, not the attributes, enchantments or effects of the item
        Set<DataComponentType> hiddenComponents = new HashSet<>(item.getDataTypes());
        hiddenComponents.remove(DataComponentTypes.CUSTOM_NAME);
        hiddenComponents.remove(DataComponentTypes.LORE);
        item.setData(DataComponentTypes.TOOLTIP_DISPLAY, TooltipDisplay.tooltipDisplay().hiddenComponents(hiddenComponents));
        return item;
    }

    private static String loreDifficultBuilder(String difficult) {
        return Difficult.valueOf(difficult).getTextDifficult();
    }

    private static NamespacedKey idKey() {
        return new NamespacedKey(Game.getBingoInstance(), "challenge_id");
    }

    /**
     * @return the id of the challenge shown by this item, or null if it is not a challenge item
     */
    public static ChallengeId getId(ItemStack item) {
        if (item == null || !item.hasItemMeta()) {
            return null;
        }
        return ChallengeId.fromString(item.getItemMeta().getPersistentDataContainer().get(idKey(), PersistentDataType.STRING));
    }

    // Object
    private final ChallengeId id;
    private Difficult difficult;
    private final String name;
    private final ItemStack item;
    private Boolean realized;
    private Boolean validated;

    private Challenge(ChallengeId id, String difficult, String name, ItemStack item) {
        this.id = id;
        this.name = name;
        this.item = item;
        this.realized = false;
        this.validated = false;

        this.difficult = Difficult.valueOf(difficult);
    }

    public Challenge(ChallengeId id, Difficult difficult, String name, ItemStack item) {
        this.id = id;
        this.difficult = difficult;
        this.name = name;
        this.item = item;

        this.realized = false;
        this.validated = false;
    }

    public ChallengeId getId() {
        return id;
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

    @Override
    public Challenge clone() {
        try {
            return (Challenge) super.clone();
        } catch (CloneNotSupportedException e) {
            return new Challenge(this.getId(), this.getDifficult(), this.getName(), this.getItem());
        }
    }
}
