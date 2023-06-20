package fr.sny1411.bingo.listener;

import fr.sny1411.bingo.Bingo;
import fr.sny1411.bingo.Game;
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
            // TODO : quand team fait, color spawn
        }
    }
}
