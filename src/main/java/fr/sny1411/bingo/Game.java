package fr.sny1411.bingo;

import fr.sny1411.bingo.utils.Challenge;
import fr.sny1411.bingo.utils.ChallengeId;
import fr.sny1411.bingo.utils.Countdown;
import fr.sny1411.bingo.utils.Environment;
import fr.sny1411.bingo.utils.Grid;
import fr.sny1411.bingo.utils.Score;
import fr.sny1411.bingo.utils.ScoreBoard;
import fr.sny1411.bingo.utils.Spawn;
import fr.sny1411.bingo.utils.Team;
import fr.sny1411.bingo.utils.Teams;
import fr.sny1411.bingo.utils.Text;
import fr.sny1411.bingo.utils.Timer;
import fr.sny1411.bingo.utils.bonus.BonusEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.title.Title;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class Game {
    public enum DisplayMode {CHILL, COMPETITION}

    public enum PlayMode {
        CLASSIC, DUEL, HANDICAP;

        public Component label() {
            return Component.translatable("bingo.game_mode." + name().toLowerCase(Locale.ROOT));
        }
    }

    public enum ModeVictoire {
        BINGO("bingo"), DEFIS("challenges");

        private final String key;

        ModeVictoire(String key) {
            this.key = key;
        }

        public Component label() {
            return Component.translatable("bingo.victory_mode." + key);
        }
    }

    public enum State {
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
    private State state;
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
        this.state = State.SETUP;
        setup();
    }

    private void setup() {
        Spawn.create();
        Environment.setGamerulesSetup();
        Spawn.teleportPlayers();
        Spawn.giveItemsPlayers();
    }

    public void end() {
        Countdown.showTitle(Title.title(Component.translatable("bingo.timer.game_over", NamedTextColor.GOLD), Component.empty()));
        Text.broadcastMessage(Text.info(Component.translatable("bingo.timer.game_over")));
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

    public List<Challenge> getChallenges() {
        return challenges;
    }

    public Challenge findChallenge(ChallengeId challengeId) {
        for (Challenge challenge : challenges) {
            if (challenge.getId() == challengeId) {
                return challenge;
            }
        }
        return null;
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

    public void setBonusChallenges(boolean bonusChallenges) {
        settings.setBonusChallenges(bonusChallenges);
    }

    public void setPlayersDamage(boolean playersDamage) {
        this.playersDamage = playersDamage;
    }

    public void setDisplayMode(DisplayMode displayMode) {
        settings.setDisplayMode(displayMode);
    }

    public void setPlayMode(PlayMode playMode) {
        settings.setPlayMode(playMode);
    }

    public void setModeVictoire(ModeVictoire modeVictoire) {
        settings.setModeVictoire(modeVictoire);
    }

    public void setState(State state) {
        this.state = state;
        if (state == State.ENDGAME) {
            Bingo.setNotListenChallenges();
            end();
        } else if (state == State.INGAME) {
            Bingo.setListenChallenges();
        }
    }

    public boolean hasBonusChallenges() {
        return settings.hasBonusChallenges();
    }

    public boolean isPlayersDamage() {
        return playersDamage;
    }

    public DisplayMode getDisplayMode() {
        return settings.getDisplayMode();
    }

    public PlayMode getPlayMode() {
        return settings.getPlayMode();
    }

    public ModeVictoire getModeVictoire() {
        return settings.getModeVictoire();
    }

    public State getState() {
        return state;
    }

    public int getNbreBingoForWin() {
        return settings.getNbreBingoForWin();
    }

    public void setNbreBingoForWin(int nbreBingoForWin) {
        settings.setNbreBingoForWin(nbreBingoForWin);
    }
}
