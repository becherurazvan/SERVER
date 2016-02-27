package ScrumTests;

import Database.DB;
import Entities.Project;
import Entities.User;
import Google.GoogleSecret;
import Scrum.ProductBacklog;
import Scrum.UserStory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Date;

/**
 * Created by rbech on 2/24/2016.
 */
public class ScrumManagementTest {

    public static void main(String[] args){
        new GoogleSecret(false);
        DB db = DB.getInstance();
        ObjectMapper mapper = new ObjectMapper();

        User u = new User("colinearproductions@gmail.com","1l","http://","user1_ID");
        db.registerUser(u);

        User u2 = new User("collinearproductions@gmail.com","2l","http://","user2_ID");
        db.registerUser(u2);

        Project p = db.createProject(u.getEmail(),"testing");
        db.joinProject(u2.getEmail(),p.getId());


        ProductBacklog productBacklog =  db.getProject(u2.getProjectId()).getProductBacklog();

        productBacklog.createUserStory("As a user\nI want this to work... please");
        productBacklog.createUserStory("As a user\nI want this to work... please");
        productBacklog.createUserStory("As a user\nI want this to work... please");


        Date date = new Date();

        System.out.println(date);



    }
}
