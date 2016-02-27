    package Scrum;

    import com.fasterxml.jackson.annotation.JsonIgnore;

    import java.util.ArrayList;

    /*
    A task is a granular part of a user story, every user story has at least one task with a set value of points
    that makes up the story points value of the user story. It can be assigned to a user, it has a status, a tag (used
    to categories it if needed) and an order (in the list of tasks of a user story)
     */
    public class Task {
        String id;
        String title;
        String userStoryId;
        int points;
        ArrayList<String> assignedTo;
        String status;
        boolean done;
        int boardOrder;
        String tag;
        ArrayList<Note> notes;


        public Task( String title, int points,String userStoryId) {
            this.title = title;
            this.userStoryId = userStoryId;
            this.points = points;
            assignedTo=new ArrayList<>();
            notes = new ArrayList<>();
            status = Constants.STATUS_NEW;

            System.out.println("New Task created id:" + id + " worth: "+points + "\n---->" +title);
        }

        public Task(){

        }

        public void setId(String id){
            this.id = id;
        }

        @JsonIgnore
        public boolean isPartOfUserStory(){
            return userStoryId!=null;
        }
        public void assignTo(String userId){
            assignedTo.add(userId);
        }

        public void unassign(String userId){
            if(assignedTo.contains(userId))
                assignedTo.remove(userId);
        }
        public void addNote(Note note){
            notes.add(note);
        }
        public boolean isDone(){
            return done;
        }


        public void setUserStoryId(String userStoryId) {
            this.userStoryId = userStoryId;
        }

        public void setBoardOrder(int boardOrder) {
            this.boardOrder = boardOrder;
        }

        public void setTag(String tag) {
            this.tag = tag;
        }

        public void setPoints(int points) {
            this.points = points;
        }

        public void setStatus(String status) {
            this.status = status;
            if(this.status.equals("DONE"))
                done = true;
            else
                done = false;
        }

        public String getId() {
            return id;
        }

        public String getTitle() {
            return title;
        }

        public String getUserStoryId() {
            return userStoryId;
        }

        public int getPoints() {
            return points;
        }

        public ArrayList<String> getAssignedTo() {
            return assignedTo;
        }

        public String getStatus() {
            return status;
        }

        public int getBoardOrder() {
            return boardOrder;
        }

        public String getTag() {
            return tag;
        }

        public ArrayList<Note> getNotes() {
            return notes;
        }

        public void print(){
            System.out.println("PRINTING TASK_____");
            System.out.println("Id:" + id);
            System.out.println("Points:" + points);
            System.out.println("Content: " +title);
        }
    }
