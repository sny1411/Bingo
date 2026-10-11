package fr.sny1411.bingo.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class Team {
    public enum Color {
        ORANGE("orange", NamedTextColor.GOLD, Material.ORANGE_BANNER, Material.ORANGE_CONCRETE),
        ROUGE("red", NamedTextColor.RED, Material.RED_BANNER, Material.RED_CONCRETE),
        VIOLET("purple", NamedTextColor.DARK_PURPLE, Material.PURPLE_BANNER, Material.PURPLE_CONCRETE),
        ROSE("pink", NamedTextColor.LIGHT_PURPLE, Material.PINK_BANNER, Material.PINK_CONCRETE),
        VERT("green", NamedTextColor.GREEN, Material.LIME_BANNER, Material.LIME_CONCRETE),
        BLEU("blue", NamedTextColor.AQUA, Material.LIGHT_BLUE_BANNER, Material.LIGHT_BLUE_CONCRETE),
        SPECTATOR("spectator", NamedTextColor.GRAY, Material.ENDER_EYE, null);

        private final String key;
        private final NamedTextColor textColor;
        private final Material materialTeamGui;
        private final Material materialBingoGui;

        Color(String key, NamedTextColor textColor, Material materialTeamGui, Material materialBingoGui) {
            this.key = key;
            this.textColor = textColor;
            this.materialTeamGui = materialTeamGui;
            this.materialBingoGui = materialBingoGui;
        }

        public Component displayName() {
            return Component.translatable("bingo.team." + key, textColor);
        }

        // The name of a player of this team, in the tab list
        public Component playerName(String playerName) {
            if (this == SPECTATOR) {
                return Component.textOfChildren(Component.text("[SPEC] ", NamedTextColor.DARK_GRAY), Component.text(playerName, textColor, TextDecoration.ITALIC));
            }
            return Component.text(playerName, textColor);
        }

        public Material getMaterialTeamGui() {
            return materialTeamGui;
        }

        public Material getMaterialBingoGui() {
            return materialBingoGui;
        }
    }

    // UUIDs rather than Player objects, which are replaced when a player reconnects
    private final Set<UUID> playerIds;
    private final Color color;
    private boolean gameFinish;

    Team(Color color) {
        this.color = color;
        gameFinish = false;
        playerIds = new LinkedHashSet<>();
    }

    void addPlayer(Player player) {
        playerIds.add(player.getUniqueId());
    }

    void removePlayer(Player player) {
        playerIds.remove(player.getUniqueId());
    }

    public boolean contains(Player player) {
        return playerIds.contains(player.getUniqueId());
    }

    public int size() {
        return playerIds.size();
    }

    public Color getColor() {
        return color;
    }

    public List<OfflinePlayer> getPlayers() {
        List<OfflinePlayer> players = new ArrayList<>();
        for (UUID playerId : playerIds) {
            players.add(Bukkit.getOfflinePlayer(playerId));
        }
        return players;
    }

    public List<Player> getOnlinePlayers() {
        List<Player> players = new ArrayList<>();
        for (UUID playerId : playerIds) {
            Player player = Bukkit.getPlayer(playerId);
            if (player != null) {
                players.add(player);
            }
        }
        return players;
    }

    public boolean isGameFinish() {
        return gameFinish;
    }

    public void setGameFinish(boolean gameFinish) {
        this.gameFinish = gameFinish;
    }

    public void sendMessage(String message) {
        sendMessage(Component.text(message));
    }

    public void sendMessage(Component message) {
        for (Player player : getOnlinePlayers()) {
            player.sendMessage(message);
        }
    }
}
