import objects.Game;
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
    private static String teamPath = "input/Teams.csv";
    private static String resultsPath = "input/2017RegularSeasonResults.csv";

    public MarchMadness2018 () {
        LOGGER.warning("----------------------- new instance -----------------------");
        simulationRepository.upsert(readInTeamFile());
        games = readInResultsFile();
    }

    public void simulateRegularSeason(){
        while(!games.isEmpty()){
            Game currentGame = games.remove();
            simulateGame(currentGame, true);
        }
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

        winningTeam.setEloRating(winningTeamElo);
        loosingTeam.setEloRating(loosingTeamElo);

        if(updateScore == true) {
            simulationRepository.upsert(winningTeam);
            simulationRepository.upsert(loosingTeam);
        }

        LOGGER.info("Winning Team: "+winningTeam.toString()+"\tLoosing Team: "+ loosingTeam.toString()
                +" updated successfully");

        return new int [] {winningTeam.getID(), loosingTeam.getID()};
    }

    public void readInTurnamentFile(String fileName){
        File turnamentFile = new File(fileName);
        try(Scanner sc = new Scanner(turnamentFile)){
            while(sc.hasNextLine()){
                Game game = parseGameLine(sc.nextLine(),true);
            }
        }
        catch(IOException e){
            System.out.println("Data in team file could not be validated");
        }
    }

    public Game parseTurnamentFileLine(String line){
        return parseGameLine(line,true);
    }

    public  Queue<Game> readInResultsFile(){
        Queue<Game> games = new LinkedList<>();

        File resultsFile = new File(resultsPath).getAbsoluteFile();
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
        String[] data = line.split("\t");
        int winnerID = Integer.parseInt(data[0]);
        int loserID = Integer.parseInt(data[1]);
        return new Game(simulationRepository.getTeam(winnerID),simulationRepository.getTeam(loserID));
    }

    private Game parseGameLine(String line , boolean isResultKnown){
        Game game = parseGameLine(line);
        if (!isResultKnown)
            game.simulate();
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

    public List<Team> getTopTeams (int n) {
        if (n < 0 || n > 365) {
            throw new IllegalArgumentException("n: " + n + "is invalid must be between [1-364]");
        }

        return simulationRepository.getTeams().values()
                .stream()
                .sorted(Comparator.comparingInt(Team::getEloRating).reversed())
                .limit(n)
                .collect(Collectors.toList());
    }

    public static void main (String[]args){
        MarchMadness2018 marchMadness2018 = new MarchMadness2018();
        marchMadness2018.simulateRegularSeason();

        marchMadness2018.getTopTeams(10).forEach(team-> {
            System.out.println(team);
        });
    }
}