package marchMadness;

import marchMadness.objects.Game;
import marchMadness.objects.SimulationRepository;
import marchMadness.objects.Team;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

public class MarchMadness2018 {
    private Queue<Game> games;
    private SimulationRepository simulationRepository = new SimulationRepository();
    private static String teamPath = "input/Teams.csv";
    private static String resultsPath = "input/2017RegularSeasonResults.csv";

    public MarchMadness2018 () {
        simulationRepository.upsert(readInTeamFile());
        games = readInResultsFile();
    }

    public void simulateRegularSeason(){
        while(!games.isEmpty()){
            Game currentGame = games.remove();
            simulateGame(currentGame);
        }
    }

    public void simulateGame(Game game){
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

        winningTeam.setEloRating((int)(winningTeam.getEloRating() + Math.round(K * (winTeamActual - winTeamExpectedScore))));
        loosingTeam.setEloRating((int)(winningTeam.getEloRating() + Math.round(K * (loseTeamActual - loseTeamExpectedScore))));

        simulationRepository.upsert(winningTeam);
        simulationRepository.upsert(loosingTeam);
    }


    public  Queue<Game> readInResultsFile(){
        Queue<Game> games = new LinkedList<>();

        File resultsFile = new File(resultsPath).getAbsoluteFile();
        try (Scanner sc = new Scanner(resultsFile)) {
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                String[] data = line.split("\t");
                int winnerID = Integer.parseInt(data[0]);
                int loserID = Integer.parseInt(data[1]);
                Game game = new Game(simulationRepository.get(winnerID),simulationRepository.get(loserID));
                games.add(game);
            }
        }
        catch (Exception e ){
            System.err.println("Data in team game file could not be validated "+ games.toArray().length);
        }
        finally {
            return games;
        }
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
        catch (Exception e ){
            System.err.println("Data in team file could not be validated \n"+e.getMessage());
        }
        finally {
            return teams;
        }
    }

    public List<Team> getTopTeams (int n){
        if(n<1 || n>364){
            throw new IllegalArgumentException("n: "+ n + "is invalid must be between [1-364]");
        }
        List<Team> teams = new ArrayList<>();
        simulationRepository.getTeams().entrySet().stream().forEach(team -> {
            teams.add(team.getValue());
        });
        Collections.sort(teams, Comparator.comparingInt(Team::getEloRating));
        return teams.stream().limit(n).collect(Collectors.toList());
    }

    public static void main (String[]args){
        MarchMadness2018 marchMadness2018 = new MarchMadness2018();
        marchMadness2018.simulateRegularSeason();

        marchMadness2018.getTopTeams(25).forEach(team-> {
            System.out.println(team);
        });
    }
}
