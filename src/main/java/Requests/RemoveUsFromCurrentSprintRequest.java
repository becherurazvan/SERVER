package Requests;

import Scrum.UserStory;

/**
 * Created by rbech on 3/6/2016.
 */
public class RemoveUsFromCurrentSprintRequest extends Request {

    UserStory toRemove;

    public RemoveUsFromCurrentSprintRequest(String tokenId, UserStory toRemove) {
        super(tokenId);
        this.toRemove = toRemove;
    }

    public RemoveUsFromCurrentSprintRequest() {

    }

    public UserStory getToRemove() {
        return toRemove;
    }
}
