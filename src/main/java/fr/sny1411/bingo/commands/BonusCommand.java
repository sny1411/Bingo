package fr.sny1411.bingo.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.tree.LiteralCommandNode;
import fr.sny1411.bingo.Bingo;
import fr.sny1411.bingo.Game;
import fr.sny1411.bingo.commands.argument.ChallengeArgument;
import fr.sny1411.bingo.listener.challenges.ChallengeVerifier;
import fr.sny1411.bingo.utils.ChallengeId;
import fr.sny1411.bingo.utils.Text;
import fr.sny1411.bingo.utils.bonus.BonusEvent;
import fr.sny1411.bingo.utils.bonus.RewardsBonusEvent;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

import java.util.List;

public class BonusCommand {
    private BonusCommand() {
    }

    public static LiteralCommandNode<CommandSourceStack> node() {
        return Commands.literal("bonus")
                .requires(source -> source.getSender() instanceof Player)
                .then(Commands.argument("challenge", new ChallengeArgument())
                        .suggests((context, builder) -> CommandSuggestions.suggest(builder, activeBonuses()))
                        .executes(context -> {
                            run((Player) context.getSource().getSender(), context.getArgument("challenge", ChallengeId.class));
                            return Command.SINGLE_SUCCESS;
                        }))
                .build();
    }

    private static List<String> activeBonuses() {
        if (!Bingo.getGame().isDefiBonus() || Bingo.getGame().getEtat() != Game.Etat.INGAME) {
            return List.of();
        }
        return Bingo.getGame().getBonusEvents().stream()
                .filter(BonusEvent::isEnable)
                .map(event -> ChallengeArgument.format(event.getChallenge().getId()))
                .toList();
    }

    private static void run(Player player, ChallengeId challengeId) {
        if (Bingo.getGame().getEtat() != Game.Etat.INGAME) {
            player.sendMessage(Text.warning(Component.translatable("bingo.command.no_game")));
            return;
        }
        BonusEvent bonusEvent = findActiveBonus(challengeId);
        if (bonusEvent == null) {
            player.sendMessage(Text.warning(Component.translatable("bingo.bonus.not_active", Component.text(ChallengeArgument.format(challengeId)))));
            return;
        }
        if (ChallengeVerifier.verifChallenge(player, challengeId)) {
            RewardsBonusEvent.setBonus(bonusEvent.getChallenge(), player);
            bonusEvent.setEnable(false);
        }
    }

    private static BonusEvent findActiveBonus(ChallengeId challengeId) {
        for (BonusEvent event : Bingo.getGame().getBonusEvents()) {
            if (event.isEnable() && event.getChallenge().getId() == challengeId) {
                return event;
            }
        }
        return null;
    }
}
