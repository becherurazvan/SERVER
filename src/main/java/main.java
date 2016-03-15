import Database.DB;
import Entities.Project;
import GCM.GCMmessenger;
import Google.DriveService;
import Google.GoogleSecret;
import Scrum.DateManager;
import Scrum.FeedManager;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.services.script.Script;
import com.google.gdata.data.geo.Point;


import java.io.IOException;

import static spark.Spark.*;

public class main {

    public static void main(String[] args) throws IOException {

        boolean isHeroku = true;
        int port = getHerokuAssignedPort();
        if (port == 4567)
            isHeroku = false;
        port(getHerokuAssignedPort());


        new DateManager();
        new GoogleSecret(isHeroku);
        new UserManager();
        new ProjectManager();
        new GCMmessenger();
        new ScrumManager();
        new FeedManager();


       //PointsTest.pointTest("collinearproductions@gmail.com");

      //  System.out.println(main.class.getProtectionDomain().getCodeSource().getLocation().getPath());


        get("/google9da1222ff433ce18.html", (request, response) -> {
            return "google-site-verification: google9da1222ff433ce18.html";
        });

        get("/updated_document", (request, response) -> {
            String fileId = request.queryMap().value("file_id");
            Project p = DB.getInstance().getProjectThatHasFileWithId(fileId);


            Script scriptService = DriveService.getScriptService(DB.getInstance().getUser(p.getLeaderEmail()));
            String text = null;
            try {
                text = ProjectUtils.readDocument(p.getUserStoriesFileId(), scriptService);
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println(text);
            p.getProductBacklog().addUserStories(ProjectUtils.readUserStories(text));

            p.notifyTeamOfBacklogUpdate();


            return "Succesfully published";
        });

        get("/incremenet_day",(request, response) -> {
            DateManager.getInstance().incrementDay();
            DB.getInstance().notifyNewDay();
            return DateManager.getInstance().getDate();
        });


        get("/report",((request, response) -> {
            String projectId = request.queryMap().value("project_id");
            ObjectMapper mapper = new ObjectMapper();
            if(DB.getInstance().getProject(projectId)!=null){
                return mapper.writeValueAsString(DB.getInstance().getProject(projectId).getRecord());
            }else{
                return "No such project exists";
            }
        }));


    }


    static int getHerokuAssignedPort() {
        ProcessBuilder processBuilder = new ProcessBuilder();
        if (processBuilder.environment().get("PORT") != null) {
            return Integer.parseInt(processBuilder.environment().get("PORT"));
        }
        return 4567; //return default port if heroku-port isn't set (i.e. on localhost)
    }


}
