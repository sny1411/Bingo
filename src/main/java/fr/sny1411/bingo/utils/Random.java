package fr.sny1411.bingo.utils;

public interface Random {
    static int choice(int min, int max) {
        return (int) (Math.random() * ((max - min) + 1)) + min;
    }
}
