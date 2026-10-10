package fr.sny1411.bingo;

import fr.sny1411.bingo.utils.Challenge;
import fr.sny1411.bingo.utils.ChallengeId;
import fr.sny1411.bingo.utils.Environment;
import fr.sny1411.bingo.utils.Grid;
import fr.sny1411.bingo.utils.Score;
import fr.sny1411.bingo.utils.ScoreBoard;
import fr.sny1411.bingo.utils.Spawn;
import fr.sny1411.bingo.utils.Team;
import fr.sny1411.bingo.utils.Teams;
import fr.sny1411.bingo.utils.Timer;
import fr.sny1411.bingo.utils.bonus.BonusEvent;
import net.kyori.adventure.text.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

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

        public Component label() {
            return Component.translatable("bingo.game_mode." + name().toLowerCase(Locale.ROOT));
        }
    }

    public enum ModeVictoire {
        BINGO("Bingo", "bingo"), DEFIS("Défis", "challenges");

        private final String name;
        private final String key;

        ModeVictoire(String name, String key) {
            this.name = name;
            this.key = key;
        }

        public String getName() {
            return name;
        }

        public Component label() {
            return Component.translatable("bingo.victory_mode." + key);
        }
    }

    public enum Etat {
        SETUP, STARTING, INGAME, ENDGAME;

        public boolean isBeforeGame() {
            return this == SETUP || this == STARTING;
        }
    }

    private final Settings settings;
    private final Timer timer;
    private final Teams teams;
    private final List<Challenge> challenges;
    private final List<BonusEvent> bonusEvents = new ArrayList<>();
    private boolean playersDamage;
    private Etat etat;
    private final Map<Team, Score> teamsScore = new HashMap<>();
    private Grid gameGrid;
    private final Map<Team, Grid> teamsGrid = new HashMap<>();
    private static Bingo bingoInstance;

    public Game() {
        this(new Settings());
    }

    public Game(Settings settings) {
        this.settings = settings;
        this.timer = new Timer(this);
        this.teams = new Teams(settings);
        this.challenges = Challenge.loadChallenges();
        this.playersDamage = false;
        this.etat = Etat.SETUP;
        setup();
    }

    private void setup() {
        Spawn.create();
        Environment.setGamerulesSetup();
        Spawn.teleportPlayers();
        Spawn.giveItemsPlayers();
    }

    public void end() {
        ScoreBoard.stop();
        Spawn.create();
        Environment.setGamerulesSetup();
        Spawn.teleportPlayers();
        Environment.clearPlayers();
    }

    public void createGrids() {
        gameGrid = Grid.random(challenges, settings);
        teamsGrid.clear();
        for (Team team : teams.values()) {
            teamsGrid.put(team, gameGrid.copy());
        }
    }

    public int getNbChallenges(Challenge.Difficult difficult) {
        int nbChallenges = 0;
        for (Challenge challenge : challenges) {
            if (challenge.getDifficult() == difficult) {
                nbChallenges++;
            }
        }
        return nbChallenges;
    }

    public Grid getGameGrid() {
        return gameGrid;
    }

    public Map<Team, Grid> getTeamsGrid() {
        return teamsGrid;
    }

    public Challenge getChallenge(Team team, ChallengeId challengeId) {
        Grid grid = teamsGrid.get(team);
        return grid == null ? null : grid.getChallenge(challengeId);
    }

    public void initBonusEvents() {
        bonusEvents.clear();
        bonusEvents.addAll(BonusEvent.createEvents(settings.getDurationMinutes()));
    }

    public List<BonusEvent> getBonusEvents() {
        return bonusEvents;
    }

    public void initScores() {
        teamsScore.clear();
        for (Team team : teams.values()) {
            if (team.getColor() != Team.Color.SPECTATOR) {
                teamsScore.put(team, new Score(team));
            }
        }
    }

    public Map<Team, Score> getTeamsScore() {
        return teamsScore;
    }

    public Settings getSettings() {
        return settings;
    }

    public Timer getTimer() {
        return timer;
    }

    public Teams getTeams() {
        return teams;
    }

    public static Bingo getBingoInstance() {
        return bingoInstance;
    }

    public static void setBingoInstance(Bingo bingoInstance) {
        Game.bingoInstance = bingoInstance;
    }

    public void setDefiBonus(boolean defiBonus) {
        settings.setDefiBonus(defiBonus);
    }

    public void setPlayersDamage(boolean playersDamage) {
        this.playersDamage = playersDamage;
    }

    public void setModeAffichage(ModeAffichage modeAffichage) {
        settings.setModeAffichage(modeAffichage);
    }

    public void setModeJeu(ModeJeu modeJeu) {
        settings.setModeJeu(modeJeu);
    }

    public void setModeVictoire(ModeVictoire modeVictoire) {
        settings.setModeVictoire(modeVictoire);
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
        return settings.isDefiBonus();
    }

    public boolean isPlayersDamage() {
        return playersDamage;
    }

    public ModeAffichage getModeAffichage() {
        return settings.getModeAffichage();
    }

    public ModeJeu getModeJeu() {
        return settings.getModeJeu();
    }

    public ModeVictoire getModeVictoire() {
        return settings.getModeVictoire();
    }

    public Etat getEtat() {
        return etat;
    }

    public int getNbreBingoForWin() {
        return settings.getNbreBingoForWin();
    }

    public void setNbreBingoForWin(int nbreBingoForWin) {
        settings.setNbreBingoForWin(nbreBingoForWin);
    }
}
