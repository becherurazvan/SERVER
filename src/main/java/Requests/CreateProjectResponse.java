package Requests;

import Entities.Project;

/**
 * Created by rbech on 1/26/2016.
 */
public class CreateProjectResponse{
    String projectName;
    String projectOwner;
    String projectId;
    String invitationCode;

    public CreateProjectResponse(Project p) {
        this.projectName = p.getTitle();
        this.projectOwner = p.getLeaderEmail();
        this.projectId = p.getId();
        this.invitationCode = p.getInvitationCode();
    }

    public CreateProjectResponse() {}

    public String getProjectName() {
        return projectName;
    }

    public String getProjectOwner() {
        return projectOwner;
    }

    public String getProjectId() {
        return projectId;
    }

    public String getInvitationCode() {
        return invitationCode;
    }

}
