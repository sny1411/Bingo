package fr.sny1411.bingo.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.tree.LiteralCommandNode;
import fr.sny1411.bingo.Bingo;
import fr.sny1411.bingo.Game;
import fr.sny1411.bingo.Settings;
import fr.sny1411.bingo.commands.argument.ChallengeArgument;
import fr.sny1411.bingo.utils.Challenge;
import fr.sny1411.bingo.utils.ChallengeId;
import fr.sny1411.bingo.utils.Grid;
import fr.sny1411.bingo.utils.Text;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.JoinConfiguration;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

// /bingo grid, to choose challenges of the grid before the game starts
public class GridCommand {
    private GridCommand() {
    }

    public static LiteralCommandNode<CommandSourceStack> node() {
        return Commands.literal("grid")
                .requires(source -> source.getSender().hasPermission("bingo.command.grid"))
                .executes(context -> {
                    list(context.getSource().getSender());
                    return Command.SINGLE_SUCCESS;
                })
                .then(Commands.literal("set")
                        .then(Commands.argument("position", IntegerArgumentType.integer(1, Grid.NB_CHALLENGES))
                                .suggests((context, builder) -> CommandSuggestions.suggestNumbers(builder, positions(false)))
                                .then(Commands.argument("challenge", new ChallengeArgument())
                                        .suggests((context, builder) -> CommandSuggestions.suggest(builder, challenges()))
                                        .executes(context -> {
                                            set(context.getSource().getSender(), IntegerArgumentType.getInteger(context, "position"), context.getArgument("challenge", ChallengeId.class));
                                            return Command.SINGLE_SUCCESS;
                                        }))))
                .then(Commands.literal("clear")
                        .then(Commands.argument("position", IntegerArgumentType.integer(1, Grid.NB_CHALLENGES))
                                .suggests((context, builder) -> CommandSuggestions.suggestNumbers(builder, positions(true)))
                                .executes(context -> {
                                    clear(context.getSource().getSender(), IntegerArgumentType.getInteger(context, "position"));
                                    return Command.SINGLE_SUCCESS;
                                })))
                .then(Commands.literal("random")
                        .executes(context -> {
                            random(context.getSource().getSender());
                            return Command.SINGLE_SUCCESS;
                        }))
                .build();
    }

    private static List<Integer> positions(boolean fixed) {
        Map<Integer, ChallengeId> fixedChallenges = settings().getFixedChallenges();
        return IntStream.rangeClosed(1, Grid.NB_CHALLENGES)
                .filter(position -> fixedChallenges.containsKey(position) == fixed)
                .boxed()
                .toList();
    }

    private static List<String> challenges() {
        return Bingo.getGame().getChallenges().stream()
                .map(challenge -> ChallengeArgument.format(challenge.getId()))
                .toList();
    }

    private static void list(CommandSender sender) {
        Map<Integer, ChallengeId> fixedChallenges = settings().getFixedChallenges();
        if (fixedChallenges.isEmpty()) {
            sender.sendMessage(Text.info(Component.translatable("bingo.grid.list.empty")));
            return;
        }
        List<Component> lines = new ArrayList<>();
        lines.add(Text.info(Component.translatable("bingo.grid.list.title", Component.text(fixedChallenges.size()), Component.text(Grid.NB_CHALLENGES))));
        fixedChallenges.forEach((position, challengeId) -> lines.add(Component.textOfChildren(
                Component.text("  " + position + "  ", NamedTextColor.YELLOW),
                challengeName(challengeId).colorIfAbsent(NamedTextColor.WHITE))));
        lines.add(Component.translatable("bingo.grid.list.others", NamedTextColor.GRAY));
        sender.sendMessage(Component.join(JoinConfiguration.newlines(), lines));
    }

    private static void set(CommandSender sender, int position, ChallengeId challengeId) {
        if (isNotSetup(sender)) {
            return;
        }
        if (Bingo.getGame().findChallenge(challengeId) == null) {
            sender.sendMessage(Text.warning(Component.translatable("bingo.command.unknown_challenge", Component.text(ChallengeArgument.format(challengeId)))));
            return;
        }
        Integer previous = settings().fixChallenge(position, challengeId);
        if (previous == null) {
            sender.sendMessage(Text.info(Component.translatable("bingo.grid.set", Component.text(position), challengeName(challengeId))));
        } else {
            sender.sendMessage(Text.info(Component.translatable("bingo.grid.moved", Component.text(position), challengeName(challengeId), Component.text(previous))));
        }
    }

    private static void clear(CommandSender sender, int position) {
        if (isNotSetup(sender)) {
            return;
        }
        if (settings().clearFixedChallenge(position) == null) {
            sender.sendMessage(Text.warning(Component.translatable("bingo.grid.not_fixed", Component.text(position))));
            return;
        }
        sender.sendMessage(Text.info(Component.translatable("bingo.grid.cleared", Component.text(position))));
    }

    private static void random(CommandSender sender) {
        if (isNotSetup(sender)) {
            return;
        }
        settings().clearFixedChallenges();
        sender.sendMessage(Text.info(Component.translatable("bingo.grid.random")));
    }

    private static boolean isNotSetup(CommandSender sender) {
        if (Bingo.getGame().getState() != Game.State.SETUP) {
            sender.sendMessage(Text.warning(Component.translatable("bingo.grid.setup_only")));
            return true;
        }
        return false;
    }

    private static Component challengeName(ChallengeId challengeId) {
        Challenge challenge = Bingo.getGame().findChallenge(challengeId);
        return challenge == null ? Component.text(ChallengeArgument.format(challengeId)) : challenge.getName();
    }

    private static Settings settings() {
        return Bingo.getGame().getSettings();
    }
}
