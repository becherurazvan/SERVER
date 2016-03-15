package Entities;

import Database.DB;
import Database.Record;
import GCM.GCMmessenger;
import Google.DriveService;
import Scrum.ProductBacklog;
import Scrum.Sprint;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.api.services.drive.Drive;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.TimeUnit;


public class Project {
    ArrayList<String> members;// email and GCMtoken
    ArrayList<User> users;
    String id;
    String title;
    String leaderEmail;
    String invitationCode;
    String projectFolderId;
    String userStoriesFileId;
    ProductBacklog productBacklog;
    Record record;


    public Project(String leaderEmail, String title) {
        users = new ArrayList<>();
        members = new ArrayList<>();
        this.leaderEmail = leaderEmail;
        this.title = title;
        id = String.valueOf(Math.abs(new Random().nextInt(9000) + 1000));
        invitationCode = id;
        productBacklog = new ProductBacklog(id,this);
        record = new Record();
    }


    public Project() {
    }

    public Record getRecord(){
        return record;
    }

    public ProductBacklog getProductBacklog() {
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

    public void addMember(User u, boolean test) {

        users.add(u);
        members.add(u.getEmail());
        System.out.println("added member to project " + id);

        /////////////////////////////////////////////////
        // to-do remove comments
        //////////////////////////

        if(test)
            return;

        if (!u.getEmail().equals(leaderEmail)) {
            DriveService.insertPermission(DriveService.getDriveService(DB.getInstance().getUser(leaderEmail)), projectFolderId, u.getEmail()); // share project folder with the new member
            sendTeamNotification(u.getName() + " has joined your project!", "New user has joined the project!");
        }



    }

    public void sendTeamNotification(String msg, String title) {
        for (String s : members) {
            String token = DB.getInstance().getUser(s).getGcmToken();
            GCMmessenger.sendSimpleNotification(msg, title, token, true);
        }
    }


    public void notifyTeamOfBacklogUpdate() {
        for (String s : members) {
            String token = DB.getInstance().getUser(s).getGcmToken();
            GCMmessenger.sendBacklogUpdateRequest(token);
        }
    }

    public void notifyTeamOfBacklogUpdate(User except) {
        for (String s : members) {
            if (!s.equals(except.getEmail())) {
                String token = DB.getInstance().getUser(s).getGcmToken();
                GCMmessenger.sendBacklogUpdateRequest(token);
            }
        }
    }

    public void notifyTeamOfFeedUpdate(){
        for (String s : members) {

                String token = DB.getInstance().getUser(s).getGcmToken();
                GCMmessenger.sendFeedUpdateRequest(token);

        }
    }


    public void setUserStoriesFileId(String userStoriesFileId) {
        this.userStoriesFileId = userStoriesFileId;
    }


    public void setProjectFolderId(String projectFolderId) {
        this.projectFolderId = projectFolderId;
    }

    public String getProjectFolderId() {
        return projectFolderId;
    }

    public String getUserStoriesFileId() {
        return userStoriesFileId;
    }


    @JsonIgnore
    public int calculateDaysInProject(){
        Date firstSprintStartDate=null;
        Date lastSprintEndDate=null;
        int latestSprint=0;

        for(Sprint sprint:productBacklog.getSprints()){
            if(sprint.getNumber()>latestSprint) {
                latestSprint = sprint.getNumber();
                if(firstSprintStartDate==null){
                    firstSprintStartDate = sprint.getStartDateCalendar().getTime();
                }
            }
        }

        for(Sprint s:productBacklog.getSprints()){
            if(s.getNumber()==latestSprint)
                lastSprintEndDate = s.getEndDateCalendar().getTime();
        }

        if(firstSprintStartDate==null){
            return 0;
        }

        long diff = lastSprintEndDate.getTime() - firstSprintStartDate.getTime();

        int difference = (int) TimeUnit.DAYS.convert(diff,TimeUnit.MILLISECONDS);

        return difference;
    }

    public void setMembers(ArrayList<String> members) {
        this.members = members;
    }

    public ArrayList<User> getUsers() {
        return users;
    }

    public void setUsers(ArrayList<User> users) {
        this.users = users;
    }
}
