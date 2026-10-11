package fr.sny1411.bingo.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Grid {
    public static final int NB_CHALLENGES = 25;

    private Challenge[][] grid;

    private Grid() {
        grid = new Challenge[5][5];
        fillGrid();
    }

    private Grid(Grid grid) {
        Challenge[][] gridCopy = new Challenge[5][5];
        for (int x = 0; x < grid.getGrid().length; x++) {
            for (int y = 0; y < grid.getGrid()[0].length; y++) {
                gridCopy[x][y] = grid.getGrid()[x][y].clone();
            }
        }

        this.grid = gridCopy;
    }

    private List<Challenge> getChallenges() {
        List<Challenge> challenges = new ArrayList<>();
        int i = 0;
        int nbEasy = 0;
        int nbMedium = 0;
        int nbHard = 0;
        int nbExtreme = 0;
        while (challenges.size() != NB_CHALLENGES) {
            Challenge challenge = Challenge.getChallenges().get(i);
            switch (challenge.getDifficult()) {
                case EASY:
                    if (nbEasy < Challenge.getMaxEasy()) {
                        nbEasy++;
                        challenges.add(challenge);
                    }
                    break;
                case MEDIUM:
                    if (nbMedium < Challenge.getMaxMedium()) {
                        nbMedium++;
                        challenges.add(challenge);
                    }
                    break;
                case HARD:
                    if (nbHard < Challenge.getMaxHard()) {
                        nbHard++;
                        challenges.add(challenge);
                    }
                    break;
                case EXTREME:
                    if (nbExtreme < Challenge.getMaxExtreme()) {
                        nbExtreme++;
                        challenges.add(challenge);
                    }
                    break;
            }
            i++;
        }
        return challenges;
    }

    private void fillGrid() {
        List<Challenge> challenges = getChallenges();
        Collections.shuffle(challenges);
        int i = 0;
        for (int x = 0; x < 5; x++) {
            for (int y = 0; y < 5; y++) {
                grid[y][x] = challenges.get(i);
                i++;
            }
        }
    }

    public static Grid random() {
        return new Grid();
    }

    public Grid copy() {
        return new Grid(this);
    }

    public Challenge getChallenge(ChallengeId challengeId) {
        for (Challenge[] row : grid) {
            for (Challenge challenge : row) {
                if (challenge.getId() == challengeId) {
                    return challenge;
                }
            }
        }
        return null;
    }

    public Challenge[][] getGrid() {
        return grid;
    }

}
