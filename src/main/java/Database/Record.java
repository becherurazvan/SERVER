package Database;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by rbech on 3/12/2016.
 */
public class Record {
    ArrayList<Entry> burndownChart;


    public Record(){
        burndownChart = new ArrayList<>();
    }

    public ArrayList<Entry> getBurndownChart() {
        return burndownChart;
    }

    public void addEntry(Entry e){
        burndownChart.add(e);
    }

    public ArrayList<Entry> getSprintBurndownByNumber(int sprintNumber){
        ArrayList<Entry> ret = new ArrayList<>();

        for(Entry e:burndownChart){
            if(e.sprintNumber==sprintNumber){
                ret.add(e);
            }
        }
        return ret;
    }


}
