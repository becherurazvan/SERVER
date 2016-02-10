package Requests;

/**
 * Created by rbech on 2/6/2016.
 */
public class SetTokenRequest extends Request{
    String authCode;
    String gcmToken;

    public SetTokenRequest(String tokenId, String authCode,String gcmToken) {
        super(tokenId);
        this.authCode = authCode;
        this.gcmToken = gcmToken;
    }

    public SetTokenRequest(){

    }
    public void setAuthCode(String authCode) {
        this.authCode = authCode;
    }

    public String getAuthCode() {
        return authCode;
    }

    public String getGcmToken() {
        return gcmToken;
    }
}
