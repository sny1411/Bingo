package fr.sny1411.bingo.listener.gui;

import fr.sny1411.bingo.Bingo;
import fr.sny1411.bingo.Game;
import fr.sny1411.bingo.utils.Items;
import fr.sny1411.bingo.utils.Team;
import fr.sny1411.bingo.utils.Text;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class TeamsGui implements Listener {
    private static final Set<Player> playersInGui = new HashSet<>();

    public static void openGui(Player player) {
        Inventory gui = GuiHolder.createInventory(GuiHolder.Type.TEAMS, 27, GuiItems.title("bingo.gui.teams.title", player));
        Iterator<Team> iteratorTeam = Bingo.getGame().getTeams().values().iterator();
        Bukkit.getConsoleSender().sendMessage(Component.text(Bingo.getGame().getTeams().values().toString()));
        int compteurTeam = 0;
        for (int i = 0; i < 27; i++) {
            if (i > 9 && i < 17) {
                if (iteratorTeam.hasNext()) {
                    Team team = iteratorTeam.next();
                    if (team.getColor() == Team.Color.SPECTATOR) {
                        gui.setItem(16, itemTeam(team.getColor(), player));
                    } else {
                        gui.setItem(10 + compteurTeam, itemTeam(team.getColor(), player));
                        compteurTeam++;
                    }
                }
            } else {
                gui.setItem(i, Items.getGlassForGui());
            }
        }

        player.openInventory(gui);
    }

    private static ItemStack itemTeam(Team.Color color, Player player) {
        List<Component> lore = new ArrayList<>();
        Component name;
        if (color == Team.Color.SPECTATOR) {
            name = Component.textOfChildren(Component.text("[SPEC] ", NamedTextColor.DARK_GRAY), color.displayName().decorate(TextDecoration.ITALIC));
            lore.add(Component.translatable("bingo.gui.teams.spectate"));
        } else {
            name = color.displayName();
            Iterator<OfflinePlayer> iteratorPlayers = Bingo.getGame().getTeams().get(color).getPlayers().iterator();
            for (int i = 0; i < Bingo.getGame().getSettings().getNbPlayerTeams(); i++) {
                String playerName = iteratorPlayers.hasNext() ? iteratorPlayers.next().getName() : "";
                lore.add(Component.text("- " + playerName, NamedTextColor.GRAY, TextDecoration.ITALIC));
            }
        }
        return GuiItems.item(color.getMaterialTeamGui(), 1, name, lore, player);
    }

    @EventHandler
    private void compassClick(PlayerInteractEvent e) {
        if (Bingo.getGame().getEtat() == Game.Etat.SETUP && Items.isTeamSelector(e.getItem())) {
            openGui(e.getPlayer());
        }
    }

    @EventHandler
    private void clickItemGui(InventoryClickEvent e) {
        if (Bingo.getGame().getEtat() == Game.Etat.SETUP && GuiHolder.typeOf(e.getView().getTopInventory()) == GuiHolder.Type.TEAMS && e.getCurrentItem() != null) {
            Player player = (Player) e.getWhoClicked();
            Team.Color color = getColor(e.getCurrentItem().getType());
            if (color == null) {
                return;
            }
            if (Bingo.getGame().getTeams().join(player, color)) {
                updateGui();
                player.playerListName(color.playerName(player.getName()));
            } else {
                player.sendMessage(Text.warning(Component.translatable("bingo.gui.teams.full")));
            }
            e.setCancelled(true);
        }
    }

    private static Team.Color getColor(Material materialTeamGui) {
        for (Team.Color color : Team.Color.values()) {
            if (color.getMaterialTeamGui() == materialTeamGui) {
                return color;
            }
        }
        return null;
    }

    private void updateGui() {
        Set<Player> playersGuiCopy = new HashSet<>(playersInGui);
        for (Player player : playersGuiCopy) {
            openGui(player);
        }
    }

    @EventHandler
    private void onOpenGui(InventoryOpenEvent e) {
        if (GuiHolder.typeOf(e.getView().getTopInventory()) == GuiHolder.Type.TEAMS) {
            playersInGui.add((Player) e.getPlayer());
        }
    }

    @EventHandler
    private void onCloseGui(InventoryCloseEvent e) {
        if (GuiHolder.typeOf(e.getView().getTopInventory()) == GuiHolder.Type.TEAMS) {
            playersInGui.remove((Player) e.getPlayer());
        }
    }

    @EventHandler
    private void onPlayerQuit(PlayerQuitEvent e) {
        playersInGui.remove(e.getPlayer());
    }
}
