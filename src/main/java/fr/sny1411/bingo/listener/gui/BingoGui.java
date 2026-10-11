package fr.sny1411.bingo.listener.gui;

import fr.sny1411.bingo.Bingo;
import fr.sny1411.bingo.Game;
import fr.sny1411.bingo.listener.challenges.ChallengeVerifier;
import fr.sny1411.bingo.utils.*;
import fr.sny1411.bingo.utils.items.collections.Concrete;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;
import java.util.logging.Level;

public class BingoGui implements Listener {
    private static final Set<Player> playersInGui = new HashSet<>();
    private static final HashMap<Player, Material> spectatorMemory = new HashMap<>();
    public static void open(Player player) {
        Inventory gui = GuiHolder.createInventory(GuiHolder.Type.BINGO, 45, Component.text("BINGO", NamedTextColor.DARK_AQUA, TextDecoration.BOLD));
        for (int i = 0; i < 45; i++) {
            gui.setItem(i, Items.getGlassForGui());
        }

        placeGrid(player, gui);
        placeTeams(player, gui);

        player.openInventory(gui);
    }

    private static void placeGrid(Player player, Inventory gui) {
        Team playerTeam = Bingo.getGame().getTeams().getTeam(player);
        Grid playerGrid = null;
        assert playerTeam != null;
        if (playerTeam.getColor() == Team.Color.SPECTATOR) {
            if (spectatorMemory.containsKey(player)) {
                playerGrid = Bingo.getGame().getTeamsGrid().get(Bingo.getGame().getTeams().getTeam(spectatorMemory.get(player)));
            } else {
                playerGrid = Bingo.getGame().getGameGrid();
            }
        } else {
            playerGrid = Bingo.getGame().getTeamsGrid().get(Bingo.getGame().getTeams().getTeam(player));
        }

        int i = 3;
        int nbItems = 0;
        while (i < 44) {
            if (i == 8) {
                i = 12;
            } else if (i == 17) {
                i = 21;
            } else if (i == 26) {
                i = 30;
            } else if (i == 35) {
                i = 39;
            }
            Challenge challenge = playerGrid.getGrid()[(nbItems / 5)][nbItems % 5];
            ItemStack item;
            if (Boolean.TRUE.equals(challenge.getValidated())) {
                item = Items.getGlassValidBingo();
            } else {
                item = challenge.getItem(player);
            }
            gui.setItem(i, item);
            nbItems++;
            i++;
        }
    }

    private static void placeTeams(Player player, Inventory gui) {
        for (Team team : Bingo.getGame().getTeams().values()) {
            Team.Color colorTeam = team.getColor();
            if (colorTeam != Team.Color.SPECTATOR) {
                Team playerTeam = Bingo.getGame().getTeams().getTeam(player);
                List<Component> loreTeams = new ArrayList<>();
                Component nbChallenges = Bingo.getGame().getModeAffichage() == Game.ModeAffichage.CHILL
                        ? Component.text(Bingo.getGame().getTeamsScore().get(team).getNbChallenges())
                        : Component.text("!!").decorate(TextDecoration.OBFUSCATED);
                loreTeams.add(Component.translatable("bingo.gui.bingo.realized", NamedTextColor.BLUE, nbChallenges.color(NamedTextColor.WHITE)));
                for (OfflinePlayer playerInTeam : team.getPlayers()) {
                    Player onlinePlayer = playerInTeam.getPlayer();
                    String playerName = onlinePlayer != null ? Bingo.getPlainSerializer().serialize(onlinePlayer.displayName()) : playerInTeam.getName();
                    loreTeams.add(Component.text("- " + playerName, NamedTextColor.GRAY, TextDecoration.ITALIC));
                }
                ItemStack item = GuiItems.item(colorTeam.getMaterialBingoGui(), 1, colorTeam.displayName(), loreTeams, player);
                if (playerTeam == team || (Objects.requireNonNull(playerTeam).getColor() == Team.Color.SPECTATOR && spectatorMemory.containsKey(player) && spectatorMemory.get(player) == item.getType())) {
                    ItemMeta itemMeta = item.getItemMeta();
                    itemMeta.addEnchant(Enchantment.UNBREAKING, 5, true);
                    itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                    item.setItemMeta(itemMeta);
                }

                switch (team.getColor()) {
                    case ORANGE:
                        gui.setItem(9, item);
                        break;
                    case ROUGE:
                        gui.setItem(10, item);
                        break;
                    case VIOLET:
                        gui.setItem(18, item);
                        break;
                    case ROSE:
                        gui.setItem(19, item);
                        break;
                    case VERT:
                        gui.setItem(27, item);
                        break;
                    case BLEU:
                        gui.setItem(28, item);
                        break;
                    default:
                        throw new IllegalStateException("Unexpected value: " + team.getColor());
                }
            }
        }
    }

    @EventHandler
    private void onClick(InventoryClickEvent e) {
        if (GuiHolder.typeOf(e.getView().getTopInventory()) == GuiHolder.Type.BINGO && e.getCurrentItem() != null) {
            Bukkit.getLogger().log(Level.INFO, "bingogui2");
            Player player = (Player) e.getWhoClicked();
            if (Objects.requireNonNull(Bingo.getGame().getTeams().getTeam(player)).getColor() == Team.Color.SPECTATOR && e.getCurrentItem() != null) {
                Material concrete = e.getCurrentItem().getType();
                if (Concrete.isConcrete(concrete)) {
                    spectatorMemory.put(player, concrete);
                }
            } else {
                ChallengeId challengeId = Challenge.getId(e.getCurrentItem());
                if (challengeId != null) {
                    ChallengeVerifier.verifChallenge(player, challengeId);
                }
            }
           updateGui();
            Bukkit.getLogger().log(Level.INFO, "refresh bingoGui");
            e.setCancelled(true);
        }
    }

    private void updateGui() {
        Set<Player> playersGuiCopy = new HashSet<>(playersInGui);
        for (Player player : playersGuiCopy) {
            open(player);
        }
    }

    @EventHandler
    private void onOpenGui(InventoryOpenEvent e) {
        if (GuiHolder.typeOf(e.getView().getTopInventory()) == GuiHolder.Type.BINGO) {
            playersInGui.add((Player) e.getPlayer());
        }
    }

    @EventHandler
    private void onCloseGui(InventoryCloseEvent e) {
        if (GuiHolder.typeOf(e.getView().getTopInventory()) == GuiHolder.Type.BINGO) {
            playersInGui.remove((Player) e.getPlayer());
        }
    }
}
