package Database;

import Scrum.DateManager;

import java.util.Calendar;

/**
 * Created by rbech on 3/14/2016.
 */
public class Action implements Comparable<Action> {


    Calendar date;
    String memberEmail;
    String memberName;
    String message;
    int id;


    public Action(String memberEmail,String memberName,  String message) {
        this.memberEmail = memberEmail;
        this.memberName=  memberName;
        this.message = message;
        date = Calendar.getInstance();

    }

    public Action() {
    }

    public void setId(int id){
        this.id = id;
    }
    public Calendar getDate() {
        return date;
    }

    public String getMemberEmail() {
        return memberEmail;
    }

    public String getMemberName() {
        return memberName;
    }

    public String getMessage() {
        return message;
    }

    public int getId() {
        return id;
    }

    @Override
    public int compareTo(Action o) {
        return o.getDate().compareTo(getDate());

    }
}
