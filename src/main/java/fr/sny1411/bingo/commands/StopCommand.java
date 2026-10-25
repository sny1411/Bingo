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
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class StopCommand {
    private StopCommand() {
    }

    public static LiteralCommandNode<CommandSourceStack> node() {
        return Commands.literal("stop")
                .requires(source -> source.getSender() instanceof Player && source.getSender().hasPermission("bingo.command.stop"))
                .executes(context -> {
                    run(context.getSource().getSender());
                    return Command.SINGLE_SUCCESS;
                })
                .build();
    }

    private static void run(CommandSender sender) {
        Game game = Bingo.getGame();
        if (game.getEtat() == Game.Etat.STARTING) {
            Countdown.cancel();
            game.setEtat(Game.Etat.SETUP);
            Text.broadcastMessage(Text.info(Component.translatable("bingo.start.cancelled")));
            return;
        }
        if (game.getEtat() != Game.Etat.INGAME) {
            sender.sendMessage(Text.warning(Component.translatable("bingo.command.no_game")));
            return;
        }
        // Stopping the timer ends the game, like at the end of the time
        game.getTimer().stop();
    }
}
