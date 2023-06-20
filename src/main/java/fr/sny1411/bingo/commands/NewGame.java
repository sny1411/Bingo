package fr.sny1411.bingo.commands;

import fr.sny1411.bingo.Bingo;
import fr.sny1411.bingo.Game;
import net.kyori.adventure.text.Component;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class NewGame implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String str, @NotNull String[] args) {
        if (sender instanceof Player) {
            if (Bingo.getGame().getEtat() == Game.Etat.INGAME) {
                if (args.length == 0 || !args[0].equalsIgnoreCase("confirm")) {
                    sender.sendMessage(Component.text("§2Une partie est en cours, faites §c\"/newGame confirm\" §2si vous êtes sûr de vous"));
                    return false;
                }
            }
            Bingo.setGame(new Game());
            return false;
        }
        return true;
    }
}
