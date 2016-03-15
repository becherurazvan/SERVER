package Requests;

import Scrum.Task;

/**
 * Created by rbech on 3/5/2016.
 */
public class UpdateTaskStateRequest extends Request{

    public String taskId;
    public String newState;
    public String usID;

    public UpdateTaskStateRequest(String tokenId, Task t) {
        super(tokenId);
        this.taskId = t.getId();
        this.newState = t.getState();
        this.usID = t.getUserStoryId();
    }

    public UpdateTaskStateRequest() {

    }

    public String getTaskId() {
        return taskId;
    }

    public String getNewState() {
        return newState;
    }

    public String getUsID() {
        return usID;
    }
}
