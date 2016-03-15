import Database.DB;
import Database.Entry;
import Entities.Project;
import Entities.User;
import Scrum.*;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;

/**
 * Created by rbech on 2/13/2016.
 */
public class PointsTest {

    public static String pointTest(String leader){
        DB db = DB.getInstance();
        new DateManager();

        User u = new User(leader, "Desene Dublate", "URL", "UID");
        User u2 = new User("colinearproductions@gmail.com", "USer 2", "URL", "UID");
        User u3 = new User("sbarcea.andrei94@gmail.com", "Andrei Sbarcea 3", "URL", "UID");

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
        String[] sprintDates = {"14 March 2016", "8 April 2016", "8 May 2016", "8 June 2016", "9 July 2016"};

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

        for (int i = 0; i < 90; i++) {
            solveRandomTask(backlog, users);
            DateManager.getInstance().incrementDay();

        }

        backlog.printStories();


        System.out.println("Project burndown chart");

        System.out.format("%5s %25s %25s %25s %25s %25s %25s %25s \n","Day #",
                "Sprint #","Sprint Day #","Total Achieved points","Total points","Sprint AchievedPoints",
                "Sprint Total Points", "Date");
        for(Entry e:p.getRecord().getBurndownChart()){
            System.out.format("%5s %25s %25s %25s %25s %25s %25s %25s \n",
                    e.getProjectDayNumber(),e.getSprintNumber(),e.getSprintDayNumber(),
                    e.getTotalAchievedPoints(),e.getTotalPoints(),e.getTotalAchievedSprintPoints(),
                    e.getTotalSprintPoints(),e.getDate().get(Calendar.DAY_OF_YEAR));
        }

        System.out.println("Project id " + p.getId());

        return p.getId();


    }


    public static UserStory generateUS(ProductBacklog productBacklog) {
        Random rnd = new Random();
        UserStory userStory = productBacklog.createUserStory("As a user \nI want to be able to read my emails\nSo that I can reply" + rnd.nextInt(1000));
        int tasks = rnd.nextInt(5) + 1;
        for (int i = 0; i < tasks; i++) {
            Task t = new Task("This task has the id  " + userStory.getId() + " and it serves as a placeholder for the task descripiton", rnd.nextInt(5) + 1, userStory.getId());
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
