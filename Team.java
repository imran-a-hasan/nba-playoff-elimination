// "Team" represents information regarding an NBA team's performance during the 2016-17 season.
// Each team has a name, a count of their wins and losses, their winning percentage, their
// head to head results against other teams, and whether or not they have been eliminated
// from playoff contention.

import java.util.Map;
import java.util.HashMap;

public class Team {
    private String name;
    private int wins;
    private int losses;
    private double winPct;
    private Map<Team, Integer> headToHead;
    private boolean eliminated;
    
    // Constructs a Team with a given name. Initially, each team has 0 wins and losses, a win
    // percentage of 0.000, no head to head results against any other team, and has not yeet
    // been eliminated from playoff contention.
    public Team(String name) {
        this.name = name;
        headToHead = new HashMap<Team, Integer>();
        eliminated = false;
    }
    
    // Determines whether two teams are equal based on their name. Returns true if the names are
    // the same, and false otherwise.
    @Override
    public boolean equals(Object other) {
        if (name.equals(((Team)(other)).name)) {
            return true;
        } else {
            return false;
        }
    }
    
    // Always returns hash code of 0.
    @Override
    public int hashCode() {
        return 0;
    }
    
    // Accepts second Team as a parameter. Returns the difference between Team's wins and losses 
    // against the given team during the 2016-17 season. For example, a team with 3 wins and 1 
    // loss against the given team (passed as a parameter) will return 2.
    public int h2h(Team other) {
        if (headToHead.containsKey(other)) {
            return headToHead.get(other) * 1;
        } else {
            return 0;
        }
    }
  
    // Updates win count and head to head information after Team wins a game against another
    // team. Accepts the name of the loser as a parameter.
    public void winGame(String loser) {
        wins++;
        updatePct();
        Team losingTeam = new Team(loser);
        updateHeadToHead(losingTeam, 1);      
    }
    
    // Updates loss count and head to head information after Team loses a game against another
    // team. Accepts the name of the winner as a parameter.
    public void loseGame(String winner) {
        losses++;
        updatePct();
        Team winningTeam = new Team(winner);
        updateHeadToHead(winningTeam, -1);
    }
    
    // Updates head to head information after Team plays a game against another team. Accepts
    // Team opponent and integer (1 for win, -1 for loss) as a parameter.
    public void updateHeadToHead(Team team, int change) {
        if (!headToHead.containsKey(team)) {
            headToHead.put(team, 0);
        }
        headToHead.put(team, headToHead.get(team) + change);
    }
    
    // Eliminates Team from playoff contention.
    public void eliminate() {
        eliminated = true;
    }
    
    // Returns true if Team has been eliminated from playoff contention, false otherwise.
    public boolean isEliminated() {
        return eliminated;
    }
    
    // Returns the Team's name.
    public String getName() {
        return name;
    }
    
    // Returns Team's win percentage.
    public double getPct() {
        return winPct;
    }
    
    // Returns the number of games Team has won.
    public int getWins() {
        return wins;
    }
    
    // Returns the number of games Team has lost.
    public int getLosses() {
        return losses;
    }
    
    // Updates win percentage of Team.
    public void updatePct() {
        winPct = wins * 1.0 / (wins + losses);
    }
    
    // Returns string representation of Team, with name and season record.
    @Override
    public String toString() {
        return name + " " + wins + " " + losses;
    }
}