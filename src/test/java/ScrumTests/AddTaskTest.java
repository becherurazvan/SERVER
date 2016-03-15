package ScrumTests;

import Entities.Project;
import Entities.User;
import Requests.AddTaskRequest;
import Scrum.ProductBacklog;
import Scrum.Task;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.json.jackson2.JacksonFactory;

import java.io.IOException;

/**
 * Created by rbech on 3/5/2016.
 */
public class AddTaskTest {

    public static void main(String[] args) throws IOException {

        ObjectMapper mapper= new ObjectMapper();
        String json = "{\"tokenId\":\"eyJhbGciOiJSUzI1NiIsImtpZCI6ImQyYjE4MGRmMWNkMmVlYzExMzhiNTNlM2E3ODBiNTNlMmNhYTlhZmQifQ.eyJpc3MiOiJodHRwczovL2FjY291bnRzLmdvb2dsZS5jb20iLCJhdWQiOiI3MzUwNjgwMDM1NDMtbmw3b2o0dm85OHM1bm5hYnY1cTEzcGdvNjg4aGtqM2wuYXBwcy5nb29nbGV1c2VyY29udGVudC5jb20iLCJzdWIiOiIxMDg1MjI1NDQyMTc4NjI4NTg5NzAiLCJlbWFpbF92ZXJpZmllZCI6dHJ1ZSwiYXpwIjoiNzM1MDY4MDAzNTQzLWV2Z3FtMHRtODBiOXZpc2FwcGxrdXJidm81b2IwMzJlLmFwcHMuZ29vZ2xldXNlcmNvbnRlbnQuY29tIiwiZW1haWwiOiJjb2xsaW5lYXJwcm9kdWN0aW9uc0BnbWFpbC5jb20iLCJpYXQiOjE0NTcxOTg5NTQsImV4cCI6MTQ1NzIwMjU1NCwibmFtZSI6IkRlc2VuZSBEdWJsYXRlIiwicGljdHVyZSI6Imh0dHBzOi8vbGg1Lmdvb2dsZXVzZXJjb250ZW50LmNvbS8tYS1MZ1ZxdkYwak0vQUFBQUFBQUFBQUkvQUFBQUFBQUFBQmcvV3RFdHNzUUFLNEkvczk2LWMvcGhvdG8uanBnIiwiZ2l2ZW5fbmFtZSI6IkRlc2VuZSIsImZhbWlseV9uYW1lIjoiRHVibGF0ZSIsImxvY2FsZSI6ImVuIn0.TddIVKokf4UMyjqCW1wCadLfudF4OMsjLyJ3tG-pxAqTP16_aTJ-NVvtfsyklcRaujYJD952TEY3Hz9bODgDEm2ODMWSCwef-pce8cRmgZdSN5_sLHCSPdiXc0rtpgU30Bye96CfCJ74VJRfQG8uT1WS2UeyzMKCbSkwv9DmT6geCOse_sbBe6LmcJ_KmYaf4d0eZjJQgSh9-7FOVAxkAlAVJClQa9VfShqVpYQnlHE5UWkl9SygXaTy-zmtu8yPtkxsbEQS1g4PrcWITCO87P1cDh8a6MYCVtmpspzOnTFvOhEEz2Ne9HSuPZGTKFbkp_PWr3ZvcDn9mYLoKax3TA\",\"task\":{\"id\":null,\"points\":0,\"state\":\"NOT_STARTED\",\"title\":\"qwe\",\"userStoryId\":\"US_1\"}}";

        AddTaskRequest addTaskRequest = mapper.readValue(json,AddTaskRequest.class);
        GoogleIdToken idToken = GoogleIdToken.parse(JacksonFactory.getDefaultInstance(),addTaskRequest.getTokenId());
        String email = idToken.getPayload().getEmail();


        Project p = new Project("","");

        Task t = addTaskRequest.getTask();

        System.out.println(t.getId() + " : " + t.getUserStoryId());
        p.getProductBacklog().createUserStory("");


        p.getProductBacklog().printStories();

        Task task = p.getProductBacklog().getStoryById(t.getId()).addTask(t);

        System.out.println("User " + email + " has added a new task:\n" + task.getId());




    }

}
