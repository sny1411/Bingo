package fr.sny1411.bingo.utils;

import fr.sny1411.bingo.Game;
import fr.sny1411.bingo.i18n.Translations;
import io.papermc.paper.datacomponent.DataComponentType;
import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.TooltipDisplay;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
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
import java.util.Locale;
import java.util.Set;
import java.util.logging.Level;

public class Challenge {
    public enum Difficult {
        EASY(1, NamedTextColor.GREEN), MEDIUM(3, NamedTextColor.GOLD), HARD(9, NamedTextColor.RED), EXTREME(27, NamedTextColor.DARK_GRAY);

        private final int points;
        private final NamedTextColor color;

        Difficult(int points, NamedTextColor color) {
            this.points = points;
            this.color = color;
        }

        public int getPoints() {
            return points;
        }

        public Component label() {
            return Component.translatable("bingo.difficulty." + name().toLowerCase(Locale.ROOT), color);
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
                    challenges.add(new Challenge(id, Difficult.valueOf(lineSplit[1]), createItem(id, lineSplit[2])));
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

    private static ItemStack createItem(ChallengeId id, String type) {
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
        itemMeta.getPersistentDataContainer().set(idKey(), PersistentDataType.STRING, id.name());
        item.setItemMeta(itemMeta);
        // The icon only shows its name and lore, not the attributes, enchantments or effects of the item
        Set<DataComponentType> hiddenComponents = new HashSet<>(item.getDataTypes());
        hiddenComponents.remove(DataComponentTypes.CUSTOM_NAME);
        hiddenComponents.remove(DataComponentTypes.LORE);
        item.setData(DataComponentTypes.TOOLTIP_DISPLAY, TooltipDisplay.tooltipDisplay().hiddenComponents(hiddenComponents));
        return item;
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
    private final ItemStack item;
    private Boolean realized;
    private Boolean validated;

    public Challenge(ChallengeId id, Difficult difficult, ItemStack item) {
        this.id = id;
        this.difficult = difficult;
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

    private String translationKey() {
        return "bingo.challenge." + id.name().toLowerCase(Locale.ROOT);
    }

    public Component getName() {
        return Component.translatable(translationKey() + ".name");
    }

    // The icon of the challenge, with its name and description in the player's language
    public ItemStack getItem(Player player) {
        ItemStack playerItem = item.clone();
        ItemMeta meta = playerItem.getItemMeta();
        meta.customName(Translations.render(getName(), player).color(NamedTextColor.LIGHT_PURPLE).decorate(TextDecoration.BOLD).decoration(TextDecoration.ITALIC, false));
        String description = PlainTextComponentSerializer.plainText().serialize(Translations.render(Component.translatable(translationKey() + ".description"), player));
        List<Component> lore = new ArrayList<>();
        for (String line : Text.divideString(description)) {
            lore.add(Component.text(line, NamedTextColor.YELLOW, TextDecoration.ITALIC));
        }
        lore.add(Translations.render(Component.translatable("bingo.challenge.lore.difficulty", difficult.label().decoration(TextDecoration.ITALIC, false)), player));
        meta.lore(lore);
        playerItem.setItemMeta(meta);
        return playerItem;
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
            return new Challenge(this.getId(), this.getDifficult(), this.item);
        }
    }
}
