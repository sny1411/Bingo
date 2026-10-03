package fr.sny1411.bingo.utils;

import fr.sny1411.bingo.Bingo;
import fr.sny1411.bingo.Game;
import fr.sny1411.bingo.utils.bonus.BonusEvent;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

public class Timer {
    private Timer() {
        throw new IllegalStateException("Utility class");
    }
    private static final List<Integer> timeMessageFin = new ArrayList<>(Arrays.asList(110 * 60, 115 * 60, 117 * 60, 118 * 60, 119 * 60, 119 * 60 + 30, 119 * 60 + 45, 119 * 60 + 50, 119 * 60 + 55, 119 * 60 + 57, 119 * 60 + 58, 119 * 60 + 59)); // en secondes
    private static final List<String> messagesFin = new ArrayList<>(Arrays.asList("§7[§eBINGO§7] §f10 minutes restantes",
            "§7[§eBINGO§7] §f5 minutes restantes",
            "§7[§eBINGO§7] §f3 minutes restantes",
            "§7[§eBINGO§7] §f2 minutes restantes",
            "§7[§eBINGO§7] §f1 minute restante",
            "§7[§eBINGO§7] §f30 secondes restantes",
            "§7[§eBINGO§7] §f15 secondes restantes",
            "§7[§eBINGO§7] §f10 secondes restantes",
            "§7[§eBINGO§7] §f5 secondes restantes",
            "§7[§eBINGO§7] §f3 secondes restantes",
            "§7[§eBINGO§7] §f2 secondes restantes",
            "§7[§eBINGO§7] §f1 seconde restante"));

    // Elapsed time is read from the clock, not counted in ticks, so the game lasts its real duration even if the server lags
    private static long startNanos;
    private static int elapsedSeconds = 0;

    private static int maxMinutes = 0;
    private static int maxHours = 2;

    private static int timeOrageLaunch; // en secondes
    private static boolean orageLaunched;
    private static int compteurMsgFin;

    private static BukkitTask task;

    public static void start(Bingo bingo) {
        if (task != null) {
            task.cancel();
        }
        startNanos = System.nanoTime();
        elapsedSeconds = 0;
        compteurMsgFin = 0;
        orageLaunched = false;
        timeOrageLaunch = Random.choice(60, 105) * 60;
        Bukkit.getLogger().log(Level.INFO, String.format("orage : %d", timeOrageLaunch / 60));

        task = Bukkit.getScheduler().runTaskTimer(bingo, Timer::tick, 1L, 1L);
    }

    private static void tick() {
        int timeInsecond = (int) TimeUnit.NANOSECONDS.toSeconds(System.nanoTime() - startNanos);
        if (timeInsecond == elapsedSeconds) {
            return;
        }
        elapsedSeconds = timeInsecond;

        // >= rather than ==: a lag spike can skip a second
        while (compteurMsgFin < timeMessageFin.size() && timeMessageFin.get(compteurMsgFin) <= timeInsecond) {
            Bukkit.broadcast(Component.text(messagesFin.get(compteurMsgFin)));
            compteurMsgFin++;
        }

        if (!orageLaunched && timeOrageLaunch <= timeInsecond) {
            orageLaunched = true;
            Bukkit.getLogger().log(Level.INFO, "ORAGE MAINTENANT");
            World world = Bukkit.getWorlds().get(0);
            world.setStorm(true);
            world.setThundering(true);
            world.setWeatherDuration(8400); // 7 minutes (en ticks)
        }

        for (BonusEvent event : BonusEvent.getEvents()) {
            if (!event.isEnable() && event.getTimeLaunch() * 60 <= timeInsecond) {
                event.setEnable(true);
            }
        }

        if (timeInsecond >= maxHours * 3600 + maxMinutes * 60) {
            end();
        }
    }

    private static void end() {
        task.cancel();
        task = null;
        Bingo.getGame().setEtat(Game.Etat.ENDGAME);
        Bukkit.getLogger().log(Level.INFO, "Fin du jeu !");
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
