package Entities;


import Database.DB;
import Google.TokenUtil;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;

public class User {
    private String email;
    private String name;
    private String pictureUrl;
    private String userId;
    private String currentProject;
    private String accessToken;
    private String refreshToken;
    private String gcmToken;

    public User(String email, String name, String pictureUrl, String userId) {
        this.email = email;
        this.name = name;
        this.pictureUrl = pictureUrl;
        this.userId = userId;
    }

    public boolean setTokens(String authToken){
        GoogleTokenResponse response = TokenUtil.getTokenResponse(authToken);
        if(response==null){
            return false;
        }
        accessToken = response.getAccessToken();
        refreshToken = response.getRefreshToken();
        System.out.println("Succesfully set the tokens for user : " + name + " \n" + accessToken + " : " + refreshToken);
        return true;
    }


    public boolean isPartOfProject(){
        return currentProject!=null;
    }

    public void joinProject(String projectId){
        this.currentProject= projectId;

    }

    public String getProjectId(){
        return currentProject;
    }


    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public String getUserId() {
        return userId;
    }

    public String getCurrentProject() {
        return currentProject;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public String getGcmToken() {
        return gcmToken;
    }

    public void setGcmToken(String gcmToken) {
        this.gcmToken = gcmToken;
    }
}
