package fr.sny1411.bingo.utils;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ScoreRulesTest {
    private static final int SIZE = 5;

    private static boolean[][] grid() {
        return new boolean[SIZE][SIZE];
    }

    @Test
    void emptyGridHasNoBingo() {
        assertEquals(0, ScoreRules.countBingos(grid()));
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2, 3, 4})
    void rowIsABingo(int row) {
        boolean[][] validated = grid();
        for (int i = 0; i < SIZE; i++) {
            validated[row][i] = true;
        }
        assertEquals(1, ScoreRules.countBingos(validated));
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2, 3, 4})
    void columnIsABingo(int column) {
        boolean[][] validated = grid();
        for (int i = 0; i < SIZE; i++) {
            validated[i][column] = true;
        }
        assertEquals(1, ScoreRules.countBingos(validated));
    }

    @Test
    void diagonalIsABingo() {
        boolean[][] validated = grid();
        for (int i = 0; i < SIZE; i++) {
            validated[i][i] = true;
        }
        assertEquals(1, ScoreRules.countBingos(validated));
    }

    @Test
    void antiDiagonalIsABingo() {
        boolean[][] validated = grid();
        for (int i = 0; i < SIZE; i++) {
            validated[SIZE - 1 - i][i] = true;
        }
        assertEquals(1, ScoreRules.countBingos(validated));
    }

    @Test
    void incompleteRowIsNotABingo() {
        boolean[][] validated = grid();
        for (int i = 0; i < SIZE - 1; i++) {
            validated[2][i] = true;
        }
        assertEquals(0, ScoreRules.countBingos(validated));
    }

    @Test
    void crossingRowAndColumnAreTwoBingos() {
        boolean[][] validated = grid();
        for (int i = 0; i < SIZE; i++) {
            validated[2][i] = true;
            validated[i][2] = true;
        }
        assertEquals(2, ScoreRules.countBingos(validated));
    }

    @Test
    void fullGridHasTwelveBingos() {
        boolean[][] validated = grid();
        for (boolean[] row : validated) {
            Arrays.fill(row, true);
        }
        assertEquals(12, ScoreRules.countBingos(validated));
    }

    @Test
    void eachDifficultyHasItsPoints() {
        assertEquals(1, ScoreRules.points(1, 0, 0, 0));
        assertEquals(3, ScoreRules.points(0, 1, 0, 0));
        assertEquals(9, ScoreRules.points(0, 0, 1, 0));
        assertEquals(27, ScoreRules.points(0, 0, 0, 1));
    }

    @Test
    void pointsAddUp() {
        assertEquals(0, ScoreRules.points(0, 0, 0, 0));
        assertEquals(2 + 3 * 3 + 9 + 2 * 27, ScoreRules.points(2, 3, 1, 2));
    }
}
