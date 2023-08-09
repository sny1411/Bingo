package fr.sny1411.bingo.commands;

import fr.sny1411.bingo.Bingo;
import fr.sny1411.bingo.Game;
import fr.sny1411.bingo.utils.Score;
import fr.sny1411.bingo.utils.Team;
import fr.sny1411.bingo.utils.Text;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Result implements CommandExecutor {
    private static final List<String> iconesClassement = new ArrayList<>(Arrays.asList("\uE002","\uE003","\uE004","➃","➄","➅"));
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (Bingo.getGame().getEtat() == Game.Etat.ENDGAME) {
            List<Score> classementPts = new ArrayList<>(Score.getTeamsScore().values());
            List<Score> classementBingo = new ArrayList<>(Score.getTeamsScore().values());

            sortClassementPts(classementPts);
            sortClassementBingo(classementBingo);

            showClassment(classementPts, classementBingo);
        }
        return false;
    }

    private static void showClassment(List<Score> classementPts, List<Score> classementBingo) {
        Text.broadcastMessage("§7==========[§eClassement§7]==========");
        if (Bingo.getGame().getModeVictoire() == Game.ModeVictoire.DEFIS) {
            showClassementPts(classementPts);
        } else {
            showClassementBingo(classementBingo);
        }
        Text.broadcastMessage("§7==============================");
    }

    private static void showClassementPts(List<Score> classementPts) {
        int i = 0;
        for (Score scoreTeam : classementPts) {
            Team team = scoreTeam.getTeam();
            Team.Color colorTeam = team.getColor();
            Text.broadcastMessage(" " + iconesClassement.get(i) + " §l" + colorTeam.getPrefixe() + colorTeam.getNom() + " §f " + scoreTeam.getScore() + " point(s) (§a" + scoreTeam.getNbEasy() + " §f| §6" + scoreTeam.getNbMedium() + " §f| §c" + scoreTeam.getNbHard() + " §f| §8" + scoreTeam.getNbExtreme() + "§f)");
            i++;
        }
    }

    private static void showClassementBingo(List<Score> classementBingo) {
        int i = 0;
        for (Score scoreTeam : classementBingo) {
            Team team = scoreTeam.getTeam();
            Team.Color colorTeam = team.getColor();
            Text.broadcastMessage(" " + iconesClassement.get(i) + " §l" + colorTeam.getPrefixe() + colorTeam.getNom() + " §f " + scoreTeam.getNbBingo() + " bingo(s) (" + scoreTeam.getScore() + " point(s))");
            i++;
        }
    }

    private static void sortClassementPts(List<Score> classementPts) {
        classementPts.sort((o1, o2) -> {
            boolean o1Less = o1.getScore() > o2.getScore();
            if (o1Less) {
                return -1;
            } else if (o1.getScore() == o2.getScore()) {
                return 0;
            } else {
                return 1;
            }
        });
    }

    private static void sortClassementBingo(List<Score> classementBingo) {
        classementBingo.sort((o1, o2) -> {
            boolean o1Less = o1.getNbBingo() > o2.getNbBingo();
            if (o1Less) {
                return -1;
            } else if (o1.getNbBingo() == o2.getNbBingo()) {
                return Integer.compare(o1.getScore(), o2.getScore());
            } else {
                return 1;
            }
        });
    }
}
