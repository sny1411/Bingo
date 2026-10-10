package fr.sny1411.bingo.listener.challenges;

import fr.sny1411.bingo.utils.*;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.*;

import static fr.sny1411.bingo.listener.challenges.ChallengeProgress.realizeChallenge;

/**
 * Challenges validated by a Minecraft advancement.
 */
public class AdvancementChallengesListener implements Listener {
    @EventHandler
    private void advencementDone(PlayerAdvancementDoneEvent e) {
        Player player = e.getPlayer();
        switch (e.getAdvancement().getKey().getKey()) {
            case "story/enter_the_nether":
                realizeChallenge(player, ChallengeId.BIENVENUE_EN_ENFER);
                break;
            case "nether/explore_nether":
                realizeChallenge(player, ChallengeId.VOYAGE_AU_BOUT_DE_L_ENFER);
                break;
            case "nether/obtain_ancient_debris":
                realizeChallenge(player, ChallengeId.AU_FOND_DES_PROFONDEURS);
                break;
            case "story/follow_ender_eye":
                realizeChallenge(player, ChallengeId.EN_SUIVANT_LES_YEUX);
                break;
            case "end/root":
                realizeChallenge(player, ChallengeId.C_EST_LA_FIN);
                break;
            case "nether/find_bastion":
                realizeChallenge(player, ChallengeId.LES_MYSTERIEUSES_CITES_D_OR);
                break;
            case "adventure/bullseye":
                realizeChallenge(player, ChallengeId.DANS_LE_MILLE);
                break;
            case "adventure/walk_on_powder_snow_with_leather_boots":
                realizeChallenge(player, ChallengeId.JESUS_DES_NEIGES);
                break;
            case "nether/charge_respawn_anchor":
                realizeChallenge(player, ChallengeId.CHARGE_A_BLOC);
                break;
            case "nether/return_to_sender":
                realizeChallenge(player, ChallengeId.RETOUR_A_L_ENVOYEUR);
                break;
            case "adventure/trade":
                realizeChallenge(player, ChallengeId.STONKS_INDUSTRIES);
                break;
            case "story/cure_zombie_villager":
                realizeChallenge(player, ChallengeId.DOCTOR_STRANGE);
                break;
            case "adventure/salvage_sherd":
                realizeChallenge(player, ChallengeId.ARCHEOLOGUE);
                break;
            case "adventure/sleep_in_bed":
                realizeChallenge(player, ChallengeId.BONNE_NUIT_LES_PETITS);
                break;
        }
    }
}
