package fr.sny1411.bingo.commands.completer;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class TimerCompleter implements TabCompleter {
    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 1) {
            return List.of("set", "add", "speed");
        }
        if (args.length == 2) {
            return switch (args[0].toLowerCase()) {
                case "set" -> List.of("59:59", "1:59:50");
                case "add" -> List.of("10:00", "1:00:00");
                case "speed" -> List.of("1", "10", "60");
                default -> List.of();
            };
        }
        return List.of();
    }
}
