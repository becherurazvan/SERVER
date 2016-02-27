import Database.DB;
import Entities.Project;
import Entities.User;
import Google.DriveService;
import Google.GoogleSecret;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.ParentReference;
import com.google.api.services.script.Script;

import java.io.IOException;
import java.util.Arrays;

/**
 * Created by rbech on 2/16/2016.
 */
public class ProjectManagerCreateProjectTest {




    public static void main(String[] args) throws InterruptedException, IOException {

        new GoogleSecret(false);
        DB db = DB.getInstance();

        User u = new User("colinearproductions@gmail.com","1l","http://","user1_ID");
        db.registerUser(u);

        User u2 = new User("collinearproductions@gmail.com","2l","http://","user2_ID");
        db.registerUser(u2);

        u.setTokens("ya29.igI0NDoIEs9aUE79aB8TKPrccdGaDHOIHrUrk-Ueh_y9YBKv3mTbc9gVH5oqP4ODR8jx", "1/xipmZQ6wC5AAoI966f3H_QuWsNkY2SSFjutmKQ9Ydt1IgOrJDtdun6zK6XiATCKT"); // colinearproductions
        u2.setTokens("ya29.jwLEyQtkE6zFabVqBWjhQTlvTMDmOm_r9vXjU91uHg1Uf3efIuMsLgn7d7NkYgTzbNeC","1/kpl6RaPXjMCkqiIuctfyqvukjLz2LZZ4oVnzPeOjswU"); // collinearproductions



        //Project p = CreateProjectTest(u,"Test project sharing");



       // DriveService.insertPermission(  DriveService.getDriveService(u),"0B8-dWhoNbYcMd0RCNmM0WUJlZVE",u2.getEmail()); // share project folder with the new member



        Drive driveService = DriveService.getDriveService(u2);


        File copiedUserStoryFile = new File(); // add the user stories template
        copiedUserStoryFile.setTitle("Notes");
        copiedUserStoryFile.setDescription("dexr");
        copiedUserStoryFile.setParents(Arrays.asList(new ParentReference().setId("0B-Y47leSbNAwZW95LWRQcUk4Z28")));
        File projectUserStoryFile = driveService.files().copy("17oGgnU3OaI0qUzWtJ1bxkON3PDPiXfmWh21F-BkVjYU", copiedUserStoryFile).execute();
        String  userStoriesId = projectUserStoryFile.getId();






    }


    public static Project CreateProjectTest(User u, String projectName){
        DB db = DB.getInstance();
        Project p = db.createProject(u.getEmail(),projectName);
      //  Drive d =DriveService.getDriveService(u);
        Thread t = new Thread(new ProjectManager.ProjectInitializer(p,u));
        t.start();
        return p;


    }

    public static void sleep(int sec) throws InterruptedException {
        for(int i=0;i<sec;i++){
            Thread.sleep(1000);
            System.out.println(sec-i);
        }
    }


}
