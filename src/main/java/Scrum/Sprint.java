package Scrum;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

/*
A sprint is a time measurement unit related to the duration of the project. A project has multiple sprints.
A sprint has multiple user stories alocated to it. A sprint keeps track of the state of the user stories allocated to it
and the progress over time. It can tell if you are on time with the progress (by computing the trend of your progress)
and it can tell you the % of your progress.
*/
public class Sprint extends Resource{


    int number;
    String startDate;
    String endDate;
    int dayOfSprint;
    ArrayList<UserStory> userStories;
    String status;
    boolean started=false;
    boolean finished=false;
    ArrayList<String> members;


    public Sprint(int number, String startDate, String endDate, ArrayList<String> members) {
        super("SPRINT_"+number,RESOURCE_SPRINT);
        this.number = number;
        this.startDate = startDate;
        this.endDate = endDate;
        userStories = new ArrayList<>();
        this.members = members;

    }

    public Sprint() {
    }

    public void addUserStory(UserStory story){
        userStories.add(story);
        story.setSprintNr(number);
    }

    public void start(){
        started = true;
        dayOfSprint=0;

    }

    public void end(){
        started=false;
        finished=true;
    }
    public boolean isFinished(){
        return finished;
    }



    public void newDay(){
        dayOfSprint++;
    }

    @JsonIgnore
    public int getTotalPoints(){
        int p=0;
        for(UserStory userStory:userStories){
            p+=userStory.countSotryPoints();
        }
        return p;
    }

    public int getNumber() {
        return number;
    }

    @JsonIgnore
    public int getAchievedPoints(){
        int p=0;
        for(UserStory s:userStories){
           p+= s.getAchievedPoints();
        }
        return p;
    }


    // TODO:  remember to increment the month by 1
    @JsonIgnore
    public Calendar getStartDateCalendar(){
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMM yyyy");
        try {
            calendar.setTime(simpleDateFormat.parse(startDate));
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
        return calendar;
    }

    @JsonIgnore
    public Calendar getEndDateCalendar(){
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMM yyyy");
        try {
            calendar.setTime(simpleDateFormat.parse(endDate));
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
        return calendar;
    }



    public ArrayList<Task> getTasksByState(String STATE){
        ArrayList<Task> tasks = new ArrayList<>();
        for(UserStory s:userStories){
            for(Task t:s.getTasks()){
                if(t.getState().equals(STATE))
                    tasks.add(t);
            }
        }
        return tasks;
    }



    @JsonIgnore
    public UserStory getUserStoryById(String id){
        for(UserStory story:userStories)
        {
            if( story.getId().equals(id))
                return story;
        }
        return null;
    }


    public void removeUserStory(UserStory story){
        story.setSprintNr(-1);
        userStories.remove(story);
    }

    @JsonIgnore
    public int getDayDuration(){

        long diff = getEndDateCalendar().getTime().getTime() - getStartDateCalendar().getTime().getTime();

        int difference = (int) TimeUnit.DAYS.convert(diff,TimeUnit.MILLISECONDS);

        return difference;

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


    public int getDayOfSprint() {
        return dayOfSprint;
    }

    public void setDayOfSprint(int dayOfSprint) {
        this.dayOfSprint = dayOfSprint;
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

    public boolean isStarted() {
        return started;
    }

    public void setStarted(boolean started) {
        this.started = started;
    }
}
