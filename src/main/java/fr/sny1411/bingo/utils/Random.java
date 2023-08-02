package fr.sny1411.bingo.utils;

public final class Random {
    private Random() {
        throw new IllegalStateException("Utility class");
    }

    public static int choice(int min, int max) {
        return (int) (Math.random() * ((max - min) + 1)) + min;
    }
}
