package fr.sny1411.bingo.commands;

import fr.sny1411.bingo.Bingo;
import fr.sny1411.bingo.Game;
import fr.sny1411.bingo.utils.*;
import fr.sny1411.bingo.utils.bonus.BonusEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
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
            if (isTeamComplete()) {
                launchGame();
            } else {
                sender.sendMessage(Component.text("§8[§c⚠§8] §fDes joueurs ne possèdent pas d'équipe"));
            }
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
            for (Player player : Bukkit.getOnlinePlayers()) {
                player.showTitle(Title.title(Component.text("\uE005"), Component.text(""), timesTitle));
            }
            Spawn.remove(bingo);
            Game game = Bingo.getGame();
            game.setEtat(Game.Etat.INGAME);
            Environment.clearPlayers();
            Environment.setGamerulesInGame(bingo);

            Timer.start(bingo);
            ScoreBoard.createScoreBoard(bingo);
            Score.init();

            for (Player player : Team.getTeams().get(Team.Color.SPECTATOR).getPlayers()) {
                if (player.isOnline()) {
                    Bukkit.getScheduler().runTask(bingo, () ->  player.setGameMode(GameMode.SPECTATOR));
                }
            }

            if (Bingo.getGame().isDefiBonus()) {
                BonusEvent.init();
            }

            try {
                TimeUnit.SECONDS.sleep(30);
            } catch (InterruptedException e) {
                e.printStackTrace();
                Thread.currentThread().interrupt();
            }

            game.setPlayersDamage(true);
        });
    }

    private static boolean isTeamComplete() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (Team.getTeam(player) == null) {
                player.sendMessage(Component.text("§8[§c⚠§8] §fVeuillez rejoindre une équipe"));
                return false;
            }
        }
        return true;
    }
}
