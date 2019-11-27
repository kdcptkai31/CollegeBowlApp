package jacksonfootball;

import java.io.Serializable;

/**
 * @author Kai Duty
 * This class holds the information for each game on the season's schedule.
 * 0 if game has not occurred yet
 * 1 if team 1 won
 * 2 if team 2 won
 */
public class Game implements Serializable{
    
    private String team1;
    private String team2;
    private int winningTeam;
    private boolean isSemifinal;
    
    //Constructor that fills in this game's teams
    public Game(String g1, String g2){this(g1, g2, false);}
    //Constructor that fills in this game's teams and semifinal status
    public Game(String g1, String g2, boolean semi){
        
        team1 = g1;
        team2 = g2;
        winningTeam = 0;
        isSemifinal = semi;
        
    }
    
    //Getters
    public String getTeam1Name(){return team1;}
    public String getTeam2Name(){return team2;}
    public int getResults(){return winningTeam;}
    public boolean getIfSemifinal(){return isSemifinal;}
    
    //Setters
    public void setWinningTeam(int results){winningTeam = results;}
    public void setTeam1Name(String str){team1 = str;}
    public void setTeam2Name(String str){team2 = str;}
    
}
