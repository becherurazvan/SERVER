package Requests;

import Scrum.Task;

/**
 * Created by rbech on 2/24/2016.
 */
public class AddTaskRequest extends  Request{
    Task task;

    public AddTaskRequest(String tokenId, Task task) {
        super(tokenId);
        this.task = task;
    }

    public AddTaskRequest() {
    }

    public Task getTask() {
        return task;
    }
}
