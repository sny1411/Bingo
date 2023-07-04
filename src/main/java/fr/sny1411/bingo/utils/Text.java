package fr.sny1411.bingo.utils;

import net.kyori.adventure.text.Component;

import java.util.ArrayList;
import java.util.List;

public interface Text {
    static List<Component> divideString(String str) {
        List<Component> dividedStrings = new ArrayList<>();

        String[] words = str.split(" ");
        StringBuilder currentString = new StringBuilder();

        for (String word : words) {
            if (currentString.length() + word.length() <= 30) {
                currentString.append(word).append(" ");
            } else {
                dividedStrings.add(Component.text("§e§o" + currentString.toString().trim()));
                currentString = new StringBuilder(word).append(" ");
            }
        }

        if (currentString.length() > 0) {
            dividedStrings.add(Component.text("§e§o" + currentString.toString().trim()));
        }

        return dividedStrings;
    }
}
