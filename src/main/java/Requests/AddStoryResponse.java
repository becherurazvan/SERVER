package Requests;

import Scrum.UserStory;

/**
 * Created by rbech on 2/24/2016.
 */
public class AddStoryResponse extends Response{

    UserStory story;
    public AddStoryResponse(boolean succesful, String message, UserStory story) {
        super(succesful, message);
        this.story=story;
    }

    public AddStoryResponse() {
    }

    public UserStory getStory() {
        return story;
    }
}
