package Requests;

/**
 * Created by rbech on 1/26/2016.
 */
public class CreateProjectRequest {
    public String projectName;
    public String email;


    public CreateProjectRequest(String projectName, String email) {
        this.projectName = projectName;
        this.email = email;
    }

    public CreateProjectRequest() {
    }

    public String getProjectName() {
        return projectName;
    }

    public String getEmail() {
        return email;
    }
}
