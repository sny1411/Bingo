package fr.sny1411.bingo.commands;

import fr.sny1411.bingo.Bingo;
import fr.sny1411.bingo.Game;
import fr.sny1411.bingo.listener.ChallengesListener;
import fr.sny1411.bingo.utils.ChallengeId;
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
            ChallengeId challengeId = ChallengeId.fromString(args[0]);
            if (isEnabledBonus(challengeId) &&
                    (ChallengesListener.verifChallenge(player, challengeId))) {
                BonusEvent bonusEvent = findBonus(challengeId);
                assert bonusEvent != null;
                RewardsBonusEvent.setBonus(bonusEvent.getChallenge(), player);
                bonusEvent.setEnable(false);
            }
        }
        return false;
    }

    private static boolean isEnabledBonus(ChallengeId challengeId) {
        for (BonusEvent event : BonusEvent.getEvents()) {
            if (event.isEnable() && event.getChallenge().getId() == challengeId) {
                return true;
            }
        }
        return false;
    }

    private static BonusEvent findBonus(ChallengeId challengeId) {
        for (BonusEvent bonusEvent : BonusEvent.getEvents()) {
            if (bonusEvent.getChallenge().getId() == challengeId) {
                return bonusEvent;
            }
        }
        return null;
    }
}
