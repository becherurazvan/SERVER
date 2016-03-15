package Scrum;

import Database.Action;
import Database.DB;
import Database.Feed;
import Database.FeedList;
import Entities.Project;
import Entities.User;
import Requests.AddStoryRequest;
import Requests.GetFeedRequest;
import Requests.Request;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.json.jackson2.JacksonFactory;

import java.util.ArrayList;

import static spark.Spark.get;
import static spark.Spark.post;

/**
 * Created by rbech on 3/14/2016.
 */
public class FeedManager {


    ObjectMapper mapper;
    DB db;

    public FeedManager(){

        db = DB.getInstance();
        mapper = new ObjectMapper();

        post("/full_feed", (request, response) -> {
            Request getFeedRequest = mapper.readValue(request.body(), Request.class);
            GoogleIdToken idToken = GoogleIdToken.parse(JacksonFactory.getDefaultInstance(), getFeedRequest.getTokenId());
            String email = idToken.getPayload().getEmail();
            User u = db.getUser(email);
            Project p = db.getProject(u.getProjectId());
            Feed feed = db.getFeed(p.getId());
            return mapper.writeValueAsString(feed);
        });



        post("/feed", (request, response) -> {
            GetFeedRequest getFeedRequest = mapper.readValue(request.body(), GetFeedRequest.class);
            GoogleIdToken idToken = GoogleIdToken.parse(JacksonFactory.getDefaultInstance(), getFeedRequest.getTokenId());
            String email = idToken.getPayload().getEmail();
            User u = db.getUser(email);
            Project p = db.getProject(u.getProjectId());

            ArrayList<Action> feedItems = new ArrayList<Action>();
            Feed feed = db.getFeed(p.getId());
            if(getFeedRequest.isOlder()){
                feedItems.addAll(feed.getBefore(getFeedRequest.getSince(),getFeedRequest.getAmount()));
            }else{
                if(getFeedRequest.getSince()!=null){
                    feedItems.addAll(feed.getLatestSince(getFeedRequest.getSince()));
                }else{
                    feedItems.addAll(feed.getLatestActions(getFeedRequest.getAmount()));
                }
            }




            return mapper.writeValueAsString(feedItems);
        });




    }
}
