
import Database.DB;
import Entities.User;
import Google.IdTokenVerifier;
import Requests.LoginRequest;
import Requests.LoginResponse;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

import static spark.Spark.*;

public class UserManager {

    DB db = DB.getInstance();

    public UserManager() {
        post("/login", (request, response) -> {

            try{
                System.out.println("Received : " + request.body());
                ObjectMapper mapper = new ObjectMapper();
                LoginRequest loginRequest = mapper.readValue(request.body(),LoginRequest.class);
                System.out.println("Received : " + loginRequest.authCode);



                // YOU ARE NO LONGER CHECKING IF THE USER IS WHO HE SAYS HE IS...
               // User u = IdTokenVerifier.verify(loginRequest.idToken,true);



                if(db.containsUser(loginRequest.getEmail())){
                    User u = db.getUser(loginRequest.getEmail());
                    return mapper.writeValueAsString(login(u,loginRequest.authCode));
                }else{
                  //  return mapper.writeValueAsString(new LoginResponse("Invalid ID token",false,false,false));
                    User u = new User(loginRequest.getEmail());
                    db.addUser(u.getEmail(),u);
                    return mapper.writeValueAsString(login(u,loginRequest.authCode));
                }


            }catch (JsonParseException jpe){
                response.status(400);
                ObjectMapper mapper = new ObjectMapper();
                return mapper.writeValueAsString(new LoginResponse("Invalid json formating of request",false,false,false));
            }
        });
    }

    public LoginResponse login(User u, String authCode){ // return something relevant
        u.setTokens(authCode); // everytime a user logs in, he will get a new refresh token and access token
        boolean partOfProject = u.isPartOfProject();
        boolean gotInvites = db.getUserInvites(u.getEmail())!=null;


        try {
            main.retrieveAllFiles( main.getDriveService(u.getAccessToken(),u.getRefreshToken()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new LoginResponse("Succesfully logged in ",true,partOfProject,gotInvites);
    }
}
