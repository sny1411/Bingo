package fr.sny1411.bingo.listener.challenges;

import fr.sny1411.bingo.utils.*;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionType;

import java.util.EnumSet;

import static fr.sny1411.bingo.listener.challenges.ChallengeProgress.realizeChallenge;

/**
 * Challenges done by the player themselves: dying, eating, gaining levels.
 */
public class PlayerChallengesListener implements Listener {
    @EventHandler
    private void onPlayerDeath(PlayerDeathEvent e) {
        realizeChallenge(e.getPlayer(), ChallengeId.SUICIDE_SQUAD);
    }

    @EventHandler
    private void playerConsume(PlayerItemConsumeEvent e) {
        ItemStack item = e.getItem();
        Player player = e.getPlayer();
        switch (e.getItem().getType()) {
            case POTION:
                PotionMeta potionMeta = (PotionMeta) item.getItemMeta();
                if (EnumSet.of(PotionType.SWIFTNESS, PotionType.LONG_SWIFTNESS, PotionType.STRONG_SWIFTNESS).contains(potionMeta.getBasePotionType()) && (player.getInventory().getItemInOffHand().getType() == Material.BREAD)) {
                    realizeChallenge(player, ChallengeId.TU_ES_UN_SORCIER_HARRY);
                }
                break;
            case COOKIE:
                realizeChallenge(player, ChallengeId.COOKIE_MONSTER);
                break;
        }
    }

    @EventHandler
    private void exChange(PlayerLevelChangeEvent e) {
        if (e.getPlayer().getLevel() >= 30) {
            realizeChallenge(e.getPlayer(), ChallengeId.EXPERIMENTE);
        }
    }
}
