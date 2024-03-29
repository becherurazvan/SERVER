package ScrumTests;

import Database.DB;
import Database.Entry;
import Entities.Project;
import Entities.User;
import Scrum.*;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Random;

/**
 * Created by rbech on 2/13/2016.
 */
public class PointsTest {

    public static String pointTest(){
        DB db = DB.getInstance();
        new DateManager();

        User u = new User("colinearproductions@gmail.com", "USer 1", "URL", "UID");
        User u2 = new User("Email 2", "USer 2", "URL", "UID");
        User u3 = new User("Email 3", "USer 3", "URL", "UID");

        db.registerUser(u);
        db.registerUser(u2);
        db.registerUser(u3);

        ArrayList<User> users = new ArrayList<>();
        users.add(u);
        users.add(u2);
        users.add(u3);

       // Project p = new Project(u.getEmail(), "Project name");
        Project p = db.createProject(u.getEmail(),"PROJECT NAME");

        p.addMember(u2, true);
        p.addMember(u3, true);
        u2.joinProject(p.getId());
        u3.joinProject(p.getId());

        ProductBacklog backlog = p.getProductBacklog();
        String[] sprintDates = {"13 March 2016", "8 April 2016", "8 May 2016", "8 June 2016", "9 July 2016"};

        for (int i = 0; i < sprintDates.length - 1; i++) {
            backlog.createSprint(sprintDates[i], sprintDates[i + 1]);
        }

        ArrayList<Sprint> sprints = backlog.getSprints();

        for (Sprint sprint : sprints) {
            Random rnd = new Random();
            int uss = rnd.nextInt(20) + 10;
            for (int i = 0; i < uss; i++) {
                sprint.addUserStory(generateUS(backlog));
            }
        }

        for (int i = 0; i < 100; i++) {
            solveRandomTask(backlog, users);
            DateManager.getInstance().incrementDay();

        }

        backlog.printStories();


        System.out.println("Project burndown chart");

        System.out.format("%5s %25s %25s %25s %25s %25s %25s \n","Day #", "Sprint #","Sprint Day #","Total Achieved points","Total points","Sprint AchievedPoints", "Sprint Total Points");
        for(Entry e:p.getRecord().getBurndownChart()){
            System.out.format("%5s %25s %25s %25s %25s %25s %25s \n",
                    e.getProjectDayNumber(),e.getSprintNumber(),e.getSprintDayNumber(),e.getTotalAchievedPoints(),e.getTotalPoints(),e.getTotalAchievedSprintPoints(),e.getTotalSprintPoints());
        }

        System.out.println("Project id " + p.getId());


        System.out.println("PROJECT DURATION : " + p.calculateDaysInProject());

        for(Sprint s:p.getProductBacklog().getSprints()){
            System.out.println(s.getDayDuration());
        }
        return p.getId();


    }

    public static void main(String[] args){
        pointTest();
    }


    public static UserStory generateUS(ProductBacklog productBacklog) {
        Random rnd = new Random();
        UserStory userStory = productBacklog.createUserStory("US Description " + rnd.nextInt(1000));
        int tasks = rnd.nextInt(5) + 1;
        for (int i = 0; i < tasks; i++) {
            Task t = new Task("Task title " + userStory.getId(), rnd.nextInt(5) + 1, userStory.getId());
            userStory.addTask(t);
        }
        return userStory;
    }


    public static void solveRandomTask(ProductBacklog backlog, ArrayList<User> users) {

        Random rnd = new Random();


        Sprint curr = backlog.getCurrentSprint();


        ArrayList<UserStory> stories = curr.getUserStories();
        for(UserStory story:stories){
            for(Task t:story.getTasks()){
                if(rnd.nextInt(8)==5&&!t.getState().equals(Task.FINISHED_STATE)){
                    User u = users.get(rnd.nextInt(users.size()));
                    t.changeState(Task.FINISHED_STATE, u);
                }
            }
        }


    }


}
