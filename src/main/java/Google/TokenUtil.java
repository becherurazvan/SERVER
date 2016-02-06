package Google;

import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;

import java.io.IOException;

/**
 * Created by rbech on 1/25/2016.
 */
public class TokenUtil {

    public static GoogleTokenResponse getTokenResponse(String authCode){
        GoogleTokenResponse tokenResponse = null;
        try {
            tokenResponse=
                    new GoogleAuthorizationCodeTokenRequest(
                            new NetHttpTransport(),
                            JacksonFactory.getDefaultInstance(),
                            "https://www.googleapis.com/oauth2/v4/token",
                            GoogleSecret.getClientId(),
                            GoogleSecret.getClientSecret(),
                            authCode,
                            "")
                            .execute();
        } catch (IOException e) {
            System.err.println("TokenUtil:: invalid grant, bad request");
           return null;
        }
        return tokenResponse;
    }


    public static GoogleCredential getCredentials(String accessToken, String refreshToken){


        GoogleCredential credential = new GoogleCredential.Builder().setClientSecrets(GoogleSecret.getClientSecrets())
                .setJsonFactory(JacksonFactory.getDefaultInstance())
                .setTransport(new NetHttpTransport())
                .build();
        credential.setAccessToken(accessToken);
        credential.setRefreshToken(refreshToken);
        System.out.println("Access token: " + accessToken + "\nRefresh token: " + refreshToken + " \nexpiration : " + credential.getExpiresInSeconds());
        return credential;
    }

}
