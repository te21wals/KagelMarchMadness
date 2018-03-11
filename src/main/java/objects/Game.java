package objects;


import java.util.Objects;

/**
 * @author kr06pern
 * @author te21wals
 */
public class Game {
    private Team winner;
    private Team looser;

    public Game(Team wTeam, Team lTeam) {
        this.winner = wTeam;
        this.looser = lTeam;
    }

    public Team getWinningTeam(){
        return this.winner;
    }
    
    public Team getLoosingTeam(){
        return this.looser;
    }

    @Override
    public String toString() {
        return "\n\t\tGame{" +"\n" +
                "\t\t\twinner  " + winner.toShortString()+"\n" +
                "\t\t\tlooser  " + looser.toShortString() +"\n" +
                "\t\t}";
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Game game = (Game) o;
        return Objects.equals(winner, game.winner) &&
                Objects.equals(looser, game.looser);
    }

    @Override
    public int hashCode() {

        return Objects.hash(winner, looser);
    }
}
