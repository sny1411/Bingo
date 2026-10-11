package fr.sny1411.bingo.utils;

import fr.sny1411.bingo.Bingo;
import fr.sny1411.bingo.Game;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public final class Text {
    private Text() {
        throw new IllegalStateException("Utility class");
    }
    // Splits a text into lines of about 30 characters, for item lores
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

    public static void validMessage(Team team, Component challengeName) {
        Component message = info(Component.translatable("bingo.challenge.validated",
                team.getColor().displayName().decorate(TextDecoration.BOLD),
                challengeName.color(NamedTextColor.YELLOW).decorate(TextDecoration.BOLD)));
        if (Bingo.getGame().getModeAffichage() == Game.ModeAffichage.CHILL) {
            Bukkit.broadcast(message);
        } else {
            team.sendMessage(message);
        }
    }

    // [BINGO] before a game message
    public static Component info(Component message) {
        return Component.textOfChildren(
                Component.text("[", NamedTextColor.GRAY),
                Component.text("BINGO", NamedTextColor.YELLOW),
                Component.text("] ", NamedTextColor.GRAY),
                message.colorIfAbsent(NamedTextColor.WHITE));
    }

    // [⚠] before a message telling a player what went wrong
    public static Component warning(Component message) {
        return Component.textOfChildren(
                Component.text("[", NamedTextColor.DARK_GRAY),
                Component.text("⚠", NamedTextColor.RED),
                Component.text("] ", NamedTextColor.DARK_GRAY),
                message.colorIfAbsent(NamedTextColor.WHITE));
    }

    public static void broadcastMessage(String message) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.sendMessage(Component.text(message));
        }
    }
}
