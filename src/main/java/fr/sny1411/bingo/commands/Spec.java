package fr.sny1411.bingo.commands;

import fr.sny1411.bingo.Bingo;
import fr.sny1411.bingo.Game;
import fr.sny1411.bingo.utils.Team;
import net.kyori.adventure.text.Component;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class Spec implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player &&  Bingo.getGame().getEtat() == Game.Etat.INGAME) {
            Player player = (Player) sender;
            if (Objects.requireNonNull(Team.getTeam(player)).isGameFinish()) {
                player.setGameMode(GameMode.SPECTATOR);
                Team.removeTeam(player);
                Team.getTeams().get(Team.Color.SPECTATOR).addPlayer(player);
            } else {
                player.sendMessage(Component.text("§8[§c⚠§8] §cVous n'avez pas terminé votre partie !"));
            }
        }
        return false;
    }
}
