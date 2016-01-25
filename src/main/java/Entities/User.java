package Entities;


public class User {
    private String email;
    private String authCode;
    private String idToken;
    private String name;
    private String teamName;

    public User(String email, String latestAuthenticationCode, String idToken) {
        this.email = email;
        this.authCode = latestAuthenticationCode;
        this.idToken = idToken;
    }

    public void setAuthCode(String authCode) {
        this.authCode = authCode;
    }

    public void setIdToken(String idToken) {
        this.idToken = idToken;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
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

    public String getName() {
        return name;
    }

    public String getTeamName() {
        return teamName;
    }
}
