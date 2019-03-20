package kmm.service;

import kmm.model.GameOutcome;
import kmm.model.SimulationResult;
import kmm.model.Team;
import kmm.repository.TeamRepository;

import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class SimulationService {
    private static final Logger LOGGER = Logger.getLogger(SimulationService.class.getName());
    private static final double denominatorConst = 400;
    private static final double k = 32;
    private final Set<GameOutcome> gameOutcomes;
    private final TeamRepository teamRepository;

    SimulationService(final Set<GameOutcome> gameOutcomes,
                      final TeamRepository teamRepository) {
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
            final Queue<GameOutcome> simulationGameOutcomes = new LinkedList<>(gameOutcomes);
            while (!simulationGameOutcomes.isEmpty()) {
                final GameOutcome result = simulationGameOutcomes.remove();
                final GameOutcome eloPrediction = getPredictedGameOutcomeByEloRanking(result);

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
    private GameOutcome getPredictedGameOutcomeByEloRanking(final GameOutcome gameOutcome) {
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

        LOGGER.info(winningTeam.getName().toUpperCase() + " has a " + winTeamExpectedScore
                + " probablility of winning");
        LOGGER.info(loosingTeam.getName().toUpperCase() + " has a "
                + loseTeamExpectedScore + " probablility of winning");

        if (updateScore) {
            winningTeam.setEloRating(winningTeamElo);
            winningTeam.addScore(winningTeamElo);

            loosingTeam.setEloRating(loosingTeamElo);
            loosingTeam.addScore(loosingTeamElo);

            teamRepository.upsert(winningTeam);
            teamRepository.upsert(loosingTeam);
        }

        LOGGER.info(winningTeam.getName() + " has a avg points score of " + winningTeam.getAverageScore());
        LOGGER.info(winningTeam.getName() + " has a an elo rating of " + winningTeam.getEloRating());
        LOGGER.info(loosingTeam.getName() + " has a avg points score of " + loosingTeam.getAverageScore());
        LOGGER.info(loosingTeam.getName() + " has a an elo rating of" + winningTeam.getEloRating());
    }

    // Not used but leaving in for debugging purposes
    public List<Team> getTopTeamsByELORatings(int limit) {
        if (limit < 0 || limit > 367) {
            throw new IllegalArgumentException("n: " + limit + "is invalid must be between [1-364]");
        }

        return teamRepository
                .getTeams()
                .values()
                .stream()
                .sorted(Comparator.comparingLong(Team::getEloRating).reversed())
                .limit(limit)
                .collect(Collectors.toList());
    }

    // Not used but leaving in for debugging purposes
    public List<Team> getTopTeamsByAverageELO(int limit) {
        if (limit < 0 || limit > 367) {
            throw new IllegalArgumentException("n: " + limit + "is invalid must be between [1-364]");
        }
        return teamRepository
                .getTeams()
                .values()
                .stream()
                .sorted(Comparator.comparingDouble(Team::getAverageScore).reversed())
                .limit(limit)
                .collect(Collectors.toList());
    }
}
