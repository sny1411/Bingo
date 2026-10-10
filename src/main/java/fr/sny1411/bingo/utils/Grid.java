package fr.sny1411.bingo.utils;

import fr.sny1411.bingo.Settings;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Grid {
    public static final int NB_CHALLENGES = 25;

    private Challenge[][] grid;

    private Grid(List<Challenge> challenges, Settings settings) {
        grid = new Challenge[5][5];
        fillGrid(challenges, settings);
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

    private static List<Challenge> pickChallenges(List<Challenge> allChallenges, Settings settings) {
        List<Challenge> challenges = new ArrayList<>();
        int i = 0;
        int nbEasy = 0;
        int nbMedium = 0;
        int nbHard = 0;
        int nbExtreme = 0;
        while (challenges.size() != NB_CHALLENGES) {
            Challenge challenge = allChallenges.get(i);
            switch (challenge.getDifficult()) {
                case EASY:
                    if (nbEasy < settings.getMaxEasy()) {
                        nbEasy++;
                        challenges.add(challenge);
                    }
                    break;
                case MEDIUM:
                    if (nbMedium < settings.getMaxMedium()) {
                        nbMedium++;
                        challenges.add(challenge);
                    }
                    break;
                case HARD:
                    if (nbHard < settings.getMaxHard()) {
                        nbHard++;
                        challenges.add(challenge);
                    }
                    break;
                case EXTREME:
                    if (nbExtreme < settings.getMaxExtreme()) {
                        nbExtreme++;
                        challenges.add(challenge);
                    }
                    break;
            }
            i++;
        }
        return challenges;
    }

    private void fillGrid(List<Challenge> allChallenges, Settings settings) {
        List<Challenge> challenges = pickChallenges(allChallenges, settings);
        Collections.shuffle(challenges);
        int i = 0;
        for (int x = 0; x < 5; x++) {
            for (int y = 0; y < 5; y++) {
                grid[y][x] = challenges.get(i);
                i++;
            }
        }
    }

    public static Grid random(List<Challenge> challenges, Settings settings) {
        return new Grid(challenges, settings);
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
