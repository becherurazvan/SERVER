package Requests;

import java.util.Calendar;

/**
 * Created by rbech on 3/14/2016.
 */
public class GetFeedRequest extends Request {
    int amount;
    Calendar since;
    boolean older;

    public GetFeedRequest(String tokenId, int amount, Calendar since, boolean older) {
        super(tokenId);
        this.amount = amount;
        this.since = since;
        this.older = older;
    }

    public GetFeedRequest() {
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public Calendar getSince() {
        return since;
    }

    public void setSince(Calendar since) {
        this.since = since;
    }

    public boolean isOlder() {
        return older;
    }

    public void setOlder(boolean older) {
        this.older = older;
    }
}
