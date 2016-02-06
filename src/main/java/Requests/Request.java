package Requests;

/**
 * Created by rbech on 2/6/2016.
 */
public class Request {
    String tokenId;

    public Request(String tokenId) {
        this.tokenId = tokenId;
    }

    public Request() {
    }

    public String getTokenId() {
        return tokenId;
    }

    public void setTokenId(String tokenId) {
        this.tokenId = tokenId;
    }
}
