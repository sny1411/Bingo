package fr.sny1411.bingo.commands;

import fr.sny1411.bingo.Bingo;
import fr.sny1411.bingo.Game;
import fr.sny1411.bingo.listener.ChallengesListener;
import fr.sny1411.bingo.utils.bonus.BonusEvent;
import fr.sny1411.bingo.utils.bonus.RewardsBonusEvent;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class Bonus implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player && Bingo.getGame().getEtat() == Game.Etat.INGAME && args.length >= 1) {
            String challengeName = args[0].replace("_", " ");
            if (isCorrectChallengeNameSyntax(challengeName) &&
                    (ChallengesListener.verifChallenge((Player) sender, "§d§l" + challengeName))) {
                // TODO : SET REWARDS
            }
        }
        return false;
    }

    private static boolean isCorrectChallengeNameSyntax(String challengeName) {
        for (BonusEvent event : BonusEvent.getEvents()) {
            if (event.isEnable() && challengeName.equals(event.getChallenge().getName().substring(4))) {
                return true;
            }
        }
        return false;
    }
}
