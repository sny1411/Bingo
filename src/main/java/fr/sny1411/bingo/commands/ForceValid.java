package fr.sny1411.bingo.commands;

import fr.sny1411.bingo.Bingo;
import fr.sny1411.bingo.Game;
import fr.sny1411.bingo.listener.challenges.ChallengeProgress;
import fr.sny1411.bingo.utils.Challenge;
import fr.sny1411.bingo.utils.ChallengeId;
import fr.sny1411.bingo.utils.Team;
import fr.sny1411.bingo.utils.Text;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class ForceValid implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            return false;
        }
        Game.Etat etat = Bingo.getGame().getEtat();
        if (etat != Game.Etat.INGAME && etat != Game.Etat.ENDGAME) {
            sender.sendMessage(Text.warning(Component.translatable("bingo.command.no_game")));
            return false;
        }
        if (args.length != 3 || !(args[0].equalsIgnoreCase("add") || args[0].equalsIgnoreCase("remove"))) {
            sender.sendMessage(Text.warning(Component.translatable("bingo.valid.usage")));
            return false;
        }

        Team team = getTeamInGame(args[1]);
        if (team == null) {
            sender.sendMessage(Text.warning(Component.translatable("bingo.valid.unknown_team", Component.text(args[1]))));
            return false;
        }
        ChallengeId challengeId = ChallengeId.fromString(args[2].toUpperCase());
        Challenge challenge = challengeId == null ? null : Bingo.getGame().getChallenge(team, challengeId);
        if (challenge == null) {
            sender.sendMessage(Text.warning(Component.translatable("bingo.valid.unknown_challenge", Component.text(args[2]))));
            return false;
        }

        if (args[0].equalsIgnoreCase("add")) {
            if (challenge.getValidated()) {
                sender.sendMessage(Text.warning(Component.translatable("bingo.valid.already_validated", challenge.getName())));
            } else {
                ChallengeProgress.valideAndRealizeChallenge(team, challengeId);
            }
        } else {
            if (!challenge.getValidated()) {
                sender.sendMessage(Text.warning(Component.translatable("bingo.valid.not_validated", challenge.getName())));
            } else {
                challenge.setValidated(false);
                challenge.setRealized(false);
                Bingo.getGame().getTeamsScore().get(team).removeChallenge(challenge);
                sender.sendMessage(Component.translatable("bingo.valid.removed", NamedTextColor.WHITE, challenge.getName(), team.getColor().displayName()));
            }
        }
        return false;
    }

    private static Team getTeamInGame(String name) {
        for (Team team : Bingo.getGame().getTeamsScore().keySet()) {
            if (team.getColor().name().equalsIgnoreCase(name)) {
                return team;
            }
        }
        return null;
    }
}
