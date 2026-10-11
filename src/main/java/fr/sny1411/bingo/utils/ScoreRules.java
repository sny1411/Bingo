package fr.sny1411.bingo.utils;

/**
 * Bingo and points rules, independent of Bukkit so that they can be unit tested.
 */
public final class ScoreRules {
    private ScoreRules() {
        throw new IllegalStateException("Utility class");
    }

    public static int countBingos(boolean[][] validated) {
        int size = validated.length;
        int nbBingo = 0;
        for (int i = 0; i < size; i++) {
            boolean row = true;
            boolean column = true;
            for (int j = 0; j < size; j++) {
                row &= validated[i][j];
                column &= validated[j][i];
            }
            nbBingo += (row ? 1 : 0) + (column ? 1 : 0);
        }
        boolean diagonal = true;
        boolean antiDiagonal = true;
        for (int i = 0; i < size; i++) {
            diagonal &= validated[i][i];
            antiDiagonal &= validated[size - 1 - i][i];
        }
        return nbBingo + (diagonal ? 1 : 0) + (antiDiagonal ? 1 : 0);
    }

    public static int points(int nbEasy, int nbMedium, int nbHard, int nbExtreme) {
        return nbEasy * Challenge.Difficult.EASY.getPoints() +
                nbMedium * Challenge.Difficult.MEDIUM.getPoints() +
                nbHard * Challenge.Difficult.HARD.getPoints() +
                nbExtreme * Challenge.Difficult.EXTREME.getPoints();
    }
}
