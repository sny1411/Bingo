package fr.sny1411.bingo.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
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
        ORANGE("§6", "Orange", NamedTextColor.GOLD, Material.ORANGE_BANNER, Material.ORANGE_CONCRETE),
        ROUGE("§c", "Rouge", NamedTextColor.RED, Material.RED_BANNER, Material.RED_CONCRETE),
        VIOLET("§5", "Violet", NamedTextColor.DARK_PURPLE, Material.PURPLE_BANNER, Material.PURPLE_CONCRETE),
        ROSE("§d", "Rose", NamedTextColor.LIGHT_PURPLE, Material.PINK_BANNER, Material.PINK_CONCRETE),
        VERT("§a", "Vert", NamedTextColor.GREEN, Material.LIME_BANNER, Material.LIME_CONCRETE),
        BLEU("§b", "Bleu", NamedTextColor.AQUA, Material.LIGHT_BLUE_BANNER, Material.LIGHT_BLUE_CONCRETE),
        SPECTATOR("§8[SPEC] §7§o", "Spectateur", NamedTextColor.GRAY, Material.ENDER_EYE, null);

        private final String prefixe;
        private final String nom;
        private final NamedTextColor textColor;
        private final Material materialTeamGui;
        private final Material materialBingoGui;

        Color(String prefixe, String nom, NamedTextColor textColor, Material materialTeamGui, Material materialBingoGui) {
            this.prefixe = prefixe;
            this.nom = nom;
            this.textColor = textColor;
            this.materialTeamGui = materialTeamGui;
            this.materialBingoGui = materialBingoGui;
        }

        public String getPrefixe() {
            return prefixe;
        }

        public String getNom() {
            return nom;
        }

        public Component displayName() {
            return Component.text(nom, textColor);
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
