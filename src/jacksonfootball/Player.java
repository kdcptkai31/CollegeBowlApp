package jacksonfootball;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author Kai Duty
 * This class holds each user's information about their projected win choices.
 */
public class Player implements Comparable<Player>, Serializable{
    
    private String name;
    private ArrayList<Integer> choices;
    private int wins;
    private int finalCombinedScore;
    
    public Player(String nameStr){
        
        name = nameStr;
        choices = new ArrayList();
        wins = 0;
        finalCombinedScore = 0;        
        
    }
    
    //Getters
    public String getName(){return name;}
    public ArrayList<Integer> getChoices(){return choices;}
    public int getWins(){return wins;}
    public int getFinalCombinedScore(){return finalCombinedScore;}
    
    //Setters
    public void addChoice(Integer choice){choices.add(choice);}
    public void setFinalCombinedScore(int num){finalCombinedScore = num;}
    public void setWinCount(int num){wins = num;}

    @Override
    public int compareTo(Player p) {
        
        if(wins > p.getWins())
            return 1;
        else if(wins < p.getWins())
            return -1;
        else
            return 0;
        
    }
    
}
