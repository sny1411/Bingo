package fr.sny1411.bingo.commands.argument;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import fr.sny1411.bingo.Bingo;
import fr.sny1411.bingo.commands.CommandSuggestions;
import fr.sny1411.bingo.utils.Team;
import io.papermc.paper.command.brigadier.MessageComponentSerializer;
import io.papermc.paper.command.brigadier.argument.CustomArgumentType;
import net.kyori.adventure.text.Component;

import java.util.concurrent.CompletableFuture;

// A team of the current game, written with the English name of its color
public class TeamArgument implements CustomArgumentType.Converted<Team, String> {
    private static final DynamicCommandExceptionType UNKNOWN = new DynamicCommandExceptionType(input ->
            MessageComponentSerializer.message().serialize(Component.translatable("bingo.valid.unknown_team", Component.text(input.toString()))));

    @Override
    public Team convert(String input) throws CommandSyntaxException {
        for (Team team : Bingo.getGame().getTeamsScore().keySet()) {
            if (team.getColor().getKey().equalsIgnoreCase(input)) {
                return team;
            }
        }
        throw UNKNOWN.create(input);
    }

    @Override
    public ArgumentType<String> getNativeType() {
        return StringArgumentType.word();
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        return CommandSuggestions.suggest(builder, Bingo.getGame().getTeamsScore().keySet().stream()
                .map(team -> team.getColor().getKey())
                .toList());
    }
}
