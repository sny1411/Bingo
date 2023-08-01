package fr.sny1411.bingo.listener.gui;

import fr.sny1411.bingo.Bingo;
import fr.sny1411.bingo.Game;
import fr.sny1411.bingo.listener.ChallengesListener;
import fr.sny1411.bingo.utils.*;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Objects;
import java.util.logging.Level;

public class BingoGui implements Listener {
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
        Grid playerGrid = Grid.getTeamsGrid().get(Team.getTeam(player));
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
                if (Team.getTeam(player) == team) {
                    itemMeta.addEnchant(Enchantment.DURABILITY, 5, true);
                    itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                }
                ArrayList<Component> loreTeams = new ArrayList<>();
                if (Bingo.getGame().getModeAffichage() == Game.ModeAffichage.CHILL) {
                    loreTeams.add(Component.text("§9Défi(s) réalisé(s): §f" + Score.getTeamsScore().get(team).getNbChallenges()));
                } else {
                    loreTeams.add(Component.text("§9Défi(s) réalisé(s): §f§k!!"));
                }
                for (Player playerTeam : team.getPlayers()) {
                    String playerName = Bingo.getPlainSerializer().serialize(playerTeam.displayName());
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
            if (Objects.requireNonNull(Team.getTeam(player)).getColor() == Team.Color.SPECTATOR) {
                // TODO : regarde clic pour changer de team
            } else {
                String itemName = Bingo.getPlainSerializer().serialize(e.getCurrentItem().displayName());
                itemName = itemName.substring(1, itemName.length() - 1);
                ChallengesListener.verifChallenge(player, itemName);
            }
            // TODO : refresh pour tout les joueurs dans le gui
            open(player);
            Bukkit.getLogger().log(Level.INFO, "refresh bingoGui");
            e.setCancelled(true);
        }
    }
}
