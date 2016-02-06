package Entities;

import Database.DB;
import com.google.api.services.drive.Drive;

import java.util.ArrayList;
import java.util.Random;


public class Project {
    ArrayList<String> members;
    String id;
    String title;
    String leaderEmail;
    String invitationCode;


    String projectFolderId;

    public Project(String leaderEmail, String title) {
        members = new ArrayList<>();
        this.leaderEmail = leaderEmail;
        this.title = title;
        id =  String.valueOf(Math.abs(new Random().nextInt(9000)+1000));
        invitationCode = "INV_"+id;


    }


    public ArrayList<String> getMembers() {
        return members;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getLeaderEmail() {
        return leaderEmail;
    }

    public String getInvitationCode() {
        return invitationCode;
    }

    public void addMember(String email){
        members.add(email);
        System.out.println("added member to project "+ id);
    }


    public void setProjectFolderId(String projectFolderId) {
        this.projectFolderId = projectFolderId;
    }
}
