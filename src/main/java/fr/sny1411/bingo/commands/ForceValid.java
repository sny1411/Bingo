package fr.sny1411.bingo.commands;

import fr.sny1411.bingo.Bingo;
import fr.sny1411.bingo.Game;
import fr.sny1411.bingo.listener.ChallengesListener;
import fr.sny1411.bingo.utils.Challenge;
import fr.sny1411.bingo.utils.ChallengeId;
import fr.sny1411.bingo.utils.Grid;
import fr.sny1411.bingo.utils.Score;
import fr.sny1411.bingo.utils.Team;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class ForceValid implements CommandExecutor {
    private static final String USAGE = "Utilisation : /valid <add|remove> <équipe> <défi>";

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            return false;
        }
        Game.Etat etat = Bingo.getGame().getEtat();
        if (etat != Game.Etat.INGAME && etat != Game.Etat.ENDGAME) {
            sender.sendMessage(warning("Aucune partie en cours"));
            return false;
        }
        if (args.length != 3 || !(args[0].equalsIgnoreCase("add") || args[0].equalsIgnoreCase("remove"))) {
            sender.sendMessage(warning(USAGE));
            return false;
        }

        Team team = getTeamInGame(args[1]);
        if (team == null) {
            sender.sendMessage(warning("L'équipe " + args[1] + " n'est pas dans la partie"));
            return false;
        }
        ChallengeId challengeId = ChallengeId.fromString(args[2].toUpperCase());
        Challenge challenge = challengeId == null ? null : Grid.getChallenge(team, challengeId);
        if (challenge == null) {
            sender.sendMessage(warning("Le défi " + args[2] + " n'est pas dans la grille"));
            return false;
        }

        if (args[0].equalsIgnoreCase("add")) {
            if (challenge.getValidated()) {
                sender.sendMessage(warning("Le défi " + challenge.getName() + " est déjà validé"));
            } else {
                ChallengesListener.valideAndRealizeChallenge(team, challengeId);
            }
        } else {
            if (!challenge.getValidated()) {
                sender.sendMessage(warning("Le défi " + challenge.getName() + " n'est pas validé"));
            } else {
                challenge.setValidated(false);
                challenge.setRealized(false);
                Score.getTeamsScore().get(team).removeChallenge(challenge);
                sender.sendMessage(Component.text("Le défi " + challenge.getName() + " n'est plus validé pour l'équipe " + team.getColor().getNom(), NamedTextColor.WHITE));
            }
        }
        return false;
    }

    private static Team getTeamInGame(String name) {
        for (Team team : Score.getTeamsScore().keySet()) {
            if (team.getColor().name().equalsIgnoreCase(name)) {
                return team;
            }
        }
        return null;
    }

    private static Component warning(String message) {
        return Component.textOfChildren(
                Component.text("[", NamedTextColor.DARK_GRAY),
                Component.text("⚠", NamedTextColor.RED),
                Component.text("] ", NamedTextColor.DARK_GRAY),
                Component.text(message, NamedTextColor.WHITE));
    }
}
