package fr.sny1411.bingo.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.tree.LiteralCommandNode;
import fr.sny1411.bingo.Bingo;
import fr.sny1411.bingo.Game;
import fr.sny1411.bingo.commands.argument.ChallengeArgument;
import fr.sny1411.bingo.commands.argument.TeamArgument;
import fr.sny1411.bingo.listener.challenges.ChallengeProgress;
import fr.sny1411.bingo.utils.Challenge;
import fr.sny1411.bingo.utils.ChallengeId;
import fr.sny1411.bingo.utils.Grid;
import fr.sny1411.bingo.utils.Team;
import fr.sny1411.bingo.utils.Text;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

// /bingo validate and /bingo invalidate, to fix a challenge by hand during or after a game
public class ValidateCommand {
    private ValidateCommand() {
    }

    public static LiteralCommandNode<CommandSourceStack> validateNode() {
        return node("validate", challenge -> !challenge.getValidated(), ValidateCommand::validate);
    }

    public static LiteralCommandNode<CommandSourceStack> invalidateNode() {
        return node("invalidate", Challenge::getValidated, ValidateCommand::invalidate);
    }

    private interface Action {
        void run(CommandSender sender, Team team, ChallengeId challengeId, Challenge challenge);
    }

    private static LiteralCommandNode<CommandSourceStack> node(String name, Predicate<Challenge> suggested, Action action) {
        return Commands.literal(name)
                .requires(source -> source.getSender().hasPermission("bingo.command.validate"))
                .then(Commands.argument("team", new TeamArgument())
                        .then(Commands.argument("challenge", new ChallengeArgument())
                                .suggests((context, builder) -> CommandSuggestions.suggest(builder, challenges(context.getArgument("team", Team.class), suggested)))
                                .executes(context -> {
                                    run(context.getSource().getSender(), context.getArgument("team", Team.class), context.getArgument("challenge", ChallengeId.class), action);
                                    return Command.SINGLE_SUCCESS;
                                })))
                .build();
    }

    private static List<String> challenges(Team team, Predicate<Challenge> suggested) {
        List<String> ids = new ArrayList<>();
        Grid grid = Bingo.getGame().getTeamsGrid().get(team);
        if (grid == null) {
            return ids;
        }
        for (Challenge[] row : grid.getGrid()) {
            for (Challenge challenge : row) {
                if (suggested.test(challenge)) {
                    ids.add(ChallengeArgument.format(challenge.getId()));
                }
            }
        }
        return ids;
    }

    private static void run(CommandSender sender, Team team, ChallengeId challengeId, Action action) {
        Game.State state = Bingo.getGame().getState();
        if (state != Game.State.INGAME && state != Game.State.ENDGAME) {
            sender.sendMessage(Text.warning(Component.translatable("bingo.command.no_game")));
            return;
        }
        Challenge challenge = Bingo.getGame().getChallenge(team, challengeId);
        if (challenge == null) {
            sender.sendMessage(Text.warning(Component.translatable("bingo.valid.unknown_challenge", Component.text(ChallengeArgument.format(challengeId)))));
            return;
        }
        action.run(sender, team, challengeId, challenge);
    }

    private static void validate(CommandSender sender, Team team, ChallengeId challengeId, Challenge challenge) {
        if (challenge.getValidated()) {
            sender.sendMessage(Text.warning(Component.translatable("bingo.valid.already_validated", challenge.getName())));
            return;
        }
        ChallengeProgress.validateAndRealizeChallenge(team, challengeId);
    }

    private static void invalidate(CommandSender sender, Team team, ChallengeId challengeId, Challenge challenge) {
        if (!challenge.getValidated()) {
            sender.sendMessage(Text.warning(Component.translatable("bingo.valid.not_validated", challenge.getName())));
            return;
        }
        challenge.setValidated(false);
        challenge.setRealized(false);
        Bingo.getGame().getTeamsScore().get(team).removeChallenge(challenge);
        sender.sendMessage(Component.translatable("bingo.valid.removed", NamedTextColor.WHITE, challenge.getName(), team.getColor().displayName()));
    }
}
