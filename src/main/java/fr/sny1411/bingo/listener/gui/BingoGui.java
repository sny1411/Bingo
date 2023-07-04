package fr.sny1411.bingo.listener.gui;

import fr.sny1411.bingo.utils.Grid;
import fr.sny1411.bingo.utils.Items;
import fr.sny1411.bingo.utils.Team;
import net.kyori.adventure.text.Component;
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

import java.util.Objects;

public class BingoGui implements Listener {
    private static final Inventory gui = Bukkit.createInventory(null, 45, Component.text("§3§lBINGO"));

    public static Inventory getGui() {
        return gui;
    }

    public static void open(Player player) {
        gui.clear();
        for (int i = 0; i < 45; i++) {
            gui.setItem(i, Items.getGlassForGui());
        }

        placeGrid(player);
        placeTeams(player);

        player.openInventory(gui);
    }

    private static void placeGrid(Player player) {
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
            gui.setItem(i, playerGrid.getGrid()[(nbItems / 5)][nbItems % 5].getItem());
            nbItems++;
            i++;
        }
    }

    private static void placeTeams(Player player) {
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
        if (e.getClickedInventory() == gui) {
            Player player = (Player) e.getWhoClicked();
            if (Objects.requireNonNull(Team.getTeam(player)).getColor() == Team.Color.SPECTATOR) {
                // TODO : regarde clic pour changer de team
            } else {
                // TODO : regarde clic dans la grille pour valider
            }
        }
    }
}
