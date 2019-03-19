package kmm;

import kmm.model.SimulationResult;
import kmm.service.SimulationService;
import kmm.service.SimulationServiceBuilder;

import java.util.logging.Logger;

public class MarchMadness2018 {
    private static final Logger LOGGER = Logger.getLogger(MarchMadness2018.class.getName());
    private final SimulationService simulationService;

    private MarchMadness2018() {
        simulationService = SimulationServiceBuilder.buildSimulationService();
    }


    public static void main(String[] args) {
        final MarchMadness2018 marchMadness2018 = new MarchMadness2018();
        SimulationResult simulationResult = marchMadness2018.simulationService.simulateGamesOnQueue(true, 1);
        LOGGER.info(simulationResult.toShortString());

        final int numberOfTeamsToPrint = 364;
        for (int i = 1; i <= marchMadness2018.simulationService.getTopTeams(numberOfTeamsToPrint).size(); i++) {
            LOGGER.info("Rank: " + i + "\t" + marchMadness2018.simulationService.getTopTeams(numberOfTeamsToPrint).get(i - 1));
        }
    }
}
