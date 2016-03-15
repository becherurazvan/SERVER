package Requests;

/**
 * Created by rbech on 3/13/2016.
 */
public class DeleteSprintRequest extends Request {
    int sprintNumber;

    public DeleteSprintRequest(String tokenId, int sprintNumber) {
        super(tokenId);
        this.sprintNumber = sprintNumber;
    }

    public DeleteSprintRequest(){};

    public int getSprintNumber() {
        return sprintNumber;
    }
}
