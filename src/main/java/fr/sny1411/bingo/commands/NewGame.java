package fr.sny1411.bingo.commands;

import fr.sny1411.bingo.Bingo;
import fr.sny1411.bingo.Game;
import fr.sny1411.bingo.utils.Countdown;
import fr.sny1411.bingo.utils.Timer;
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
                Timer.stop();
            }
            if (Bingo.getGame().getEtat() == Game.Etat.STARTING) {
                Countdown.cancel();
            }
            Bingo.setGame(new Game(Bingo.getGame().getSettings()));
            return false;
        }
        return true;
    }
}
