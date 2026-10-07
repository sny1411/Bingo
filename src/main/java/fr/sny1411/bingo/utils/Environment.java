package fr.sny1411.bingo.utils;

import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
import org.bukkit.GameRules;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Objects;

public final class Environment {
    private Environment() {
        throw new IllegalStateException("Utility class");
    }

    private static final List<World> worlds = Bukkit.getWorlds();

    public static void setGamerulesSetup() {
        for (World world : worlds) {
            Objects.requireNonNull(Bukkit.getWorld(world.getName())).setDifficulty(Difficulty.PEACEFUL);
            world.setGameRule(GameRules.ADVANCE_WEATHER, false);
            world.setGameRule(GameRules.ADVANCE_TIME, false);
            // Only the overworld has a clock: setting the time of the Nether or the End throws
            if (world.getEnvironment() == World.Environment.NORMAL) {
                world.setTime(1500);
            }
        }
    }

    public static void setGamerulesInGame() {
        for (World world : worlds) {
            Objects.requireNonNull(Bukkit.getWorld(world.getName())).setDifficulty(Difficulty.HARD);
            world.setGameRule(GameRules.ADVANCE_WEATHER, true);
            world.setGameRule(GameRules.ADVANCE_TIME, true);
            world.setGameRule(GameRules.SPECTATORS_GENERATE_CHUNKS, false);
            world.setGameRule(GameRules.SHOW_ADVANCEMENT_MESSAGES, false);
        }
    }

    public static void clearPlayers() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.getInventory().clear();
        }
    }
}
