package fr.sny1411.bingo.commands;

import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;

public final class CommandSuggestions {
    private CommandSuggestions() {
        throw new IllegalStateException("Utility class");
    }

    // Suggests the values that start with what is already typed
    public static CompletableFuture<Suggestions> suggest(SuggestionsBuilder builder, Collection<String> values) {
        for (String value : values) {
            if (value.startsWith(builder.getRemaining())) {
                builder.suggest(value);
            }
        }
        return builder.buildFuture();
    }

    // Numbers are suggested as numbers, so that the client sorts them in order
    public static CompletableFuture<Suggestions> suggestNumbers(SuggestionsBuilder builder, Collection<Integer> values) {
        for (int value : values) {
            if (String.valueOf(value).startsWith(builder.getRemaining())) {
                builder.suggest(value);
            }
        }
        return builder.buildFuture();
    }
}
