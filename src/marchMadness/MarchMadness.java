package marchMadness;


import marchMadness.objects.Team;

import java.io.File;
import java.util.HashMap;
import java.util.Scanner;

/**
 * @author kr06pern - Koushik Pernati
 * @author te21wals - Tom Walsh
 */



public class MarchMadness {
    private static String teamPath = "input/Teams.csv";
    private static String resultsPath = "input/2017RegularSeasonResults.csv";

    public static HashMap<Integer, String> teamIntMap = 
            initTeamMap(teamPath);
    public static HashMap<String, Integer> teamNameMap = 
            nameTeamMap(teamPath);
    public static HashMap<Integer, Team> teamMap = initTeamObjMap();
     
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args){
        simulateGames(resultsPath);

            try (Scanner in = new Scanner(System.in)) {
                System.out.println("PLEASE ENTER THE TWO TEAMS YOU WOULD LIKE TO "
                        + "SIMULATE!");
                
                while(true){
                    String firstTeam = in.nextLine();
                    String secondTeam = in.nextLine();

                    if(firstTeam.equals("NO") || secondTeam.equals("NO")){
                        break;
                    }
                    //System.out.println(firstTeam+secondTeam);
                    getProbsOfTeam(firstTeam.toLowerCase(), secondTeam.toLowerCase());
                }
            }
        
    }
    
    static HashMap initTeamMap(String fileName){
        HashMap<Integer, String> hmap = new HashMap<>();
        try{
            try (Scanner sc = new Scanner(new File(fileName).getAbsolutePath())) {
                sc.nextLine();
                while(sc.hasNextLine()){
                    String [] data = sc.nextLine().split(",");
                    hmap.put(Integer.parseInt(data[0]) , data[1].toLowerCase());
                }
            }
        } catch(Exception e){
            System.out.println(e.getMessage());
        }
        
        return hmap;
    }
    
    static HashMap nameTeamMap(String fileName){
        HashMap<String, Integer> hmap = new HashMap<>();
        try{
            try (Scanner sc = new Scanner(new File(fileName))) {
                sc.nextLine();
                while(sc.hasNextLine()){
                    String [] data = sc.nextLine().split(",");
                    hmap.put(data[1].toLowerCase(), Integer.parseInt(data[0]));
                }
            }
        } catch(Exception e){
            System.out.println(e.getMessage());
        }
        
        return hmap;
    }
    
    static HashMap<Integer, Team> initTeamObjMap(){
        HashMap<Integer, Team> teams = new HashMap<>();
        int start = 1101; 
        int finish = 1464;
        
        for(int i = start; i<=finish;i++){
            teams.put(i, new Team(teamIntMap.get(i),i));
        }
        return teams;
    } 
    
    static void simulateGames(String fileName){
        try {
            Scanner in = new Scanner(new File(fileName).getAbsolutePath());
            String header = in.nextLine();
            System.out.println(header);
            while(in.hasNextLine()){
                String str = in.nextLine();
                String[] data = str.split(",");
                int winTeam = Integer.parseInt(data[0]);
                int loseTeam = Integer.parseInt(data[1]);
                System.out.println(teamIntMap.get(winTeam) 
                        + " vs " + teamIntMap.get(loseTeam) );
                gameRatingsResults(winTeam,loseTeam);
            }
            in.close();
        } catch (Exception ex) {
            System.out.println("In Simulate Games and cating the error - File not found!!");
        }
    }
    
    static void gameRatingsResults(int winTeam, int loseTeam){
        int winTeamElo = teamMap.get(winTeam).getRating();
        int loseTeamElo = teamMap.get(loseTeam).getRating();
        
        double winScoreTeam = Math.pow(10.0, winTeamElo/400.0);
        double loseScoreTeam = Math.pow(10.0, loseTeamElo/400.0);
        
        double winTeamExpectedScore = winScoreTeam / 
                (winScoreTeam + loseScoreTeam);
        double loseTeamExpectedScore = loseScoreTeam / 
                (winScoreTeam + loseScoreTeam);
        
        double winTeamActual = 1.0;
        double loseTeamActual = 0.0;
        
        //K is the K-factor into considering the new rating
        double K = 32.0;
        
        int winTeamRating = (int) (winTeamElo + Math.round(K * 
                (winTeamActual - winTeamExpectedScore)));
        int loseTeamRating = (int) (loseTeamElo + Math.round(K * 
                (loseTeamActual - loseTeamExpectedScore)));
        
        Team one = teamMap.get(winTeam);
        one.setRating(winTeamRating);
        Team two = teamMap.get(loseTeam);
        two.setRating(loseTeamRating);
        
        teamMap.put(winTeam, one);
        teamMap.put(loseTeam, two);
    }
    
    static void getProbsOfTeam(String fTeam, String sTeam){
        
        int winTeam = teamNameMap.get(fTeam);
        int loseTeam = teamNameMap.get(sTeam);
        
        int winTeamElo = teamMap.get(winTeam).getRating();
        int loseTeamElo = teamMap.get(loseTeam).getRating();
        
        double winScoreTeam = Math.pow(10.0, winTeamElo/400.0);
        double loseScoreTeam = Math.pow(10.0, loseTeamElo/400.0);
        
        double winTeamExpectedScore = winScoreTeam / 
                (winScoreTeam + loseScoreTeam);
        double loseTeamExpectedScore = loseScoreTeam / 
                (winScoreTeam + loseScoreTeam);
    }
}
