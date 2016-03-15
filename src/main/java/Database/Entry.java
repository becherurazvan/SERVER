package Database;

import Scrum.DateManager;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.xml.crypto.Data;
import java.util.Calendar;

public class Entry implements Comparable<Entry> {

    public static String PART_OF_SPRINT_TAG = "SPRINT";

    public Calendar date; // date of record
    public int totalPoints; // total project points
    public int totalAchievedPoints; // total achieved points
    public int projectDayNumber;
    public String tag;


    public int totalSprintPoints;
    public int totalAchievedSprintPoints;
    public int sprintNumber;
    public int sprintDayNumber;


    @Override
    public int compareTo(Entry o) {
        return date.compareTo(o.date);
    }


    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public Calendar getDate() {
        return date;
    }

    public void setDate(Calendar date) {
        this.date = (Calendar) date.clone();
    }

    @JsonIgnore
    public String getParsedDate() {
        return date.get(Calendar.DAY_OF_MONTH) + "." + (date.get(Calendar.MONTH) + 1) + "." + date.get(Calendar.YEAR);

    }

    public int getTotalPoints() {
        return totalPoints;
    }

    public void setTotalPoints(int totalPoints) {
        this.totalPoints = totalPoints;
    }

    public int getTotalAchievedPoints() {
        return totalAchievedPoints;
    }

    public void setTotalAchievedPoints(int totalAchievedPoints) {
        this.totalAchievedPoints = totalAchievedPoints;
    }

    public int getProjectDayNumber() {
        return projectDayNumber;
    }

    public void setProjectDayNumber(int projectDayNumber) {
        this.projectDayNumber = projectDayNumber;
    }

    public int getTotalSprintPoints() {
        return totalSprintPoints;
    }

    public void setTotalSprintPoints(int totalSprintPoints) {
        this.totalSprintPoints = totalSprintPoints;
    }

    public int getTotalAchievedSprintPoints() {
        return totalAchievedSprintPoints;
    }

    public void setTotalAchievedSprintPoints(int totalAchievedSprintPoints) {
        this.totalAchievedSprintPoints = totalAchievedSprintPoints;
    }

    public int getSprintNumber() {
        return sprintNumber;
    }

    public void setSprintNumber(int sprintNumber) {
        this.sprintNumber = sprintNumber;
    }

    public int getSprintDayNumber() {
        return sprintDayNumber;
    }

    public void setSprintDayNumber(int sprintDayNumber) {
        this.sprintDayNumber = sprintDayNumber;
    }
}
