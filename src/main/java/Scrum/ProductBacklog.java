package Scrum;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;

/*
The product backlog contains the overall progress (over sprints), a list of all user stories and their allocation
and a list of sprints and which one is the current sprint.
*/
public class ProductBacklog {
    ArrayList<Sprint> sprints;
    Sprint currentSprint;
    int achievedPoints;

    ArrayList<UserStory> userStories;

    int latestSprintNumber;

    String projectId,projectTitle;

    public ProductBacklog(String projectId,String projectTitle) {
        latestSprintNumber=0;
        userStories = new ArrayList<>();
        this.projectId = projectId;
        this.projectTitle = projectTitle;
        sprints = new ArrayList<>();
        System.out.println("Succesfully created product backlog for project " + projectTitle);
    }


    public ProductBacklog(){

    }

    public Sprint getCurrentSprint() {
        return currentSprint;
    }

    public void addUserStory(UserStory story){
        userStories.add(story);
    }

    public void addUserStories(ArrayList<UserStory> stories){
        for(UserStory story:stories)
            userStories.add(story);
    }

    public UserStory createUserStory(String id,String description){
        UserStory s = new UserStory(id,description);
        userStories.add(s);
        return s;
    }
    public void addSprint(Sprint s){
        sprints.add(s);
    }

    public Sprint createSprint(int sprintNumber,String startDate,String endDate){
        Sprint s = new Sprint(sprintNumber,startDate,endDate);
        sprints.add(s);
        return s;
    }

    @JsonIgnore
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

    public ArrayList<UserStory> getUserStories() {
        return userStories;
    }
}
