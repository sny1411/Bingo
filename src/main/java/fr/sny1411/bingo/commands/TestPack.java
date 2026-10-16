package fr.sny1411.bingo.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class TestPack {
    private TestPack() {
    }

    public static LiteralCommandNode<CommandSourceStack> node() {
        return Commands.literal("testpack")
                .requires(source -> source.getSender().hasPermission("bingo.testPack"))
                .executes(context -> {
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        player.showTitle(Title.title(Component.text("\uE001"), Component.text("")));
                        player.playSound(player.getLocation(), Sound.MUSIC_DISC_WAIT, 3.0F, 0);
                    }
                    return Command.SINGLE_SUCCESS;
                })
                .build();
    }
}
