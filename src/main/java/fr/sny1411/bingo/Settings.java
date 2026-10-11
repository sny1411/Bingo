package fr.sny1411.bingo;

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
}
