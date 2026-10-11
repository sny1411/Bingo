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
    private Timer() {
        throw new IllegalStateException("Utility class");
    }
    private static final List<Integer> END_WARNING_TIMES = List.of(10 * 60, 5 * 60, 3 * 60, 2 * 60, 60, 30, 15, 10, 5, 3, 2, 1); // remaining seconds

    // Real time rather than ticks, so the game lasts its real duration even if the server lags
    private static final Stopwatch stopwatch = new Stopwatch();
    private static int elapsedSeconds = 0;

    private static int maxMinutes = 0;
    private static int maxHours = 2;

    private static int stormTime; // in seconds
    private static boolean stormStarted;
    private static int nextEndWarning;

    private static BukkitTask task;

    public static void start(Bingo bingo) {
        stopwatch.start();
        elapsedSeconds = 0;
        nextEndWarning = 0;
        while (nextEndWarning < END_WARNING_TIMES.size() && END_WARNING_TIMES.get(nextEndWarning) >= getDurationSeconds()) {
            nextEndWarning++;
        }
        stormStarted = false;
        stormTime = Random.choice(60, 105) * 60;
        Bukkit.getLogger().log(Level.INFO, String.format("Storm planned at minute %d", stormTime / 60));

        task = Bukkit.getScheduler().runTaskTimer(bingo, Timer::tick, 1L, 1L);
    }

    private static void tick() {
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

    private static int getDurationSeconds() {
        return maxHours * 3600 + maxMinutes * 60;
    }

    private static void end() {
        task.cancel();
        task = null;
        Bingo.getGame().setEtat(Game.Etat.ENDGAME);
        Bukkit.getLogger().log(Level.INFO, "Game over");
    }

    public static int getMaxMinutes() {
        return maxMinutes;
    }

    public static void setMaxMinutes(int maxMinutes) {
        Timer.maxMinutes = maxMinutes;
    }

    public static int getMaxHours() {
        return maxHours;
    }

    public static void setMaxHours(int maxHours) {
        Timer.maxHours = maxHours;
    }

    public static int getSeconds() {
        return elapsedSeconds % 60;
    }

    public static int getMinutes() {
        return elapsedSeconds / 60 % 60;
    }

    public static int getHours() {
        return elapsedSeconds / 3600;
    }

    public static boolean isRun() {
        return task != null;
    }
    public static void stop() {
        if (isRun()) {
            end();
        }
    }
}
