package fr.sny1411.bingo;

import fr.sny1411.bingo.utils.ChallengeId;
import fr.sny1411.bingo.utils.Grid;

import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

/**
 * The game settings, kept from one game to the next by /bingo new.
 */
public class Settings {
    public static final int MIN_DURATION_MINUTES = 30;
    public static final int MAX_DURATION_MINUTES = 4 * 60;
    public static final int DURATION_STEP_MINUTES = 15;

    private boolean defiBonus;
    private Game.ModeAffichage modeAffichage;
    private Game.ModeJeu modeJeu;
    private Game.ModeVictoire modeVictoire;
    private int nbreBingoForWin;
    private int durationMinutes;
    private int nbTeams;
    private int nbPlayerTeams;
    private int maxEasy;
    private int maxMedium;
    private int maxHard;
    private int maxExtreme;
    // Positions from 1 to 25, in reading order, chosen with /bingo grid
    private final Map<Integer, ChallengeId> fixedChallenges = new TreeMap<>();

    public Settings() {
        reset();
    }

    public void reset() {
        defiBonus = false;
        modeAffichage = Game.ModeAffichage.CHILL;
        modeJeu = Game.ModeJeu.CLASSIC;
        modeVictoire = Game.ModeVictoire.BINGO;
        nbreBingoForWin = 3;
        durationMinutes = 2 * 60;
        nbTeams = 4;
        nbPlayerTeams = 2;
        setChallengePreset(nbPlayerTeams);
        fixedChallenges.clear();
    }

    public boolean isDefiBonus() {
        return defiBonus;
    }

    public void setDefiBonus(boolean defiBonus) {
        this.defiBonus = defiBonus;
    }

    public Game.ModeAffichage getModeAffichage() {
        return modeAffichage;
    }

    public void setModeAffichage(Game.ModeAffichage modeAffichage) {
        this.modeAffichage = modeAffichage;
    }

    public Game.ModeJeu getModeJeu() {
        return modeJeu;
    }

    public void setModeJeu(Game.ModeJeu modeJeu) {
        this.modeJeu = modeJeu;
    }

    public Game.ModeVictoire getModeVictoire() {
        return modeVictoire;
    }

    public void setModeVictoire(Game.ModeVictoire modeVictoire) {
        this.modeVictoire = modeVictoire;
    }

    public int getNbreBingoForWin() {
        return nbreBingoForWin;
    }

    public void setNbreBingoForWin(int nbreBingoForWin) {
        this.nbreBingoForWin = nbreBingoForWin;
    }

    public int getDurationMinutes() {
        return durationMinutes;
    }

    public void setDurationMinutes(int durationMinutes) {
        this.durationMinutes = durationMinutes;
    }

    public int getNbTeams() {
        return nbTeams;
    }

    public void setNbTeams(int nbTeams) {
        this.nbTeams = nbTeams;
    }

    public int getNbPlayerTeams() {
        return nbPlayerTeams;
    }

    public void setNbPlayerTeams(int nbPlayerTeams) {
        this.nbPlayerTeams = nbPlayerTeams;
    }

    public int getMaxEasy() {
        return maxEasy;
    }

    public void setMaxEasy(int maxEasy) {
        this.maxEasy = maxEasy;
    }

    public int getMaxMedium() {
        return maxMedium;
    }

    public void setMaxMedium(int maxMedium) {
        this.maxMedium = maxMedium;
    }

    public int getMaxHard() {
        return maxHard;
    }

    public void setMaxHard(int maxHard) {
        this.maxHard = maxHard;
    }

    public int getMaxExtreme() {
        return maxExtreme;
    }

    public void setMaxExtreme(int maxExtreme) {
        this.maxExtreme = maxExtreme;
    }

    public int getMaxTotal() {
        return maxEasy + maxMedium + maxHard + maxExtreme;
    }

    public boolean verifSettingsToHigh() {
        return getMaxTotal() < getNbRandomChallenges();
    }

    public int getNbRandomChallenges() {
        return Grid.NB_CHALLENGES - fixedChallenges.size();
    }

    // The challenges the grid will have, fixed ones included, out of 25
    public int getNbChallengesInGrid() {
        return Math.min(getMaxTotal(), getNbRandomChallenges()) + fixedChallenges.size();
    }

    public Map<Integer, ChallengeId> getFixedChallenges() {
        return Collections.unmodifiableMap(fixedChallenges);
    }

    // Returns the position the challenge was fixed at before, or null
    public Integer fixChallenge(int position, ChallengeId challengeId) {
        Integer previous = null;
        for (Map.Entry<Integer, ChallengeId> entry : fixedChallenges.entrySet()) {
            if (entry.getValue() == challengeId && entry.getKey() != position) {
                previous = entry.getKey();
            }
        }
        if (previous != null) {
            fixedChallenges.remove(previous);
        }
        fixedChallenges.put(position, challengeId);
        return previous;
    }

    public ChallengeId clearFixedChallenge(int position) {
        return fixedChallenges.remove(position);
    }

    public void clearFixedChallenges() {
        fixedChallenges.clear();
    }

    // Challenges per difficulty recommended for a number of players per team
    public void setChallengePreset(int nbPlayerTeams) {
        switch (nbPlayerTeams) {
            case 1 -> setPreset(13, 8, 4, 0);
            case 2 -> setPreset(10, 10, 5, 0);
            case 3 -> setPreset(6, 13, 6, 0);
            case 4 -> setPreset(2, 10, 12, 1);
            default -> { }
        }
    }

    private void setPreset(int maxEasy, int maxMedium, int maxHard, int maxExtreme) {
        this.maxEasy = maxEasy;
        this.maxMedium = maxMedium;
        this.maxHard = maxHard;
        this.maxExtreme = maxExtreme;
    }
}
