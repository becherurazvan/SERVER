package Requests;

/**
 * Created by rbech on 1/30/2016.
 */
public class JoinProjectRequest extends Request{
    String invitationCode;



    public JoinProjectRequest(String idToken, String invitationCode) {
        super(idToken);
        this.invitationCode = invitationCode;
    }

    public JoinProjectRequest(){}


    public String getInvitationCode() {
        return invitationCode;
    }
}
