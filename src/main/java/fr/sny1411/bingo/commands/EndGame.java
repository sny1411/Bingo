package fr.sny1411.bingo.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.tree.LiteralCommandNode;
import fr.sny1411.bingo.Bingo;
import fr.sny1411.bingo.Game;
import fr.sny1411.bingo.utils.Countdown;
import fr.sny1411.bingo.utils.Text;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

public class EndGame {
    private EndGame() {
    }

    public static LiteralCommandNode<CommandSourceStack> node() {
        return Commands.literal("stopgame")
                .requires(source -> source.getSender() instanceof Player && source.getSender().hasPermission("bingo.stopGame"))
                .executes(context -> {
                    run();
                    return Command.SINGLE_SUCCESS;
                })
                .build();
    }

    private static void run() {
        Game game = Bingo.getGame();
        if (game.getEtat() == Game.Etat.STARTING) {
            Countdown.cancel();
            game.setEtat(Game.Etat.SETUP);
            Text.broadcastMessage(Text.info(Component.translatable("bingo.start.cancelled")));
            return;
        }
        game.setEtat(Game.Etat.ENDGAME);
        game.getTimer().stop();
    }
}
