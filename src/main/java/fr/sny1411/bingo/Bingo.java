package fr.sny1411.bingo;

import fr.sny1411.bingo.commands.NewGame;
import io.papermc.lib.PaperLib;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class Bingo extends JavaPlugin {
    private static Game game;

    @Override
    public void onEnable() {
        PaperLib.suggestPaper(this);
        game = new Game();

        Objects.requireNonNull(getCommand("newGame")).setExecutor(new NewGame());
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
