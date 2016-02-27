package Google;

import Entities.User;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.Permission;
import com.google.api.services.script.Script;

import java.io.IOException;

/**
 * Created by rbech on 2/6/2016.
 */
public class DriveService {



    public static Drive getDriveService(User u) {


        String accessToken = u.getAccessToken();
        String refreshToken = u.getRefreshToken();

        GoogleCredential credentials = TokenUtil.getCredentials(accessToken, refreshToken);
        Drive drive =
                new Drive.Builder(new NetHttpTransport(), JacksonFactory.getDefaultInstance(), credentials)
                        .setApplicationName("SCRUMcompanionTest1")
                        .build();


        return drive;
    }


    public static Drive getDriveService(String accessToken, String refreshToken) {
        GoogleCredential credentials = TokenUtil.getCredentials(accessToken, refreshToken);
        Drive drive =
                new Drive.Builder(new NetHttpTransport(), JacksonFactory.getDefaultInstance(), credentials)
                        .setApplicationName("SCRUMcompanionTest1")
                        .build();


        return drive;
    }

    public static Script getScriptService(User u){
        String accessToken = u.getAccessToken();
        String refreshToken = u.getRefreshToken();

        GoogleCredential credentials = TokenUtil.getCredentials(accessToken, refreshToken);
        return new Script.Builder(new NetHttpTransport(), JacksonFactory.getDefaultInstance(), credentials)
                .setApplicationName("SCRUMcompanionTest1")
                .build();
    }

    public static Script getScriptService(String accessToken, String refreshToken){
        GoogleCredential credentials = TokenUtil.getCredentials(accessToken, refreshToken);
        return new Script.Builder(new NetHttpTransport(), JacksonFactory.getDefaultInstance(), credentials)
                .setApplicationName("SCRUMcompanionTest1")
                .build();
    }



    public static Permission insertPermission(Drive service, String fileId,String userEmail) {
        Permission newPermission = new Permission();

        newPermission.setValue(userEmail);
        newPermission.setType("user");
        newPermission.setRole("writer");
        try {
            return service.permissions().insert(fileId, newPermission).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


}
