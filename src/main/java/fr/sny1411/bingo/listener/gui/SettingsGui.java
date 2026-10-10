package fr.sny1411.bingo.listener.gui;

import fr.sny1411.bingo.Bingo;
import fr.sny1411.bingo.Game;
import fr.sny1411.bingo.Settings;
import fr.sny1411.bingo.listener.gui.GuiHolder.Type;
import fr.sny1411.bingo.utils.Challenge;
import fr.sny1411.bingo.utils.Grid;
import fr.sny1411.bingo.utils.Items;
import fr.sny1411.bingo.utils.SkullCustom;
import fr.sny1411.bingo.utils.Spawn;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class SettingsGui implements Listener {
    private static final String PLUS_TEXTURE = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjA1NmJjMTI0NGZjZmY5OTM0NGYxMmFiYTQyYWMyM2ZlZTZlZjZlMzM1MWQyN2QyNzNjMTU3MjUzMWYifX19";
    private static final String MINUS_TEXTURE = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNGU0YjhiOGQyMzYyYzg2NGUwNjIzMDE0ODdkOTRkMzI3MmE2YjU3MGFmYmY4MGMyYzViMTQ4Yzk1NDU3OWQ0NiJ9fX0=";

    private static Component setting(String key) {
        return Component.translatable(key, NamedTextColor.AQUA, TextDecoration.BOLD);
    }

    private static Component clickToEdit() {
        return Component.translatable("bingo.gui.settings.click_to_edit", NamedTextColor.GRAY);
    }

    private static ItemStack plus(Player player) {
        return GuiItems.named(SkullCustom.getCustomSkull(PLUS_TEXTURE), Component.text("+", NamedTextColor.GREEN), List.of(), player);
    }

    private static ItemStack minus(Player player) {
        return GuiItems.named(SkullCustom.getCustomSkull(MINUS_TEXTURE), Component.text("-", NamedTextColor.RED), List.of(), player);
    }

    private static ItemStack back(Player player) {
        return GuiItems.item(Material.BARRIER, Component.translatable("bingo.gui.back", NamedTextColor.RED), player);
    }

    public static void open(Player player, Type type) {
        switch (type) {
            case SETTINGS -> openGui(player);
            case SETTINGS_GRID -> openGuiDifficult(player);
            case SETTINGS_VICTORY -> openGuiVictoire(player);
            case SETTINGS_TEAMS -> openGuiTeams(player);
            default -> throw new IllegalArgumentException("Not a settings GUI: " + type);
        }
    }

    private static void openGui(Player player) {
        Inventory gui = GuiHolder.createInventory(Type.SETTINGS, 27, GuiItems.title("bingo.gui.settings.title", player));
        for (int i = 0; i < 27; i++) {
            if (i < 10 || i > 16) {
                gui.setItem(i, Items.getGlassForGui());
            }
        }
        Game game = Bingo.getGame();

        gui.setItem(10, GuiItems.item(Material.WHITE_BANNER, 1, setting("bingo.gui.settings.teams"), List.of(clickToEdit()), player));

        boolean chill = game.getModeAffichage() == Game.ModeAffichage.CHILL;
        gui.setItem(11, GuiItems.item(chill ? Material.IRON_BLOCK : Material.NETHERITE_BLOCK, 1, setting("bingo.gui.settings.display"), List.of(
                GuiItems.option(Component.translatable("bingo.display_mode.chill"), chill),
                GuiItems.option(Component.translatable("bingo.display_mode.competition"), !chill)), player));

        gui.setItem(12, GuiItems.item(Material.CAULDRON, 1, setting("bingo.gui.settings.grid"), List.of(clickToEdit()), player));

        if (game.getModeJeu() == Game.ModeJeu.DUEL) {
            game.setModeVictoire(Game.ModeVictoire.DEFIS);
        } else if (game.getModeJeu() == Game.ModeJeu.HANDICAP) {
            game.setModeVictoire(Game.ModeVictoire.BINGO);
        }
        List<Component> modes = new ArrayList<>();
        for (Game.ModeJeu mode : Game.ModeJeu.values()) {
            modes.add(GuiItems.option(mode.label(), game.getModeJeu() == mode));
        }
        gui.setItem(13, GuiItems.item(Material.CRAFTING_TABLE, 1, setting("bingo.gui.settings.game_mode"), modes, player));

        gui.setItem(14, GuiItems.item(Material.REDSTONE, 1, setting("bingo.gui.settings.victory"), List.of(clickToEdit()), player));

        boolean bonus = game.isDefiBonus();
        gui.setItem(15, GuiItems.item(bonus ? Material.GLOWSTONE : Material.REDSTONE_LAMP, 1, setting("bingo.gui.settings.bonus"), List.of(
                pair(Component.translatable("bingo.gui.on"), Component.translatable("bingo.gui.off"), bonus)), player));

        gui.setItem(16, GuiItems.item(Material.REDSTONE_BLOCK, Component.translatable("bingo.gui.settings.reset", NamedTextColor.RED, TextDecoration.BOLD), player));

        player.openInventory(gui);
    }

    // "First / Second" with the selected one in bold yellow
    private static Component pair(Component first, Component second, boolean firstSelected) {
        return Component.textOfChildren(
                firstSelected ? first.color(NamedTextColor.YELLOW).decorate(TextDecoration.BOLD) : first.color(NamedTextColor.GRAY),
                Component.text(" / ", NamedTextColor.GRAY),
                firstSelected ? second.color(NamedTextColor.GRAY) : second.color(NamedTextColor.YELLOW).decorate(TextDecoration.BOLD));
    }

    private static void openGuiTeams(Player player) {
        Inventory gui = GuiHolder.createInventory(Type.SETTINGS_TEAMS, 27, GuiItems.title("bingo.gui.settings.teams.title", player));
        Settings settings = Bingo.getGame().getSettings();
        gui.setItem(3, plus(player));
        gui.setItem(5, plus(player));
        gui.setItem(21, minus(player));
        gui.setItem(23, minus(player));
        gui.setItem(12, GuiItems.item(Material.DIAMOND_HORSE_ARMOR, settings.getNbTeams(), Component.translatable("bingo.gui.settings.teams.count", NamedTextColor.AQUA), List.of(), player));
        gui.setItem(14, GuiItems.item(Material.PUFFERFISH, settings.getNbPlayerTeams(), Component.translatable("bingo.gui.settings.teams.players", NamedTextColor.AQUA), List.of(), player));
        gui.setItem(26, back(player));
        player.openInventory(gui);
    }

    private static ItemStack difficultyItem(Material material, Challenge.Difficult difficult, String key, NamedTextColor color, Player player) {
        int max = maxChallenges(difficult);
        return GuiItems.item(max > 0 ? material : Material.STRUCTURE_VOID, Math.max(max, 1), Component.translatable(key, color), List.of(
                Component.text("[ " + max + " / " + Bingo.getGame().getNbChallenges(difficult) + " ]", NamedTextColor.GRAY)), player);
    }

    private static int maxChallenges(Challenge.Difficult difficult) {
        Settings settings = Bingo.getGame().getSettings();
        return switch (difficult) {
            case EASY -> settings.getMaxEasy();
            case MEDIUM -> settings.getMaxMedium();
            case HARD -> settings.getMaxHard();
            case EXTREME -> settings.getMaxExtreme();
        };
    }

    private static void openGuiDifficult(Player player) {
        Inventory gui = GuiHolder.createInventory(Type.SETTINGS_GRID, 27, GuiItems.title("bingo.gui.settings.grid.title", player));
        for (int slot : new int[]{1, 3, 5, 7}) {
            gui.setItem(slot, plus(player));
        }
        for (int slot : new int[]{19, 21, 23, 25}) {
            gui.setItem(slot, minus(player));
        }
        gui.setItem(10, difficultyItem(Material.COAL, Challenge.Difficult.EASY, "bingo.gui.settings.grid.easy", NamedTextColor.DARK_GRAY, player));
        gui.setItem(12, difficultyItem(Material.COPPER_INGOT, Challenge.Difficult.MEDIUM, "bingo.gui.settings.grid.medium", NamedTextColor.GOLD, player));
        gui.setItem(14, difficultyItem(Material.AMETHYST_SHARD, Challenge.Difficult.HARD, "bingo.gui.settings.grid.hard", NamedTextColor.LIGHT_PURPLE, player));
        gui.setItem(16, difficultyItem(Material.NETHERITE_SCRAP, Challenge.Difficult.EXTREME, "bingo.gui.settings.grid.extreme", NamedTextColor.DARK_RED, player));
        gui.setItem(18, getTotalItem(player));
        gui.setItem(26, back(player));
        player.openInventory(gui);
    }

    private static ItemStack getDurationItem(Player player) {
        int duration = Bingo.getGame().getSettings().getDurationMinutes();
        return GuiItems.item(Material.CLOCK, duration / Settings.DURATION_STEP_MINUTES, setting("bingo.gui.settings.victory.duration"), List.of(
                Component.text(String.format("%d h %02d", duration / 60, duration % 60), NamedTextColor.GRAY)), player);
    }

    private static ItemStack getTotalItem(Player player) {
        int total = Bingo.getGame().getSettings().getMaxTotal();
        boolean complete = !Bingo.getGame().getSettings().verifSettingsToHigh();
        List<Component> lore = complete ? List.of() : List.of(Component.translatable("bingo.gui.settings.grid.missing", NamedTextColor.GRAY, Component.text(Grid.NB_CHALLENGES)));
        return GuiItems.item(Material.MAP, Math.max(total, 1), Component.translatable("bingo.gui.settings.grid.total", complete ? NamedTextColor.GREEN : NamedTextColor.RED,
                Component.text(total), Component.text(Grid.NB_CHALLENGES)), lore, player);
    }

    private static void openGuiVictoire(Player player) {
        Inventory gui = GuiHolder.createInventory(Type.SETTINGS_VICTORY, 27, GuiItems.title("bingo.gui.settings.victory.title", player));
        gui.setItem(3, plus(player));
        gui.setItem(7, plus(player));
        gui.setItem(21, minus(player));
        gui.setItem(25, minus(player));

        Game game = Bingo.getGame();
        gui.setItem(12, GuiItems.item(Material.SPECTRAL_ARROW, game.getNbreBingoForWin(), setting("bingo.gui.settings.victory.bingos"), List.of(), player));

        List<Component> winLore = new ArrayList<>();
        winLore.add(pair(Game.ModeVictoire.BINGO.label(), Game.ModeVictoire.DEFIS.label(), game.getModeVictoire() == Game.ModeVictoire.BINGO));
        if (game.getModeJeu() == Game.ModeJeu.DUEL || game.getModeJeu() == Game.ModeJeu.HANDICAP) {
            winLore.add(Component.translatable("bingo.gui.settings.victory.locked", NamedTextColor.RED));
        }
        gui.setItem(14, GuiItems.item(Material.TARGET, 1, setting("bingo.gui.settings.victory.mode"), winLore, player));
        gui.setItem(16, getDurationItem(player));
        gui.setItem(26, back(player));
        player.openInventory(gui);
    }

    public static boolean isSettingsGui(Inventory inventory) {
        Type type = GuiHolder.typeOf(inventory);
        return type == Type.SETTINGS || type == Type.SETTINGS_GRID || type == Type.SETTINGS_VICTORY || type == Type.SETTINGS_TEAMS;
    }

    // Shows a change of the settings to the other players who have the settings open
    private static void refreshOthers(Player player) {
        for (Player other : Bukkit.getOnlinePlayers()) {
            Inventory open = other.getOpenInventory().getTopInventory();
            if (other != player && isSettingsGui(open)) {
                open(other, GuiHolder.typeOf(open));
            }
        }
    }

    @EventHandler
    public void clickItems(PlayerInteractEvent e) {
        if (Bingo.getGame().getEtat() == Game.Etat.SETUP) {
            if (Items.isSettings(e.getItem()) && e.getPlayer().hasPermission(Spawn.SETTINGS_PERMISSION)) {
                openGui(e.getPlayer());
            }
        }
    }

    @EventHandler
    private void inventoryClick(InventoryClickEvent e) {
        Inventory clickedInventory = e.getClickedInventory();
        if (Bingo.getGame().getEtat() != Game.Etat.SETUP || !isSettingsGui(clickedInventory)) {
            return;
        }
        e.setCancelled(true);
        if (!e.getWhoClicked().hasPermission(Spawn.SETTINGS_PERMISSION) || e.getCurrentItem() == null) {
            return;
        }
        Player player = (Player) e.getWhoClicked();
        Type type = GuiHolder.typeOf(clickedInventory);
        Material currentItem = e.getCurrentItem().getType();
        if (currentItem == Material.BARRIER) {
            openGui(player);
            return;
        }
        if (type == Type.SETTINGS) {
            switch (currentItem) {
                case WHITE_BANNER -> openGuiTeams(player);
                case CAULDRON -> openGuiDifficult(player);
                case REDSTONE -> openGuiVictoire(player);
                default -> {
                    if (clickMain(player, currentItem)) {
                        settingChanged(player, type);
                    }
                }
            }
            return;
        }
        boolean changed = switch (type) {
            case SETTINGS_GRID -> clickGrid(e.getSlot());
            case SETTINGS_VICTORY -> clickVictory(player, currentItem, e.getSlot());
            case SETTINGS_TEAMS -> clickTeams(e.getSlot());
            default -> false;
        };
        if (changed) {
            settingChanged(player, type);
        }
    }

    private static void settingChanged(Player player, Type type) {
        open(player, type);
        refreshOthers(player);
    }

    private static void playClick(Player player) {
        player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_WORK_LIBRARIAN, 500.0f, 1.0f);
    }

    // Each click method returns true when a setting changed
    private static boolean clickMain(Player player, Material currentItem) {
        Game game = Bingo.getGame();
        switch (currentItem) {
            case IRON_BLOCK -> game.setModeAffichage(Game.ModeAffichage.COMPETITION);
            case NETHERITE_BLOCK -> game.setModeAffichage(Game.ModeAffichage.CHILL);
            case CRAFTING_TABLE -> {
                Game.ModeJeu[] modes = Game.ModeJeu.values();
                game.setModeJeu(modes[(game.getModeJeu().ordinal() + 1) % modes.length]);
            }
            case REDSTONE_LAMP -> game.setDefiBonus(true);
            case GLOWSTONE -> game.setDefiBonus(false);
            case REDSTONE_BLOCK -> {
                // TODO: reset the settings
                player.playSound(player.getLocation(), Sound.BLOCK_AMETHYST_BLOCK_BREAK, 500.0f, 1.0f);
                return true;
            }
            default -> {
                return false;
            }
        }
        playClick(player);
        return true;
    }

    private static boolean clickGrid(int slot) {
        Game game = Bingo.getGame();
        Settings settings = game.getSettings();
        switch (slot) {
            case 1 -> {
                if (!settings.verifSettingsToHigh()) return false;
                settings.setMaxEasy(settings.getMaxEasy() + 1);
            }
            case 3 -> {
                if (!settings.verifSettingsToHigh()) return false;
                settings.setMaxMedium(settings.getMaxMedium() + 1);
            }
            case 5 -> {
                if (!settings.verifSettingsToHigh() || game.getNbChallenges(Challenge.Difficult.HARD) <= settings.getMaxHard()) return false;
                settings.setMaxHard(settings.getMaxHard() + 1);
            }
            case 7 -> {
                if (!settings.verifSettingsToHigh() || game.getNbChallenges(Challenge.Difficult.EXTREME) <= settings.getMaxExtreme()) return false;
                settings.setMaxExtreme(settings.getMaxExtreme() + 1);
            }
            case 19 -> {
                if (settings.getMaxEasy() <= 0) return false;
                settings.setMaxEasy(settings.getMaxEasy() - 1);
            }
            case 21 -> {
                if (settings.getMaxMedium() <= 0) return false;
                settings.setMaxMedium(settings.getMaxMedium() - 1);
            }
            case 23 -> {
                if (settings.getMaxHard() <= 0) return false;
                settings.setMaxHard(settings.getMaxHard() - 1);
            }
            case 25 -> {
                if (settings.getMaxExtreme() <= 0) return false;
                settings.setMaxExtreme(settings.getMaxExtreme() - 1);
            }
            default -> {
                return false;
            }
        }
        return true;
    }

    private static boolean clickVictory(Player player, Material currentItem, int slot) {
        Game game = Bingo.getGame();
        Settings settings = game.getSettings();
        if (currentItem == Material.TARGET) {
            if (game.getModeJeu() == Game.ModeJeu.DUEL || game.getModeJeu() == Game.ModeJeu.HANDICAP) {
                return false;
            }
            playClick(player);
            game.setModeVictoire(game.getModeVictoire() == Game.ModeVictoire.BINGO ? Game.ModeVictoire.DEFIS : Game.ModeVictoire.BINGO);
            return true;
        }
        switch (slot) {
            case 3 -> {
                if (game.getNbreBingoForWin() >= 10) return false;
                game.setNbreBingoForWin(game.getNbreBingoForWin() + 1);
            }
            case 21 -> {
                if (game.getNbreBingoForWin() <= 1) return false;
                game.setNbreBingoForWin(game.getNbreBingoForWin() - 1);
            }
            case 7 -> {
                if (settings.getDurationMinutes() >= Settings.MAX_DURATION_MINUTES) return false;
                settings.setDurationMinutes(settings.getDurationMinutes() + Settings.DURATION_STEP_MINUTES);
            }
            case 25 -> {
                if (settings.getDurationMinutes() <= Settings.MIN_DURATION_MINUTES) return false;
                settings.setDurationMinutes(settings.getDurationMinutes() - Settings.DURATION_STEP_MINUTES);
            }
            default -> {
                return false;
            }
        }
        return true;
    }

    private static boolean clickTeams(int slot) {
        Settings settings = Bingo.getGame().getSettings();
        switch (slot) {
            case 3 -> {
                if (settings.getNbTeams() >= 6) return false;
                settings.setNbTeams(settings.getNbTeams() + 1);
            }
            case 5 -> {
                if (settings.getNbPlayerTeams() >= 10) return false;
                settings.setNbPlayerTeams(settings.getNbPlayerTeams() + 1);
                setChallengePreset();
            }
            case 21 -> {
                if (settings.getNbTeams() <= 2) return false;
                settings.setNbTeams(settings.getNbTeams() - 1);
            }
            case 23 -> {
                if (settings.getNbPlayerTeams() <= 1) return false;
                settings.setNbPlayerTeams(settings.getNbPlayerTeams() - 1);
                setChallengePreset();
            }
            default -> {
                return false;
            }
        }
        Bingo.getGame().getTeams().create();
        return true;
    }

    private static void setChallengePreset() {
        resetColorTab();
        Settings settings = Bingo.getGame().getSettings();
        settings.setChallengePreset(settings.getNbPlayerTeams());
    }

    private static void resetColorTab() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.displayName(Component.text(player.getName()));
        }
    }
}
