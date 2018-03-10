package objects;


/**
 * @author kr06pern
 * @author te21wals
 */
public class Game {
    private Team winner;
    private Team lTeam;

    public Game(Team wTeam, Team lTeam) {
        this.winner = wTeam;
        this.lTeam = lTeam;
    }

    public void simulate(){
        winner = winner.getEloRating() >= lTeam.getEloRating() ? winner : lTeam;
        lTeam = lTeam.getEloRating() <  winner.getEloRating() ? lTeam : winner;
    }

    public Team getWinningTeam(){
        return this.winner;
    }
    
    public Team getLoosingTeam(){
        return this.lTeam;
    }

    @Override
    public String toString() {
        return "Game{" +
                "winner=" + winner +
                ", lTeam=" + lTeam +
                '}';
    }
}
