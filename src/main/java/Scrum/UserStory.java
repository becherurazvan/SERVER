package Scrum;

/*
A user story is in the form of <as a> < i want to> <so that>(optional)
A user story is composed of at least one task, represents a smaller step required to achieve the <i want to> of the
user story. The amount of points a user story is worth is the sum of points it's tasks are worth.
A user story is part of a sprint or unassigned, it has a order in the sprint backlog and a state.
 */

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;

public class UserStory extends Resource {

    String id;
    String description;
    ArrayList<Task> tasks;
    int sprintNr;
    int lastTaskId = 0;


    public UserStory(String description) {
        tasks = new ArrayList<>();
        this.description = description;
        sprintNr=-1;
    }

    public UserStory() {

    }


    public Task addTask(Task task) {
        task.setId(generateTaskId());
        tasks.add(task);
        return task;
    }

    public void setId(String id) {
        this.id = id;
        setResourceId(id);
        setResourceType(RESOURCE_US);
    }



    public void addTasks(ArrayList<Task> tasks) {
        for (Task t : tasks) {
            t.setId(generateTaskId());
            lastTaskId++;
            this.tasks.add(t);
        }
    }


    //////////////////////////////// BOILERPLATE
    public void printTasks() {
        for (Task t : tasks) {
            t.print();
        }
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setSprintNr(int sprintNr) {
        this.sprintNr = sprintNr;
    }


    public String getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    @JsonIgnore
    public Task getTaskById(String id){
        for(Task t:tasks){
            System.out.println("Searching " +id + " : " +t.getId());
            if(t.getId().equals(id)){
                return t;
            }
        }
        return null;
    }

    public ArrayList<Task> getTasks() {
        return tasks;
    }


    @JsonIgnore
    public int countSotryPoints() {
        int storyPoints = 0;
        for (Task t : tasks) {
            storyPoints += t.getPoints();
        }

        return storyPoints;
    }


    @JsonIgnore
    public int getAchievedPoints() {
        int points = 0;
        for (Task t : tasks) {
            if (t.state.equals(Task.FINISHED_STATE)) {
                points += t.getPoints();
            }
        }
        return points;
    }




    public int getSprintNr() {
        return sprintNr;
    }

    public String generateTaskId() {
        lastTaskId++;
        return id + "-T_" + lastTaskId;
    }


}
