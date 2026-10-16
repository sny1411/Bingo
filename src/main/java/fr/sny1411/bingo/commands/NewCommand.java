package fr.sny1411.bingo.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.tree.LiteralCommandNode;
import fr.sny1411.bingo.Bingo;
import fr.sny1411.bingo.Game;
import fr.sny1411.bingo.utils.Countdown;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class NewCommand {
    private NewCommand() {
    }

    public static LiteralCommandNode<CommandSourceStack> node() {
        return Commands.literal("new")
                .requires(source -> source.getSender() instanceof Player && source.getSender().hasPermission("bingo.command.new"))
                .executes(context -> {
                    run(context.getSource().getSender(), false);
                    return Command.SINGLE_SUCCESS;
                })
                .then(Commands.literal("confirm")
                        .executes(context -> {
                            run(context.getSource().getSender(), true);
                            return Command.SINGLE_SUCCESS;
                        }))
                .build();
    }

    private static void run(CommandSender sender, boolean confirmed) {
        if (Bingo.getGame().getEtat() == Game.Etat.INGAME) {
            if (!confirmed) {
                sender.sendMessage(Component.translatable("bingo.newgame.confirm", NamedTextColor.DARK_GREEN, Component.text("\"/bingo new confirm\"", NamedTextColor.RED)));
                return;
            }
            Bingo.getGame().getTimer().stop();
        }
        if (Bingo.getGame().getEtat() == Game.Etat.STARTING) {
            Countdown.cancel();
        }
        Bingo.setGame(new Game(Bingo.getGame().getSettings()));
    }
}
