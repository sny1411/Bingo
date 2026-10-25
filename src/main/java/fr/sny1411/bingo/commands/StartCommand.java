package fr.sny1411.bingo.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.tree.LiteralCommandNode;
import fr.sny1411.bingo.Bingo;
import fr.sny1411.bingo.Game;
import fr.sny1411.bingo.utils.*;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class StartCommand {
    private final Bingo bingo;

    private StartCommand(Bingo bingo) {
        this.bingo = bingo;
    }

    public static LiteralCommandNode<CommandSourceStack> node(Bingo bingo) {
        StartCommand start = new StartCommand(bingo);
        return Commands.literal("start")
                .requires(source -> source.getSender() instanceof Player && source.getSender().hasPermission("bingo.command.start"))
                .executes(context -> {
                    start.run(context.getSource().getSender());
                    return Command.SINGLE_SUCCESS;
                })
                .build();
    }

    private void run(CommandSender sender) {
        if (Bingo.getGame().getEtat() != Game.Etat.SETUP) {
            return;
        }
        if (Bingo.getGame().getSettings().isGridIncomplete()) {
            sender.sendMessage(gridIncompleteMessage());
            return;
        }
        Bingo.getGame().createGrids();
        if (isTeamComplete()) {
            Bingo.getGame().setEtat(Game.Etat.STARTING);
            Countdown.start(bingo, this::startGame);
        } else {
            sender.sendMessage(Text.warning(Component.translatable("bingo.start.players_without_team")));
        }
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
        return Text.warning(Component.translatable("bingo.start.grid_incomplete", Component.text(Bingo.getGame().getSettings().getNbChallengesInGrid()), Component.text(Grid.NB_CHALLENGES)));
    }

    private static boolean isTeamComplete() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (Bingo.getGame().getTeams().getTeam(player) == null) {
                player.sendMessage(Text.warning(Component.translatable("bingo.start.join_a_team")));
                return false;
            }
        }
        return true;
    }
}
