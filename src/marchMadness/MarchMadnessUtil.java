package marchMadness;

import marchMadness.objects.Game;
import marchMadness.objects.Team;
import marchMadness.objects.TeamLookup;
import marchMadness.objects.SimulationRepository;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.List;

public static class MarchMadnessUtil {
    private static String teamPath = "input/Teams.csv";
    private static String resultsPath = "input/2017RegularSeasonResults.csv";
    private static SimulationRepository simulationRepository = SimulationRepository.INSTANCE;
    public static MarchMadnessUtil marchMadnessUtil = new MarchMadnessUtil();

    private MarchMadnessUtil() {
        // singleton }
    }

     public  List<Team> readInTeamFile(){
        List<Team> teams = new ArrayList<>();
        Scanner sc = new Scanner(new File(teamPath).getAbsolutePath());

        try{
            while(sc.hasNextLine()){

                String line = sc.nextLine();
                String [] data = line.split(",");
                Team team = new Team (data[1],Integer.parseInt(data[0]));
                teams.add(team);
            }
        }
        catch (Exception e ){
            System.err.println("Data in team file could not be validated");
        }
        finally {
            return teams;
        }
    }

    public List<Game> readInResultsFile(){
        List<Game> games = new ArrayList<>();
        Scanner sc = new Scanner(new File(resultsPath).getAbsolutePath());
        try{
            while(sc.hasNextLine()){
                String line = sc.nextLine();
                String [] data = line.split(",");
                Game game = new Game(simulationRepository.get(Integer.parseInt(data[0])),
                        simulationRepository.get(Integer.parseInt(data[1])));
                games.add(game);
            }
        }
        catch (Exception e ){
            System.err.println("Data in team game file could not be validated");
        }
        finally {
            return games;
        }
    }
}
