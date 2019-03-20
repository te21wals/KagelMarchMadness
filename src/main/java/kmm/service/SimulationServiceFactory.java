package kmm.service;

import kmm.model.GameOutcome;
import kmm.model.Team;
import kmm.repository.TeamRepository;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class SimulationServiceFactory {
    private static final String TEAM_PATH = "input/Teams.csv";
    private static final String RESULTS_PATH = "input/2019RegularSeasonResults.csv";

    private static Set<GameOutcome> loadSeasonGamesFromFile(final Map<Integer, Team> integerTeamMap) {
        Set<GameOutcome> gameOutcomes = new HashSet<>();

        final File resultsFile = new File(RESULTS_PATH).getAbsoluteFile();
        try (Scanner sc = new Scanner(resultsFile)) {
            while (sc.hasNextLine()) {
                gameOutcomes.add(
                        parseGameLine(sc.nextLine(),
                                integerTeamMap)
                );
            }
        } catch (Exception e) {
            System.err.println("Data in game file could not be validated " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return gameOutcomes;
    }

    private static Map<Integer, Team> readInTeamFile() {
        final Map<Integer, Team> integerTeamMap = new HashMap<>();

        final File teamFile = new File(TEAM_PATH).getAbsoluteFile();
        try (Scanner sc = new Scanner(teamFile)) {
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                String[] data = line.split(",");
                integerTeamMap.put(Integer.parseInt(data[0]), new Team(data[1], Integer.parseInt(data[0])));
            }
        } catch (IOException e) {
            System.err.println("Data in team file could not be validated \n" + e.getMessage());
        }
        return integerTeamMap;
    }

    private static GameOutcome parseGameLine(final String line,
                                             final Map<Integer, Team> integerTeamMap) {
        final String[] data = line.split(",");
        final int winnerID = Integer.parseInt(data[0]);
        final int loserID = Integer.parseInt(data[1]);
        return new GameOutcome(integerTeamMap.get(winnerID), integerTeamMap.get(loserID));
    }

    public static SimulationService buildSimulationService() {
        final Map<Integer, Team> idTeamMap = readInTeamFile();
        return new SimulationService(loadSeasonGamesFromFile(idTeamMap),
                new TeamRepository(idTeamMap)
        );
    }
}
