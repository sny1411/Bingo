package fr.sny1411.bingo.commands;

import fr.sny1411.bingo.Bingo;
import io.papermc.paper.command.brigadier.Commands;

public final class BingoCommands {
    private BingoCommands() {
        throw new IllegalStateException("Utility class");
    }

    public static void register(Commands commands, Bingo bingo) {
        commands.register(NewGame.node(), "Creates a new game");
        commands.register(Start.node(bingo), "Starts the game");
        commands.register(fr.sny1411.bingo.commands.Bingo.node(), "Shows your challenge grid during a game");
        commands.register(TestPack.node(), "Shows a title and plays a sound to check whether the resource pack is installed");
        commands.register(EndGame.node(), "Stops the game before the end");
        commands.register(Result.node(), "Shows the ranking once the game is over");
        commands.register(Spec.node(), "Lets players switch to spectator mode once their game is finished");
    }
}
