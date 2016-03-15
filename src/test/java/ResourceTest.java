import Database.Action;
import Database.Feed;
import Database.FeedList;
import Scrum.Resource;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.org.apache.regexp.internal.RE;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by rbech on 3/14/2016.
 */
public class ResourceTest {
    public static void main(String[] args) throws InterruptedException {

        Feed feed = new Feed();

        int initialItems=10;

        for(int i=1;i<initialItems+1;i++){
            feed.registerAction(new Action("member email","member name", " message number "+i));
            Thread.sleep(600);
            System.out.println(i);
        }


        FeedList feedList = new FeedList();



        // print latest 5
        feedList.add(feed.getLatestActions(5));
        System.err.println("Latest 5");
        for(Action a:feedList.getActions()){
            System.out.println(a.getDate().getTime() + " : " + a.getMessage());
        }
        System.out.println();


        feed.registerAction(new Action("member email","member name", " message number "+(initialItems+1)));
        Thread.sleep(1000);
        feed.registerAction(new Action("member email","member name", " message number "+(initialItems+2)));
        Thread.sleep(1000);
        feed.registerAction(new Action("member email","member name", " message number "+(initialItems+3)));


        System.err.println("All since " + feed.getActions().get(5).getDate().getTime());
        feedList.add(feed.getLatestSince(feed.getActions().get(5).getDate()));
        for(Action a:feedList.getActions()){
            System.out.println(a.getDate().getTime() + " : " + a.getMessage());
        }



        Thread.sleep(1000);

        System.err.println("Before " + feed.getActions().get(5).getDate().getTime());
        feedList.getActions().clear();
        feedList.add(feed.getBefore(feed.getActions().get(5).getDate(),3));

        for(Action a:feedList.getActions()){
            System.out.println(a.getDate().getTime() + " : " + a.getMessage());
        }





    }
}
