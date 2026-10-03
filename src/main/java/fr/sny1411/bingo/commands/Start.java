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
import org.bukkit.scheduler.BukkitScheduler;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Start implements CommandExecutor {
    private static final List<String> colorStart = new ArrayList<>(Arrays.asList("§b", "§9", "§1"));
    private static final Title.Times timesTitle = Title.Times.times(Duration.ZERO, Duration.ofSeconds(1), Duration.ZERO);

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
        BukkitScheduler scheduler = Bukkit.getScheduler();
        for (int i = 3; i > 0; i--) {
            Title title = Title.title(Component.text(colorStart.get(3 - i) + i), Component.text(""), timesTitle);
            scheduler.runTaskLater(bingo, () -> showTitle(title), (3L - i) * 20L);
        }
        scheduler.runTaskLater(bingo, this::startGame, 60L);
    }

    private void startGame() {
        showTitle(Title.title(Component.text("\uE005"), Component.text(""), timesTitle));
        Spawn.remove();
        Game game = Bingo.getGame();
        game.setEtat(Game.Etat.INGAME);
        Environment.clearPlayers();
        Environment.setGamerulesInGame();

        Timer.start(bingo);
        ScoreBoard.createScoreBoard(bingo);
        Score.init();

        for (Player player : Team.getTeams().get(Team.Color.SPECTATOR).getPlayers()) {
            if (player.isOnline()) {
                player.setGameMode(GameMode.SPECTATOR);
            }
        }

        if (Bingo.getGame().isDefiBonus()) {
            BonusEvent.init();
        }

        Bukkit.getScheduler().runTaskLater(bingo, () -> game.setPlayersDamage(true), 30 * 20L);
    }

    private static void showTitle(Title title) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.showTitle(title);
        }
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
