package fr.sny1411.bingo.utils;

import java.util.ArrayList;
import java.util.List;

public class BonusEvent {
    private static final List<BonusEvent> events = new ArrayList<>();

    private final Challenge challenge;
    public enum DIFFICULT {I, II, III}

    public static void init() {
        int nbEvents = Random.choice(1,4);
        for (int i = 0; i < nbEvents; i++) {
            boolean add = false;
            while (Boolean.FALSE.equals(add)) {
                List<Challenge> challenges = Challenge.getChallenges();
                Challenge challenge = challenges.get(Random.choice(0, challenges.size()));
                if (challenge.getDifficult() != Challenge.Difficult.EXTREME) { // TODO : DOIT RECUP UNE CASE DANS LA GRID PLUTOT
                    boolean inEvent = false;
                    for (BonusEvent event : events) {
                        if (event.getChallenge().equals(challenge)) {
                            inEvent = true;
                            break;
                        }
                    }
                    if (Boolean.FALSE.equals(inEvent)) {
                        add = true;
                        events.add(new BonusEvent(challenge));
                    }
                }
            }
        }
    }

    private BonusEvent(Challenge challenge) {
        this.challenge = challenge;
    }

    public static List<BonusEvent> getEvents() {
        return events;
    }

    public Challenge getChallenge() {
        return challenge;
    }
}
