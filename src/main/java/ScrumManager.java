import Database.DB;
import Entities.Project;
import Entities.User;
import Requests.*;
import Scrum.ProductBacklog;
import Scrum.Sprint;
import Scrum.Task;
import Scrum.UserStory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.json.jackson2.JacksonFactory;

import static spark.Spark.post;

/**
 * Created by rbech on 2/24/2016.
 */
public class ScrumManager {


    ObjectMapper mapper;
    DB db;

    public ScrumManager(){


        db= DB.getInstance();

        mapper = new ObjectMapper();
        post("/scrum/add_userstory", (request, response) -> {
            AddStoryRequest addStoryRequest = mapper.readValue(request.body(),AddStoryRequest.class);
            GoogleIdToken idToken = GoogleIdToken.parse(JacksonFactory.getDefaultInstance(),addStoryRequest.getTokenId());
            String email = idToken.getPayload().getEmail();

            User u = db.getUser(email);
            Project p  = db.getProject(u.getProjectId());
            ProductBacklog productBacklog = p.getProductBacklog();
            UserStory story = productBacklog.createUserStory(addStoryRequest.getStoryDescription());

            System.out.println("User " + email + " has added a new user story with id " + story.getId());

            notifyInTen(p,u);

            story.refreshStoryPoints();


            return mapper.writeValueAsString(new AddStoryResponse(true,"",story));
        });


        post("/scrum/add_task", (request, response) -> {
            AddTaskRequest addTaskRequest = mapper.readValue(request.body(),AddTaskRequest.class);
            GoogleIdToken idToken = GoogleIdToken.parse(JacksonFactory.getDefaultInstance(),addTaskRequest.getTokenId());
            String email = idToken.getPayload().getEmail();

            User u = db.getUser(email);
            Project p  = db.getProject(u.getProjectId());
            ProductBacklog productBacklog = p.getProductBacklog();
            Task task = productBacklog.addTask(addTaskRequest.getTask());

            System.out.println("User " + email + " has added a new task:\n" + task.getTitle());



            notifyInTen(p,u);



            return mapper.writeValueAsString(task);
        });



        post("/scrum/update_userstory", (request, response) -> {
            UpdateUserStoryRequest updateUserStoryRequest = mapper.readValue(request.body(),UpdateUserStoryRequest.class);
            GoogleIdToken idToken = GoogleIdToken.parse(JacksonFactory.getDefaultInstance(),updateUserStoryRequest.getTokenId());
            String email = idToken.getPayload().getEmail();

            User u = db.getUser(email);
            Project p  = db.getProject(u.getProjectId());
            ProductBacklog productBacklog = p.getProductBacklog();

            UserStory story = productBacklog.updateUserStory(updateUserStoryRequest.getStory());


            System.out.println("User " + email + " updated user story :" + story.getId());

            notifyInTen(p,u);



            return mapper.writeValueAsString(story);
        });



        post("/scrum/create_sprint", (request, response) -> {
            CreateSprintRequest createSprintRequest = mapper.readValue(request.body(),CreateSprintRequest.class);
            GoogleIdToken idToken = GoogleIdToken.parse(JacksonFactory.getDefaultInstance(),createSprintRequest.getTokenId());
            String email = idToken.getPayload().getEmail();

            User u = db.getUser(email);
            Project p  = db.getProject(u.getProjectId());
            ProductBacklog productBacklog = p.getProductBacklog();

            Sprint sprint = productBacklog.createSprint(createSprintRequest.getStartDate(),createSprintRequest.getEndDate());


            System.out.println("User " + email + " created a new sprint:" + sprint.getNumber());

           // notifyInTen(p,u);



            return mapper.writeValueAsString(sprint);
        });


    }

    public void notifyInTen(Project p,User u){
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                p.notifyTeamOfBacklogUpdate(u);
            }
        });

        t.start();
    }
}
