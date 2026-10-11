package fr.sny1411.bingo.listener.challenges;

import fr.sny1411.bingo.Bingo;
import fr.sny1411.bingo.utils.*;
import org.bukkit.entity.*;

import java.util.Objects;

/**
 * Marks the challenges of a team as realized or validated, and updates its score.
 */
public final class ChallengeProgress {
    private ChallengeProgress() {
        throw new IllegalStateException("Utility class");
    }

    static void realizeChallenge(Player player, ChallengeId challengeId) {
        Challenge challenge = Bingo.getGame().getChallenge(Bingo.getGame().getTeams().getTeam(player), challengeId);
        if (challenge != null && !challenge.getRealized()) {
            challenge.setRealized(true);
        }
    }

    private static void valideChallenge(Player player, ChallengeId challengeId) {
        Challenge challenge = Bingo.getGame().getChallenge(Bingo.getGame().getTeams().getTeam(player), challengeId);
        if (challenge != null && !challenge.getValidated()) {
            challenge.setValidated(true);
            Team teamPlayer = Bingo.getGame().getTeams().getTeam(player);
            Text.validMessage(teamPlayer, challenge.getName());
            Bingo.getGame().getTeamsScore().get(teamPlayer).addChallenge(challenge);
        }
    }

    static boolean verifValideChallenge(Player player, ChallengeId challengeId) {
        if (Boolean.TRUE.equals(Objects.requireNonNull(Bingo.getGame().getChallenge(Bingo.getGame().getTeams().getTeam(player), challengeId)).getRealized())) {
            valideChallenge(player, challengeId);
            return true;
        }
        return false;
    }

    public static void valideAndRealizeChallenge(Player player, ChallengeId challengeId) {
        valideAndRealizeChallenge(Bingo.getGame().getTeams().getTeam(player), challengeId);
    }

    public static void valideAndRealizeChallenge(Team team, ChallengeId challengeId) {
        Challenge challenge = Bingo.getGame().getChallenge(team, challengeId);
        if (challenge != null && !challenge.getValidated()) {
            challenge.setValidated(true);
            challenge.setRealized(true);
            Text.validMessage(team, challenge.getName());
            Bingo.getGame().getTeamsScore().get(team).addChallenge(challenge);
        }
    }
}
