package Database;

import Entities.Project;
import Entities.User;
import Requests.CreateProjectResponse;

import Requests.Response;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.jws.soap.SOAPBinding;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by rbech on 1/25/2016.
 */
public class DB {


    private HashMap<String, User> registeredUsers = new HashMap<>();
    private HashMap<String, Project> projects = new HashMap<>();

    private static DB instance = null;
    protected DB() {}
    public static DB getInstance() {
        if (instance == null)
            instance = new DB();
        return instance;
    }



    //////////////////////////////////////////////////////////// USERS
    public void registerUser(User u){
        registeredUsers.put(u.getEmail(),u);
        System.out.println("Succesfuly registered : " + u.getEmail());
    }
    public User getUser(String email){
        return registeredUsers.get(email);
    }
    public boolean userExists(String email){
        return registeredUsers.containsKey(email);
    }
    public void setUserTokens(){

    }
    public void userJoinedProject(String email, String projectId){

    }


    //////////////////////////////////////////////////////////// PROJECTS

    public Project createProject(String ownerEmail, String projectName){
        Project p = new Project(ownerEmail,projectName);
        projects.put(p.getId(),p);
        registeredUsers.get(ownerEmail).joinProject(p.getId());
        p.addMember(getUser(ownerEmail));
        System.out.println("Succefully created project : " + projectName);
        return p;
    }

    public void setFolderId(String projectId,String id){
        projects.get(projectId).setProjectFolderId(id);
    }

    public void setUserStoriesFileId(String projectId,String id){
        projects.get(projectId).setUserStoriesFileId(id);
    }



    public Project getProject(String id){

        return projects.get(id);
    }

    public Response joinProject(String email, String invitationCode){
        User u = registeredUsers.get(email);



        if(!projects.containsKey(invitationCode)){
            return new Response(false,"Invitation code invalid");
        }else{
            Project project = projects.get(invitationCode);
            project.addMember(getUser(email));
            u.joinProject(project.getId());


            return new Response(true,"Succesfully joined project with id : " +project.getId());
        }
    }


    public Project getProjectThatHasFileWithId(String id){
        Project pr = null;
        for(String s:projects.keySet()){
            Project p = projects.get(s);
            if(p.getUserStoriesFileId().equals(id)){
                pr = p;
            }
        }
        return pr;
    }





}
