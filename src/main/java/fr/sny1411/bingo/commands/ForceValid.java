package fr.sny1411.bingo.commands;

import fr.sny1411.bingo.utils.Grid;
import fr.sny1411.bingo.utils.Score;
import fr.sny1411.bingo.utils.Team;
import net.kyori.adventure.text.Component;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class ForceValid implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (sender instanceof Player) {
            String mode = args[0];
            String teamStr = args[1];
            String challengeName = args[2].replace("_"," ");

            Boolean modeAdd = null;
            if (mode.equals("add")) {
                modeAdd = true;
            } else if (mode.equals("remove")) {
                modeAdd = false;
            } else {
                sender.sendMessage(Component.text("§8[§c⚠§8] §fErreur de syntaxe"));
            }

            if (modeAdd != null) {
                Team team = Team.getTeams().get(Team.Color.valueOf(teamStr));
                challengeName = "§d§l" + challengeName;
                Score.getTeamsScore().get(team).addChallenge(Objects.requireNonNull(Grid.getChallenge(team, challengeName)));
                Objects.requireNonNull(Grid.getChallenge(team, challengeName)).setValidated(true);
            }
        }
        return false;
    }
}
