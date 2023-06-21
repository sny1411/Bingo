package fr.sny1411.bingo.utils;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class Team {
    private static HashMap<Color, Team> teams;

    public static void init() {
        removeTeams();
        createTeams();
    }

    public static void createTeams() {
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

    public enum Color {
        ORANGE("§6", "Orange", Material.ORANGE_BANNER),
        ROUGE("§c", "Rouge", Material.RED_BANNER),
        VIOLET("§5", "Violet", Material.PURPLE_BANNER),
        ROSE("§d", "Rose", Material.PINK_BANNER),
        VERT("§a", "Vert", Material.LIME_BANNER),
        BLEU("§b", "Bleu", Material.LIGHT_BLUE_BANNER),
        SPECTATOR("§8[SPEC] §7§o", "Spectateur", Material.ENDER_EYE);

        private final String prefixe;
        private final String nom;
        private final Material material;

        Color(String prefixe, String nom, Material material) {
            this.prefixe = prefixe;
            this.nom = nom;
            this.material = material;
        }

        public String getPrefixe() {
            return prefixe;
        }

        public String getNom() {
            return nom;
        }

        public Material getMaterial() {
            return material;
        }
    }

    private static int nbTeams = 4;
    private static int nbPlayerTeams = 2;
    private final Set<Player> players;
    private Color color;

    private Team(Color color) {
        this.color = color;
        players = new HashSet<>();
    }

    public boolean addPlayer(Player player) {
        if (players.size() != nbPlayerTeams) {
            Team.removeTeam(player);
            return players.add(player);
        }
        return false;
    }

    public boolean removePlayer(Player player) {
        return players.remove(player);
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
    }
}
