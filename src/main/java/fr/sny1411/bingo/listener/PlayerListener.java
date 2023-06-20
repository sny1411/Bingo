package fr.sny1411.bingo.listener;

import fr.sny1411.bingo.Bingo;
import fr.sny1411.bingo.Game;
import fr.sny1411.bingo.utils.Spawn;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerListener implements Listener {

    @EventHandler
    private void onPlayerJoin(PlayerJoinEvent e) {
        if (Bingo.getGame().getEtat() == Game.Etat.SETUP) {
            Spawn.teleportPlayer(e.getPlayer());
        }
    }

    @EventHandler
    private void onDamage(EntityDamageEvent e) {
        if (e.getEntity() instanceof Player && !Bingo.getGame().isPlayersDamage()) {
            e.setCancelled(true);
        }
    }
}
