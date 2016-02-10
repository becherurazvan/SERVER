package Entities;

import Database.DB;
import GCM.GCMmessenger;
import com.google.api.services.drive.Drive;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;


public class Project {
    HashMap<String,String> members;  // email and GCMtoken
    String id;
    String title;
    String leaderEmail;
    String invitationCode;


    String projectFolderId;

    public Project(String leaderEmail, String title) {
        members = new HashMap<>();
        this.leaderEmail = leaderEmail;
        this.title = title;
        id =  String.valueOf(Math.abs(new Random().nextInt(9000)+1000));
        invitationCode = "INV_"+id;


    }


    public ArrayList<String> getMembers() {
        return new ArrayList<String>(members.keySet());
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

    public void addMember(User u){
        members.put(u.getEmail(),u.getGcmToken());
        System.out.println("added member to project "+ id);

        sendTeamNotification(u.getName() + " has joined your project!");
    }

    public void sendTeamNotification(String msg){
        for(String s:members.keySet()){
            GCMmessenger.sendSimpleNotification(msg,"New User joined!",members.get(s),true);
        }
    }

    public void setProjectFolderId(String projectFolderId) {
        this.projectFolderId = projectFolderId;
    }
}
