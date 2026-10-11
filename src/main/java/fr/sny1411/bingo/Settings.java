package fr.sny1411.bingo;

import fr.sny1411.bingo.utils.Grid;

/**
 * The game settings, kept from one game to the next by /newGame.
 */
public class Settings {
    public static final int MIN_DURATION_MINUTES = 30;
    public static final int MAX_DURATION_MINUTES = 4 * 60;
    public static final int DURATION_STEP_MINUTES = 15;

    private boolean defiBonus = false;
    private Game.ModeAffichage modeAffichage = Game.ModeAffichage.CHILL;
    private Game.ModeJeu modeJeu = Game.ModeJeu.CLASSIC;
    private Game.ModeVictoire modeVictoire = Game.ModeVictoire.BINGO;
    private int nbreBingoForWin = 3;
    private int durationMinutes = 2 * 60;
    private int nbTeams = 4;
    private int nbPlayerTeams = 2;
    private int maxEasy = 6;
    private int maxMedium = 13;
    private int maxHard = 6;
    private int maxExtreme = 0;

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
        return getMaxTotal() < Grid.NB_CHALLENGES;
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
