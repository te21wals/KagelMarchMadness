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
    private static final String TURNAMENT_SEED_FILE = "input/2019TurnamentSeeds.csv";
    private static final boolean adjustStartingEloBySeed = true;
    private static final int maxBonus = 320;


    private static Set<GameOutcome> loadSeasonGamesFromFile(final Map<Integer, Team> integerTeamMap) {
        Set<GameOutcome> gameOutcomes = new HashSet<>();

        final File resultsFile = new File(RESULTS_PATH).getAbsoluteFile();
        try (Scanner sc = new Scanner(resultsFile)) {
            while (sc.hasNextLine()) {
                gameOutcomes.add(
                        parseGameLine(
                                sc.nextLine(),
                                integerTeamMap
                        )
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
        final Map<Integer, Integer> idSeedMap = readTurnamentSeeds();
        final Map<Integer, Team> integerTeamMap = new HashMap<>();

        final File teamFile = new File(TEAM_PATH).getAbsoluteFile();
        try (Scanner sc = new Scanner(teamFile)) {
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                String[] data = line.split(",");
                // todo: if the team is in the seed map
                // todo: adjust the elo of the starting sku accordingly

                if (idSeedMap.containsKey(Integer.parseInt(data[0])) && adjustStartingEloBySeed) {
                    Team team = new Team(data[1], Integer.parseInt(data[0]));
                    double bonus = maxBonus / idSeedMap.get(Integer.parseInt(data[0]));
                    System.out.println("updated " + team.getName() + " by " + bonus + " because it is a "
                            + idSeedMap.get(Integer.parseInt(data[0])) + " seed."
                    );
                    team.setEloRating(Math.round(team.getRating() + bonus));
                    integerTeamMap.put(Integer.parseInt(data[0]), team);
                } else {
                    integerTeamMap.put(Integer.parseInt(data[0]), new Team(data[1], Integer.parseInt(data[0])));
                }
            }
        } catch (IOException e) {
            System.err.println("Data in team file could not be validated \n" + e.getMessage());
        }
        return integerTeamMap;
    }

    private static Map<Integer, Integer> readTurnamentSeeds() {
        final Map<Integer, Integer> seedIdMap = new HashMap<>();

        final File teamFile = new File(TURNAMENT_SEED_FILE).getAbsoluteFile();
        try (Scanner sc = new Scanner(teamFile)) {
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                String[] data = line.split(",");
                seedIdMap.put(Integer.parseInt(data[1]), Integer.parseInt(data[0]));
            }
        } catch (IOException e) {
            System.err.println("Data in team file could not be validated \n" + e.getMessage());
        }
        return seedIdMap;
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
        return new SimulationService(
                loadSeasonGamesFromFile(idTeamMap),
                new TeamRepository(idTeamMap)
        );
    }
}
