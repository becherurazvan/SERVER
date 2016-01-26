package Entities;

import Database.DB;

import java.util.ArrayList;
import java.util.Random;


public class Project {
    ArrayList<User> members;
    ArrayList<String> invitedUsers;
    int id;
    String title;
    String leaderEmail;
    String invitationCode;

    String projectFolderId;

    public Project(String leaderEmail, String title) {
        members = new ArrayList<>();
        invitedUsers = new ArrayList<>();
        this.leaderEmail = leaderEmail;
        this.title = title;
        id =  Math.abs(new Random().nextInt());



    }

    public void inviteMember(String email){
        invitedUsers.add(email);
    }

    public String join(String email){
        if(invitedUsers.contains(email)){
            if(members.contains(email))
                return "Already a member";
            members.add(DB.getInstance().getUser(email));
            invitedUsers.remove(email);
            return "Succesfully joined project " + title;
        }else{
            return "You have not been invited to this project";
        }
    }

    public String joinWithInvidationCode(String email,String code){
        if(code.equals(invitationCode))
        {
            if(members.contains(email))
                return "Already a member";
            members.add(DB.getInstance().getUser(email));
            if(invitedUsers.contains(email))
                invitedUsers.remove(email);
            return "Succesfully joined project " + title;
        }else{
            return "Invitation code invalid";
        }
    }

    public ArrayList<User> getMembers() {
        return members;
    }

    public ArrayList<String> getInvitedUsers() {
        return invitedUsers;
    }

    public String getId() {
        return String.valueOf(id);
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
}
