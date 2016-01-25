
import Database.DB;
import Entities.User;
import Requests.LoginRequest;
import Requests.LoginResponse;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;

import static spark.Spark.*;

public class UserManager {

    DB db = DB.getInstance();


    public UserManager() {

        post("/login", (request, response) -> {
            try{
                System.out.println("Received : " + request.body());
                ObjectMapper mapper = new ObjectMapper();
                LoginRequest loginRequest = mapper.readValue(request.body(),LoginRequest.class);


                if(db.contains(loginRequest.getEmail())){

                    return mapper.writeValueAsString(new LoginResponse("Welcome user"));
                }else {

                    db.addUser(loginRequest.getEmail(), new User(loginRequest.getEmail(), loginRequest.getAuthCode(), loginRequest.getIdToken()));
                    System.out.println("Registered " + loginRequest.getEmail());
                    return mapper.writeValueAsString(new LoginResponse("Succesfully registered mofo"));
                }
            }catch (JsonParseException jpe){
                response.status(400);
                System.err.println("JPE "  + jpe.getMessage());
                return jpe.toString();
            }catch (Exception e){
                response.status(400);
                System.err.println("Exception e "  + e.getMessage());
                return "nope";
            }
        });




    }
}
