package kmm.repository;

import kmm.model.Team;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

public class TeamIDRepository {

    private final Map<Integer, Team> teams;

    public TeamIDRepository(final Map<Integer, Team> teams) {
        this.teams = teams;
    }

    public final Map<Integer, Team> getTeams() {
        return teams;
    }

    public Team upsert(final Team team) {
        if (team == null) {
            throw new IllegalArgumentException("team can not be null");
        }
        // add or update if the team exists
        teams.put(team.getID(), team);
        return team;
    }

    public Team get(final int ID) {
        final Team team = teams.get(ID);
        if (team == null) {
            throw new NoSuchElementException("team cannot be null");
        }
        return team;
    }

    public List<Team> upsert(final List<Team> teamList) {
        if (teamList == null) {
            throw new IllegalArgumentException("teamList can not be null");
        }
        teamList.forEach(this::upsert);
        return teamList;
    }
}
