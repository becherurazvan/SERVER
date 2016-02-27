package Google;


import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.json.jackson2.JacksonFactory;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class GoogleSecret {



    static GoogleClientSecrets clientSecrets;

    public GoogleSecret(boolean isHeroku){




        String secretFilePath;
        if(isHeroku)
            secretFilePath= new java.io.File("").getAbsolutePath()+"/target/classes/client_secret_script.json"; // remember to change back to without "script";
        else
            secretFilePath= new java.io.File("").getAbsolutePath()+"/src/main/java/client_secret_script.json"; // remember to change back to without "script";

        System.out.println("\n \n \n \n \n \n \n \n \n + NEW FILE ABSOLUT PATH:" + new java.io.File("").getAbsolutePath() + "\n \n \n \n \n \n \n \n \n");



        try {
            GoogleSecret.clientSecrets = GoogleClientSecrets.load(JacksonFactory.getDefaultInstance(),new FileReader(secretFilePath));
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
