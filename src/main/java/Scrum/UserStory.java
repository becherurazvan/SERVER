package Scrum;

/*
A user story is in the form of <as a> < i want to> <so that>(optional)
A user story is composed of at least one task, represents a smaller step required to achieve the <i want to> of the
user story. The amount of points a user story is worth is the sum of points it's tasks are worth.
A user story is part of a sprint or unassigned, it has a order in the sprint backlog and a status.
 */

import Entities.User;

import java.util.ArrayList;

public class UserStory { // all or some, must at some point have some kind of acceptance tests attatched

    String id;
    String description;
    ArrayList<Note> notes;
    ArrayList<Task> tasks;
    ArrayList<String> assignedTo;
    int storyPoints=0;
    String status;
    String sprintId;
    int achievedStoryPoints;
    int boardOrder;

    String fileId;
    String bookmarkId;


    public UserStory(String id,String description) {
        notes=new ArrayList<>();
        tasks = new ArrayList<>();
        assignedTo = new ArrayList<>();
        this.id = id;
        status = Constants.STATUS_NEW;
        refreshStoryPoints();
        refreshAssignedTo();
        this.description = description;

        System.out.println("\n      New user story created id:" + id + "\n" +description);
    }
    public UserStory(){

    }



    public void addTask(Task task){
        tasks.add(task);
    }
    public void addNote(Note note){
        notes.add(note);
    }
    public void refreshStoryPoints(){
        storyPoints =0;
        for(Task t:tasks){
            storyPoints+=t.getPoints();
        }
    }

    public int getAchievedStoryPoints(){
        int p=0;
        for(Task t:tasks){
            if(t.isDone()){
                p+=t.getPoints();
            }
        }
        return p;
    }

    public void refreshAssignedTo(){
        assignedTo=new ArrayList<>();
        for(Task t:tasks){
            for(String s:t.getAssignedTo()){
                if(!assignedTo.contains(s))
                    assignedTo.add(s);
            }
        }
    }

    public void addTasks(ArrayList<Task> tasks){
        for(Task t:tasks)
            this.tasks.add(t);

        refreshStoryPoints();
    }

    public void printTasks(){
        for(Task t:tasks){
            t.print();
        }
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setSprintId(String sprintId) {
        this.sprintId = sprintId;
    }

    public void setBoardOrder(int boardOrder) {
        this.boardOrder = boardOrder;
    }

    public String getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public ArrayList<Note> getNotes() {
        return notes;
    }

    public ArrayList<Task> getTasks() {
        return tasks;
    }

    public ArrayList<String> getAssignedTo() {
        return assignedTo;
    }

    public int getStoryPoints() {
        return storyPoints;
    }

    public String getStatus() {
        return status;
    }

    public String getSprintId() {
        return sprintId;
    }

    public int getBoardOrder() {
        return boardOrder;
    }
}
