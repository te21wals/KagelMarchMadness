package kmm.objects;

import java.util.ArrayList;
import java.util.List;

public class SimulationResult {
    private int correct;
    private int incorrect;

    List<Game> successes;
    List<Game> failures;

    public SimulationResult(){
        this.correct = 0;
        this.incorrect= 0;
        this.successes = new ArrayList<>();
        this.failures = new ArrayList<>();
    }

    public int getCorrect() {
        return correct;
    }

    public void setCorrect(int correct) {
        this.correct = correct;
    }

    public int getIncorrect() {
        return incorrect;
    }

    public void setIncorrect(int incorrect) {
        this.incorrect = incorrect;
    }
    
    public List<Game> getSuccesses() {
        return successes;
    }
    
    public List<Game> getFailures() {
        return failures;
    }
    
    public void addSuccess(Game game){
        successes.add(game);
    }

    public void addFailure(Game game){
        failures.add(game);
    }

    @Override
    public String toString() {
        return "SimulationResult{" +
                "\n\tcorrect=" + correct +
                "\n\tincorrect=" + incorrect +
                "\n\tsuccesses=" + successes +
                "\n\tfailures=" + failures  +
                "\n}";
    }

    public String toShortString() {
        return "SimulationResult{" +
                "\n\tcorrect=" + correct +
                "\n\tincorrect=" + incorrect +
                "\n}";
    }
}
