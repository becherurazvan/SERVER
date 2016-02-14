package Google;

import Entities.User;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.script.Script;

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

    public static Script getScriptService(User u){
        String accessToken = u.getAccessToken();
        String refreshToken = u.getRefreshToken();

        GoogleCredential credentials = TokenUtil.getCredentials(accessToken, refreshToken);
        return new Script.Builder(new NetHttpTransport(), JacksonFactory.getDefaultInstance(), credentials)
                .setApplicationName("SCRUMcompanionTest1")
                .build();
    }


}
