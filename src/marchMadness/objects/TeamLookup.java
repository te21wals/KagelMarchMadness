package marchMadness.objects;

import java.util.HashMap;
import java.util.Map;

public class TeamLookup {
    private static Map<Integer,String> idNameMap = new HashMap<>();
    private static Map<String, Integer> nameIDMap = new HashMap<>();

    public static TeamLookup INSTANCE = new TeamLookup();

    private TeamLookup(){
        // Singleton
    }

    public void addToMaps (Team team){
        String teamName = team.getName();
        int teamID = team.getID();
        idNameMap.put(teamID,teamName);
        nameIDMap.put(teamName,teamID);
    }
    public int getTeamID(String name){
        return nameIDMap.get(name);
    }

    public String getTeamName(int ID){
        return idNameMap.get(ID);
    }
}
