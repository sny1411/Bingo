package fr.sny1411.bingo.utils;

import fr.sny1411.bingo.Bingo;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

public class Timer {
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
            "§7[§eBINGO§7] §f1 secondes restantes"));


    private static int seconds = 0;
    private static int minutes = 0;
    private static int hours = 0;

    private static int maxMinutes = 0;
    private static int maxHours = 2;

    private static boolean run;

    public static void start(Bingo bingo) {
        Bukkit.getScheduler().runTaskAsynchronously(bingo, () -> {
            int timeOrageLaunch = (int) (Math.random() * ((105 - 60) + 1)) + 60;
            Bukkit.getLogger().log(Level.INFO, String.format("orage : %d", timeOrageLaunch));

            int timeInsecond;
            int compteurMsgFin = 0;
            run = true;
            while ((hours < maxHours || minutes < maxMinutes) && run) {
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    Thread.currentThread().interrupt();
                }
                if (seconds < 59) {
                    seconds++;
                } else {
                    seconds = 0;
                    if (minutes < 59) {
                        minutes++;
                    } else {
                        minutes = 0;
                        hours++;
                    }
                }

                timeInsecond = (hours * 3600 + minutes * 60 + seconds);

                if (timeMessageFin.get(compteurMsgFin) == timeInsecond) {
                    Bukkit.broadcast(Component.text(messagesFin.get(compteurMsgFin)));
                    compteurMsgFin++;
                }

                if (timeOrageLaunch * 60 == timeInsecond) {
                    Bukkit.getScheduler().scheduleSyncDelayedTask(bingo, () -> {
                        Bukkit.getServer().getWorlds().get(0).setWeatherDuration(8400); // 7 minutes (en ticks)
                    });
                }

                // TODO : manque event défi bonus
            }
        });
        run = false;

        // TODO : fin du jeu
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
        return seconds;
    }

    public static int getMinutes() {
        return minutes;
    }

    public static int getHours() {
        return hours;
    }

    public static boolean isRun() {
        return run;
    }
}
