package fr.sny1411.bingo.commands;

import fr.sny1411.bingo.Bingo;
import fr.sny1411.bingo.Game;
import fr.sny1411.bingo.utils.Environment;
import fr.sny1411.bingo.utils.Grid;
import fr.sny1411.bingo.utils.Spawn;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Start implements CommandExecutor {
    private final Bingo bingo;

    public Start(Bingo bingo) {
        this.bingo = bingo;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String str, @NotNull String[] args) {
        if (sender instanceof Player && (Bingo.getGame().getEtat() == Game.Etat.SETUP)) {
            Grid.createGrids();
            launchGame();
            // TODO lancement compteur

        }
        return false;
    }

    private void launchGame() {
        Bukkit.getScheduler().runTaskAsynchronously(bingo, () -> {
            List<String> colorStart = new ArrayList<>(Arrays.asList("§b", "§9", "§1"));
            Title.Times timesTitle = Title.Times.times(Duration.ZERO, Duration.ofSeconds((long) 1.0), Duration.ZERO);
            for (int i = 3; i > 0; i--) {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    player.showTitle(Title.title(Component.text(colorStart.get(3 - i) + i), Component.text(""), timesTitle));
                }
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    Thread.currentThread().interrupt();
                }
            }
            Bukkit.getScheduler().runTask(bingo, Spawn::remove);
            Game game = Bingo.getGame();
            game.setEtat(Game.Etat.INGAME);
            Environment.clearPlayers();
            Environment.setGamerulesInGame();

            try {
                TimeUnit.SECONDS.sleep(30);
            } catch (InterruptedException e) {
                e.printStackTrace();
                Thread.currentThread().interrupt();
            }

            game.setPlayersDamage(true);
        });
    }
}
