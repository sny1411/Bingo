package fr.sny1411.bingo.commands;

import fr.sny1411.bingo.Bingo;
import fr.sny1411.bingo.Game;
import fr.sny1411.bingo.utils.*;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class Start implements CommandExecutor {
    private final Bingo bingo;

    public Start(Bingo bingo) {
        this.bingo = bingo;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String str, @NotNull String[] args) {
        if (sender instanceof Player && (Bingo.getGame().getEtat() == Game.Etat.SETUP)) {
            if (Bingo.getGame().getSettings().verifSettingsToHigh()) {
                sender.sendMessage(gridIncompleteMessage());
                return false;
            }
            Bingo.getGame().createGrids();
            if (isTeamComplete()) {
                Bingo.getGame().setEtat(Game.Etat.STARTING);
                Countdown.start(bingo, this::startGame);
            } else {
                sender.sendMessage(Component.text("§8[§c⚠§8] §fDes joueurs ne possèdent pas d'équipe"));
            }
        }
        return false;
    }

    private void startGame() {
        Countdown.showTitle(Title.title(Component.text("\uE005"), Component.text(""), Countdown.TITLE_TIMES));
        Spawn.remove();
        Game game = Bingo.getGame();
        game.setEtat(Game.Etat.INGAME);
        Environment.clearPlayers();
        Environment.setGamerulesInGame();

        game.getTimer().start(bingo);
        ScoreBoard.createScoreBoard(bingo);
        game.initScores();

        for (Player player : Bingo.getGame().getTeams().get(Team.Color.SPECTATOR).getOnlinePlayers()) {
            player.setGameMode(GameMode.SPECTATOR);
        }

        if (Bingo.getGame().isDefiBonus()) {
            game.initBonusEvents();
        }

        Bukkit.getScheduler().runTaskLater(bingo, () -> game.setPlayersDamage(true), 30 * 20L);
    }

    private static Component gridIncompleteMessage() {
        return Component.textOfChildren(
                Component.text("[", NamedTextColor.DARK_GRAY),
                Component.text("⚠", NamedTextColor.RED),
                Component.text("] ", NamedTextColor.DARK_GRAY),
                Component.text("La grille a " + Bingo.getGame().getSettings().getMaxTotal() + " défis sur " + Grid.NB_CHALLENGES + " : ajoutes-en dans les paramètres de la grille", NamedTextColor.WHITE));
    }

    private static boolean isTeamComplete() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (Bingo.getGame().getTeams().getTeam(player) == null) {
                player.sendMessage(Component.text("§8[§c⚠§8] §fVeuillez rejoindre une équipe"));
                return false;
            }
        }
        return true;
    }
}
