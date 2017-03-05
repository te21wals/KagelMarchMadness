/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package marchmadness;

import java.io.File;
import java.util.HashMap;
import java.util.Scanner;

/**
 *
 * @author te21wals
 */
public class MarchMadness {
    public static HashMap<Integer, String> teamMap = initTeamMap("Teams.csv");
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
    }
    
    public static HashMap initTeamMap(String fileName){
        HashMap<Integer, String> hmap = new HashMap<Integer, String>();
        try{
            Scanner sc = new Scanner(new File(fileName));
            sc.nextLine();
            while(sc.hasNextLine()){
                String [] data = sc.nextLine().split(",");
                hmap.put(Integer.parseInt(data[0]) , data[1]);
            }
            
        } catch(Exception e){
            System.out.println(e.getMessage());
        }
        
        return hmap;
    }
}
