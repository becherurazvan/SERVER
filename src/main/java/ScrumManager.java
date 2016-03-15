import Database.Action;
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

import static spark.Spark.delete;
import static spark.Spark.post;
import static spark.Spark.put;

/**
 * Created by rbech on 2/24/2016.
 */
public class ScrumManager {


    ObjectMapper mapper;
    DB db;

    public ScrumManager() {


        db = DB.getInstance();

        mapper = new ObjectMapper();

        /*
        Add a user story to the product backlog
         */
        post("/scrum/add_userstory", (request, response) -> {
            AddStoryRequest addStoryRequest = mapper.readValue(request.body(), AddStoryRequest.class);
            GoogleIdToken idToken = GoogleIdToken.parse(JacksonFactory.getDefaultInstance(), addStoryRequest.getTokenId());
            String email = idToken.getPayload().getEmail();

            User u = db.getUser(email);
            Project p = db.getProject(u.getProjectId());
            ProductBacklog productBacklog = p.getProductBacklog();
            UserStory story = productBacklog.createUserStory(addStoryRequest.getStoryDescription());

            System.out.println("User " + email + " has added a new user story with id " + story.getId());

            p.notifyTeamOfBacklogUpdate(u);

            productBacklog.registerResource(story);
            //////////////////////////////////////////////////////////////
            System.err.println("pREPHIX " + story.generateTag() + " postphix");
            Action a = new Action(u.getEmail(),u.getName()," has created a new " + story.generateTag());
            db.registerAction(p.getId(),a);
            p.notifyTeamOfFeedUpdate();

            return mapper.writeValueAsString(p);
        });



        /*
        Add a task to a given user story, a task cannot exist without a user story
         */
        post("/scrum/add_task", (request, response) -> {
            AddTaskRequest addTaskRequest = mapper.readValue(request.body(), AddTaskRequest.class);
            GoogleIdToken idToken = GoogleIdToken.parse(JacksonFactory.getDefaultInstance(), addTaskRequest.getTokenId());
            String email = idToken.getPayload().getEmail();

            User u = db.getUser(email);
            Project p = db.getProject(u.getProjectId());
            ProductBacklog productBacklog = p.getProductBacklog();
            Task t = addTaskRequest.getTask();
            UserStory targetStory = productBacklog.getStoryById(t.getUserStoryId());
            Task task = productBacklog.getStoryById(t.getUserStoryId()).addTask(t);
            System.out.println("User " + email + " has added a new task:\n" + task.getId());


            //////////////////////////////////////////////////////////////
            productBacklog.registerResource(task);

            Action a = new Action(u.getEmail(),u.getName()," has added a new " +task.generateTag() + " to " + targetStory.generateTag());
            db.registerAction(p.getId(),a);


            p.notifyTeamOfBacklogUpdate(u);
            p.notifyTeamOfFeedUpdate();

            return mapper.writeValueAsString(p);
        });



        /*
        Edit an existing user story
         */
        post("/scrum/update_userstory", (request, response) -> {
            UpdateUserStoryRequest updateUserStoryRequest = mapper.readValue(request.body(), UpdateUserStoryRequest.class);
            GoogleIdToken idToken = GoogleIdToken.parse(JacksonFactory.getDefaultInstance(), updateUserStoryRequest.getTokenId());
            String email = idToken.getPayload().getEmail();

            User u = db.getUser(email);
            Project p = db.getProject(u.getProjectId());
            ProductBacklog productBacklog = p.getProductBacklog();

            UserStory story = productBacklog.updateUserStory(updateUserStoryRequest.getStory());
            System.out.println("User " + email + " updated user story :" + story.getId());


            //////////////////////////////////////////////////////////////

            Action a = new Action(u.getEmail(),u.getName()," has edited a new "+ story.generateTag());
            db.registerAction(p.getId(),a);


            p.notifyTeamOfBacklogUpdate(u);
            p.notifyTeamOfFeedUpdate();
            return mapper.writeValueAsString(p);
        });


        /*
        Create a new sprint
         */
        post("/scrum/create_sprint", (request, response) -> {
            CreateSprintRequest createSprintRequest = mapper.readValue(request.body(), CreateSprintRequest.class);
            GoogleIdToken idToken = GoogleIdToken.parse(JacksonFactory.getDefaultInstance(), createSprintRequest.getTokenId());
            String email = idToken.getPayload().getEmail();

            User u = db.getUser(email);
            Project p = db.getProject(u.getProjectId());
            ProductBacklog productBacklog = p.getProductBacklog();

            Sprint sprint = productBacklog.createSprint(createSprintRequest.getStartDate(), createSprintRequest.getEndDate());
            System.out.println("User " + email + " created a new sprint:" + sprint.getNumber());




            //////////////////////////////////////////////////////////////
            productBacklog.registerResource(sprint);

            Action a = new Action(u.getEmail(),u.getName()," has added a new "+ sprint.generateTag());
            db.registerAction(p.getId(),a);

            p.notifyTeamOfBacklogUpdate(u);
            p.notifyTeamOfFeedUpdate();


            return mapper.writeValueAsString(p);
        });



        /*
        Edit the state of a task
         */
        put("/scrum/task", (request, response) -> {
            UpdateTaskStateRequest updateTaskStateRequest = mapper.readValue(request.body(), UpdateTaskStateRequest.class);

            GoogleIdToken idToken = GoogleIdToken.parse(JacksonFactory.getDefaultInstance(), updateTaskStateRequest.getTokenId());
            String email = idToken.getPayload().getEmail();

            User u = db.getUser(email);
            Project p = db.getProject(u.getProjectId());
            ProductBacklog productBacklog = p.getProductBacklog();

            UserStory story = productBacklog.getStoryById(updateTaskStateRequest.getUsID());
            Task task = story.getTaskById(updateTaskStateRequest.getTaskId());


            String taskAssignedTo = task.getAssignedTo();


            //////////////////////////////////////////////////////////////


            if(task.getState().equals(Task.FINISHED_STATE)){
                Task cloned  =story.addTask(new Task(task.getDescription(),task.getPoints(),task.getUserStoryId()));

                productBacklog.registerResource(cloned);
                Action a = new Action(u.getEmail(),u.getName()," has cloned a "+ cloned.generateTag());
                db.registerAction(p.getId(),a);

            }else {
                if (taskAssignedTo == null) {
                    task.changeState(updateTaskStateRequest.getNewState(), u);

                    if(updateTaskStateRequest.getNewState().equals(Task.FINISHED_STATE)){
                        Action a = new Action(u.getEmail(),u.getName()," has finished working on "+ task.generateTag());
                        db.registerAction(p.getId(),a);
                    }else if(updateTaskStateRequest.getNewState().equals(Task.WORKING_ON_STATE)){
                        Action a = new Action(u.getEmail(),u.getName()," has started working on "+ task.generateTag());
                        db.registerAction(p.getId(),a);
                    }else if(updateTaskStateRequest.getNewState().equals(Task.NOT_STARTED_STATE)){
                        Action a = new Action(u.getEmail(),u.getName()," gave up on "+ task.generateTag());
                        db.registerAction(p.getId(),a);
                    }
                } else {
                    if (taskAssignedTo.equals(u.getName())) {
                        task.changeState(updateTaskStateRequest.getNewState(), u);

                        if(updateTaskStateRequest.getNewState().equals(Task.FINISHED_STATE)){
                            Action a = new Action(u.getEmail(),u.getName()," has finished working on "+ task.generateTag());
                            db.registerAction(p.getId(),a);
                        }else if(updateTaskStateRequest.getNewState().equals(Task.WORKING_ON_STATE)){
                            Action a = new Action(u.getEmail(),u.getName()," has started working on "+ task.generateTag());
                            db.registerAction(p.getId(),a);
                        }else if(updateTaskStateRequest.getNewState().equals(Task.NOT_STARTED_STATE)){
                            Action a = new Action(u.getEmail(),u.getName()," gave up on "+ task.generateTag());
                            db.registerAction(p.getId(),a);
                        }
                    }
                }
            }


            p.notifyTeamOfBacklogUpdate(u);
            p.notifyTeamOfFeedUpdate();
            return mapper.writeValueAsString(p);
        });




        /*
        edit the description and points of a task
         */
        put("/scrum/task_edit", (request, response) -> {
            EditTaskRequest req = mapper.readValue(request.body(),EditTaskRequest.class);
            GoogleIdToken idToken = GoogleIdToken.parse(JacksonFactory.getDefaultInstance(), req.getTokenId());
            String email = idToken.getPayload().getEmail();
            System.out.println(mapper.writeValueAsString(req));
            User u = db.getUser(email);
            Project p = db.getProject(u.getProjectId());
            ProductBacklog productBacklog = p.getProductBacklog();

            Task t= req.getTask();


            Task task = productBacklog.getStoryById(t.getUserStoryId()).getTaskById(t.getId());



            task.setDescription(req.getTask().getDescription());
            task.setPoints(req.getTask().getPoints());

            //////////////////////////////////////////////////////////////
            Action a = new Action(u.getEmail(),u.getName()," edited a "+ task.generateTag());
            db.registerAction(p.getId(),a);

            p.notifyTeamOfBacklogUpdate(u);
            p.notifyTeamOfFeedUpdate();
            return mapper.writeValueAsString(p);


        });


        // add US to current sprint
        post("/scrum/currentsprint", (request, response) -> {

            AddUsToCurrentSprintRequest req = mapper.readValue(request.body(), AddUsToCurrentSprintRequest.class);
            GoogleIdToken idToken = GoogleIdToken.parse(JacksonFactory.getDefaultInstance(), req.getTokenId());
            String email = idToken.getPayload().getEmail();

            User u = db.getUser(email);
            Project p = db.getProject(u.getProjectId());
            ProductBacklog productBacklog = p.getProductBacklog();

            UserStory story = productBacklog.getStoryById(req.getStory().getId());



            if(!productBacklog.getCurrentSprint().getUserStories().contains(story)) {
                productBacklog.getCurrentSprint().addUserStory(story);

                //////////////////////////////////////////////////////////////
                Action a = new Action(u.getEmail(),u.getName()," has added  "+story.generateTag() + " to " +productBacklog.getCurrentSprint().generateTag());
                db.registerAction(p.getId(),a);
            }




            p.notifyTeamOfBacklogUpdate(u);
            p.notifyTeamOfFeedUpdate();

            return mapper.writeValueAsString(p);
        });




        // Delete User story from current sprint
        post("/scrum/removeus", (request, response) -> {

            RemoveUsFromCurrentSprintRequest req = mapper.readValue(request.body(), RemoveUsFromCurrentSprintRequest.class);
            GoogleIdToken idToken = GoogleIdToken.parse(JacksonFactory.getDefaultInstance(), req.getTokenId());
            String email = idToken.getPayload().getEmail();
            User u = db.getUser(email);
            Project p = db.getProject(u.getProjectId());
            ProductBacklog productBacklog = p.getProductBacklog();

            UserStory story = productBacklog.getStoryById(req.getToRemove().getId());


            productBacklog.getCurrentSprint().removeUserStory(story);


            //////////////////////////////////////////////////////////////

            //////////////////////////////////////////////////////////////
            Action a = new Action(u.getEmail(),u.getName()," has removed "+story.getDescription() +  " from " + productBacklog.getCurrentSprint().generateTag());
            db.registerAction(p.getId(),a);

            p.notifyTeamOfBacklogUpdate(u);
            p.notifyTeamOfFeedUpdate();

            return mapper.writeValueAsString(p);
        });



        // Remove sprint
        post("/scrum/remove_sprint",((request, response) -> {
            DeleteSprintRequest req = mapper.readValue(request.body(), DeleteSprintRequest.class);
            GoogleIdToken idToken = GoogleIdToken.parse(JacksonFactory.getDefaultInstance(), req.getTokenId());
            String email = idToken.getPayload().getEmail();

            User u = db.getUser(email);
            Project p = db.getProject(u.getProjectId());
            ProductBacklog productBacklog = p.getProductBacklog();



            Sprint toRemove = productBacklog.getSprintByNumber(req.getSprintNumber());
            productBacklog.removeResourceById(toRemove.getResourceId());
            productBacklog.deleteSprint(req.getSprintNumber());

            //////////////////////////////////////////////////////////////

            Action a = new Action(u.getEmail(),u.getName()," has removed Sprint " + toRemove.getNumber());
            db.registerAction(p.getId(),a);


            p.notifyTeamOfBacklogUpdate(u);
            p.notifyTeamOfFeedUpdate();

            return mapper.writeValueAsString(p);
        }));
    }



}
