package fr.sny1411.bingo.utils;

import fr.sny1411.bingo.Bingo;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.time.Duration;
import java.util.List;

public final class Countdown {
    private Countdown() {
        throw new IllegalStateException("Utility class");
    }

    private static final int SECONDS = 3;
    private static final List<NamedTextColor> COLORS = List.of(NamedTextColor.DARK_BLUE, NamedTextColor.BLUE, NamedTextColor.AQUA); // 1, 2, 3
    public static final Title.Times TITLE_TIMES = Title.Times.times(Duration.ZERO, Duration.ofSeconds(1), Duration.ZERO);

    private static BukkitTask task;
    private static int remaining;

    public static void start(Bingo bingo, Runnable onEnd) {
        remaining = SECONDS;
        task = Bukkit.getScheduler().runTaskTimer(bingo, () -> tick(onEnd), 0L, 20L);
    }

    private static void tick(Runnable onEnd) {
        if (remaining > 0) {
            showTitle(Title.title(Component.text(remaining, COLORS.get(remaining - 1)), Component.empty(), TITLE_TIMES));
            remaining--;
            return;
        }
        cancel();
        onEnd.run();
    }

    public static void cancel() {
        if (task != null) {
            task.cancel();
            task = null;
        }
    }

    public static void showTitle(Title title) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.showTitle(title);
        }
    }
}
