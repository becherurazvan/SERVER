package Requests;


public class LoginResponse extends Response {
    boolean partOfTeam;
    String projectId;


    public LoginResponse(String greeting, boolean loginSuccesful, boolean partOfTeam,String projectId) {
        super(loginSuccesful,greeting);
        this.partOfTeam = partOfTeam;
        this.projectId = projectId;
    }

    public LoginResponse(){}

    public boolean isPartOfTeam() {
        return partOfTeam;
    }

    public String getProjectId() {
        return projectId;
    }
}
