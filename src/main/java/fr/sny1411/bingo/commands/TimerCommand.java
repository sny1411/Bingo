package fr.sny1411.bingo.commands;

import fr.sny1411.bingo.Bingo;
import fr.sny1411.bingo.Game;
import fr.sny1411.bingo.utils.Text;
import fr.sny1411.bingo.utils.Timer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class TimerCommand implements CommandExecutor {
    private static final int MAX_SPEED = 600;

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (Bingo.getGame().getEtat() != Game.Etat.INGAME) {
            sender.sendMessage(Text.warning(Component.translatable("bingo.command.no_game")));
            return false;
        }
        if (args.length != 2) {
            sender.sendMessage(usage());
            return false;
        }

        Timer timer = Bingo.getGame().getTimer();
        switch (args[0].toLowerCase()) {
            case "set", "add" -> {
                int seconds = parseTime(args[1]);
                if (seconds < 0) {
                    sender.sendMessage(usage());
                    return false;
                }
                int elapsed = args[0].equalsIgnoreCase("set") ? seconds : timer.getElapsedSeconds() + seconds;
                if (elapsed < timer.getElapsedSeconds()) {
                    sender.sendMessage(Text.warning(Component.translatable("bingo.timer.cannot_go_back")));
                    return false;
                }
                timer.setElapsedSeconds(elapsed);
                sender.sendMessage(Text.info(Component.translatable("bingo.timer.elapsed", Component.text(formatTime(elapsed)), Component.text(formatTime(timer.getDurationSeconds())))));
            }
            case "speed" -> {
                int speed = parseSpeed(args[1]);
                if (speed < 1) {
                    sender.sendMessage(usage());
                    return false;
                }
                timer.setSpeed(speed);
                sender.sendMessage(Text.info(Component.translatable("bingo.timer.speed", Component.text(speed))));
            }
            default -> sender.sendMessage(usage());
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

    private static Component usage() {
        return Text.warning(Component.translatable("bingo.timer.usage", Component.text("/timer set <[hh:]mm:ss> | /timer add <[hh:]mm:ss> | /timer speed <1-" + MAX_SPEED + ">")));
    }

    private static String formatTime(int seconds) {
        return String.format("%d:%02d:%02d", seconds / 3600, seconds / 60 % 60, seconds % 60);
    }
}
