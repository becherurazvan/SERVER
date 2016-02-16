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

import java.util.ArrayList;
import java.util.Arrays;

import static spark.Spark.*;

public class ProjectManager {

    public static final String USER_STORIES_TEMPLATE_ID = "17oGgnU3OaI0qUzWtJ1bxkON3PDPiXfmWh21F-BkVjYU";
    public static final String USER_STORIES_TEMPLATE_DESCRIPTION="UserStories file created by the ScrumCompanion Server";
    public static final String NOTES_TEMPLATE_ID="15YBv6Q36uEo-BMFxYu0UTylfIOmbre5e6cCJhkRwifk";
    public static final String NOTES_TEMPLATE_DESCRIPTION="Notes file created by the ScrumCompanion server";
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


        post("/project/list_members", (request, response) -> {
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




    }






    public static class ProjectInitializer implements Runnable{
        Project p;
        Drive driveService;
        Script sriptService;

        public ProjectInitializer(Project p, User u){
            this.p = p;
            driveService = DriveService.getDriveService(u);

        }

        @Override
        public void run() {
            try {
                System.out.println("Initializing project >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
                String folderId; // id of project folder that will be set when the folder is created
                String userStoriesId; // id of the stories file;
                String notesFileId;// id of the notes file;

                File body = new File(); // create project folder
                body.setTitle(p.getTitle());
                body.setDescription(FOLDER_DESCRIPTION);
                body.setMimeType("application/vnd.google-apps.folder");
                body.setFolderColorRgb("#ffd83b");
                File f = driveService.files().insert(body).execute();
                folderId = f.getId();


                DB.getInstance().setFolderId(p.getId(),folderId);// set the folder id of the project


                File copiedUserStoryFile = new File(); // add the user stories template
                copiedUserStoryFile.setTitle("User Stories");
                copiedUserStoryFile.setDescription(USER_STORIES_TEMPLATE_DESCRIPTION);
                copiedUserStoryFile.setParents(Arrays.asList(new ParentReference().setId(folderId)));
                File projectUserStoryFile = driveService.files().copy(USER_STORIES_TEMPLATE_ID, copiedUserStoryFile).execute();
                userStoriesId = projectUserStoryFile.getId();

                DB.getInstance().setUserStoriesFileId(p.getId(),userStoriesId); // set the id of the user stories in the project



                File copiedNotesFile = new File(); // add the user stories template
                copiedUserStoryFile.setTitle("Notes");
                copiedUserStoryFile.setDescription(NOTES_TEMPLATE_DESCRIPTION);
                copiedUserStoryFile.setParents(Arrays.asList(new ParentReference().setId(folderId)));
                File projectNotesFile = driveService.files().copy(NOTES_TEMPLATE_ID, copiedUserStoryFile).execute();
                notesFileId = projectNotesFile.getId();

                DB.getInstance().setNotesFileId(p.getId(),notesFileId);


            }catch (Exception e){
                e.printStackTrace();
            }

        }
    }


}
