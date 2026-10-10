package fr.sny1411.bingo.listener;

import fr.sny1411.bingo.Bingo;
import fr.sny1411.bingo.Game;
import fr.sny1411.bingo.utils.Spawn;
import fr.sny1411.bingo.utils.Team;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLocaleChangeEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerListener implements Listener {

    @EventHandler
    private void onPlayerJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        e.joinMessage(connectionMessage("+", NamedTextColor.GREEN, player.getName()));
        Game.Etat etat = Bingo.getGame().getEtat();
        if (etat == Game.Etat.SETUP) {
            Spawn.teleportPlayer(player);
            Spawn.giveItemsPlayer(player);
        } else if ((etat == Game.Etat.STARTING || etat == Game.Etat.INGAME) && Bingo.getGame().getTeams().getTeam(player) == null) {
                Bingo.getGame().getTeams().join(player, Team.Color.SPECTATOR);
                player.setGameMode(GameMode.SPECTATOR);
        }
    }

    // The items given during the setup are named in the player's language
    @EventHandler
    private void onLocaleChange(PlayerLocaleChangeEvent e) {
        if (Bingo.getGame().getEtat() == Game.Etat.SETUP) {
            Spawn.giveItemsPlayer(e.getPlayer(), e.locale());
        }
    }

    @EventHandler
    private void onPlayerQuit(PlayerQuitEvent e) {
        e.quitMessage(connectionMessage("-", NamedTextColor.RED, e.getPlayer().getName()));
        if (Bingo.getGame().getEtat() == Game.Etat.SETUP) {
            Bingo.getGame().getTeams().leave(e.getPlayer());
        }
    }

    // [+] Player when they join, [-] Player when they leave
    private static Component connectionMessage(String sign, NamedTextColor color, String playerName) {
        return Component.textOfChildren(
                Component.text("[", NamedTextColor.DARK_GRAY),
                Component.text(sign, color),
                Component.text("] ", NamedTextColor.DARK_GRAY),
                Component.text(playerName, NamedTextColor.YELLOW));
    }

    @EventHandler
    private void onDamage(EntityDamageEvent e) {
        if (e.getEntity() instanceof Player && !Bingo.getGame().isPlayersDamage()) {
            e.setCancelled(true);
        }
    }
}
