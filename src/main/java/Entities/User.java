package Entities;


import Google.TokenUtil;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;

public class User {
    private String email;
    private String name;
    private String id;

    private int currentProject;
    private boolean partOfProject;

    private String accessToken;
    private String refreshToken;

    public User(String email) {
        this.email = email;
        partOfProject=false;
    }

    public void setTokens(String authCode) {
        GoogleTokenResponse response = TokenUtil.getTokenResponse(authCode);
        accessToken = response.getAccessToken();
        refreshToken = response.getRefreshToken();
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public boolean isPartOfProject(){
        return partOfProject;
    }

    public String getEmail() {
        return email;
    }
}
