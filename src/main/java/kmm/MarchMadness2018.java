package kmm;

import kmm.model.SimulationResult;
import kmm.model.Team;
import kmm.service.SimulationService;
import kmm.service.SimulationServiceFactory;

import java.util.List;
import java.util.logging.Logger;

public class MarchMadness2018 {
    private static final Logger LOGGER = Logger.getLogger(MarchMadness2018.class.getName());
    private final SimulationService simulationService;

    private MarchMadness2018() {
        simulationService = SimulationServiceFactory.buildSimulationService();
    }


    public static void main(String[] args) {
        final MarchMadness2018 marchMadness2018 = new MarchMadness2018();

        SimulationResult simulationResult = marchMadness2018
                .simulationService
                .simulateGamesOnQueue(true, 1);

        LOGGER.info(simulationResult.toShortString());

        final int numberOfTeamsToPrint = 366;

        final List<Team> topTeamsByELO = marchMadness2018
                .simulationService
                .getTopTeamsByELORatings(numberOfTeamsToPrint);

        final List<Team> topTeamsByAverageELO = marchMadness2018
                .simulationService
                .getTopTeamsByAverageELO(numberOfTeamsToPrint);

//        for (int i = 1; i <= topTeamsByELO.size(); i++) {
//            LOGGER.info("Rank(elo): " + i + "\t" +
//                    topTeamsByELO.get(i - 1)
//            );
//        }

        for (int i = 1; i <= topTeamsByAverageELO.size(); i++) {
            LOGGER.info("Rank(avg): " + i + "\t" +
                    topTeamsByAverageELO.get(i - 1)
            );
        }
    }
}
