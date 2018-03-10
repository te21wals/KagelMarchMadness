import objects.Game;
import objects.SimulationResult;
import objects.SimulationRepository;
import objects.Team;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class MarchMadness2018 {
    private static final Logger LOGGER = Logger.getLogger(MarchMadness2018.class.getName());

    private Queue<Game> games;

    private SimulationRepository simulationRepository = new SimulationRepository();

    public final String teamPath = "input/Teams.csv";
    public final String resultsPath = "input/2017RegularSeasonResults.csv";
    public final String turnamentPath = "input/2017NCAATourneyResults.csv";

    public MarchMadness2018 () {
        LOGGER.warning("----------------------- new instance -----------------------");
        games = new LinkedList<>();
        simulationRepository.upsert(readInTeamFile());
    }


    public SimulationResult simulateGamesOnQueue(boolean updateScore){
        SimulationResult simulationResult = new SimulationResult();

        while(!games.isEmpty()){
                Game result = games.remove();
                Game eloPrediction = getPrediction(result);
                if(result.equals(eloPrediction)){
                    simulationResult.setCorrect(simulationResult.getCorrect()+1);
                    simulationResult.addSuccess(eloPrediction);

                }
                else{
                    simulationResult.setIncorrect(simulationResult.getIncorrect()+1);
                    simulationResult.addFailure(eloPrediction);
                }
                simulateGame(result, updateScore);
        }
        return simulationResult;
    }

    public Game getPrediction(Game game){
        Team higherElo = game.getWinningTeam().getEloRating() >= game.getLoosingTeam().getEloRating()
                ?game.getWinningTeam() : game.getLoosingTeam();
        Team lowerElo = game.getLoosingTeam().getEloRating() <= game.getWinningTeam().getEloRating()
                ?game.getLoosingTeam() : game.getWinningTeam();

        return new Game(higherElo,lowerElo);
    }

    public int[] simulateGame(Game game, boolean updateScore){
        Team winningTeam = game.getWinningTeam();
        Team loosingTeam = game.getLoosingTeam();

        double winScoreTeam = Math.pow(10.0, winningTeam.getEloRating()/400.0);
        double loseScoreTeam = Math.pow(10.0, loosingTeam.getEloRating()/400.0);

        double winTeamExpectedScore = winScoreTeam / (winScoreTeam + loseScoreTeam);
        double loseTeamExpectedScore = loseScoreTeam / (winScoreTeam + loseScoreTeam);

        double winTeamActual = 1.0;
        double loseTeamActual = 0.0;

        //K is the K-factor into considering the new rating
        double K = 32.0;

        int winningTeamElo = (int)(winningTeam.getEloRating() + Math.round(K * (winTeamActual - winTeamExpectedScore)));
        int loosingTeamElo = (int)(winningTeam.getEloRating() + Math.round(K * (loseTeamActual - loseTeamExpectedScore)));

        if(updateScore == true) {
            winningTeam.setEloRating(winningTeamElo);
            loosingTeam.setEloRating(loosingTeamElo);
            simulationRepository.upsert(winningTeam);
            simulationRepository.upsert(loosingTeam);
        }

        return new int [] {winningTeam.getID(), loosingTeam.getID()};
    }


    public  void readInResultsFile(String file){
        File resultsFile = new File(file).getAbsoluteFile();
        try (Scanner sc = new Scanner(resultsFile)) {
            while (sc.hasNextLine()) {
                games.add(parseGameLine(sc.nextLine()));
            }
        }
        catch (Exception e ){
            System.err.println("Data in game file could not be validated "+ e.getMessage());
        }
    }

    private Game parseGameLine(String line){
        String[] data = line.split(",");
        int winnerID = Integer.parseInt(data[0]);
        int loserID = Integer.parseInt(data[1]);
        Game game =  new Game(simulationRepository.get(winnerID),simulationRepository.get(loserID));
        //System.out.println(game.toString());
        return game;
    }

    public  List<Team> readInTeamFile(){
        List<Team> teams = new ArrayList<>();

        File teamFile = new File(teamPath).getAbsoluteFile();
        try (Scanner sc = new Scanner(teamFile)) {
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                String[] data = line.split(",");
                Team team = new Team(data[1], Integer.parseInt(data[0]));
                teams.add(team);
            }
        }
        catch (IOException e ){
            System.err.println("Data in team file could not be validated \n"+e.getMessage());
        }
        finally {
            return teams;
        }
    }
    // Not used but leaving in for debugging purposes
    public List<Team> getTopTeams (int n) {
        if (n < 0 || n > 365) {
            throw new IllegalArgumentException("n: " + n + "is invalid must be between [1-364]");
        }

        return simulationRepository.getTEAMS().values()
                .stream()
                .sorted(Comparator.comparingInt(Team::getEloRating).reversed())
                .limit(n)
                .collect(Collectors.toList());
    }

    public static void main (String[]args){
        MarchMadness2018 marchMadness2018 = new MarchMadness2018();

        marchMadness2018.readInResultsFile(marchMadness2018.resultsPath);
        marchMadness2018.simulateGamesOnQueue(true);

        marchMadness2018.readInResultsFile(marchMadness2018.turnamentPath);
        SimulationResult simulationResult = marchMadness2018.simulateGamesOnQueue(false);
        System.out.println(simulationResult.toString());
    }
}
