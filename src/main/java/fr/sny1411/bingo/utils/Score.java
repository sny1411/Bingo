package fr.sny1411.bingo.utils;

import java.util.HashMap;

public class Score {
    private static final HashMap<Team, Score> teamsScore = new HashMap<>();

    public static void init() {
        for (Team team : Team.getTeams().values()) {
            if (team.getColor() != Team.Color.SPECTATOR) {
                teamsScore.put(team, new Score());
            }
        }
    }

    public static HashMap<Team, Score> getTeamsScore() {
        return teamsScore;
    }

    private int nbEasy;
    private int nbMedium;
    private int nbHard;
    private int nbExtreme;
    private int nbChallenges;
    private int nbBingo;

    public Score() {
        nbEasy = 0;
        nbMedium = 0;
        nbHard = 0;
        nbExtreme = 0;
        nbBingo = 0;
        nbChallenges = 0;
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
    }
}
