package Scrum;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

/*
A sprint is a time measurement unit related to the duration of the project. A project has multiple sprints.
A sprint has multiple user stories alocated to it. A sprint keeps track of the state of the user stories allocated to it
and the progress over time. It can tell if you are on time with the progress (by computing the trend of your progress)
and it can tell you the % of your progress.
*/
public class Sprint {

    int number;
    String startDate;
    String endDate;
    int durationInDays;
    int dayOfSprint;
    HashMap<Integer,Integer> progressPerDay;// day - story points done;
    ArrayList<UserStory> userStories;
    String status;
    ArrayList<Note> notes;
    boolean started;

    public Sprint(int number, String startDate, String endDate) {
        this.number = number;
        this.startDate = startDate;
        this.endDate = endDate;
        status=Constants.ON_TIME;
        userStories = new ArrayList<>();
        progressPerDay = new HashMap<>();
    }

    public void addUserStory(UserStory story){
        userStories.add(story);
    }
    public void start(){
        started = true;
        dayOfSprint=1;
    }
    public void newDay(){
        progressPerDay.put(dayOfSprint,getCurrentPoints());
        dayOfSprint++;
    }

    public int getCurrentPoints(){
        int p=0;
        for(UserStory userStory:userStories){
            p+=userStory.getStoryPoints();
        }
        return p;
    }

    public int getNumber() {
        return number;
    }

    public int getAchievedPoints(){
        int p=0;
        for(UserStory s:userStories){
           p+= s.getAchievedStoryPoints();
        }
        return p;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public int getDurationInDays() {
        return durationInDays;
    }

    public void setDurationInDays(int durationInDays) {
        this.durationInDays = durationInDays;
    }

    public int getDayOfSprint() {
        return dayOfSprint;
    }

    public void setDayOfSprint(int dayOfSprint) {
        this.dayOfSprint = dayOfSprint;
    }

    public HashMap<Integer, Integer> getProgressPerDay() {
        return progressPerDay;
    }

    public void setProgressPerDay(HashMap<Integer, Integer> progressPerDay) {
        this.progressPerDay = progressPerDay;
    }

    public ArrayList<UserStory> getUserStories() {
        return userStories;
    }

    public void setUserStories(ArrayList<UserStory> userStories) {
        this.userStories = userStories;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ArrayList<Note> getNotes() {
        return notes;
    }

    public void setNotes(ArrayList<Note> notes) {
        this.notes = notes;
    }

    public boolean isStarted() {
        return started;
    }

    public void setStarted(boolean started) {
        this.started = started;
    }
}
