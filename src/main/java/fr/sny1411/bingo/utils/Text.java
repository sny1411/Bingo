package fr.sny1411.bingo.utils;

import fr.sny1411.bingo.Bingo;
import fr.sny1411.bingo.Game;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public final class Text {
    private Text() {
        throw new IllegalStateException("Utility class");
    }
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

    public static void validMessage(Team team, String challengeName) {
        String message = "§7[§eBINGO§7] §fL'équipe §l" + team.getColor().getPrefixe() + team.getColor().getNom() + "§r a réalisé le défi §e§l" + challengeName;
        if (Bingo.getGame().getModeAffichage() == Game.ModeAffichage.CHILL) {
            broadcastMessage(message);
        } else {
            team.sendMessage(message);
        }
    }

    public static void broadcastMessage(String message) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.sendMessage(Component.text(message));
        }
    }
}
