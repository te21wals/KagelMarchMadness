package kmm.model;


import java.util.Objects;

/**
 * @author kr06pern
 * @author te21wals
 */
public class GameOutcome {
    private Team winner;
    private Team looser;

    public GameOutcome(Team wTeam, Team lTeam) {
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
        return "\n\t\tGameOutcome{" +"\n" +
                "\t\t\twinner  " + winner.toShortString()+"\n" +
                "\t\t\tlooser  " + looser.toShortString() +"\n" +
                "\t\t}";
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GameOutcome gameOutcome = (GameOutcome) o;
        return Objects.equals(winner, gameOutcome.winner) &&
                Objects.equals(looser, gameOutcome.looser);
    }

    @Override
    public int hashCode() {
        return Objects.hash(winner, looser);
    }
}
