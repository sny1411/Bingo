package fr.sny1411.bingo.listener;

import fr.sny1411.bingo.Bingo;
import fr.sny1411.bingo.Game;
import fr.sny1411.bingo.utils.Spawn;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerListener implements Listener {

    @EventHandler
    private void onPlayerJoin(PlayerJoinEvent e) {
        e.joinMessage(Component.text(String.format("§8[§a+§8]§e %s", e.getPlayer())));
        if (Bingo.getGame().getEtat() == Game.Etat.SETUP) {
            Spawn.teleportPlayer(e.getPlayer());
        }
    }

    @EventHandler
    private void onPlayerQuit(PlayerQuitEvent e) {
        e.quitMessage(Component.text(String.format("§8[§c-§8]§e %s", e.getPlayer())));
    }

    @EventHandler
    private void onDamage(EntityDamageEvent e) {
        if (e.getEntity() instanceof Player && !Bingo.getGame().isPlayersDamage()) {
            e.setCancelled(true);
        }
    }
}
