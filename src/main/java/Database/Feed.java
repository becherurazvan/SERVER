package Database;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by rbech on 3/14/2016.
 */
public class Feed {
    ArrayList<Action> actions = new ArrayList<>();

    @JsonIgnore
    AtomicInteger counter = new AtomicInteger(0);

    public void registerAction(Action a) {
        a.setId(counter.addAndGet(1));
        actions.add(a);
    }

    public Feed() {
    }

    ;

    public ArrayList<Action> getActions() {
        return actions;
    }


    public ArrayList<Action> getLatestActions(int count) {

        Collections.sort(actions);
        ArrayList<Action> ret = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            if(actions.size()<=i)
                return ret;
            ret.add(actions.get(i));
        }
        Collections.sort(actions);


        return ret;

    }

    public ArrayList<Action> getLatestSince(Calendar since) {
        ArrayList<Action> ret = new ArrayList<>();
        Collections.sort(actions);

        for (Action a : actions) {
            if (isNewer(a.getDate(), since))
                ret.add(a);
            else
                return ret;
        }
        return ret;
    }

    public ArrayList<Action> getBefore(Calendar before, int amount) {
        ArrayList<Action> ret = new ArrayList<>();
        Collections.sort(actions);

        int index=0;
        for (Action a : actions) {
            if (isOlder(a.getDate(), before)) {
                ret.add(a);
                index++;
            }
            if(index>=amount){
                Collections.sort(ret);
                return ret;
            }

        }
        Collections.sort(ret);

        return ret;




    }


    public boolean isNewer(Calendar item, Calendar since) {
        return item.compareTo(since) == 1;
    }

    public boolean isOlder(Calendar item, Calendar before){
        return item.compareTo(before) == -1;
    }

//
//    // for client
//    public void add(ArrayList<Action> actions){
//        for(Action a:actions){
//            if(!idExists(a.getId())){
//                this.actions.add(a);
//            }
//        }
//    }
//    public void addSingle(Action action){
//            if(!idExists(action.getId())){
//                this.actions.add(action);
//            }
//    }
//
//
//    public boolean idExists(int id){
//        for(Action a:this.actions){
//            if(a.getId()==id)
//                return true;
//
//        }
//        return false;
//    }
//


}
