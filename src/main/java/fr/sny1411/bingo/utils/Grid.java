package fr.sny1411.bingo.utils;

import fr.sny1411.bingo.Settings;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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
                gridCopy[x][y] = grid.getGrid()[x][y].copy();
            }
        }

        this.grid = gridCopy;
    }

    // Draws the challenges of the positions that are not fixed, the difficulty settings being maxima
    private static List<Challenge> pickChallenges(List<Challenge> allChallenges, Settings settings) {
        List<Challenge> challenges = new ArrayList<>();
        int i = 0;
        int nbEasy = 0;
        int nbMedium = 0;
        int nbHard = 0;
        int nbExtreme = 0;
        while (challenges.size() != settings.getNbRandomChallenges()) {
            Challenge challenge = allChallenges.get(i);
            if (settings.getFixedChallenges().containsValue(challenge.getId())) {
                i++;
                continue;
            }
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
        Iterator<Challenge> randomChallenges = challenges.iterator();
        Map<Integer, ChallengeId> fixed = settings.getFixedChallenges();
        for (int i = 0; i < NB_CHALLENGES; i++) {
            ChallengeId fixedId = fixed.get(i + 1);
            grid[i / 5][i % 5] = fixedId == null ? randomChallenges.next() : find(allChallenges, fixedId);
        }
    }

    private static Challenge find(List<Challenge> challenges, ChallengeId challengeId) {
        for (Challenge challenge : challenges) {
            if (challenge.getId() == challengeId) {
                return challenge;
            }
        }
        throw new IllegalArgumentException("Unknown challenge " + challengeId);
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
