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
            Player player = (Player) sender;
            String challengeName = "§d§l" + args[0].replace("_", " ");
            if (isCorrectChallengeNameSyntax(challengeName) &&
                    (ChallengesListener.verifChallenge(player, challengeName))) {
                BonusEvent bonusEvent = findBonus(challengeName);
                assert bonusEvent != null;
                RewardsBonusEvent.setBonus(bonusEvent.getChallenge(), player);
                bonusEvent.setEnable(false);
            }
        }
        return false;
    }

    private static boolean isCorrectChallengeNameSyntax(String challengeName) {
        for (BonusEvent event : BonusEvent.getEvents()) {
            if (event.isEnable() && challengeName.equals(event.getChallenge().getName())) {
                return true;
            }
        }
        return false;
    }

    private static BonusEvent findBonus(String challengeName) {
        for (BonusEvent bonusEvent : BonusEvent.getEvents()) {
            if (bonusEvent.getChallenge().getName().equals(challengeName)) {
                return bonusEvent;
            }
        }
        return null;
    }
}
