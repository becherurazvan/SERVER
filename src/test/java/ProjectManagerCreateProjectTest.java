import Database.DB;
import Entities.Project;
import Entities.User;
import Google.DriveService;
import Google.GoogleSecret;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.ParentReference;
import com.google.api.services.script.Script;

import java.util.Arrays;

/**
 * Created by rbech on 2/16/2016.
 */
public class ProjectManagerCreateProjectTest {




    public static void main(String[] args){

        new GoogleSecret(false);
        DB db = DB.getInstance();

        User u = new User("user1@gail.com","user1","http://","user1_ID");
        db.registerUser(u);

        Project p = db.createProject(u.getEmail(),"Project A++");

        u.setTokens("ya29.igI0NDoIEs9aUE79aB8TKPrccdGaDHOIHrUrk-Ueh_y9YBKv3mTbc9gVH5oqP4ODR8jx", "1/xipmZQ6wC5AAoI966f3H_QuWsNkY2SSFjutmKQ9Ydt1IgOrJDtdun6zK6XiATCKT");

        Drive d =DriveService.getDriveService(u);
        DriveService.insertPermission(d,"0B8-dWhoNbYcMNzhPS0QtUmtGOWc","collinearproductions@gmail.com");


        /*
        Thread t = new Thread(new ProjectManager.ProjectInitializer(p,u));
        t.start();
        */
    }


}
