package Requests;

import Scrum.UserStory;

/**
 * Created by rbech on 2/24/2016.
 */
public class UpdateUserStoryRequest extends Request {
    UserStory story;

    public UpdateUserStoryRequest(String tokenId, UserStory story) {
        super(tokenId);
        this.story = story;
    }

    public UpdateUserStoryRequest() {
    }

    public UserStory getStory() {
        return story;
    }
}
