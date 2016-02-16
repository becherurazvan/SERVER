
import Database.DB;
import Entities.User;
import GCM.GCMmessenger;
import Requests.*;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.json.jackson2.JacksonFactory;

import static spark.Spark.*;

public class UserManager {

    DB db = DB.getInstance();

    public UserManager() {


        DB db = DB.getInstance();
        ObjectMapper mapper = new ObjectMapper();

        /*
            When a user logs in, if it is the first time, we register him with all the data that we get from his tokenId
            If he is a returning user, we tell him wether he is part of a project or not
            At the point when the user joins a project, we will have his authentication and refresh token
            so we dont need them right now
         */
        post("/user/login", (request, response) -> {
            try{
                System.out.println("Received : " + request.body());

                Request loginRequest = mapper.readValue(request.body(),Request.class);

                GoogleIdToken idToken = GoogleIdToken.parse(JacksonFactory.getDefaultInstance(),loginRequest.getTokenId());
                GoogleIdToken.Payload payload = idToken.getPayload();

                String userId = payload.getSubject();
                System.out.println("User ID: " + userId);
                String email = payload.getEmail();
                String name = (String) payload.get("name");
                String pictureUrl = (String) payload.get("picture");
                System.out.println("Email: " + email + " name :  + " + name  +  " is trying to join");

                //returning user
                if(db.userExists(email)){
                    User u = db.getUser(email);
                    boolean partOfProject = u.isPartOfProject();
                    String projectId = u.getProjectId();
                    return  mapper.writeValueAsString(new LoginResponse("Welcome back! currently part of project? :" + partOfProject,true,partOfProject,projectId));
                }else{
                    User u = new User(email,name,pictureUrl,userId);
                    db.registerUser(u);
                    return mapper.writeValueAsString(new LoginResponse("Welcome new user!",true,false,null));
                }

            }catch (JsonParseException jpe){
                response.status(400);
                return mapper.writeValueAsString(new LoginResponse("Invalid json formating of request",false,false,null));
            }
        });

        post("/user/set_token", (request, response) -> {
            try{
                System.out.println("Received : " + request.body());
                SetTokenRequest setTokenRequest = mapper.readValue(request.body(),SetTokenRequest.class);

                GoogleIdToken idToken = GoogleIdToken.parse(JacksonFactory.getDefaultInstance(),setTokenRequest.getTokenId());
                GoogleIdToken.Payload payload = idToken.getPayload();
                String authCode = setTokenRequest.getAuthCode();
                String gcmToken = setTokenRequest.getGcmToken();

                User u = db.getUser(payload.getEmail());
                u.setGcmToken(gcmToken);

                GCMmessenger.sendSimpleNotification("Hello "+u.getName(),"Welcome!",u.getGcmToken(),true);

                if(u.setTokens(authCode)==true){
                    return mapper.writeValueAsString(new Response(true,"Succesfully set the tokens"));
                }else{
                    return mapper.writeValueAsString(new Response(false,"AuthCode invalid, please restart the application and try again"));
                }


            }catch (JsonParseException jpe){
                response.status(400);
                return mapper.writeValueAsString(new Response(false,"Invalid json formating of request"));
            }
        });



    }







}
