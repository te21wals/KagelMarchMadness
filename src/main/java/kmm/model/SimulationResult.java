package kmm.model;

import java.util.ArrayList;
import java.util.List;

public class SimulationResult {
    private int correct;
    private int incorrect;

    private List<GameOutcome> successes;
    private List<GameOutcome> failures;

    public SimulationResult() {
        this.correct = 0;
        this.incorrect = 0;
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

    public void addSuccess(GameOutcome gameOutcome) {
        successes.add(gameOutcome);
    }

    public void addFailure(GameOutcome gameOutcome) {
        failures.add(gameOutcome);
    }

    @Override
    public String toString() {
        return "SimulationResult{" +
                "\n\tcorrect=" + correct +
                "\n\tincorrect=" + incorrect +
                "\n\tsuccesses=" + successes +
                "\n\tfailures=" + failures +
                "\n}";
    }

    public String toShortString() {
        return "SimulationResult{" +
                "\n\tcorrect=" + correct +
                "\n\tincorrect=" + incorrect +
                "\n}";
    }
}
