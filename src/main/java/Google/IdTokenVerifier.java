package Google;

import Database.DB;
import Entities.User;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Arrays;

// This class verifies that the user is who he says he is
public class IdTokenVerifier {

    static GoogleIdTokenVerifier verifier;


    public IdTokenVerifier(){

        verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), JacksonFactory.getDefaultInstance())
                .setAudience(Arrays.asList(GoogleSecret.getClientId()))
                // If you retrieved the token on Android using the Play Services 8.3 API or newer, set
                // the issuer to "https://accounts.google.com". Otherwise, set the issuer to
                // "accounts.google.com". If you need to verify tokens from multiple sources, build
                // a GoogleIdTokenVerifier for each issuer and try them both.
                .setIssuer("https://accounts.google.com")
                .build();


    }


    public static User verify(String idTokenString, boolean print) throws GeneralSecurityException, IOException {


        // (Receive idTokenString by HTTPS POST)

        System.err.println("ID TOKEN STRING " + idTokenString);
        GoogleIdToken idToken = verifier.verify(idTokenString);
        if (idToken != null) {
            GoogleIdToken.Payload payload = idToken.getPayload();

            // Print user identifier
            String userId = payload.getSubject();
            System.out.println("User ID: " + userId);

            // Get profile information from payload
            String email = payload.getEmail();
            boolean emailVerified = Boolean.valueOf(payload.getEmailVerified());
            String name = (String) payload.get("name");
            String pictureUrl = (String) payload.get("picture");
            String locale = (String) payload.get("locale");
            String familyName = (String) payload.get("family_name");

            if(print){
                System.out.println("Email : " + email);
                System.out.println("Email Verified : " + emailVerified);
                System.out.println("Name : " + name);
                System.out.println("Picture URL : " + pictureUrl);
                System.out.println("Locale  : " + locale);
                System.out.println("Family Name : " + familyName);
            }

            DB db = DB.getInstance();

            if(db.containsUser(email)) // if the user is already registered
            {
                System.out.println("User known");
                return db.getUser(email);

            }else{ // if it is the first time the user logs in
                User u= new User(email);
                db.addUser(email,u);
                return u;
            }


        } else {
            System.out.println("Invalid ID token !!!");
            return null;
        }
    }
}
