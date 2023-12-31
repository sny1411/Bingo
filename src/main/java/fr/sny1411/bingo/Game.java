package fr.sny1411.bingo;

import fr.sny1411.bingo.utils.Challenge;
import fr.sny1411.bingo.utils.Environment;
import fr.sny1411.bingo.utils.Spawn;
import fr.sny1411.bingo.utils.Team;

public class Game {
    public enum ModeAffichage {CHILL, COMPETITION}

    public enum ModeJeu {
        CLASSIC("Classique"), DUEL("Duel"), HANDICAP("Handicap");

        private final String name;

        ModeJeu(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    public enum ModeVictoire {
        BINGO("Bingo"), DEFIS("Défis");

        private final String name;

        ModeVictoire(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    public enum Etat {SETUP, INGAME, ENDGAME}

    private int nbreBingoForWin;

    private boolean defiBonus;
    private boolean playersDamage;
    private ModeAffichage modeAffichage;
    private ModeJeu modeJeu;
    private ModeVictoire modeVictoire;
    private Etat etat;
    private static Bingo bingoInstance;

    private Game(boolean defiBonus, boolean playersDamage, ModeAffichage modeAffichage, ModeJeu modeJeu, ModeVictoire modeVictoire, Etat etat) {
        this.defiBonus = defiBonus;
        this.playersDamage = playersDamage;
        this.modeAffichage = modeAffichage;
        this.modeJeu = modeJeu;
        this.modeVictoire = modeVictoire;
        this.etat = etat;
        this.nbreBingoForWin = 3;
    }

    public Game() {
        this(false, false, ModeAffichage.CHILL, ModeJeu.CLASSIC, ModeVictoire.BINGO, Etat.SETUP);
        setup();
    }

    private void setup() {
        Spawn.create(bingoInstance);
        Environment.setGamerulesSetup(bingoInstance);
        Spawn.teleportPlayers();
        Spawn.giveItemsPlayers();
        Team.createTeams();
        Challenge.init();
    }

    public void end() {
        Spawn.create(bingoInstance);
        Environment.setGamerulesSetup(bingoInstance);
        Spawn.teleportPlayers();
        Environment.clearPlayers();
    }

    public static Bingo getBingoInstance() {
        return bingoInstance;
    }

    public static void setBingoInstance(Bingo bingoInstance) {
        Game.bingoInstance = bingoInstance;
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
        if (etat == Etat.ENDGAME) {
            Bingo.setNotListenChallenges();
            end();
        } else if (etat == Etat.INGAME) {
            Bingo.setListenChallenges();
        }
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

    public int getNbreBingoForWin() {
        return nbreBingoForWin;
    }

    public void setNbreBingoForWin(int nbreBingoForWin) {
        this.nbreBingoForWin = nbreBingoForWin;
    }
}
