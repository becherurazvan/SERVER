package Scrum;

import com.sun.istack.internal.NotNull;

/*
A note is an object with an ID and a string that holds notes about another object like a task or user stories.
In app it will show when checking details about the task or US and in drive it will be in a file called NOTES
With an annotation @Note ID=10.  This way if it needs to be a long text, the user will be able to set it from drive
instead of in-app
 */
public class Note {
    String id;
    String text;
    String setBy; // id;
    String date;

    public Note(@NotNull  String id, @NotNull String text, String setBy, String date) {
        this.id = id;
        this.text = text;
        this.setBy = setBy;
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public String getSetBy() {
        return setBy;
    }

    public String getDate() {
        return date;
    }
}
