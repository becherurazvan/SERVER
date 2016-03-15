package Requests;

import Scrum.UserStory;

/**
 * Created by rbech on 3/5/2016.
 */
public class AddUsToCurrentSprintRequest extends Request{
    UserStory story;

    public AddUsToCurrentSprintRequest(String tokenId, UserStory story) {
        super(tokenId);
        this.story = story;
    }

    public AddUsToCurrentSprintRequest() {
    }

    public UserStory getStory() {
        return story;
    }
}
