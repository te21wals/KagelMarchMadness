package marchMadness.objects;

/**
 * @author kr06pern
 * @author te21wals
 */
public class Game {
    private Team wTeam; 
    private Team lTeam;
    
    public Game(Team wTeamIn, Team lTeamIn){
        this.wTeam = wTeamIn;
        this.lTeam = lTeamIn;
    }
    public Team getWinningTeam(){
        return this.wTeam;
    }
    public void setWinningTeam(Team wTeamIn){
        this.wTeam = wTeamIn;
    }
    
    public Team getLoosingTeam(){
        return this.lTeam;
    }
    public void setLoosingTeam(Team lTeamIn){
        this.lTeam = lTeamIn;
    }
}
