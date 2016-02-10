package Google;


import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.json.jackson2.JacksonFactory;

import java.io.FileReader;
import java.io.IOException;

public class GoogleSecret {



    static GoogleClientSecrets clientSecrets;

    public GoogleSecret(){
        String secretFilePath = new java.io.File("").getAbsolutePath()+"/src/main/java/client_secret.json";

        try {
            clientSecrets = GoogleClientSecrets.load(JacksonFactory.getDefaultInstance(),new FileReader(secretFilePath));
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public static GoogleClientSecrets getClientSecrets(){
        return clientSecrets;
    }

    public static String getClientId(){
        return clientSecrets.getDetails().getClientId();
    }

    public static String getClientSecret(){
        return  clientSecrets.getDetails().getClientSecret();
    }


}
