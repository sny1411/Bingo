package fr.sny1411.bingo.utils.bonus;

import fr.sny1411.bingo.utils.Challenge;
import fr.sny1411.bingo.utils.Grid;
import fr.sny1411.bingo.utils.Random;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class BonusEvent {
    private static final List<BonusEvent> events = new ArrayList<>();
    public static List<BonusEvent> getEvents() {
        return events;
    }

    public static void init() {
        createBonus();
        sortEvents();
    }

    private static void createBonus() {
        int nbEvents = Random.choice(1,4);
        for (int i = 0; i < nbEvents; i++) {
            events.add(new BonusEvent(Random.choice(15,90)));
        }
    }

    private static void sortEvents() {
        events.sort(Comparator.comparingInt(BonusEvent::getTimeLaunch));
    }

    private Challenge challenge;
    private final int timeLaunch;
    private boolean enable;

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
        while (!find) {
            x = Random.choice(0,4);
            y = Random.choice(0,4);

            boolean canAdd = false;
            for (Grid grid : Grid.getTeamsGrid().values()) {
                Challenge challengeChoice = grid.getGrid()[x][y];
                if (Boolean.TRUE.equals(challengeChoice.getRealized()) && challengeChoice.getDifficult() == Challenge.Difficult.EXTREME) {
                    canAdd = true;
                    break;
                }
            }
            if (!canAdd) {
                find = true;
            }
        }
        this.challenge = Grid.getGameGrid().getGrid()[x][y];
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

    public void setEnable(boolean enable) {
        this.enable = enable;
        if (enable) {
            setChallenge();
        }
    }
}
