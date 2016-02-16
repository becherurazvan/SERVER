package Entities;

import Database.DB;
import GCM.GCMmessenger;
import Google.DriveService;
import Scrum.ProductBacklog;
import com.google.api.services.drive.Drive;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;


public class Project {
    ArrayList<String> members;  // email and GCMtoken
    String id;
    String title;
    String leaderEmail;
    String invitationCode;


    String projectFolderId;
    String userStoriesFileId;
    String notesFileId;

    ProductBacklog productBacklog;

    public Project(String leaderEmail, String title) {
        members = new ArrayList<>();
        this.leaderEmail = leaderEmail;
        this.title = title;
        id =  String.valueOf(Math.abs(new Random().nextInt(9000)+1000));
        invitationCode = id;
        productBacklog = new ProductBacklog(id,title);


    }

    public Project(){

    }



    public ProductBacklog getProductBacklog(){
        return productBacklog;
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

    public void addMember(User u){

        members.add(u.getEmail());
        System.out.println("added member to project "+ id);

        if(!u.getEmail().equals(leaderEmail))
             DriveService.insertPermission(  DriveService.getDriveService(u),projectFolderId,u.getEmail()); // share project folder with the new member


      //  sendTeamNotification(u.getName() + " has joined your project!");
    }

    public void sendTeamNotification(String msg){
        for(String s:members){
            String token = DB.getInstance().getUser(s).getGcmToken();
            GCMmessenger.sendSimpleNotification(msg,"New User joined!",token,true);
        }
    }

    public void setUserStoriesFileId(String userStoriesFileId) {
        this.userStoriesFileId = userStoriesFileId;
    }

    public void setNotesFileId(String notesFileId) {
        this.notesFileId = notesFileId;
    }

    public void setProjectFolderId(String projectFolderId) {
        this.projectFolderId = projectFolderId;
    }
}
