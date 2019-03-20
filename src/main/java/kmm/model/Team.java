package kmm.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/*
 * This class is used to represent a team
 *
 * @author kr06pern - Koushik Pernati
 * @author te21wals - Tom Walsh
 */
public class Team {

    //Instance variables
    private String name;
    private int ID;
    private Long rating;
    private List<Long> scores;

    /*
     * Constructor for marchMadness.Team class
     * @param name - The name of the team
     * @param ID - The ID of the team
     */
    public Team(String name, int number) {
        this.name = name;
        this.ID = number;
        this.rating = 1200l;
        this.scores = new ArrayList<>();
    }

    /*
     * Gets the name of the team
     * @return name of the team
     */
    public String getName() {
        return this.name;
    }

    /*
     * Gets the ID of the team
     * @return the ID of the team
     */
    public int getID() {
        return this.ID;
    }

    /*
     * Gets the rating of the team
     * @return the rating of the team
     */
    public Long getEloRating() {
        return this.rating;
    }

    /*
     * Sets the rating of the team
     * @param rating - rating of the team to be set
     */
    public void setEloRating(Long rating) {
        this.rating = rating;
    }

    public Long getRating() {
        return rating;
    }

    public List<Long> getScores() {
        return scores;
    }

    public void addScore(final Long score) {
        this.scores.add(score);
    }

    public double getAverageScore() {
        return scores
                .stream()
                .mapToDouble(a -> a)
                .average().orElse(0);
    }

    @Override
    public String toString() {
        return "Team{" +
                " name=" + name +
                ", ID=" + ID +
                ", rating=" + rating +
                ", average=" + getAverageScore() +
                '}';
    }

    public String toShortString() {
        return String.format("%4d", getID()) + "\t" + String.format("%14s", getName()) + "\t" + String.format("%20d", getEloRating());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Team team = (Team) o;
        return ID == team.ID &&
                Objects.equals(name, team.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, ID, rating);
    }
}
