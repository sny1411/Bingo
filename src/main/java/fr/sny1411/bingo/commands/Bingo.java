package fr.sny1411.bingo.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.tree.LiteralCommandNode;
import fr.sny1411.bingo.Game;
import fr.sny1411.bingo.listener.gui.BingoGui;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import org.bukkit.entity.Player;

public class Bingo {
    private Bingo() {
    }

    public static LiteralCommandNode<CommandSourceStack> node() {
        return Commands.literal("bingo")
                .requires(source -> source.getSender() instanceof Player)
                .executes(context -> {
                    if (fr.sny1411.bingo.Bingo.getGame().getEtat() == Game.Etat.INGAME) {
                        BingoGui.open((Player) context.getSource().getSender());
                    }
                    return Command.SINGLE_SUCCESS;
                })
                .build();
    }
}
