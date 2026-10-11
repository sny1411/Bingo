package fr.sny1411.bingo;

import fr.sny1411.bingo.commands.*;
import fr.sny1411.bingo.commands.completer.BonusCompleter;
import fr.sny1411.bingo.commands.completer.ForceValidCompleter;
import fr.sny1411.bingo.commands.completer.TimerCompleter;
import fr.sny1411.bingo.i18n.Translations;
import fr.sny1411.bingo.listener.challenges.AdvancementChallengesListener;
import fr.sny1411.bingo.listener.challenges.MobChallengesListener;
import fr.sny1411.bingo.listener.challenges.PlayerChallengesListener;
import fr.sny1411.bingo.listener.challenges.WorldChallengesListener;
import fr.sny1411.bingo.listener.PlayerListener;
import fr.sny1411.bingo.listener.SetupListener;
import fr.sny1411.bingo.listener.gui.BingoGui;
import fr.sny1411.bingo.listener.gui.SettingsGui;
import fr.sny1411.bingo.listener.gui.TeamsGui;
import fr.sny1411.bingo.utils.Items;
import fr.sny1411.bingo.utils.Spawn;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.jar.JarFile;
import java.util.logging.Level;

public final class Bingo extends JavaPlugin {
    private static Game game;
    private static final PlainTextComponentSerializer plainSerializer = PlainTextComponentSerializer.plainText();
    private static final PluginManager pluginManager = Bukkit.getServer().getPluginManager();

    private static final List<Listener> challengesListeners = List.of(
            new MobChallengesListener(),
            new PlayerChallengesListener(),
            new AdvancementChallengesListener(),
            new WorldChallengesListener());

    @Override
    public void onEnable() {
        try (JarFile jar = new JarFile(getFile())) {
            Translations.load(jar, getLogger());
        } catch (IOException e) {
            getLogger().log(Level.SEVERE, "Could not read the plugin jar to load the translations", e);
        }
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
        Objects.requireNonNull(getCommand("spec")).setExecutor(new Spec());
        Objects.requireNonNull(getCommand("timer")).setExecutor(new TimerCommand());
        Objects.requireNonNull(getCommand("timer")).setTabCompleter(new TimerCompleter());

        pluginManager.registerEvents(new PlayerListener(), this);
        pluginManager.registerEvents(new SetupListener(), this);
        pluginManager.registerEvents(new TeamsGui(), this);
        pluginManager.registerEvents(new SettingsGui(), this);
        pluginManager.registerEvents(new BingoGui(), this);

        Bukkit.getScheduler().runTaskTimer(this, Spawn::updateSettingsItems, 20L, 20L);
    }

    @Override
    public void onDisable() {
        Translations.unload();
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
        for (Listener listener : challengesListeners) {
            pluginManager.registerEvents(listener, Game.getBingoInstance());
        }
    }
    public static void setNotListenChallenges() {
        for (Listener listener : challengesListeners) {
            HandlerList.unregisterAll(listener);
        }
    }
}
