package fr.sny1411.bingo.listener.gui;

import fr.sny1411.bingo.Bingo;
import fr.sny1411.bingo.Game;
import fr.sny1411.bingo.utils.Items;
import fr.sny1411.bingo.utils.Team;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class TeamsGui implements Listener {
    private static final Inventory gui = Bukkit.createInventory(null, 27, Component.text("§3§lSélection des équipes"));
    private static final Set<Player> playersInGui = new HashSet<>();

    public static void openGui(Player player) {
        gui.clear();
        Iterator<Team> iteratorTeam = Team.getTeams().values().iterator();
        Bukkit.getConsoleSender().sendMessage(Component.text(Team.getTeams().values().toString()));
        int compteurTeam = 0;
        for (int i = 0; i < 27; i++) {
            if (i > 9 && i < 17) {
                if (iteratorTeam.hasNext()) {
                    Team team = iteratorTeam.next();
                    if (team.getColor() == Team.Color.SPECTATOR) {
                        gui.setItem(16, itemTeam(team.getColor()));
                    } else {
                        gui.setItem(10 + compteurTeam, itemTeam(team.getColor()));
                        compteurTeam++;
                    }
                }
            } else {
                gui.setItem(i, Items.getGlassForGui());
            }
        }

        player.openInventory(gui);
    }

    private static ItemStack itemTeam(Team.Color color) {
        ItemStack item = new ItemStack(color.getMaterial());
        ItemMeta itemMeta = item.getItemMeta();

        itemMeta.displayName(Component.text(String.format("%s%s", color.getPrefixe(), color.getNom())));
        Team team = Team.getTeams().get(color);

        List<Component> listLore = new ArrayList<>();
        if (color == Team.Color.SPECTATOR) {
            listLore.add(Component.text("§8>>§7 Clique pour observer la partie !"));
        } else {
            Iterator<Player> iteratorPlayers = team.getPlayers().iterator();
            for (int i = 0; i < Team.getNbPlayerTeams(); i++) {
                if (iteratorPlayers.hasNext()) {
                    listLore.add(Component.text(String.format("§7§o- %s", iteratorPlayers.next().getName())));
                } else {
                    listLore.add(Component.text("§7§o- "));
                }
            }
        }

        itemMeta.lore(listLore);
        item.setItemMeta(itemMeta);
        return item;
    }

    @EventHandler
    private void compassClick(PlayerInteractEvent e) {
        if (Bingo.getGame().getEtat() == Game.Etat.SETUP && e.getMaterial() == Material.COMPASS) {
            openGui(e.getPlayer());
        }
    }

    @EventHandler
    private void clickItemGui(InventoryClickEvent e) {
        if (Bingo.getGame().getEtat() == Game.Etat.SETUP && (e.getClickedInventory() == gui && e.getCurrentItem() != null)) {
            Player player = (Player) e.getWhoClicked();
            switch (e.getCurrentItem().getType()) {
                case ORANGE_BANNER:
                    Team.getTeams().get(Team.Color.ORANGE).addPlayer(player);
                    break;
                case RED_BANNER:
                    Team.getTeams().get(Team.Color.ROUGE).addPlayer(player);
                    break;
                case PURPLE_BANNER:
                    Team.getTeams().get(Team.Color.VIOLET).addPlayer(player);
                    break;
                case PINK_BANNER:
                    Team.getTeams().get(Team.Color.ROSE).addPlayer(player);
                    break;
                case LIME_BANNER:
                    Team.getTeams().get(Team.Color.VERT).addPlayer(player);
                    break;
                case LIGHT_BLUE_BANNER:
                    Team.getTeams().get(Team.Color.BLEU).addPlayer(player);
                    break;
                case ENDER_EYE:
                    Team.getTeams().get(Team.Color.SPECTATOR).addPlayer(player);
                    break;
                default:
                    return;
            }
            updateGui();
            player.playerListName(Component.text(Objects.requireNonNull(Team.getTeam(player)).getColor().getPrefixe() + player.getName()));
            // TODO :REGARDER POUR METTRE COULEUR AU DESSUS DU PSEUDO
        }
    }

    private void updateGui() {
        for (Player player : playersInGui) {
            openGui(player);
        }
    }

    @EventHandler
    private void onOpenGui(InventoryOpenEvent e) {
        playersInGui.add((Player) e.getPlayer());
    }

    @EventHandler
    private void onCloseGui(InventoryCloseEvent e) {
        playersInGui.remove((Player) e.getPlayer());
    }
}
