package fr.sny1411.bingo.utils.bonus;

import fr.sny1411.bingo.Bingo;
import fr.sny1411.bingo.utils.Challenge;
import fr.sny1411.bingo.utils.Grid;
import fr.sny1411.bingo.utils.Random;
import fr.sny1411.bingo.utils.Text;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Level;

public class BonusEvent {
    public static List<BonusEvent> createEvents(int durationMinutes) {
        List<BonusEvent> events = new ArrayList<>();
        int nbEvents = Random.choice(1, 4);
        for (int i = 0; i < nbEvents; i++) {
            // Between 1/8 and 3/4 of the game, like between 15 and 90 minutes in a 2-hour game
            events.add(new BonusEvent(Random.choice(durationMinutes / 8, durationMinutes * 3 / 4)));
        }
        events.sort(Comparator.comparingInt(BonusEvent::getTimeLaunch));
        for (BonusEvent event : events) {
            Bukkit.getLogger().log(Level.INFO, "event : " + event.getTimeLaunch());
        }
        return events;
    }

    private Challenge challenge;
    private final int timeLaunch;
    private boolean enable;
    private boolean launched;

    private BonusEvent(Challenge challenge, int timeLaunch) {
        this.challenge = challenge;
        this.timeLaunch = timeLaunch;
        this.enable = false;
    }

    private BonusEvent(int timeLaunch) {
        this(null,timeLaunch);
    }

    private void setChallenge() {
        Challenge[][] gameGrid = Bingo.getGame().getGameGrid().getGrid();
        List<Challenge> candidates = new ArrayList<>();
        for (int x = 0; x < gameGrid.length; x++) {
            for (int y = 0; y < gameGrid[x].length; y++) {
                if (!isRealizedExtreme(x, y) && !isRunningBonus(gameGrid[x][y])) {
                    candidates.add(gameGrid[x][y]);
                }
            }
        }
        if (candidates.isEmpty()) {
            for (Challenge[] row : gameGrid) {
                candidates.addAll(List.of(row));
            }
        }
        this.challenge = candidates.get(Random.choice(0, candidates.size() - 1));
    }

    private static boolean isRealizedExtreme(int x, int y) {
        for (Grid grid : Bingo.getGame().getTeamsGrid().values()) {
            Challenge challenge = grid.getGrid()[x][y];
            if (Boolean.TRUE.equals(challenge.getRealized()) && challenge.getDifficult() == Challenge.Difficult.EXTREME) {
                return true;
            }
        }
        return false;
    }

    private boolean isRunningBonus(Challenge challenge) {
        for (BonusEvent event : Bingo.getGame().getBonusEvents()) {
            if (event != this && event.isEnable() && event.getChallenge().getId() == challenge.getId()) {
                return true;
            }
        }
        return false;
    }

    public Challenge getChallenge() {
        return challenge;
    }

    public int getTimeLaunch() {
        return timeLaunch;
    }

    public boolean isEnable() {
        return enable;
    }

    public boolean isLaunched() {
        return launched;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
        if (enable) {
            launched = true;
            setChallenge();
            annonceLancement();
        }
    }

    private void annonceLancement() {
        Text.broadcastMessage("§7[§eBINGO§7] §fBonus: " + challenge.getName());
        Text.broadcastMessage("§8≫ §7La première équipe à compléter le défi remportera une §9récompense de niveau " + challenge.getDifficult().getTextDifficult());
    }
}
