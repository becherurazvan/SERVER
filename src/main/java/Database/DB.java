package Database;

import Entities.Project;
import Entities.User;

import javax.jws.soap.SOAPBinding;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by rbech on 1/25/2016.
 */
public class DB {



    private HashMap<String, User> registeredUsers = new HashMap<>();
    private HashMap<String, Project> activeProjects = new HashMap<>();
    private HashMap<String, ArrayList<String>> invites = new HashMap<>();// string is email of user, project id it has been invited to



    private static DB instance = null;
    protected DB() {}
    public static DB getInstance(){
        if(instance==null)
            instance = new DB();
        return instance;
    }

    public void addProject(Project p){
        activeProjects.put(p.getId(),p);
    }

    public void createInvitation(String projectId, String userEmail){
        if(invites.containsKey(userEmail))
            invites.get(userEmail).add(projectId);
        else {
            ArrayList<String> projects = new ArrayList<>();
            projects.add(projectId);
            invites.put(userEmail,projects);
        }
    }

    public ArrayList<String> getUserInvites(String email){
        if(!invites.containsKey(email))
            return null;
        return invites.get(email);
    }


    public boolean containsUser(String userEmail){
     return registeredUsers.containsKey(userEmail);
    }

    public HashMap<String, User> getRegisteredUsers() {
        return registeredUsers;
    }


    public void addUser(String k, User v){
        System.out.println("Added new user: " + k);
        registeredUsers.put(k,v);
    }

    public User getUser(String email){
        return registeredUsers.get(email);
    }
}
