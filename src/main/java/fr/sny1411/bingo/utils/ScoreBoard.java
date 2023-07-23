package fr.sny1411.bingo.utils;

import fr.sny1411.bingo.Bingo;
import fr.sny1411.bingo.Game;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;
import org.bukkit.scoreboard.Score;

import java.util.Objects;

public abstract class ScoreBoard {
    private static final ScoreboardManager manager = Bukkit.getScoreboardManager();

    public static void createScoreBoard(Bingo bingo) {
        Bukkit.getScheduler().runTaskTimer(bingo, () -> {
            for (Player player : Bukkit.getOnlinePlayers()) {
                Scoreboard board = manager.getNewScoreboard();
                Objective objective = board.registerNewObjective("scoreBoardInfo", Criteria.DUMMY, Component.text("\uE005"));
                objective.setDisplaySlot(DisplaySlot.SIDEBAR);
                Score score4 = objective.getScore("§6§l»§f§l§m                   ");
                score4.setScore(8);
                Score score5 = objective.getScore("  ");
                score5.setScore(7);
                Score score = objective.getScore("  Equipes §7: §e" + Team.getNbTeams());
                score.setScore(6);
                if (Objects.requireNonNull(Team.getTeam(player)).getColor() != Team.Color.SPECTATOR) {
                    Score score1;
                    if (Bingo.getGame().getModeVictoire() == Game.ModeVictoire.BINGO) {
                        score1 = objective.getScore("  Bingos §7: §e" + fr.sny1411.bingo.utils.Score.getTeamsScore().get(Team.getTeam(player)).getNbBingo());
                    } else {
                        score1 = objective.getScore("  Défis §7: §e" + fr.sny1411.bingo.utils.Score.getTeamsScore().get(Team.getTeam(player)).getScore());
                    }
                    score1.setScore(5);
                }
                Score score2 = objective.getScore("  Mode §7: §e" + Bingo.getGame().getModeJeu().getName());
                score2.setScore(4);
                Score score6 = objective.getScore("   ");
                score6.setScore(3);
                String temps = "";
                int seconds = Timer.getSeconds();
                int minutes = Timer.getMinutes();
                if (Timer.getHours() == 0) {
                    if (minutes < 10) {
                        temps += ("0" + minutes);
                    } else {
                        temps += (minutes);
                    }
                    if (seconds < 10) {
                        temps += (":0" + seconds);
                    } else {
                        temps += (":" + seconds);
                    }
                    Score score3 = objective.getScore("  Durée §7: §e" + temps);
                    score3.setScore(2);
                } else {
                    if (minutes < 10) {
                        temps += (":0" + minutes);
                    } else {
                        temps += (":" + minutes);
                    }
                    if (seconds < 10) {
                        temps += (":0" + seconds);
                    } else {
                        temps += (":" + seconds);
                    }
                    Score score3 = objective.getScore("  Durée §7: §e0" + Timer.getHours() + temps);
                    score3.setScore(2);
                }
                Score score7 = objective.getScore(" ");
                score7.setScore(1);
                Score score8 = objective.getScore("§6§l»§f§l§m                   ");
                score8.setScore(0);
                player.setScoreboard(board);
            }

            if (!Timer.isRun()) {
                Thread.currentThread().interrupt();
            }
        }, 0L, 1L); // TODO : REMETTRE CORRECTEMENT
    }
}
