package fr.sny1411.bingo.listener;

import fr.sny1411.bingo.Bingo;
import fr.sny1411.bingo.Game;
import fr.sny1411.bingo.utils.Team;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerMoveEvent;

public class SetupListener implements Listener {
    private final Game game = Bingo.getGame();

    @EventHandler
    private void onInventoryMove(InventoryClickEvent e) {
        if (game.getEtat() == Game.Etat.SETUP) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    private void onPlayerDrop(PlayerDropItemEvent e) {
        if (game.getEtat() == Game.Etat.SETUP) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    private void onBlockPlace(BlockPlaceEvent e) {
        if (game.getEtat() != Game.Etat.INGAME) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    private void onBlockBreak(BlockBreakEvent e) {
        if (game.getEtat() != Game.Etat.INGAME) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    private void onPlayerMove(PlayerMoveEvent e) {
        if (game.getEtat() == Game.Etat.SETUP) {
            Player player = e.getPlayer();
            Team team = Team.getTeam(player);
            if (team != null) {
                Location location = e.getPlayer().getLocation();
                location.setY(200);
                Block block = Bukkit.getWorlds().get(0).getBlockAt(location);
                Material type = block.getType();
                if (type == Material.AIR || type == Material.WHITE_STAINED_GLASS) {
                    return;
                }
                switch (team.getColor()) {
                    case BLEU:
                        block.setType(Material.LIGHT_BLUE_STAINED_GLASS);
                        break;
                    case ROSE:
                        block.setType(Material.PINK_STAINED_GLASS);
                        break;
                    case VERT:
                        block.setType(Material.LIME_STAINED_GLASS);
                        break;
                    case ROUGE:
                        block.setType(Material.RED_STAINED_GLASS);
                        break;
                    case ORANGE:
                        block.setType(Material.ORANGE_STAINED_GLASS);
                        break;
                    case VIOLET:
                        block.setType(Material.PURPLE_STAINED_GLASS);
                        break;
                }
            }
        }
    }
}
