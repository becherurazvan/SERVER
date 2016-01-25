package Requests;



public class LoginRequest {
    public String email;
    public String authCode;
    public String idToken;


    public LoginRequest() {

    }

    public String getEmail() {
        return email;
    }

    public String getAuthCode() {
        return authCode;
    }

    public String getIdToken() {
        return idToken;
    }
}
