package fr.sny1411.bingo.listener;

import fr.sny1411.bingo.Bingo;
import fr.sny1411.bingo.Game;
import fr.sny1411.bingo.utils.Spawn;
import fr.sny1411.bingo.utils.Team;
import net.kyori.adventure.text.Component;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerListener implements Listener {

    @EventHandler
    private void onPlayerJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        e.joinMessage(Component.text(String.format("§8[§a+§8]§e %s", player.getName())));
        if (Bingo.getGame().getEtat() == Game.Etat.SETUP) {
            Spawn.teleportPlayer(player);
            Spawn.giveItemsPlayer(player);
        } else if (Bingo.getGame().getEtat() == Game.Etat.INGAME && (!Team.updatePlayerJoinInGame(player))) {
                Team.getTeams().get(Team.Color.SPECTATOR).addPlayer(player);
                player.setGameMode(GameMode.SPECTATOR);
        }
    }

    @EventHandler
    private void onPlayerQuit(PlayerQuitEvent e) {
        e.quitMessage(Component.text(String.format("§8[§c-§8]§e %s", e.getPlayer().getName())));
        if (Bingo.getGame().getEtat() == Game.Etat.SETUP) {
            Team.removeTeam(e.getPlayer());
        }
    }

    @EventHandler
    private void onDamage(EntityDamageEvent e) {
        if (e.getEntity() instanceof Player && !Bingo.getGame().isPlayersDamage()) {
            e.setCancelled(true);
        }
    }
}
