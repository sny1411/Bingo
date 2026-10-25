package fr.sny1411.bingo;

import fr.sny1411.bingo.utils.ChallengeId;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SettingsTest {

    @Test
    void defaultChallengesMatchThePresetOfTheDefaultTeamSize() {
        Settings settings = new Settings();
        Settings preset = new Settings();
        preset.setChallengePreset(settings.getNbPlayerTeams());
        assertEquals(preset.getMaxEasy(), settings.getMaxEasy());
        assertEquals(preset.getMaxMedium(), settings.getMaxMedium());
        assertEquals(preset.getMaxHard(), settings.getMaxHard());
        assertEquals(preset.getMaxExtreme(), settings.getMaxExtreme());
    }

    @Test
    void resetBringsBackEveryDefault() {
        Settings settings = new Settings();
        settings.setDefiBonus(true);
        settings.setModeAffichage(Game.ModeAffichage.COMPETITION);
        settings.setModeJeu(Game.ModeJeu.DUEL);
        settings.setModeVictoire(Game.ModeVictoire.DEFIS);
        settings.setNbreBingoForWin(7);
        settings.setDurationMinutes(Settings.MAX_DURATION_MINUTES);
        settings.setNbTeams(6);
        settings.setNbPlayerTeams(4);
        settings.setChallengePreset(4);
        settings.fixChallenge(1, ChallengeId.BATMAN);

        settings.reset();

        Settings defaults = new Settings();
        assertEquals(defaults.isDefiBonus(), settings.isDefiBonus());
        assertEquals(defaults.getModeAffichage(), settings.getModeAffichage());
        assertEquals(defaults.getModeJeu(), settings.getModeJeu());
        assertEquals(defaults.getModeVictoire(), settings.getModeVictoire());
        assertEquals(defaults.getNbreBingoForWin(), settings.getNbreBingoForWin());
        assertEquals(defaults.getDurationMinutes(), settings.getDurationMinutes());
        assertEquals(defaults.getNbTeams(), settings.getNbTeams());
        assertEquals(defaults.getNbPlayerTeams(), settings.getNbPlayerTeams());
        assertEquals(defaults.getMaxEasy(), settings.getMaxEasy());
        assertEquals(defaults.getMaxMedium(), settings.getMaxMedium());
        assertEquals(defaults.getMaxHard(), settings.getMaxHard());
        assertEquals(defaults.getMaxExtreme(), settings.getMaxExtreme());
        assertTrue(settings.getFixedChallenges().isEmpty());
    }

    @Test
    void fixingAChallengeAlreadyFixedMovesIt() {
        Settings settings = new Settings();
        assertNull(settings.fixChallenge(3, ChallengeId.BATMAN));
        assertEquals(3, settings.fixChallenge(7, ChallengeId.BATMAN));
        assertEquals(Map.of(7, ChallengeId.BATMAN), settings.getFixedChallenges());
    }

    @Test
    void fixingAPositionAgainReplacesItsChallenge() {
        Settings settings = new Settings();
        settings.fixChallenge(3, ChallengeId.BATMAN);
        assertNull(settings.fixChallenge(3, ChallengeId.AFFAME));
        assertEquals(Map.of(3, ChallengeId.AFFAME), settings.getFixedChallenges());
    }

    @Test
    void clearingGivesThePositionBackToTheDraw() {
        Settings settings = new Settings();
        settings.fixChallenge(3, ChallengeId.BATMAN);
        settings.fixChallenge(4, ChallengeId.AFFAME);
        assertEquals(ChallengeId.BATMAN, settings.clearFixedChallenge(3));
        assertNull(settings.clearFixedChallenge(3));
        settings.clearFixedChallenges();
        assertTrue(settings.getFixedChallenges().isEmpty());
    }

    @Test
    void fixedPositionsLowerTheChallengesToDraw() {
        Settings settings = new Settings();
        settings.setMaxEasy(settings.getMaxEasy() - 2);
        assertTrue(settings.isGridIncomplete());
        settings.fixChallenge(1, ChallengeId.BATMAN);
        settings.fixChallenge(2, ChallengeId.AFFAME);
        assertEquals(23, settings.getNbRandomChallenges());
        assertFalse(settings.isGridIncomplete());
        assertEquals(25, settings.getNbChallengesInGrid());
        settings.setMaxEasy(settings.getMaxEasy() - 2);
        assertEquals(23, settings.getNbChallengesInGrid());
    }
}
