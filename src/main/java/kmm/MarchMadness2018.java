package kmm;

import kmm.objects.Game;
import kmm.objects.SimulationResult;
import kmm.objects.repos.SimulationRepository;
import kmm.objects.Team;

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
    public final Queue<Game> regularSeason2017Results;

    public MarchMadness2018 () {
        LOGGER.warning("----------------------- new instance -----------------------");
        games = new LinkedList<>();
        simulationRepository.upsert(readInTeamFile());
        this.regularSeason2017Results = readInResultsFile(resultsPath);
    }

    public SimulationResult simulateGamesOnQueue(boolean updateScore, int n){
        return simulateGamesOnQueue(updateScore,n,regularSeason2017Results.stream().collect(Collectors.toList()));
    }


    public SimulationResult simulateGamesOnQueue(boolean updateScore, int n, List<Game> gamesList ){
        if(n<=0) throw new IllegalArgumentException("n must be >0");
        SimulationResult simulationResult = new SimulationResult();
        do {
            games.addAll(gamesList);
            while (!games.isEmpty()) {
                Game result = games.remove();
                Game eloPrediction = getPrediction(result);
                if (result.equals(eloPrediction)) {
                    simulationResult.setCorrect(simulationResult.getCorrect() + 1);
                    simulationResult.addSuccess(eloPrediction);

                } else {
                    simulationResult.setIncorrect(simulationResult.getIncorrect() + 1);
                    simulationResult.addFailure(eloPrediction);
                }
                simulateGame(result, updateScore);
            }
            n--;
        }
        while(n>0);

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

        //K is the K-factor into considering the new rating was 32 changing to 20
        double K = 50.0;

        long winningTeamElo = (long)(winningTeam.getEloRating() + Math.round(K * (winTeamActual - winTeamExpectedScore)));
        long loosingTeamElo = (long)(winningTeam.getEloRating() + Math.round(K * (loseTeamActual - loseTeamExpectedScore)));

        if(updateScore == true) {
            winningTeam.setEloRating(winningTeamElo);
            loosingTeam.setEloRating(loosingTeamElo);
            simulationRepository.upsert(winningTeam);
            simulationRepository.upsert(loosingTeam);
        }

        return new int [] {winningTeam.getID(), loosingTeam.getID()};
    }


    public  Queue<Game> readInResultsFile(String file){
        Queue<Game> games = new LinkedList<>();
        File resultsFile = new File(file).getAbsoluteFile();
        try (Scanner sc = new Scanner(resultsFile)) {
            while (sc.hasNextLine()) {
                games.add(parseGameLine(sc.nextLine()));
            }
        }
        catch (Exception e ){
            System.err.println("Data in game file could not be validated "+ e.getMessage());
        }
        finally {
            return games;
        }
    }

    private Game parseGameLine(String line){
        String[] data = line.split(",");
        int winnerID = Integer.parseInt(data[0]);
        int loserID = Integer.parseInt(data[1]);
        Game game =  new Game(simulationRepository.get(winnerID),simulationRepository.get(loserID));
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
                .sorted(Comparator.comparingLong(Team::getEloRating).reversed())
                .limit(n)
                .collect(Collectors.toList());
    }

    public static void main (String[]args){
        MarchMadness2018 marchMadness2018 = new MarchMadness2018();

        marchMadness2018.readInResultsFile(marchMadness2018.resultsPath);
        int recordsParsed = 2000;//Integer.MAX_VALUE/16;
        Date date = new Date();
        LOGGER.info("Start: " + date.toString()+"\t Simulations: "+ recordsParsed);

        int num = 1;
        //int num = recordsParsed/(500 % recordsParsed);
        for(int i =0;i<num; i++){
            Date now = new Date();
            marchMadness2018.simulateGamesOnQueue(true,1);
            LOGGER.info("\tsimulation: " + i + "/" + num +" time elapsed: "+ ((date.getTime()-now.getTime())/ 1000) +"sec");
        }
        marchMadness2018.getTopTeams(364).forEach(team -> {
            System.out.println(team.toShortString());
        });

        SimulationResult simulationResult = marchMadness2018.simulateGamesOnQueue(false,1,
                marchMadness2018.readInResultsFile(marchMadness2018.turnamentPath)
                        .stream().collect(Collectors.toList()));
        LOGGER.info(simulationResult.toShortString());

        simulationResult = marchMadness2018.simulateGamesOnQueue(false,1,
                marchMadness2018.regularSeason2017Results.stream().collect(Collectors.toList()));
        LOGGER.info(simulationResult.toShortString());
    }
}
