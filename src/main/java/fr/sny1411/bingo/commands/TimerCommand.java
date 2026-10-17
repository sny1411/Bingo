package fr.sny1411.bingo.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import fr.sny1411.bingo.Bingo;
import fr.sny1411.bingo.Game;
import fr.sny1411.bingo.commands.argument.DurationArgument;
import fr.sny1411.bingo.utils.Text;
import fr.sny1411.bingo.utils.Timer;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;

import java.util.List;
import java.util.function.BiConsumer;

public class TimerCommand {
    private static final int MAX_SPEED = 600;
    private static final List<String> SPEED_EXAMPLES = List.of("1", "10", "60");

    private TimerCommand() {
    }

    public static LiteralCommandNode<CommandSourceStack> node() {
        return Commands.literal("timer")
                .requires(source -> source.getSender().hasPermission("bingo.command.timer"))
                .then(durationBranch("set", TimerCommand::set))
                .then(durationBranch("add", TimerCommand::add))
                .then(Commands.literal("speed")
                        .then(Commands.argument("speed", IntegerArgumentType.integer(1, MAX_SPEED))
                                .suggests((context, builder) -> CommandSuggestions.suggest(builder, SPEED_EXAMPLES))
                                .executes(context -> {
                                    setSpeed(context.getSource().getSender(), IntegerArgumentType.getInteger(context, "speed"));
                                    return Command.SINGLE_SUCCESS;
                                })))
                .build();
    }

    private static LiteralArgumentBuilder<CommandSourceStack> durationBranch(String name, BiConsumer<CommandSender, Integer> action) {
        return Commands.literal(name)
                .then(Commands.argument("time", new DurationArgument())
                        .executes(context -> {
                            action.accept(context.getSource().getSender(), context.getArgument("time", Integer.class));
                            return Command.SINGLE_SUCCESS;
                        }));
    }

    private static void set(CommandSender sender, int seconds) {
        if (isNotInGame(sender)) {
            return;
        }
        Timer timer = Bingo.getGame().getTimer();
        if (seconds < timer.getElapsedSeconds()) {
            sender.sendMessage(Text.warning(Component.translatable("bingo.timer.cannot_go_back")));
            return;
        }
        timer.setElapsedSeconds(seconds);
        sender.sendMessage(Text.info(Component.translatable("bingo.timer.elapsed", Component.text(formatTime(seconds)), Component.text(formatTime(timer.getDurationSeconds())))));
    }

    private static void add(CommandSender sender, int seconds) {
        if (isNotInGame(sender)) {
            return;
        }
        set(sender, Bingo.getGame().getTimer().getElapsedSeconds() + seconds);
    }

    private static void setSpeed(CommandSender sender, int speed) {
        if (isNotInGame(sender)) {
            return;
        }
        Bingo.getGame().getTimer().setSpeed(speed);
        sender.sendMessage(Text.info(Component.translatable("bingo.timer.speed", Component.text(speed))));
    }

    private static boolean isNotInGame(CommandSender sender) {
        if (Bingo.getGame().getEtat() != Game.Etat.INGAME) {
            sender.sendMessage(Text.warning(Component.translatable("bingo.command.no_game")));
            return true;
        }
        return false;
    }

    private static String formatTime(int seconds) {
        return String.format("%d:%02d:%02d", seconds / 3600, seconds / 60 % 60, seconds % 60);
    }
}
