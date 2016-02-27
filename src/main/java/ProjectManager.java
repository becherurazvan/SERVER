/**
 * Created by rbech on 1/26/2016.
 */

import Database.DB;
import Entities.Project;
import Entities.User;
import Entities.UserInfo;
import Google.DriveService;
import Requests.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.ParentReference;
import com.google.api.services.script.Script;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import static spark.Spark.*;

public class ProjectManager {

    public static final String USER_STORIES_TEMPLATE_ID = "17oGgnU3OaI0qUzWtJ1bxkON3PDPiXfmWh21F-BkVjYU";
    public static final String USER_STORIES_TEMPLATE_DESCRIPTION="UserStories file created by the ScrumCompanion Server";
    public static final String FOLDER_DESCRIPTION = "This folder was created for your project by the ScrumCompanion Server";

    public ProjectManager() {


        DB db = DB.getInstance();

        post("/project/create", (request, response) -> {
            ObjectMapper mapper = new ObjectMapper();
            CreateProjectRequest createProjectRequest = mapper.readValue(request.body(), CreateProjectRequest.class);
            System.out.println("Received : " + request.body());

            String projectName = createProjectRequest.getProjectName();
            GoogleIdToken idToken = GoogleIdToken.parse(JacksonFactory.getDefaultInstance(),createProjectRequest.getTokenId());
            String projectOwner = idToken.getPayload().getEmail();
            User u = db.getUser(projectOwner);
            Project p = db.createProject(projectOwner,projectName);

            Thread t = new Thread(new ProjectInitializer(p,u));
            t.start(); // initialising the project might fail, since there is no way for us to know how long it will take
            // we will have to notify the user at some point that the project is ready for him, will take advantage of GCM
            // to do this

            return mapper.writeValueAsString(new CreateProjectResponse(p));
        });

        post("/project/join", (request, response) -> { // returns plain response
            ObjectMapper mapper = new ObjectMapper();
            JoinProjectRequest joinRequest = mapper.readValue(request.body(),JoinProjectRequest.class);

            GoogleIdToken idToken = GoogleIdToken.parse(JacksonFactory.getDefaultInstance(),joinRequest.getTokenId());
            String email = idToken.getPayload().getEmail();
            System.out.println("User : " + email +  " with invitation code : " + joinRequest.getInvitationCode());

            return mapper.writeValueAsString(db.joinProject(email,joinRequest.getInvitationCode()));
        });

        get("/project/list_members", (request, response) -> {
            ObjectMapper mapper = new ObjectMapper();
            String projectId = request.queryMap("project_id").value();

            Project p = db.getProject(projectId);


            if(p==null){
                return "There is no project with id : " + projectId;
            }else{
                ArrayList<UserInfo> members = new ArrayList<UserInfo>();
                for(String s:p.getMembers()){
                    String name = DB.getInstance().getUser(s).getName();
                    String email = DB.getInstance().getUser(s).getEmail();
                    members.add(new UserInfo(name,email));
                }
                System.out.println(mapper.writeValueAsString(members));
                return mapper.writeValueAsString(members);
            }



        });



        post("/project/get_project", (request, response) -> {
            ObjectMapper mapper = new ObjectMapper();
            Request r= mapper.readValue(request.body(),Request.class);

            GoogleIdToken idToken = GoogleIdToken.parse(JacksonFactory.getDefaultInstance(),r.getTokenId());
            String email = idToken.getPayload().getEmail();

            String projectId = db.getUser(email).getProjectId();
            Project p = db.getProject(projectId);



            if(p==null){
                return "There is no project with id : " + projectId;
            }else{
                return mapper.writeValueAsString(p);
            }



        });



    }






    public static class ProjectInitializer implements Runnable{
        Project p;
        Drive driveService;
        Script scriptService;
        User u;

        public ProjectInitializer(Project p, User u){
            this.p = p;
            driveService = DriveService.getDriveService(u);
            scriptService = DriveService.getScriptService(u);
            this.u = u;

        }

        @Override
        public void run() {
            try {



                System.out.println("Initializing project >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
                String folderId; // id of project folder that will be set when the folder is created
                String userStoriesId; // id of the stories file;
                String notesFileId;// id of the notes file;


                //////////////// CREATE FOLDER PROJECT IN THE DRIVE OF THE LEADER
                File body = new File();
                body.setTitle(p.getTitle());
                body.setDescription(FOLDER_DESCRIPTION);
                body.setMimeType("application/vnd.google-apps.folder");
                body.setFolderColorRgb("#1FA5D1");
                File f = driveService.files().insert(body).execute();
                folderId = f.getId();
                DB.getInstance().setFolderId(p.getId(),folderId);// set the folder id of the project



                // Add the user story file to the project folder
                File copiedUserStoryFile = new File(); // add the user stories template
                copiedUserStoryFile.setTitle("User Stories");
                copiedUserStoryFile.setDescription(USER_STORIES_TEMPLATE_DESCRIPTION);
                copiedUserStoryFile.setParents(Arrays.asList(new ParentReference().setId(folderId)));
                File projectUserStoryFile = driveService.files().copy(USER_STORIES_TEMPLATE_ID, copiedUserStoryFile).execute();
                userStoriesId = projectUserStoryFile.getId();
                DB.getInstance().setUserStoriesFileId(p.getId(),userStoriesId); // set the id of the user stories in the project



                u.sendNotification("Project Ready","Your project is ready",true);




               // readUserStories(p,scriptService);

                p.notifyTeamOfBacklogUpdate();


            }catch (Exception e){
                e.printStackTrace();
            }

        }
    }

    public static void readUserStories(Project p,Script scriptService){

        String text = null;
        try {
            text = ProjectUtils.readDocument(p.getUserStoriesFileId(),scriptService);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(text);
        p.getProductBacklog().addUserStories(ProjectUtils.readUserStories(text));



    }


}
