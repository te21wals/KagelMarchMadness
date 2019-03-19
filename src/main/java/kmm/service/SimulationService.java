package kmm.service;

import kmm.model.GameOutcome;
import kmm.model.SimulationResult;
import kmm.model.Team;
import kmm.repository.TeamRepository;

import java.util.*;
import java.util.stream.Collectors;

public class SimulationService {
    private static final double denominatorConst = 400.0;
    private static final double k = 32;
    private final Set<GameOutcome> gameOutcomes;
    private final TeamRepository teamRepository;

    SimulationService(Set<GameOutcome> gameOutcomes, TeamRepository teamRepository) {
        this.gameOutcomes = gameOutcomes;
        this.teamRepository = teamRepository;
    }

    public SimulationResult simulateGamesOnQueue(final boolean updateScore,
                                                 final int totalNumberOfSimulations) {
        if (totalNumberOfSimulations <= 0) throw new IllegalArgumentException("n must be > 0");
        final SimulationResult simulationResult = new SimulationResult();

        int simulationCountDown = totalNumberOfSimulations;
        do {
            // for each simulation re-add all the gameOutcomes for that season
            final Queue<GameOutcome> simulationGameOutcomes = new LinkedList<>();
            simulationGameOutcomes.addAll(gameOutcomes);
            while (!simulationGameOutcomes.isEmpty()) {
                final GameOutcome result = simulationGameOutcomes.remove();
                final GameOutcome eloPrediction = getPredictedGameOutcome(result);
                if (result.equals(eloPrediction)) {
                    simulationResult.setCorrect(simulationResult.getCorrect() + 1);
                    simulationResult.addSuccess(eloPrediction);

                } else {
                    simulationResult.setIncorrect(simulationResult.getIncorrect() + 1);
                    simulationResult.addFailure(eloPrediction);
                }
                simulateGame(result, updateScore);
            }
            simulationCountDown--;
        }
        while (simulationCountDown > 0);

        return simulationResult;
    }

    /*
        returns a predicted GameOutcome with
            - the winningTeam having the higher elo prior to game.
            - the loosingTeam having the lowerElo prior to game.
     */
    private GameOutcome getPredictedGameOutcome(final GameOutcome gameOutcome) {
        final Team higherElo = gameOutcome.getWinningTeam().getEloRating() >= gameOutcome.getLoosingTeam().getEloRating()
                ? gameOutcome.getWinningTeam() : gameOutcome.getLoosingTeam();

        final Team lowerElo = gameOutcome.getLoosingTeam().getEloRating() <= gameOutcome.getWinningTeam().getEloRating()
                ? gameOutcome.getLoosingTeam() : gameOutcome.getWinningTeam();

        return new GameOutcome(higherElo, lowerElo);
    }

    private void simulateGame(final GameOutcome gameOutcome,
                              final boolean updateScore) {
        Team winningTeam = gameOutcome.getWinningTeam();
        Team loosingTeam = gameOutcome.getLoosingTeam();


        double winScoreTeam = Math.pow(10.0, winningTeam.getEloRating() / denominatorConst);
        double loseScoreTeam = Math.pow(10.0, loosingTeam.getEloRating() / denominatorConst);

        double winTeamExpectedScore = winScoreTeam / (winScoreTeam + loseScoreTeam);
        double loseTeamExpectedScore = loseScoreTeam / (winScoreTeam + loseScoreTeam);

        double winTeamActual = 1.0;
        double loseTeamActual = 0.0;

        long winningTeamElo = winningTeam.getEloRating() +
                Math.round(k * (winTeamActual - winTeamExpectedScore));
        long loosingTeamElo = winningTeam.getEloRating() +
                Math.round(k * (loseTeamActual - loseTeamExpectedScore));

        if (updateScore) {
            winningTeam.setEloRating(winningTeamElo);
            loosingTeam.setEloRating(loosingTeamElo);
            teamRepository.upsert(winningTeam);
            teamRepository.upsert(loosingTeam);
        }
    }

    // Not used but leaving in for debugging purposes
    public List<Team> getTopTeams(int n) {
        if (n < 0 || n > 365) {
            throw new IllegalArgumentException("n: " + n + "is invalid must be between [1-364]");
        }

        return teamRepository.getTeams().values()
                .stream()
                .sorted(Comparator.comparingLong(Team::getEloRating).reversed())
                .limit(n)
                .collect(Collectors.toList());
    }
}
