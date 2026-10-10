package fr.sny1411.bingo.commands;

import fr.sny1411.bingo.Bingo;
import fr.sny1411.bingo.Game;
import fr.sny1411.bingo.utils.Timer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class TimerCommand implements CommandExecutor {
    private static final int MAX_SPEED = 600;
    private static final String USAGE = "Utilisation : /timer set <[hh:]mm:ss> | /timer add <[hh:]mm:ss> | /timer speed <1-" + MAX_SPEED + ">";

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (Bingo.getGame().getEtat() != Game.Etat.INGAME) {
            sender.sendMessage(warning("Aucune partie en cours"));
            return false;
        }
        if (args.length != 2) {
            sender.sendMessage(warning(USAGE));
            return false;
        }

        Timer timer = Bingo.getGame().getTimer();
        switch (args[0].toLowerCase()) {
            case "set", "add" -> {
                int seconds = parseTime(args[1]);
                if (seconds < 0) {
                    sender.sendMessage(warning(USAGE));
                    return false;
                }
                int elapsed = args[0].equalsIgnoreCase("set") ? seconds : timer.getElapsedSeconds() + seconds;
                if (elapsed < timer.getElapsedSeconds()) {
                    sender.sendMessage(warning("Le chrono ne peut pas revenir en arrière"));
                    return false;
                }
                timer.setElapsedSeconds(elapsed);
                sender.sendMessage(info("Chrono : " + formatTime(elapsed) + " / " + formatTime(timer.getDurationSeconds())));
            }
            case "speed" -> {
                int speed = parseSpeed(args[1]);
                if (speed < 1) {
                    sender.sendMessage(warning(USAGE));
                    return false;
                }
                timer.setSpeed(speed);
                sender.sendMessage(info("Vitesse du chrono : ×" + speed));
            }
            default -> sender.sendMessage(warning(USAGE));
        }
        return false;
    }

    // [hh:]mm:ss, or -1 if the format is wrong
    static int parseTime(String time) {
        String[] parts = time.split(":", -1);
        if (parts.length < 2 || parts.length > 3) {
            return -1;
        }
        int seconds = 0;
        for (int i = 0; i < parts.length; i++) {
            if (!parts[i].matches("\\d{1,2}")) {
                return -1;
            }
            int value = Integer.parseInt(parts[i]);
            if (i > 0 && value >= 60) {
                return -1;
            }
            seconds = seconds * 60 + value;
        }
        return seconds;
    }

    private static int parseSpeed(String speed) {
        if (!speed.matches("\\d{1,4}")) {
            return -1;
        }
        int value = Integer.parseInt(speed);
        return value <= MAX_SPEED ? value : -1;
    }

    private static String formatTime(int seconds) {
        return String.format("%d:%02d:%02d", seconds / 3600, seconds / 60 % 60, seconds % 60);
    }

    private static Component info(String message) {
        return Component.textOfChildren(
                Component.text("[", NamedTextColor.GRAY),
                Component.text("BINGO", NamedTextColor.YELLOW),
                Component.text("] ", NamedTextColor.GRAY),
                Component.text(message, NamedTextColor.WHITE));
    }

    private static Component warning(String message) {
        return Component.textOfChildren(
                Component.text("[", NamedTextColor.DARK_GRAY),
                Component.text("⚠", NamedTextColor.RED),
                Component.text("] ", NamedTextColor.DARK_GRAY),
                Component.text(message, NamedTextColor.WHITE));
    }
}
