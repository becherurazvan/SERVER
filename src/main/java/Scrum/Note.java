package Scrum;


/*
A note is an object with an ID and a string that holds notes about another object like a task or user stories.
In app it will show when checking details about the task or US and in drive it will be in a file called NOTES
With an annotation @Note ID=10.  This way if it needs to be a long text, the user will be able to set it from drive
instead of in-app
 */
public class Note {
    String id;
    String text;
    String date;

    public Note(  String id, String text, String date) {
        this.id = id;
        this.text = text;
        this.date = date;
    }

    public Note(){}

    public String getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public String getDate() {
        return date;
    }
}
