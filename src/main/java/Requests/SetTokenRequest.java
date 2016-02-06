package Requests;

/**
 * Created by rbech on 2/6/2016.
 */
public class SetTokenRequest extends Request{
    String authCode;

    public SetTokenRequest(String tokenId, String authCode) {
        super(tokenId);
        this.authCode = authCode;
    }

    public SetTokenRequest(){

    }
    public void setAuthCode(String authCode) {
        this.authCode = authCode;
    }

    public String getAuthCode() {
        return authCode;
    }
}
