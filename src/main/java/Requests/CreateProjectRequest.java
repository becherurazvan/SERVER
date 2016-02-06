package Requests;

/**
 * Created by rbech on 1/26/2016.
 */
public class CreateProjectRequest extends Request {
    String projectName;


    public CreateProjectRequest(String tokenId, String projectName) {
        super(tokenId);
        this.projectName = projectName;
    }

    public CreateProjectRequest(){}

    public String getProjectName() {
        return projectName;
    }


}
