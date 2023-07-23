package fr.sny1411.bingo.commands.completer;

import fr.sny1411.bingo.utils.Challenge;
import fr.sny1411.bingo.utils.Grid;
import fr.sny1411.bingo.utils.Team;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ForceValidCompleter implements TabCompleter {

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        List<String> list = new ArrayList<>();
        if (args.length == 1) {
            list.add("add");
            list.add("remove");
        } else if (args.length == 2) {
            for (Team.Color color : Team.getTeams().keySet()) {
                list.add(color.name());
            }
        } else if (args.length == 3) {
            for (Challenge[] challengeList : Grid.getGameGrid().getGrid()) {
                for (Challenge challenge : challengeList) {
                    list.add(challenge.getName().replace(" ", "_").substring(4));
                }
            }
        }
        return list;
    }
}
