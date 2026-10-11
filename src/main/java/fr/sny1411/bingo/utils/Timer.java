package fr.sny1411.bingo.utils;

import fr.sny1411.bingo.Bingo;
import fr.sny1411.bingo.Game;
import fr.sny1411.bingo.utils.bonus.BonusEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitTask;

import java.util.List;
import java.util.logging.Level;

public class Timer {
    private static final List<Integer> END_WARNING_TIMES = List.of(10 * 60, 5 * 60, 3 * 60, 2 * 60, 60, 30, 15, 10, 5, 3, 2, 1); // remaining seconds

    // Real time rather than ticks, so the game lasts its real duration even if the server lags
    private final Stopwatch stopwatch = new Stopwatch();
    private int elapsedSeconds = 0;

    private final Game game;
    private int stormTime; // in seconds
    private boolean stormStarted;
    private int nextEndWarning;

    private BukkitTask task;

    public Timer(Game game) {
        this.game = game;
    }

    public void start(Bingo bingo) {
        stopwatch.start();
        elapsedSeconds = 0;
        nextEndWarning = 0;
        skipEndWarnings(getDurationSeconds() - 1);
        stormStarted = false;
        // Between half and 7/8 of the game, like between 60 and 105 minutes in a 2-hour game
        stormTime = Random.choice(getDurationSeconds() / 2, getDurationSeconds() * 7 / 8);
        Bukkit.getLogger().log(Level.INFO, String.format("Storm planned at minute %d", stormTime / 60));

        task = Bukkit.getScheduler().runTaskTimer(bingo, this::tick, 1L, 1L);
    }

    private void tick() {
        int elapsed = stopwatch.elapsedSeconds();
        if (elapsed == elapsedSeconds) {
            return;
        }
        elapsedSeconds = elapsed;

        // >= rather than ==: a lag spike can skip a second
        int remaining = getDurationSeconds() - elapsed;
        while (nextEndWarning < END_WARNING_TIMES.size() && END_WARNING_TIMES.get(nextEndWarning) >= remaining) {
            Bukkit.broadcast(endWarningMessage(END_WARNING_TIMES.get(nextEndWarning)));
            nextEndWarning++;
        }

        if (!stormStarted && stormTime <= elapsed) {
            stormStarted = true;
            Bukkit.getLogger().log(Level.INFO, "Storm started");
            World world = Bukkit.getWorlds().get(0);
            world.setStorm(true);
            world.setThundering(true);
            world.setWeatherDuration(8400); // 7 minutes (in ticks)
        }

        for (BonusEvent event : BonusEvent.getEvents()) {
            if (!event.isEnable() && event.getTimeLaunch() * 60 <= elapsed) {
                event.setEnable(true);
            }
        }

        if (remaining <= 0) {
            end();
        }
    }

    private void skipEndWarnings(int remainingSeconds) {
        while (nextEndWarning < END_WARNING_TIMES.size() && END_WARNING_TIMES.get(nextEndWarning) > remainingSeconds) {
            nextEndWarning++;
        }
    }

    public void setElapsedSeconds(int seconds) {
        stopwatch.setElapsedSeconds(seconds);
        skipEndWarnings(getDurationSeconds() - seconds);
    }

    public int getElapsedSeconds() {
        return elapsedSeconds;
    }

    public int getSpeed() {
        return stopwatch.getSpeed();
    }

    public void setSpeed(int speed) {
        stopwatch.setSpeed(speed);
    }

    public int getDurationSeconds() {
        return game.getSettings().getDurationMinutes() * 60;
    }

    private static Component endWarningMessage(int remainingSeconds) {
        String remaining;
        if (remainingSeconds >= 60) {
            int minutes = remainingSeconds / 60;
            remaining = minutes + (minutes == 1 ? " minute restante" : " minutes restantes");
        } else {
            remaining = remainingSeconds + (remainingSeconds == 1 ? " seconde restante" : " secondes restantes");
        }
        return Component.textOfChildren(
                Component.text("[", NamedTextColor.GRAY),
                Component.text("BINGO", NamedTextColor.YELLOW),
                Component.text("] ", NamedTextColor.GRAY),
                Component.text(remaining, NamedTextColor.WHITE));
    }

    private void end() {
        task.cancel();
        task = null;
        game.setEtat(Game.Etat.ENDGAME);
        Bukkit.getLogger().log(Level.INFO, "Game over");
    }

    public int getSeconds() {
        return elapsedSeconds % 60;
    }

    public int getMinutes() {
        return elapsedSeconds / 60 % 60;
    }

    public int getHours() {
        return elapsedSeconds / 3600;
    }

    public boolean isRun() {
        return task != null;
    }
    public void stop() {
        if (isRun()) {
            end();
        }
    }
}
