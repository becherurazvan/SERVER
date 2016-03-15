package Scrum;

import Entities.User;
import com.fasterxml.jackson.annotation.JsonIgnore;


/*
A task is a granular part of a user story, every user story has at least one task with a set value of points
that makes up the story points value of the user story. It can be assigned to a user, it has a state, a tag (used
to categories it if needed) and an order (in the list of tasks of a user story)
 */
public class Task extends Resource {


    public static final String NOT_STARTED_STATE = "NOT_STARTED";
    public static final String WORKING_ON_STATE = "WORKING_ON";
    public static final String FINISHED_STATE = "FINISHED";
    public static final String HAS_ISSUE_STATE = "HAS_ISSUE";

    String id;
    String description;
    String userStoryId;
    int points;
    String state;
    String assignedTo;

    public Task(String title, int points, String userStoryId) {
        this.description = title;
        this.userStoryId = userStoryId;
        this.points = points;
        state = NOT_STARTED_STATE;
    }


    //////////////////////// BOILERPLATE


    public Task changeState(String newState, User user){

        if(newState.equals(NOT_STARTED_STATE)){
            assignedTo=null;
        }else{
            assignedTo=user.getName();
        }
        state = newState;

        return this;
    }


    public Task changeState(String newState){
        state = newState;
        return this;
    }

    public Task() {

    }
    public void setId(String id) {
        this.id = id;
        setResourceId(id);
        setResourceType(RESOURCE_TASK);
    }

    public String getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public String getUserStoryId() {
        return userStoryId;
    }

    public int getPoints() {
        return points;
    }

    public String getState() {
        return state;
    }

    @JsonIgnore
    public void print() {
        System.out.println("PRINTING TASK_____");
        System.out.println("Id:" + id);
        System.out.println("Points:" + points);
        System.out.println("Content: " + description);
    }

    public String getAssignedTo(){
        return  assignedTo;
    }

    public void assignTo(String asignee){
        this.assignedTo = asignee;
    }

    public void unasign(){
        assignedTo= null;
    }


    @JsonIgnore
    public boolean isDone(){
        return state.equals(FINISHED_STATE);
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPoints(int points) {
        this.points = points;
    }
}
