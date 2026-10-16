package fr.sny1411.bingo.commands.argument;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import io.papermc.paper.command.brigadier.MessageComponentSerializer;
import io.papermc.paper.command.brigadier.argument.CustomArgumentType;
import net.kyori.adventure.text.Component;

import java.util.List;
import java.util.concurrent.CompletableFuture;

// A duration written [hh:]mm:ss, parsed as a number of seconds
public class DurationArgument implements CustomArgumentType.Converted<Integer, String> {
    private static final List<String> EXAMPLES = List.of("05:00", "10:00", "30:00");
    private static final DynamicCommandExceptionType INVALID = new DynamicCommandExceptionType(input ->
            MessageComponentSerializer.message().serialize(Component.translatable("bingo.timer.invalid_duration", Component.text(input.toString()))));

    @Override
    public Integer convert(String input) throws CommandSyntaxException {
        int seconds = parse(input);
        if (seconds < 0) {
            throw INVALID.create(input);
        }
        return seconds;
    }

    @Override
    public ArgumentType<String> getNativeType() {
        return StringArgumentType.greedyString();
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        for (String example : EXAMPLES) {
            if (example.startsWith(builder.getRemaining())) {
                builder.suggest(example);
            }
        }
        return builder.buildFuture();
    }

    // -1 if the format is wrong
    static int parse(String time) {
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
}
