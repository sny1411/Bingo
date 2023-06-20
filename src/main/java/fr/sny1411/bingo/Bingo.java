package fr.sny1411.bingo;

import io.papermc.lib.PaperLib;
import org.bukkit.plugin.java.JavaPlugin;

public final class Bingo extends JavaPlugin {

    @Override
    public void onEnable() {
        PaperLib.suggestPaper(this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
