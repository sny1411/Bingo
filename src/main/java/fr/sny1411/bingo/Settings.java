package fr.sny1411.bingo;

/**
 * The game settings, kept from one game to the next by /newGame.
 */
public class Settings {
    private boolean defiBonus = false;
    private Game.ModeAffichage modeAffichage = Game.ModeAffichage.CHILL;
    private Game.ModeJeu modeJeu = Game.ModeJeu.CLASSIC;
    private Game.ModeVictoire modeVictoire = Game.ModeVictoire.BINGO;
    private int nbreBingoForWin = 3;

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
}
