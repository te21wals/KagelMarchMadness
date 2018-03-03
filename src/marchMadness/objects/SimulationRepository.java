package marchMadness.objects;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

public class SimulationRepository {
    private static Map <Integer, Team> teams = new HashMap<>();
    public static SimulationRepository INSTANCE = new SimulationRepository();

    private SimulationRepository(){
        // Singleton
    }

    public Team upsert (Team team){
        if(team == null){
            throw new IllegalArgumentException("team can not be null");
        }
        // add or update if the team exists
        teams.put(team.getID(),team);
        return team;
    }

    public Team get (int ID){
        Team team = teams.get(ID);
        if(team == null){
            throw new NoSuchElementException("team does not exist");
        }
        return team;
    }

    public List<Team> upsert(List<Team> teamList){
        if(teamList == null){
            throw new IllegalArgumentException("teamList can not be null");
        }
        teamList.forEach(team -> upsert(team));
        return teamList;
    }
}
