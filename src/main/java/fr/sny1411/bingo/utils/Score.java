package fr.sny1411.bingo.utils;

import fr.sny1411.bingo.Bingo;
import fr.sny1411.bingo.Game;

import java.util.HashMap;

public class Score {
    private static final HashMap<Team, Score> teamsScore = new HashMap<>();

    public static void init() {
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
        return nbEasy * Challenge.Difficult.EASY.getPoints() +
                nbMedium * Challenge.Difficult.MEDIUM.getPoints() +
                nbHard * Challenge.Difficult.HARD.getPoints() +
                nbExtreme * Challenge.Difficult.EXTREME.getPoints();
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
        Challenge[][] challenges = Grid.getTeamsGrid().get(team).getGrid();
        nbBingo = bingoY(challenges) + bingoX(challenges) + bingoDiag(challenges);

        testGameFinish();
    }

    private int bingoY(Challenge[][] grid) {
        int nbBingoY = 0;
        boolean isBingo;
        for (Challenge[] challenges : grid) {
            isBingo = true;
            for (Challenge challenge : challenges) {
                if (Boolean.FALSE.equals(challenge.getValidated())) {
                    isBingo = false;
                    break;
                }
            }
            if (isBingo) {
                nbBingoY++;
            }
        }
        return nbBingoY;
    }

    private int bingoX(Challenge[][] grid) {
        int nbBingoX = 0;
        boolean isBingo;
        for (int x = 0; x < grid[0].length; x++) {
            isBingo = true;
            for (Challenge[] challenges : grid) {
                if (Boolean.FALSE.equals(challenges[x].getValidated())) {
                    isBingo = false;
                    break;
                }
            }
            if (isBingo) {
                nbBingoX++;
            }
        }
        return nbBingoX;
    }

    private int bingoDiag(Challenge[][] grid) {
        boolean isBingo = true;
        int nbBingoXY = 0;
        for (int xy = 0; xy < grid.length; xy++) {
            if (Boolean.FALSE.equals(grid[xy][xy].getValidated())) {
                isBingo = false;
                break;
            }
        }
        if (isBingo) {
            nbBingoXY++;
        }

        for (int xy = 0; xy < grid.length; xy++) {
            isBingo = true;
            if (Boolean.FALSE.equals(grid[4 - xy][xy].getValidated())) {
                isBingo = false;
                break;
            }
        }
        if (isBingo) {
            nbBingoXY++;
        }

        return nbBingoXY;
    }

    private void testGameFinish() {
        Game game = Bingo.getGame();
        if ((game.getModeVictoire() == Game.ModeVictoire.BINGO && nbBingo >= game.getNbreBingoForWin()) ||
                (game.getModeVictoire() == Game.ModeVictoire.DEFIS && nbChallenges == 25) && !team.isGameFinish()) {
            team.setGameFinish(true);
            Text.broadcastMessage("§7[§eBINGO§7] §fL'équipe " + team.getColor().getPrefixe() + team.getColor().getNom() + " §fa fini sa partie");
            Text.broadcastMessage("Elle peut continuer de jouer ou devenir spectatrice");
            team.sendMessage("§7§oUtilisez la commande §e§o/spec §7§opour devenir spectateur");
        }
    }
}
