package Scrum;

import java.util.ArrayList;

/*
The product backlog contains the overall progress (over sprints), a list of all user stories and their allocation
and a list of sprints and which one is the current sprint.
*/
public class ProductBacklog {
    ArrayList<Sprint> sprints;
    Sprint currentSprint;
    int achievedPoints;

    public ProductBacklog() {
    }

    public Sprint getCurrentSprint() {
        return currentSprint;
    }

    public int getTotalPoints() {
        int points = 0;
        for(Sprint s:sprints){
            s.getCurrentPoints();
        }
        return points;
    }

    public int getAchievedPoints() {
        int p=0;
        for(Sprint s:sprints){
            p+=s.getAchievedPoints();
        }
        return p;
    }

    public void finishedCurrentSprint(){
        int currentSprintNr=currentSprint.getNumber();
        for(Sprint s:sprints){
            if(s.getNumber()==currentSprintNr+1)
                currentSprint = s;
        }
    }
}
