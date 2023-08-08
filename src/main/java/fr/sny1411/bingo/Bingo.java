package fr.sny1411.bingo;

import fr.sny1411.bingo.commands.*;
import fr.sny1411.bingo.commands.completer.BonusCompleter;
import fr.sny1411.bingo.commands.completer.ForceValidCompleter;
import fr.sny1411.bingo.listener.ChallengesListener;
import fr.sny1411.bingo.listener.PlayerListener;
import fr.sny1411.bingo.listener.SetupListener;
import fr.sny1411.bingo.listener.gui.BingoGui;
import fr.sny1411.bingo.listener.gui.SettingsGui;
import fr.sny1411.bingo.listener.gui.TeamsGui;
import fr.sny1411.bingo.utils.Items;
import io.papermc.lib.PaperLib;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class Bingo extends JavaPlugin {
    private static Game game;
    private static final PlainTextComponentSerializer plainSerializer = PlainTextComponentSerializer.plainText();
    private static final PluginManager pluginManager = Bukkit.getServer().getPluginManager();

    private static final ChallengesListener challengesListener = new ChallengesListener();

    @Override
    public void onEnable() {
        PaperLib.suggestPaper(this);
        Items.init();
        Game.setBingoInstance(this);
        game = new Game();

        Objects.requireNonNull(getCommand("newGame")).setExecutor(new NewGame());
        Objects.requireNonNull(getCommand("start")).setExecutor(new Start(this));
        Objects.requireNonNull(getCommand("bingo")).setExecutor(new fr.sny1411.bingo.commands.Bingo());
        Objects.requireNonNull(getCommand("testPack")).setExecutor(new TestPack());
        Objects.requireNonNull(getCommand("valid")).setExecutor(new ForceValid());
        Objects.requireNonNull(getCommand("valid")).setTabCompleter(new ForceValidCompleter());
        Objects.requireNonNull(getCommand("stopGame")).setExecutor(new EndGame());
        Objects.requireNonNull(getCommand("result")).setExecutor(new Result());
        Objects.requireNonNull(getCommand("bonus")).setExecutor(new Bonus());
        Objects.requireNonNull(getCommand("bonus")).setTabCompleter(new BonusCompleter());

        pluginManager.registerEvents(new PlayerListener(), this);
        pluginManager.registerEvents(new SetupListener(), this);
        pluginManager.registerEvents(new TeamsGui(), this);
        pluginManager.registerEvents(new SettingsGui(), this);
        pluginManager.registerEvents(new BingoGui(), this);
    }

    public static Game getGame() {
        return game;
    }

    public static void setGame(Game game) {
        Bingo.game = game;
    }
    public static PlainTextComponentSerializer getPlainSerializer() {
        return plainSerializer;
    }
    public static void setListenChallenges() {
        pluginManager.registerEvents(challengesListener, Game.getBingoInstance());
    }
    public static void setNotListenChallenges() {
        HandlerList.unregisterAll(challengesListener);
    }
}
