package fr.sny1411.bingo;

public class Game {
    enum ModeAffichage {CHILL, COMPETITION}

    enum ModeJeu {CLASSIC, DUEL, HANDICAP}

    enum ModeVictoire {BINGO, DEFIS}

    enum Etat {SETUP, INGAME, ENDGAME}

    private boolean defiBonus;
    private boolean playersDamage;
    private ModeAffichage modeAffichage;
    private ModeJeu modeJeu;
    private ModeVictoire modeVictoire;
    private Etat etat;

    private Game(boolean defiBonus, boolean playersDamage, ModeAffichage modeAffichage, ModeJeu modeJeu, ModeVictoire modeVictoire, Etat etat) {
        this.defiBonus = defiBonus;
        this.playersDamage = playersDamage;
        this.modeAffichage = modeAffichage;
        this.modeJeu = modeJeu;
        this.modeVictoire = modeVictoire;
        this.etat = etat;
    }

    public Game() {
        this(false, false, ModeAffichage.CHILL, ModeJeu.CLASSIC, ModeVictoire.BINGO, Etat.SETUP);
    }

    public void setDefiBonus(boolean defiBonus) {
        this.defiBonus = defiBonus;
    }

    public void setPlayersDamage(boolean playersDamage) {
        this.playersDamage = playersDamage;
    }

    public void setModeAffichage(ModeAffichage modeAffichage) {
        this.modeAffichage = modeAffichage;
    }

    public void setModeJeu(ModeJeu modeJeu) {
        this.modeJeu = modeJeu;
    }

    public void setModeVictoire(ModeVictoire modeVictoire) {
        this.modeVictoire = modeVictoire;
    }

    public void setEtat(Etat etat) {
        this.etat = etat;
    }

    public boolean isDefiBonus() {
        return defiBonus;
    }

    public boolean isPlayersDamage() {
        return playersDamage;
    }

    public ModeAffichage getModeAffichage() {
        return modeAffichage;
    }

    public ModeJeu getModeJeu() {
        return modeJeu;
    }

    public ModeVictoire getModeVictoire() {
        return modeVictoire;
    }

    public Etat getEtat() {
        return etat;
    }
}
