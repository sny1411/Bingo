package fr.sny1411.bingo.commands.completer;

import fr.sny1411.bingo.Bingo;
import fr.sny1411.bingo.utils.bonus.BonusEvent;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class BonusCompleter implements TabCompleter {
    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (Bingo.getGame().isDefiBonus()) {
            List<String> listBonusEnable = new ArrayList<>();
            for (BonusEvent event : BonusEvent.getEvents()) {
                if (event.isEnable()) {
                    listBonusEnable.add(event.getChallenge().getName().substring(4).replace(" ", "_"));
                }
            }
            return listBonusEnable;
        }
        return null;
    }
}
