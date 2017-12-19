// Updates standings game-by-game, and checks to see if a team has been eliminated from
// playoff contention at the end of a day.

import org.apache.poi.hssf.usermodel.HSSFCell;
import java.util.ArrayList;
import java.util.Date;

public class StandingsMain {
    
    public static void main(String[] args) throws Exception {
        Standings west = new Standings("west");
        Standings east = new Standings("east");
        ExcelReader excel = new ExcelReader();
        ArrayList scores = excel.makeList("Analytics_Attachment.xls", 1);
        findEliminations(scores, west, east);
    }
    
    
    // Accepts ArrayList containg Excel spreadsheet of scores, western conference, and eastern
    // conference initial Standings as parameters. Goes through game by game for each day of games,
    // and updates the standings with the results of the games from that day. At the end of the
    // day, checks to see if any teams have been eliminated from playoff contention. Repeats
    // for each day of the 2016-17 NBA season.
    public static void findEliminations(ArrayList scores, Standings west, Standings east) {       
        System.out.printf("%8s %30s\n", "Team", "Elimination Date");
        Date currentDate = ((HSSFCell)(((ArrayList)(scores.get(1))).get(0))).getDateCellValue();
        for (int i = 1; i < scores.size(); i++) {
            ArrayList currentLine = (ArrayList)(scores.get(i));
            
            int winnerNum = -1;
            int loserNum = -1;
            String winnerName = ((HSSFCell)(currentLine.get(5))).getStringCellValue();
            if (winnerName.equals("Home")) {
                winnerNum = 1;
                loserNum = 2;
            } else {
                winnerNum = 2;
                loserNum = 1;
            }
            
            String winningTeam = ((HSSFCell)(currentLine.get(winnerNum))).getStringCellValue();
            String losingTeam = ((HSSFCell)(currentLine.get(loserNum))).getStringCellValue();   
            if (west.hasTeam(winningTeam)) {
                west.win(winningTeam, losingTeam);                     
            } 
            if (west.hasTeam(losingTeam)) {
                west.lose(winningTeam, losingTeam);
            } 
            if (east.hasTeam(winningTeam)) {
                east.win(winningTeam, losingTeam);
            }
            if (east.hasTeam(losingTeam)) {
                east.lose(winningTeam, losingTeam);
            }
            
            if (i < scores.size() - 1) {
                ArrayList nextLine = (ArrayList)(scores.get(i + 1));
                Date nextDate = ((HSSFCell)(nextLine.get(0))).getDateCellValue();
                if (!nextDate.equals(currentDate)) {
                    eliminations(currentDate, west, east);
                    currentDate = nextDate;
                }
            }
        }
        eliminations(currentDate, west, east);
    }
    
    public static void eliminations(Date currentDate, Standings west, Standings east) {
        west.sortStandings();
        east.sortStandings();
        west.checkForEliminations(currentDate);
        east.checkForEliminations(currentDate);
    }
}