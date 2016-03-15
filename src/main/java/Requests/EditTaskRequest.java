package Requests;

import Scrum.Task;

/**
 * Created by rbech on 3/13/2016.
 */
public class EditTaskRequest extends Request{

    Task task;

    public EditTaskRequest(String tokenId, Task task) {
        super(tokenId);
        this.task = task;
    }

    public EditTaskRequest() {
    }

    public Task getTask() {
        return task;
    }
}
