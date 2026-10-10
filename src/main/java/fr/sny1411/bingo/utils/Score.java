package fr.sny1411.bingo.utils;

import fr.sny1411.bingo.Bingo;
import fr.sny1411.bingo.Game;

import java.util.HashMap;

public class Score {
    private static final HashMap<Team, Score> teamsScore = new HashMap<>();

    public static void init() {
        teamsScore.clear();
        for (Team team : Team.getTeams().values()) {
            if (team.getColor() != Team.Color.SPECTATOR) {
                teamsScore.put(team, new Score(team));
            }
        }
    }

    public static HashMap<Team, Score> getTeamsScore() {
        return teamsScore;
    }
    private final Team team;

    private int nbEasy;
    private int nbMedium;
    private int nbHard;
    private int nbExtreme;
    private int nbChallenges;
    private int nbBingo;

    public Score(Team team) {
        nbEasy = 0;
        nbMedium = 0;
        nbHard = 0;
        nbExtreme = 0;
        nbBingo = 0;
        nbChallenges = 0;

        this.team = team;
    }

    public int getScore() {
        return ScoreRules.points(nbEasy, nbMedium, nbHard, nbExtreme);
    }

    public int getNbEasy() {
        return nbEasy;
    }

    public int getNbMedium() {
        return nbMedium;
    }

    public int getNbHard() {
        return nbHard;
    }

    public int getNbExtreme() {
        return nbExtreme;
    }

    public int getNbBingo() {
        return nbBingo;
    }

    public int getNbChallenges() {
        return nbChallenges;
    }

    public Team getTeam() {
        return team;
    }

    public void addChallenge(Challenge challenge) {
        switch (challenge.getDifficult()) {
            case EASY:
                nbEasy++;
                break;
            case MEDIUM:
                nbMedium++;
                break;
            case HARD:
                nbHard++;
                break;
            case EXTREME:
                nbExtreme++;
                break;
        }
        nbChallenges++;
        updateNbBingo();

        testGameFinish();
    }

    public void removeChallenge(Challenge challenge) {
        switch (challenge.getDifficult()) {
            case EASY:
                nbEasy--;
                break;
            case MEDIUM:
                nbMedium--;
                break;
            case HARD:
                nbHard--;
                break;
            case EXTREME:
                nbExtreme--;
                break;
        }
        nbChallenges--;
        updateNbBingo();
    }

    private void updateNbBingo() {
        Challenge[][] grid = Grid.getTeamsGrid().get(team).getGrid();
        boolean[][] validated = new boolean[grid.length][grid.length];
        for (int y = 0; y < grid.length; y++) {
            for (int x = 0; x < grid.length; x++) {
                validated[y][x] = !Boolean.FALSE.equals(grid[y][x].getValidated());
            }
        }
        nbBingo = ScoreRules.countBingos(validated);
    }

    private void testGameFinish() {
        Game game = Bingo.getGame();
        if (game.getEtat() == Game.Etat.INGAME && ((game.getModeVictoire() == Game.ModeVictoire.BINGO && nbBingo >= game.getNbreBingoForWin()) ||
                (game.getModeVictoire() == Game.ModeVictoire.DEFIS && nbChallenges == 25)) && !team.isGameFinish()) {
            team.setGameFinish(true);
            Text.broadcastMessage("§7[§eBINGO§7] §fL'équipe " + team.getColor().getPrefixe() + team.getColor().getNom() + " §fa fini sa partie");
            Text.broadcastMessage("Elle peut continuer de jouer ou devenir spectatrice");
            team.sendMessage("§7§oUtilisez la commande §e§o/spec §7§opour devenir spectateur");
        }
    }
}
