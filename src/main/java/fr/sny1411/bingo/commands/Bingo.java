package fr.sny1411.bingo.commands;

import fr.sny1411.bingo.Game;
import fr.sny1411.bingo.listener.gui.BingoGui;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class Bingo implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String str, @NotNull String[] args) {
        if (sender instanceof Player && (fr.sny1411.bingo.Bingo.getGame().getEtat() == Game.Etat.INGAME)) {
            BingoGui.open((Player) sender);
        }
        return false;
    }
}
