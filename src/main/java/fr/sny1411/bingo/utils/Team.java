package fr.sny1411.bingo.utils;

import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;

public class Team {
    public enum Color {
        ORANGE("§6", "Orange", Material.ORANGE_BANNER, Material.ORANGE_CONCRETE),
        ROUGE("§c", "Rouge", Material.RED_BANNER, Material.RED_CONCRETE),
        VIOLET("§5", "Violet", Material.PURPLE_BANNER, Material.PURPLE_CONCRETE),
        ROSE("§d", "Rose", Material.PINK_BANNER, Material.PINK_CONCRETE),
        VERT("§a", "Vert", Material.LIME_BANNER, Material.LIME_CONCRETE),
        BLEU("§b", "Bleu", Material.LIGHT_BLUE_BANNER, Material.LIGHT_BLUE_CONCRETE),
        SPECTATOR("§8[SPEC] §7§o", "Spectateur", Material.ENDER_EYE, null);

        private final String prefixe;
        private final String nom;
        private final Material materialTeamGui;
        private final Material materialBingoGui;

        Color(String prefixe, String nom, Material materialTeamGui, Material materialBingoGui) {
            this.prefixe = prefixe;
            this.nom = nom;
            this.materialTeamGui = materialTeamGui;
            this.materialBingoGui = materialBingoGui;
        }

        public String getPrefixe() {
            return prefixe;
        }

        public String getNom() {
            return nom;
        }

        public Material getMaterialTeamGui() {
            return materialTeamGui;
        }

        public Material getMaterialBingoGui() {
            return materialBingoGui;
        }
    }

    private final Set<Player> players;
    private final Color color;
    private boolean gameFinish;

    Team(Color color) {
        this.color = color;
        gameFinish = false;
        players = new HashSet<>();
    }

    void addPlayer(Player player) {
        players.add(player);
    }

    void removePlayer(Player player) {
        players.remove(player);
    }

    public Color getColor() {
        return color;
    }

    public Set<Player> getPlayers() {
        return players;
    }

    public boolean isGameFinish() {
        return gameFinish;
    }

    public void setGameFinish(boolean gameFinish) {
        this.gameFinish = gameFinish;
    }

    public void sendMessage(String message) {
        Component componentMessage = Component.text(message);
        for (Player player : this.getPlayers()) {
            if (player.isOnline()) {
                player.sendMessage(componentMessage);
            }
        }
    }
}
