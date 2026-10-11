package fr.sny1411.bingo.commands;

import fr.sny1411.bingo.Bingo;
import fr.sny1411.bingo.Game;
import fr.sny1411.bingo.utils.*;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class Start implements CommandExecutor {
    private final Bingo bingo;

    public Start(Bingo bingo) {
        this.bingo = bingo;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String str, @NotNull String[] args) {
        if (sender instanceof Player && (Bingo.getGame().getEtat() == Game.Etat.SETUP)) {
            if (Bingo.getGame().getSettings().verifSettingsToHigh()) {
                sender.sendMessage(gridIncompleteMessage());
                return false;
            }
            Bingo.getGame().createGrids();
            if (isTeamComplete()) {
                Bingo.getGame().setEtat(Game.Etat.STARTING);
                Countdown.start(bingo, this::startGame);
            } else {
                sender.sendMessage(Text.warning(Component.translatable("bingo.start.players_without_team")));
            }
        }
        return false;
    }

    private void startGame() {
        Countdown.showTitle(Title.title(Component.text("\uE005"), Component.text(""), Countdown.TITLE_TIMES));
        Spawn.remove();
        Game game = Bingo.getGame();
        game.setEtat(Game.Etat.INGAME);
        Environment.clearPlayers();
        Environment.setGamerulesInGame();

        game.getTimer().start(bingo);
        ScoreBoard.createScoreBoard(bingo);
        game.initScores();

        for (Player player : Bingo.getGame().getTeams().get(Team.Color.SPECTATOR).getOnlinePlayers()) {
            player.setGameMode(GameMode.SPECTATOR);
        }

        if (Bingo.getGame().isDefiBonus()) {
            game.initBonusEvents();
        }

        Bukkit.getScheduler().runTaskLater(bingo, () -> game.setPlayersDamage(true), 30 * 20L);
    }

    private static Component gridIncompleteMessage() {
        return Text.warning(Component.translatable("bingo.start.grid_incomplete", Component.text(Bingo.getGame().getSettings().getMaxTotal()), Component.text(Grid.NB_CHALLENGES)));
    }

    private static boolean isTeamComplete() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (Bingo.getGame().getTeams().getTeam(player) == null) {
                player.sendMessage(Text.warning(Component.translatable("bingo.start.join_a_team")));
                return false;
            }
        }
        return true;
    }
}
