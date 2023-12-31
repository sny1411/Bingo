package fr.sny1411.bingo.utils;

import fr.sny1411.bingo.Bingo;
import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
import org.bukkit.GameRule;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Objects;

public final class Environment {
    private Environment() {
        throw new IllegalStateException("Utility class");
    }

    private static final List<World> worlds = Bukkit.getWorlds();

    public static void setGamerulesSetup(Bingo bingo) {
        Bukkit.getScheduler().runTask(bingo, () -> {
            for (World world : worlds) {
                Objects.requireNonNull(Bukkit.getWorld(world.getName())).setDifficulty(Difficulty.PEACEFUL);
                world.setGameRule(GameRule.DO_WEATHER_CYCLE, false);
                world.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
                world.setTime(1500);
            }
        });
    }

    public static void setGamerulesInGame(Bingo bingo) {
        Bukkit.getScheduler().runTask(bingo, () -> {
            for (World world : worlds) {
                Objects.requireNonNull(Bukkit.getWorld(world.getName())).setDifficulty(Difficulty.HARD);
                world.setGameRule(GameRule.DO_WEATHER_CYCLE, true);
                world.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, true);
                world.setGameRule(GameRule.SPECTATORS_GENERATE_CHUNKS, false);
                world.setGameRule(GameRule.ANNOUNCE_ADVANCEMENTS, false);
            }
        });
    }

    public static void clearPlayers() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.getInventory().clear();
        }
    }
}
