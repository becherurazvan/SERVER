
import Entities.User;
import Requests.LoginRequest;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;

import static spark.Spark.*;

public class UserManager {

   HashMap<String, User> registeredUsers = new HashMap<>();


    public UserManager() {

        post("/login", (request, response) -> {
            try{

                System.out.println("Received : " + request.body());
                ObjectMapper mapper = new ObjectMapper();
                LoginRequest loginRequest = mapper.readValue(request.body(),LoginRequest.class);

                registeredUsers.put(loginRequest.getEmail(),new User(loginRequest.getEmail(),loginRequest.getAuthCode(),loginRequest.getIdToken()));
                System.out.println("Registered " + loginRequest.getEmail());
                return "Succesfuly registered";
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
