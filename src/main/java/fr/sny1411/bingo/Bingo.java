package fr.sny1411.bingo;

import fr.sny1411.bingo.commands.NewGame;
import fr.sny1411.bingo.listener.PlayerListener;
import fr.sny1411.bingo.listener.SetupListener;
import fr.sny1411.bingo.listener.gui.SettingsGui;
import fr.sny1411.bingo.listener.gui.TeamsGui;
import fr.sny1411.bingo.utils.Items;
import io.papermc.lib.PaperLib;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class Bingo extends JavaPlugin {
    private static Game game;

    @Override
    public void onEnable() {
        PaperLib.suggestPaper(this);
        Items.init();
        Game.setBingoInstance(this);
        game = new Game();

        Objects.requireNonNull(getCommand("newGame")).setExecutor(new NewGame());
        Objects.requireNonNull(getCommand("start")).setExecutor(new NewGame());

        Bukkit.getServer().getPluginManager().registerEvents(new PlayerListener(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new SetupListener(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new TeamsGui(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new SettingsGui(), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static Game getGame() {
        return game;
    }

    public static void setGame(Game game) {
        Bingo.game = game;
    }
}
