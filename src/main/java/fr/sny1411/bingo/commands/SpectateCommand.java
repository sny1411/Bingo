package fr.sny1411.bingo.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.tree.LiteralCommandNode;
import fr.sny1411.bingo.Bingo;
import fr.sny1411.bingo.Game;
import fr.sny1411.bingo.utils.Team;
import fr.sny1411.bingo.utils.Text;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import java.util.Objects;

public class SpectateCommand {
    private SpectateCommand() {
    }

    public static LiteralCommandNode<CommandSourceStack> node() {
        return Commands.literal("spectate")
                .requires(source -> source.getSender() instanceof Player)
                .executes(context -> {
                    run((Player) context.getSource().getSender());
                    return Command.SINGLE_SUCCESS;
                })
                .build();
    }

    private static void run(Player player) {
        if (Bingo.getGame().getEtat() == Game.Etat.INGAME) {
            if (Objects.requireNonNull(Bingo.getGame().getTeams().getTeam(player)).isGameFinish()) {
                player.setGameMode(GameMode.SPECTATOR);
                Bingo.getGame().getTeams().join(player, Team.Color.SPECTATOR);
            } else {
                player.sendMessage(Text.warning(Component.translatable("bingo.spec.not_finished", NamedTextColor.RED)));
            }
        }
    }
}
