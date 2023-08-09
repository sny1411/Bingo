package fr.sny1411.bingo.listener.gui;

import fr.sny1411.bingo.Bingo;
import fr.sny1411.bingo.Game;
import fr.sny1411.bingo.listener.ChallengesListener;
import fr.sny1411.bingo.utils.*;
import fr.sny1411.bingo.utils.items.collections.Concrete;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
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
        Inventory gui = Bukkit.createInventory(null, 45, Component.text("§3§lBINGO"));
        for (int i = 0; i < 45; i++) {
            gui.setItem(i, Items.getGlassForGui());
        }

        placeGrid(player, gui);
        placeTeams(player, gui);

        player.openInventory(gui);
    }

    private static void placeGrid(Player player, Inventory gui) {
        Team playerTeam = Team.getTeam(player);
        Grid playerGrid = null;
        assert playerTeam != null;
        if (playerTeam.getColor() == Team.Color.SPECTATOR) {
            if (spectatorMemory.containsKey(player)) {
                playerGrid = Grid.getTeamsGrid().get(Team.getTeam(spectatorMemory.get(player)));
            } else {
                playerGrid = Grid.getGameGrid();
            }
        } else {
            playerGrid = Grid.getTeamsGrid().get(Team.getTeam(player));
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
                item = challenge.getItem();
            }
            gui.setItem(i, item);
            nbItems++;
            i++;
        }
    }

    private static void placeTeams(Player player, Inventory gui) {
        for (Team team : Team.getTeams().values()) {
            Team.Color colorTeam = team.getColor();
            if (colorTeam != Team.Color.SPECTATOR) {
                ItemStack item = new ItemStack(colorTeam.getMaterialBingoGui());
                ItemMeta itemMeta = item.getItemMeta();
                itemMeta.displayName(Component.text(colorTeam.getPrefixe() + colorTeam.getNom()));
                Team playerTeam = Team.getTeam(player);
                if (playerTeam == team || (Objects.requireNonNull(playerTeam).getColor() == Team.Color.SPECTATOR && spectatorMemory.containsKey(player) && spectatorMemory.get(player) == item.getType())) {
                    itemMeta.addEnchant(Enchantment.DURABILITY, 5, true);
                    itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                }
                ArrayList<Component> loreTeams = new ArrayList<>();
                if (Bingo.getGame().getModeAffichage() == Game.ModeAffichage.CHILL) {
                    loreTeams.add(Component.text("§9Défi(s) réalisé(s): §f" + Score.getTeamsScore().get(team).getNbChallenges()));
                } else {
                    loreTeams.add(Component.text("§9Défi(s) réalisé(s): §f§k!!"));
                }
                for (Player playerInTeam : team.getPlayers()) {
                    String playerName = Bingo.getPlainSerializer().serialize(playerInTeam.displayName());
                    loreTeams.add(Component.text("§7§o- " + playerName));
                }
                itemMeta.lore(loreTeams);
                item.setItemMeta(itemMeta);

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
        if (e.getView().title().equals(Component.text("§3§lBINGO")) && e.getCurrentItem() != null) {
            Bukkit.getLogger().log(Level.INFO, "bingogui2");
            Player player = (Player) e.getWhoClicked();
            if (Objects.requireNonNull(Team.getTeam(player)).getColor() == Team.Color.SPECTATOR && e.getCurrentItem() != null) {
                Material concrete = e.getCurrentItem().getType();
                if (Concrete.isConcrete(concrete)) {
                    spectatorMemory.put(player, concrete);
                }
            } else {
                String itemName = Bingo.getPlainSerializer().serialize(e.getCurrentItem().displayName());
                itemName = itemName.substring(1, itemName.length() - 1);
                ChallengesListener.verifChallenge(player, itemName);
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
        if (e.getView().title().equals(Component.text("§3§lBINGO"))) {
            playersInGui.add((Player) e.getPlayer());
        }
    }

    @EventHandler
    private void onCloseGui(InventoryCloseEvent e) {
        if (e.getView().title().equals(Component.text("§3§lBINGO"))) {
            playersInGui.remove((Player) e.getPlayer());
        }
    }
}
