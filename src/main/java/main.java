import GCM.GCMmessenger;
import GCM.Message;
import Google.GoogleSecret;
import Google.TokenUtil;
import com.google.api.client.googleapis.auth.oauth2.*;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import com.google.api.services.script.model.ExecutionRequest;
import com.google.api.services.script.model.Operation;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import static spark.Spark.*;
public class main {

    public static void main(String[] args) throws IOException {

        boolean isHeroku=true;
        int port = getHerokuAssignedPort();
        if(port==4567)
            isHeroku=false;
        port(getHerokuAssignedPort());


        new GoogleSecret(isHeroku);
        new UserManager();
         new ProjectManager();
        new GCMmessenger();



        get("/google9da1222ff433ce18.html", (request, response) -> {
            return "google-site-verification: google9da1222ff433ce18.html";
        });

        get("/updated_document", (request, response) -> {
            System.out.println("Document updated " + request.queryMap().value("file_id"));
            return "Succesfully published";
        });



    }


    static int getHerokuAssignedPort() {
        ProcessBuilder processBuilder = new ProcessBuilder();
        if (processBuilder.environment().get("PORT") != null) {
            return Integer.parseInt(processBuilder.environment().get("PORT"));
        }
        return 4567; //return default port if heroku-port isn't set (i.e. on localhost)
    }

    public static Drive getDriveService(String accessToken, String refreshToken) {
        GoogleCredential credentials = TokenUtil.getCredentials(accessToken, refreshToken);
        Drive drive =
                new Drive.Builder(new NetHttpTransport(), JacksonFactory.getDefaultInstance(), credentials)
                        .setApplicationName("SCRUMcompanionTest1")
                        .build();
        return drive;
    }


    public static List<File> retrieveAllFiles(Drive service) throws IOException {
        List<File> result = new ArrayList<File>();
        Drive.Files.List request = service.files().list();

        do {
            try {
                FileList files = request.execute();

                result.addAll(files.getItems());
                request.setPageToken(files.getNextPageToken());
            } catch (IOException e) {
                System.out.println("An error occurred: " + e);
                request.setPageToken(null);
            }
        } while (request.getPageToken() != null &&
                request.getPageToken().length() > 0);

        for (File f : result)
            System.out.println(f.getTitle());

        return result;
    }
}
