package marchMadness.objects;


/**
 * @author kr06pern
 * @author te21wals
 */
public class Game {
    private Team wTeam;
    private Team lTeam;

    public Game(Team wTeam, Team lTeam) {
        this.wTeam = wTeam;
        this.lTeam = lTeam;
    }

    public void simulate(){
        wTeam = wTeam.getEloRating() >= lTeam.getEloRating() ? wTeam : lTeam;
        lTeam = lTeam.getEloRating() <  wTeam.getEloRating() ? lTeam : wTeam;
    }

    public Team getWinningTeam(){
        return this.wTeam;
    }
    
    public Team getLoosingTeam(){
        return this.lTeam;
    }
}
