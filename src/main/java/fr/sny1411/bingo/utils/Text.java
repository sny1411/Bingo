package fr.sny1411.bingo.utils;

import java.util.ArrayList;
import java.util.List;

public interface Text {
    static List<String> divideString(String str) {
        List<String> dividedStrings = new ArrayList<>();

        String[] words = str.split(" ");
        StringBuilder currentString = new StringBuilder();

        for (String word : words) {
            if (currentString.length() + word.length() <= 30) {
                currentString.append(word).append(" ");
            } else {
                dividedStrings.add(currentString.toString().trim());
                currentString = new StringBuilder(word).append(" ");
            }
        }

        if (currentString.length() > 0) {
            dividedStrings.add(currentString.toString().trim());
        }

        return dividedStrings;
    }
}
