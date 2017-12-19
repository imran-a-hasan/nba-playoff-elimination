// Represents the standings for a particular conference. Ranks the teams in each conference
// 1-15 based on season record and tiebreaker information. 

import org.apache.poi.hssf.usermodel.HSSFCell;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Map;
import java.util.TreeMap;

public class Standings {
    
    private List<Team> ranking;
    
    // Constructs an initial standings for a conference. Takes in String parameter (must be either
    // "west" or "east", case insensitive) and creates an intial standings for the conference.
    // Populates with 15 teams with initial records of 0-0.
    public Standings(String conference) throws Exception {
        ranking = new ArrayList<Team>();
        ExcelReader reader = new ExcelReader();
        ArrayList excelData = reader.makeList("Analytics_Attachment.xls", 0);
        int startIndex = 1;
        if (conference.equals("west")) {
            startIndex = 16;
        }
        for (int i = startIndex; i < startIndex + 15; i++) {
            ArrayList teamLine = (ArrayList)(excelData.get(i));
            String teamName = ((HSSFCell)(teamLine.get(0))).getStringCellValue();
            String teamConf = ((HSSFCell)(teamLine.get(2))).getStringCellValue();
            if (teamConf.toUpperCase().equals(conference.toUpperCase())) {
                ranking.add(new Team(teamName));
            }
        }
    }   
    
    // Checks if the conference contains a team. Accepts the name of the team as a parameter. Returns
    // true if the team is in the conference, false otherwise.
    public boolean hasTeam(String name) {
        if (ranking.contains(new Team(name))) {
            return true;
        } else  {
            return false;
        }
    }
    
    // Updates team's record after a win. Accepts names of winner and loser of game.
    public void win(String winner, String loser) {
        int winIndex = ranking.indexOf(new Team(winner));
        ranking.get(winIndex).winGame(loser);
    }
    
    // Updates team's record after a loss. Accepts names of winner and loser of game.
    public void lose(String winner, String loser) {
        int loseIndex = ranking.indexOf(new Team(loser));
        ranking.get(loseIndex).loseGame(winner);
    }
    // Sorts standings based on season record and tiebreaker criteria.
    public void sortStandings() {
        Collections.sort(ranking, new Comparator<Team>() {
            @Override
            public int compare(Team team1, Team team2) {
                return tiebreaker(team1, team2);
            }
        });
    }
    
    // Returns difference between two team's win percentages. If they are equal, returns difference
    // between their head to head records. Otherwise, returns 0.
    public int tiebreaker(Team team1, Team team2) {
        if (team1.getPct() != team2.getPct()) {
            return (int)((team2.getPct() - team1.getPct())*1000);
        } else {
            int h2h = team1.h2h(team2);
            if (h2h != 0) {
                return h2h * -1;
            } else {
                return 0;
            }
        }
    }
    
    // Checks if a team has been eliminated from playoff contention based on their best possible 
    // record, the worst possible record of the 8th seed in their conference, and tiebreaker 
    // criteria. If a team has been eliminated, prints out the date on which they were eliminated.
    public void checkForEliminations(Date date) {
        Team eighthSeed = ranking.get(7);
        SimpleDateFormat df = new SimpleDateFormat("MM-dd-yyy");
        int eighthWins = eighthSeed.getWins();
        int eighthLosses = eighthSeed.getLosses();
        int eighthRemainingGames = 82 - eighthWins - eighthLosses;
        for (int i = 8; i < ranking.size(); i++) {
            Team currSeed = ranking.get(i);
            if (!currSeed.isEliminated()) {
                int currWins = currSeed.getWins();
                int currLosses = currSeed.getLosses();
                int currRemainingGames = 82 - currWins - currLosses;
                int winDiff = eighthWins - currWins;
                int lossDiff = eighthLosses - currLosses;
                double gamesBack = (winDiff * 1.0 - lossDiff) / 2;
                double makeUpGames = currRemainingGames / 2.0 + eighthRemainingGames / 2.0;

                if (gamesBack > makeUpGames) {
                    currSeed.eliminate();
                    System.out.printf("%-25s %10s\n", currSeed.getName(), df.format(date));
                } else if (gamesBack == makeUpGames) {
                    if (tiebreaker(currSeed, eighthSeed) > 0) {
                        currSeed.eliminate();
                        System.out.printf("%-25s %10s\n", currSeed.getName(), df.format(date));
                    }
                }
            }
        }
    }
    
    // Returns String represenation of conference standings. Gives 1-15 ranking to each team
    // based on record/tiebreaker information and prints them in order.
    public String toString() {
        String output = "";
        int rank = 1;
        for (Team team : ranking) {
            output += rank + " " + team + "\n";
            rank++;
        }
        return output;
    }
}