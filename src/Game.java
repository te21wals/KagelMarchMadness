/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package marchmadness;

/**
 * @author kr06pern
 * @author te21wals
 */
public class Game {
    private Team wTeam; 
    private Team lTeam;
    
    public Game(Team wTeamIn, Team lTeamIn){
        this.wTeam = wTeamIn;
        this.lTeam = lTeamIn;
    }
    public Team getWTeam(){
        return this.wTeam;
    }
    public void setWTeam(Team wTeamIn){
        this.wTeam = wTeamIn;
    }
    
    public Team getLTeam(){
        return this.lTeam;
    }
    public void setLTeam(Team lTeamIn){
        this.lTeam = lTeamIn;
    }
}
