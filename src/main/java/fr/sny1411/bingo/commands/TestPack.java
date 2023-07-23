package fr.sny1411.bingo.commands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class TestPack implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.showTitle(Title.title(Component.text("\uE001"), Component.text("")));
            player.playSound(player.getLocation(), Sound.MUSIC_DISC_WAIT, 3.0F, 0);
        }
        return false;
    }
}
