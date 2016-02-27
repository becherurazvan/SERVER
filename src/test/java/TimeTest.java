import Scrum.DateManager;

/**
 * Created by rbech on 2/27/2016.
 */
public class TimeTest implements DateManager.DayChangeListener{

    public TimeTest() {


        DateManager dateManager = DateManager.getInstance();

        dateManager.registerListener(this);


        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        dateManager.incrementDay();
    }


    @Override
    public void onDateChanged(int day, int month, int year) {
        System.out.println("Date has changed to " + day + ":" + month + ":" +year);
    }



    public static void main(String[] args) throws InterruptedException {
        new TimeTest();
    }


}
