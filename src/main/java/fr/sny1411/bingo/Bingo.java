package fr.sny1411.bingo;

import fr.sny1411.bingo.commands.*;
import fr.sny1411.bingo.commands.completer.ForceValidCompleter;
import fr.sny1411.bingo.listener.ChallengesListener;
import fr.sny1411.bingo.listener.PlayerListener;
import fr.sny1411.bingo.listener.SetupListener;
import fr.sny1411.bingo.listener.gui.BingoGui;
import fr.sny1411.bingo.listener.gui.SettingsGui;
import fr.sny1411.bingo.listener.gui.TeamsGui;
import fr.sny1411.bingo.utils.Items;
import io.papermc.lib.PaperLib;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
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
        PluginManager pluginManager = Bukkit.getServer().getPluginManager();

        Objects.requireNonNull(getCommand("newGame")).setExecutor(new NewGame());
        Objects.requireNonNull(getCommand("start")).setExecutor(new Start(this));
        Objects.requireNonNull(getCommand("bingo")).setExecutor(new fr.sny1411.bingo.commands.Bingo());
        Objects.requireNonNull(getCommand("testPack")).setExecutor(new TestPack());
        Objects.requireNonNull(getCommand("valid")).setExecutor(new ForceValid());
        Objects.requireNonNull(getCommand("valid")).setTabCompleter(new ForceValidCompleter());
        Objects.requireNonNull(getCommand("stopGame")).setExecutor(new EndGame());

        pluginManager.registerEvents(new PlayerListener(), this);
        pluginManager.registerEvents(new SetupListener(), this);
        pluginManager.registerEvents(new TeamsGui(), this);
        pluginManager.registerEvents(new SettingsGui(), this);
        pluginManager.registerEvents(new BingoGui(), this);
        pluginManager.registerEvents(new ChallengesListener(), this);
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
