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
    public static final String READ_DOCUMENT_FUNCTION = "readDocument";

    public static void main(String[] args) throws IOException {


        new GoogleSecret(false);
        DB db = DB.getInstance();

        User u = new User("user1@gail.com","user1","http://","user1_ID");
        User u1 = new User("user2@gail.com","user2","http://","user2_ID");
        User u2 = new User("user3@gail.com","user3","http://","user3_ID");
        User u3 = new User("user4@gail.com","user4","http://","user4_ID");

        db.registerUser(u1);
        db.registerUser(u2);
        db.registerUser(u3);
        db.registerUser(u);

        Project p = db.createProject(u.getEmail(),"Project A++");

        p.setProjectFolderId("0B8-dWhoNbYcMc0tyeG53MVFJSUU"); // project folder - templates

        db.joinProject(u1.getEmail(),p.getInvitationCode());
        db.joinProject(u2.getEmail(),p.getInvitationCode());
        db.joinProject(u3.getEmail(),p.getInvitationCode());

        u.setTokens("ya29.igI0NDoIEs9aUE79aB8TKPrccdGaDHOIHrUrk-Ueh_y9YBKv3mTbc9gVH5oqP4ODR8jx", "1/xipmZQ6wC5AAoI966f3H_QuWsNkY2SSFjutmKQ9Ydt1IgOrJDtdun6zK6XiATCKT");

        Drive service = DriveService.getDriveService(u);
        Script scriptService =DriveService.getScriptService(u);

        String loremIpsumFileId = "1hodo-L9tsX_uCG9-3VZ_FAWg8iM2jPlR-SUhEf9epao";
        String userStoriesId = "17oGgnU3OaI0qUzWtJ1bxkON3PDPiXfmWh21F-BkVjYU";
        System.out.println();


        String text = readDocument(userStoriesId,scriptService);
        System.out.println(text);
        p.getProductBacklog().addUserStories(readUserStories(text));


        ObjectMapper mapper  = new ObjectMapper();
        System.out.println(mapper.writeValueAsString(p));

        Project pr = mapper.readValue(mapper.writeValueAsString(p),Project.class);

    }


    public static ArrayList<UserStory> readUserStories(String txt) {
        ArrayList<String> splitted = new ArrayList<>(Arrays.asList(txt.split("@UserStory")));
        splitted.remove(0);

        ArrayList<UserStory> stories = new ArrayList<>();

        System.out.println(splitted.size() + " USER STORIES DETECTED");
        for (String s : splitted) {
            ArrayList<String> userStoryInfo = new ArrayList<>(Arrays.asList(s.split("\\r?\\n")));
            String userStoryId="";
            String description="";
            ArrayList<Task> tasks = new ArrayList<>();
            int line =0;
            for (String str : userStoryInfo) {
                if(!str.contains("id") && line ==0){
                    System.out.println("UserStory doesnt have an id, ignoring it");
                    break;
                }else if(line==0){
                    userStoryId= str.replace("id=","");
                }else{
                    if(!str.contains("@"))
                    {
                        description =description+"\n"+str;
                    }
                }
                line++;
            }
            tasks = getTasksFromString(s,userStoryId);
            if(!userStoryId.equals("")) {
                UserStory story = new UserStory(userStoryId, description);
                if(tasks!=null)
                    story.addTasks(tasks);
                stories.add(story);

            }

        }

        return stories;
    }

    public static ArrayList<Task> getTasksFromString(String s,String userStoryId){ // format: @Task id=1, points=10, title= askdaskdlasjdlkasjd
        ArrayList<Task> tasks = new ArrayList<>();
        if(!s.contains("@Task"))
            return null;
        ArrayList<String> tasksInfo = new ArrayList<>(Arrays.asList(s.split("\\r?\\n")));
        for(String str:tasksInfo){
            String taskId;
            String taskTitle = "";
            int taskPoints;
            if(str.contains("@Task")){
                String[] splittedTask = str.replace("@Task","").trim().split(",");
                taskId = splittedTask[0].replaceAll("\\s","").replace("id=","");
                taskPoints =Integer.parseInt(splittedTask[1].replaceAll("\\s","").replace("points=",""));
                for(int i=2;i<splittedTask.length;i++){
                    taskTitle = taskTitle + splittedTask[i].replace("title=","").replace("title =","");
                }
                tasks.add(new Task(taskId,taskTitle,taskPoints,userStoryId));
            }
        }
        return tasks;
    }




    public static String readDocument(String documentId, Script scriptService) throws IOException {
        List<Object> params = new ArrayList<>();
        params.add(documentId);

        ExecutionRequest request = new ExecutionRequest()
                .setParameters(params)
                .setFunction(READ_DOCUMENT_FUNCTION);

        Operation op = scriptService.scripts().run(SCRIPT_ID, request).execute();


        if (op.getError() != null) {
            // The API executed, but the script returned an error.
            Map<String, Object> detail = op.getError().getDetails().get(0);
            System.err.println(
                    "Script error! Message: " + detail.get("errorMessage"));
            return null;
        } else {
            // Here, the function returns an array of strings, so the
            // result must be cast into a Java List<String>.
            String documentText = (String) op.getResponse().get("result");
            return documentText;

        }
    }


}
