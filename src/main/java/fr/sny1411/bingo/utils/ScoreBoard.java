package fr.sny1411.bingo.utils;

import fr.sny1411.bingo.Bingo;
import fr.sny1411.bingo.Game;
import fr.sny1411.bingo.i18n.Translations;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class ScoreBoard {
    private ScoreBoard() {
        throw new IllegalStateException("Utility class");
    }
    private static final ScoreboardManager manager = Bukkit.getScoreboardManager();
    private static BukkitTask task;

    public static void createScoreBoard(Bingo bingo) {
        stop();
        task = Bukkit.getScheduler().runTaskTimer(bingo, () -> {
            for (Player player : Bukkit.getOnlinePlayers()) {
                Scoreboard board = manager.getNewScoreboard();
                Objective objective = board.registerNewObjective("scoreBoardInfo", Criteria.DUMMY, Component.text("\uE005"));
                objective.setDisplaySlot(DisplaySlot.SIDEBAR);
                List<Component> lines = lines(player);
                for (int i = 0; i < lines.size(); i++) {
                    // Each line is an entry named after its position, and shows its text in the player's language
                    Score line = objective.getScore("line" + i);
                    line.customName(Translations.render(lines.get(i), player));
                    line.setScore(lines.size() - 1 - i);
                }
                player.setScoreboard(board);
            }
        }, 0L, 20L);
    }

    private static List<Component> lines(Player player) {
        Game game = Bingo.getGame();
        Component separator = Component.textOfChildren(
                Component.text("»", NamedTextColor.GOLD, TextDecoration.BOLD),
                Component.text("                   ", NamedTextColor.WHITE, TextDecoration.BOLD, TextDecoration.STRIKETHROUGH));
        List<Component> lines = new ArrayList<>();
        lines.add(separator);
        lines.add(Component.empty());
        lines.add(line("bingo.scoreboard.teams", Component.text(game.getSettings().getNbTeams())));
        Team team = Objects.requireNonNull(game.getTeams().getTeam(player));
        if (team.getColor() != Team.Color.SPECTATOR) {
            fr.sny1411.bingo.utils.Score score = game.getTeamsScore().get(team);
            if (game.getModeVictoire() == Game.ModeVictoire.BINGO) {
                lines.add(line("bingo.scoreboard.bingos", Component.text(score.getNbBingo())));
            } else {
                lines.add(line("bingo.scoreboard.challenges", Component.text(score.getNbChallenges())));
            }
        }
        lines.add(line("bingo.scoreboard.mode", game.getModeJeu().label()));
        lines.add(Component.empty());
        Timer timer = game.getTimer();
        String duration = timer.getHours() == 0
                ? String.format("%02d:%02d", timer.getMinutes(), timer.getSeconds())
                : String.format("%02d:%02d:%02d", timer.getHours(), timer.getMinutes(), timer.getSeconds());
        lines.add(line("bingo.scoreboard.duration", Component.text(duration)));
        lines.add(Component.empty());
        lines.add(separator);
        return lines;
    }

    private static Component line(String key, Component value) {
        return Component.text("  ").append(Component.translatable(key, value.color(NamedTextColor.YELLOW)));
    }

    public static void stop() {
        if (task != null) {
            task.cancel();
            task = null;
        }
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.setScoreboard(manager.getMainScoreboard());
        }
    }
}
