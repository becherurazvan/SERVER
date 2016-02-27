import Database.DB;
import Entities.Project;
import Entities.User;
import Google.DriveService;
import Google.GoogleSecret;
import Scrum.Task;
import Scrum.UserStory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpResponse;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.services.script.Script;
import com.google.api.services.script.model.ExecutionRequest;
import com.google.api.services.script.model.Operation;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.Assert;
import spark.utils.IOUtils;

import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Created by rbech on 2/6/2016.
 */
public class ProjectMembersTest {

    public static final String SCRIPT_ID = "M-U-JJbsD3ADEzkqkBzkAYUWzQRt5QScL";

    public static void main(String[] args) throws IOException {


        new GoogleSecret(false);
        DB db = DB.getInstance();



        User u = new User("colinearproductions@gmail.com","1l","http://","user1_ID");
        User u2 = new User("collinearproductions@gmail.com","2l","http://","user2_ID");
        u.setTokens("ya29.igI0NDoIEs9aUE79aB8TKPrccdGaDHOIHrUrk-Ueh_y9YBKv3mTbc9gVH5oqP4ODR8jx", "1/xipmZQ6wC5AAoI966f3H_QuWsNkY2SSFjutmKQ9Ydt1IgOrJDtdun6zK6XiATCKT"); // colinearproductions
        u2.setTokens("ya29.jwLEyQtkE6zFabVqBWjhQTlvTMDmOm_r9vXjU91uHg1Uf3efIuMsLgn7d7NkYgTzbNeC","1/kpl6RaPXjMCkqiIuctfyqvukjLz2LZZ4oVnzPeOjswU"); // collinearproductions


        Script scriptService = DriveService.getScriptService(u);

        String text = null;
        try {
            text = ProjectUtils.readDocument("17oGgnU3OaI0qUzWtJ1bxkON3PDPiXfmWh21F-BkVjYU",scriptService);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(text);

        Project p = new Project(u.getEmail(),"testing around");

        p.getProductBacklog().addUserStories(ProjectUtils.readUserStories(text));

        ObjectMapper mapper = new ObjectMapper();
        System.out.println(mapper.writeValueAsString(p));


    }



}
