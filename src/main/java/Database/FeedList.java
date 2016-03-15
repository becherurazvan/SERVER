package Database;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by rbech on 3/14/2016.
 */
public class FeedList {


    ArrayList<Action> actions = new ArrayList<>();


    public FeedList(){}

    public void add(ArrayList<Action> actions) {
        for (Action a : actions) {
            if (!idExists(a.getId())) {
                this.actions.add(a);
            }
        }
        Collections.sort(this.actions);
    }

    public void addSingle(Action action) {
        if (!idExists(action.getId())) {
            this.actions.add(action);
        }
        Collections.sort(actions);
    }


    public boolean idExists(int id) {
        for (Action a : this.actions) {
            if (a.getId() == id)
                return true;

        }
        return false;
    }

    public ArrayList<Action> getActions() {
        Collections.sort(actions);
        return actions;
    }
}
