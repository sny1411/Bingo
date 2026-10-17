package fr.sny1411.bingo.commands.argument;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import fr.sny1411.bingo.utils.ChallengeId;
import io.papermc.paper.command.brigadier.MessageComponentSerializer;
import io.papermc.paper.command.brigadier.argument.CustomArgumentType;
import net.kyori.adventure.text.Component;

import java.util.Locale;

// A challenge id, written in lower case; each command suggests the challenges it accepts
public class ChallengeArgument implements CustomArgumentType.Converted<ChallengeId, String> {
    private static final DynamicCommandExceptionType UNKNOWN = new DynamicCommandExceptionType(input ->
            MessageComponentSerializer.message().serialize(Component.translatable("bingo.command.unknown_challenge", Component.text(input.toString()))));

    @Override
    public ChallengeId convert(String input) throws CommandSyntaxException {
        ChallengeId id = ChallengeId.fromString(input.toUpperCase(Locale.ROOT));
        if (id == null) {
            throw UNKNOWN.create(input);
        }
        return id;
    }

    @Override
    public ArgumentType<String> getNativeType() {
        return StringArgumentType.word();
    }

    public static String format(ChallengeId id) {
        return id.name().toLowerCase(Locale.ROOT);
    }
}
