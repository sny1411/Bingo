package fr.sny1411.bingo.utils;

import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
import org.bukkit.GameRule;
import org.bukkit.World;

import java.util.List;
import java.util.Objects;

public abstract class Environnement {

    private static final List<World> worlds = Bukkit.getWorlds();

    public void setGamerule() {
        for (World world : worlds) {
            Objects.requireNonNull(Bukkit.getWorld(world.getName())).setDifficulty(Difficulty.HARD);
            world.setGameRule(GameRule.DO_WEATHER_CYCLE, true);
            world.setGameRule(GameRule.SPECTATORS_GENERATE_CHUNKS, false);
        }
    }
}
