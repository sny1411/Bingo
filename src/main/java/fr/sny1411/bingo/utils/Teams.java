package fr.sny1411.bingo.utils;

import fr.sny1411.bingo.Settings;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.EnumMap;
import java.util.Map;

/**
 * The teams of a game, created from the number of teams of the settings, plus the spectators.
 */
public class Teams {
    private final Settings settings;
    private final Map<Team.Color, Team> teams = new EnumMap<>(Team.Color.class);

    public Teams(Settings settings) {
        this.settings = settings;
        create();
    }

    public void create() {
        teams.clear();
        int i = 0;
        for (Team.Color color : Team.Color.values()) {
            if (i >= settings.getNbTeams()) {
                break;
            }
            teams.put(color, new Team(color));
            i++;
        }
        teams.put(Team.Color.SPECTATOR, new Team(Team.Color.SPECTATOR));
    }

    public Team get(Team.Color color) {
        return teams.get(color);
    }

    public Collection<Team> values() {
        return teams.values();
    }

    public Team getTeam(Player player) {
        for (Team team : teams.values()) {
            if (team.getPlayers().contains(player)) {
                return team;
            }
        }
        return null;
    }

    public Team getTeam(Material materialBingoGui) {
        for (Team team : teams.values()) {
            if (team.getColor().getMaterialBingoGui() == materialBingoGui) {
                return team;
            }
        }
        return null;
    }

    public void join(Player player, Team.Color color) {
        Team team = teams.get(color);
        if (color == Team.Color.SPECTATOR || team.getPlayers().size() != settings.getNbPlayerTeams()) {
            leave(player);
            team.addPlayer(player);
        }
    }

    public void leave(Player player) {
        for (Team team : teams.values()) {
            if (team.getPlayers().contains(player)) {
                team.removePlayer(player);
                return;
            }
        }
    }

    public boolean updatePlayerJoinInGame(Player player) {
        for (Team team : teams.values()) {
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
}
