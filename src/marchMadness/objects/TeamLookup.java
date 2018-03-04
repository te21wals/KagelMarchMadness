package marchMadness.objects;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Deprecated
public class TeamLookup {
    private static Map<Integer,String> idNameMap = new HashMap<>();
    private static Map<String, Integer> nameIDMap = new HashMap<>();


    public TeamLookup(){

    }

    public Team addToMaps (Team team){
        if(team == null){
            throw new IllegalArgumentException("team can not be null");
        }

        String teamName = team.getName();
        int teamID = team.getID();
        idNameMap.put(teamID,teamName);
        nameIDMap.put(teamName,teamID);
        return team;
    }

    public List<Team> addToMaps(List<Team> teams){
        if(teams == null){
            throw new IllegalArgumentException("teams can not be null");
        }

        teams.forEach(team -> addToMaps(team));
        return teams;
    }

    public int getTeamID(String name){
        return nameIDMap.get(name);
    }

    public String getTeamName(int ID){
        return idNameMap.get(ID);
    }
}
