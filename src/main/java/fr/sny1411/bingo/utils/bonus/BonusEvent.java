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
        boolean find = false;
        int x = -1;
        int y = -1;
        int nbTest = 0;
        while (!find && nbTest < 25) {
            x = Random.choice(0,4);
            y = Random.choice(0,4);

            boolean canAdd = false;
            for (Grid grid : Bingo.getGame().getTeamsGrid().values()) {
                Challenge challengeChoice = grid.getGrid()[x][y];
                if (Boolean.TRUE.equals(challengeChoice.getRealized()) && challengeChoice.getDifficult() == Challenge.Difficult.EXTREME) {
                    canAdd = true;
                    break;
                }
            }
            if (!canAdd) {
                find = true;
            }
            nbTest++;
        }
        this.challenge = Bingo.getGame().getGameGrid().getGrid()[x][y];
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
