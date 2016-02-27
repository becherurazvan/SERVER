package Requests;

/**
 * Created by rbech on 2/24/2016.
 */
public class CreateSprintRequest extends Request {
    String startDate;
    String endDate;

    public CreateSprintRequest(String tokenId, String startDate, String endDate) {
        super(tokenId);
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public CreateSprintRequest() {
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }
}
