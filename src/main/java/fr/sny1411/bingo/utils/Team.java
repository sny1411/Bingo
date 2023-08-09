package fr.sny1411.bingo.utils;

import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class Team {
    private static HashMap<Color, Team> teams;

    public static void createTeams() {
        removeTeams();
        int i = 0;
        for (Color color : Color.values()) {
            createTeam(color);
            i++;
            if (i >= nbTeams) {
                break;
            }
        }
        createTeam(Color.SPECTATOR);
    }

    public static Team getTeam(Player player) {
        for (Team team : teams.values()) {
            if (team.getPlayers().contains(player)) {
                return team;
            }
        }
        return null;
    }

    private static void createTeam(Color color) {
        teams.put(color, new Team(color));
    }

    public static void removeTeams() {
        teams = new HashMap<>();
    }

    public static void removeTeam(Player playerRemove) {
        for (Team team : teams.values()) {
            for (Player player : team.getPlayers()) {
                if (player == playerRemove) {
                    team.removePlayer(playerRemove);
                    return;
                }
            }
        }
    }

    public static boolean updatePlayerJoinInGame(Player player) {
        for (Team team : Team.getTeams().values()) {
            for (Player playerTeam : team.getPlayers()) {
                if (playerTeam.getUniqueId().equals(player.getUniqueId())) {
                    team.removePlayer(playerTeam);
                    team.addPlayer(player);
                    return true;
                }
            }
        }
        return false;
    }

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

    private static int nbTeams = 4;
    private static int nbPlayerTeams = 2;
    private final Set<Player> players;
    private final Color color;
    private boolean gameFinish;

    private Team(Color color) {
        this.color = color;
        gameFinish = false;
        players = new HashSet<>();
    }

    public void addPlayer(Player player) {
        if (color == Color.SPECTATOR || players.size() != nbPlayerTeams) {
            Team.removeTeam(player);
            players.add(player);
        }
    }

    public void removePlayer(Player player) {
        players.remove(player);
    }

    public static HashMap<Color, Team> getTeams() {
        return teams;
    }

    public Color getColor() {
        return color;
    }

    public Set<Player> getPlayers() {
        return players;
    }

    public static int getNbTeams() {
        return nbTeams;
    }

    public static void setNbTeams(int nbTeams) {
        Team.nbTeams = nbTeams;
        createTeams();
    }

    public static int getNbPlayerTeams() {
        return nbPlayerTeams;
    }

    public static void setNbPlayerTeams(int nbPlayerTeams) {
        Team.nbPlayerTeams = nbPlayerTeams;
        createTeams();
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

    public static Team getTeam(Material materialBingoGui) {
        for (Team team : teams.values()) {
            if (team.getColor().getMaterialBingoGui() == materialBingoGui) {
                return team;
            }
        }
        return null;
    }
}
