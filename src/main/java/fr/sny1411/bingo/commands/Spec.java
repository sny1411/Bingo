package fr.sny1411.bingo.commands;

import fr.sny1411.bingo.Bingo;
import fr.sny1411.bingo.Game;
import fr.sny1411.bingo.utils.Team;
import fr.sny1411.bingo.utils.Text;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
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
            if (Objects.requireNonNull(Bingo.getGame().getTeams().getTeam(player)).isGameFinish()) {
                player.setGameMode(GameMode.SPECTATOR);
                Bingo.getGame().getTeams().join(player, Team.Color.SPECTATOR);
            } else {
                player.sendMessage(Text.warning(Component.translatable("bingo.spec.not_finished", NamedTextColor.RED)));
            }
        }
        return false;
    }
}
