package Requests;

/**
 * Created by rbech on 2/24/2016.
 */
public class AddStoryRequest extends Request{

    String storyDescription;
    public AddStoryRequest(String tokenId, String storyDescription) {
        super(tokenId);
        this.storyDescription = storyDescription;
    }

    public AddStoryRequest() {

    }

    public String getStoryDescription() {
        return storyDescription;
    }
}
