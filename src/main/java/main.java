import Requests.LoginResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.client.googleapis.auth.oauth2.*;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;


import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class main {

    public static void main(String[] args){

       // new UserManager();



        String secretFilePath = new java.io.File("").getAbsolutePath()+"/src/main/java/client_secret.json";

        try {
            GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JacksonFactory.getDefaultInstance(),new FileReader(secretFilePath));

            /*
            GoogleTokenResponse tokenResponse =
                    new GoogleAuthorizationCodeTokenRequest(
                            new NetHttpTransport(),
                            JacksonFactory.getDefaultInstance(),
                            "https://www.googleapis.com/oauth2/v4/token",
                            clientSecrets.getDetails().getClientId(),
                            clientSecrets.getDetails().getClientSecret(),
                            "4/oedFaHbeu2tawECV7NrLycK4YPXHfj5KJBJNPG_WI3c",
                            "")
                            .execute();
            */

            String accessToken =  "ya29.dAJUCP0Fpe1sjAYi0m7qxfyCGLl36w_bSwilSHNd6p6Qgv9_PRI5Bg7JD4qN13XAc4OF"; //tokenResponse.getAccessToken();

            System.out.println(accessToken);


            GoogleCredential credential = new GoogleCredential().setAccessToken(accessToken);
            Drive drive =
                    new Drive.Builder(new NetHttpTransport(), JacksonFactory.getDefaultInstance(), credential)
                            .setApplicationName("SCRUMcompanionTest1")
                            .build();



            List<File> files = retrieveAllFiles(drive);
            System.out.println(files);
            for (File file : files) {
                System.out.println( file.getId() + " - " + file.getTitle());
            }



        } catch (IOException e) {
            e.printStackTrace();
        }




    }

    private static List<File> retrieveAllFiles(Drive service) throws IOException {
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

        return result;
    }
}
