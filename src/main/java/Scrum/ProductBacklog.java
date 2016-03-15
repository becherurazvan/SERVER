package Scrum;

import Database.Entry;
import Entities.Project;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;
import java.util.Calendar;

/*
The product backlog contains the overall progress (over sprints),
a list of all user stories and their allocation
and a list of sprints and which one is the current sprint.
*/
public class ProductBacklog implements DateManager.DayChangeListener {
    ArrayList<Sprint> sprints;
    Sprint currentSprint;


    ArrayList<UserStory> userStories;
    int latestUserStoryId = 1; // for id pooling
    int latestSprintNumber = 0;
    int dayOfProject = 0;
    String projectId;
    Project project;


    ArrayList<Resource> resources;



    public ProductBacklog(String projectId, Project p) {
        userStories = new ArrayList<>();
        this.projectId = projectId;
        sprints = new ArrayList<>();
        resources = new ArrayList<>();
        DateManager.getInstance().registerListener(this);
        System.out.println("Succesfully created product backlog for project " + projectId);
        this.project = p;
    }

    // to be used when parsing from drive document
    public void addUserStories(ArrayList<UserStory> stories) {
        for (UserStory story : stories) {
            story.setId(getUserStoryIdFromPool());
            userStories.add(story);
        }
    }

    public void addUserStory(UserStory story) {

        story.setId(getUserStoryIdFromPool());
        userStories.add(story);

    }

    // create user stories only from the product backlog
    public UserStory createUserStory(String description) {
        UserStory s = new UserStory(description);
        s.setId(getUserStoryIdFromPool());
        userStories.add(s);
        return s;
    }


    public void addSprint(Sprint s) {
        sprints.add(s);
        if (startToday(s)) { // if the sprint that has been added has today as start date and the current sprint is marked as ended
            currentSprint = s;
            s.start();
            System.out.println("Sprint is starting today");
        } else {
            System.out.println("Sprint is not starting today");
        }
    }

    public Sprint createSprint(String startDate, String endDate) {

        Sprint s = new Sprint(getSprintNumberFromPool(), startDate, endDate, project.getMembers());
        System.out.println("Creating new sprint from " + startDate + " to " + endDate);
        addSprint(s);
        return s;
    }


    public ArrayList<UserStory> getUserStories() {
        return userStories;
    }

    public void printStories() {
        for (UserStory story : userStories) {
            System.out.println(story.getId() + ":" + story.getDescription() + " Part of sprint : " + story.getSprintNr() + " WORTH:" + story.countSotryPoints() + " DONE: " + story.getAchievedPoints());
        }
    }

    private String getUserStoryIdFromPool() {
        String id = "US_" + latestUserStoryId;
        latestUserStoryId++;
        return id;
    }


    public UserStory updateUserStory(UserStory story) {
        UserStory s = null;
        for (UserStory userStory : userStories) {
            if (userStory.getId().equals(story.getId())) {
                userStory.setDescription(story.getDescription());
                s = userStory;
            }
        }
        if (s == null) {
            System.out.println("No such user story exists");
        }
        return s;
    }

    public ArrayList<Sprint> getSprints() {
        return sprints;
    }

    private int getSprintNumberFromPool() {
        latestSprintNumber++;
        return latestSprintNumber;
    }

    public boolean startToday(Sprint sprint) {
        Calendar calendar = DateManager.getInstance().getCalendar();
        if (DateManager.sameDay(sprint.getStartDateCalendar(), calendar))
            return true;
        else
            return false;
    }

    public void setCurrentSprint(Sprint s) {
        this.currentSprint = s;
    }

    public UserStory getStoryById(String id) {
        for (UserStory story : userStories) {
            System.out.println(story.getId() + " : " + id);
            if (story.getId().equals(id)) {
                return story;
            }
        }
        System.out.println("No such story exists");
        return null;
    }

    @Override
    public void onDateChanged(Calendar calendar) {

        dayOfProject++;
        System.out.println();
        System.out.println("Day has changed");


        if (currentSprint != null) {
            int dateDif = currentSprint.getEndDateCalendar().compareTo(calendar);
            if (dateDif <= 0) { // if the last day of sprint has passed
                currentSprint.end();
                System.out.println("Current sprint has ended");
                currentSprint = null;
            } else if (dateDif > 0) { // if it has not ended
                currentSprint.newDay();
            }
        }

        if (currentSprint == null)
            System.out.println("No current sprint");
        for (Sprint sprint : sprints) {
            if (startToday(sprint)) { // if one of the sprints start today
                System.out.println("New sprint started! until " + sprint.getEndDate());
                currentSprint = sprint;
                sprint.start();
            }
        }
        if (currentSprint == null) {
            System.out.println("No sprint ready to start today");
        }

        project.getRecord().addEntry(generateEntry());


    }


    public Entry generateEntry() {
        Entry e = new Entry();

        if (currentSprint != null) {
            e.setSprintNumber(currentSprint.getNumber());
            e.setSprintDayNumber(currentSprint.getDayOfSprint());
            e.setTotalSprintPoints(currentSprint.getTotalPoints());
            e.setTotalAchievedSprintPoints(currentSprint.getAchievedPoints());
            e.setTag(Entry.PART_OF_SPRINT_TAG);
        } else {
            e.setSprintNumber(-1);
            e.setSprintDayNumber(-1);
            e.setTotalSprintPoints(-1);
            e.setTotalAchievedSprintPoints(-1);

        }

        e.setProjectDayNumber(dayOfProject);
        e.setTotalAchievedPoints(getTotalPointsAchieved());
        e.setTotalPoints(getTotalUserStoryPoints());


        e.setDate(DateManager.getInstance().getCalendar());



        return e;
    }


    @JsonIgnore
    public int getTotalUserStoryPoints() {
        int total = 0;
        for (UserStory story : userStories) {
            total += story.countSotryPoints();
        }
        return total;
    }


    @JsonIgnore
    public int getTotalPointsAchieved() {
        int total = 0;
        for (UserStory story : userStories) {
            total += story.getAchievedPoints();
        }
        return total;
    }


    /////// BOILERPLATE


    public ProductBacklog() {

    }

    public void deleteSprint(int sprintNumber){
        for(Sprint s:sprints){
            if(s.getNumber()==sprintNumber){
                sprints.remove(s);
                return;
            }
        }
    }

    public Sprint getCurrentSprint() {
        return currentSprint;
    }


    public void registerResource(Resource resource){
        resources.add(resource);

    }


    public Resource getResourceById(String resourceId){
        for(Resource resource:resources){
            if(resource.getResourceId().equals(resourceId)){
                return resource;
            }
        }
        return null;
    }

    public void removeResourceById(String resourceId){
        for(Resource resource:resources){
            if(resource.getResourceId().equals(resourceId)){
                resources.remove(resource);
                return;
            }
        }
    }





    public Sprint getSprintByNumber(int n){
        for(Sprint s:sprints){
            if(s.getNumber()==n){
                return s;
            }
        }
        return null;
    }



}
