package Entities;


import Database.DB;
import GCM.GCMmessenger;
import Google.TokenUtil;
import com.fasterxml.jackson.annotation.JacksonInject;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;

public class User {
    public String email;
    public String name;
    public String pictureUrl;

    @JsonIgnore
    private String userId;
    @JsonIgnore
    private String currentProject;
    @JsonIgnore
    private String accessToken;
    @JsonIgnore
    private String refreshToken;
    @JsonIgnore
    private String gcmToken;


    public User(String email, String name, String pictureUrl, String userId) {
        this.email = email;
        this.name = name;
        this.pictureUrl = pictureUrl;
        this.userId = userId;

    }

    public User() {
    }
    @JsonIgnore
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
    @JsonIgnore
    public boolean setTokens(String accessToken,String refreshToken){

        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        return true;
    }

    @JsonIgnore
    public boolean isPartOfProject(){
        return currentProject!=null;
    }
    @JsonIgnore
    public void joinProject(String projectId){
        this.currentProject= projectId;

    }
    @JsonIgnore
    public String getProjectId(){
        return currentProject;
    }


    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }
    @JsonIgnore
    public String getAccessToken() {
        return accessToken;
    }
    @JsonIgnore
    public String getRefreshToken() {
        return refreshToken;
    }
    @JsonIgnore
    public String getGcmToken() {
        return gcmToken;
    }

    public void sendNotification(String msg,String title,boolean banner){
        GCMmessenger.sendSimpleNotification(msg,title,gcmToken,banner);
    }



    public void setGcmToken(String gcmToken) {
        this.gcmToken = gcmToken;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }
}
