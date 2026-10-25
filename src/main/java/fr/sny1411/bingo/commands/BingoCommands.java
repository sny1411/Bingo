package fr.sny1411.bingo.commands;

import com.mojang.brigadier.Command;
import fr.sny1411.bingo.Bingo;
import fr.sny1411.bingo.Game;
import fr.sny1411.bingo.listener.gui.BingoGui;
import io.papermc.paper.command.brigadier.Commands;
import org.bukkit.entity.Player;

public final class BingoCommands {
    private BingoCommands() {
        throw new IllegalStateException("Utility class");
    }

    public static void register(Commands commands, Bingo bingo) {
        commands.register(Commands.literal("bingo")
                .executes(context -> {
                    if (context.getSource().getSender() instanceof Player player && Bingo.getGame().getState() == Game.State.INGAME) {
                        BingoGui.open(player);
                    }
                    return Command.SINGLE_SUCCESS;
                })
                .then(NewCommand.node())
                .then(StartCommand.node(bingo))
                .then(StopCommand.node())
                .then(ResultsCommand.node())
                .then(TimerCommand.node())
                .then(GridCommand.node())
                .then(ValidateCommand.validateNode())
                .then(ValidateCommand.invalidateNode())
                .then(BonusCommand.node())
                .then(SpectateCommand.node())
                .then(PackCommand.node())
                .build(), "Shows your challenge grid during a game, and the other commands of the plugin");
    }
}
