package Scrum;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by rbech on 2/27/2016.
 */
public class DateManager implements Runnable {


    ArrayList<DayChangeListener> listeners;
    Calendar calendar;

    boolean automatic = false;

    private static DateManager instance;

    static public DateManager getInstance() {
        if (instance == null) {
            instance = new DateManager();
        }

        return instance;
    }

    public DateManager() {

        listeners = new ArrayList<>();

        calendar = Calendar.getInstance();
        calendar.setTime(new Date());

        if (automatic) {
            Thread t = new Thread(this);
            t.start();
        }
    }


    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(3600000); // sleep hour

                Calendar current = Calendar.getInstance();
                current.setTime(new Date());

                if (!sameDay(current,calendar)) {
                    calendar=current;

                    for (DayChangeListener listener : listeners) {
                        listener.onDateChanged(calendar);
                    }
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    public void registerListener(DayChangeListener listener) {
        listeners.add(listener);
    }


    public interface DayChangeListener {
        public void onDateChanged(Calendar calendar);
    }


    public void incrementDay() {
        System.err.println("TEST DATE " + calendar.get(Calendar.DAY_OF_YEAR));
        if (automatic)
            return;
        calendar.add(Calendar.DATE, 1);

        System.out.println();
        System.out.println("---Incremented, new date:" + getDate());

        for (DayChangeListener listener : listeners) {
            listener.onDateChanged(calendar);
        }


    }


    public String getDate() {
        return calendar.get(Calendar.DAY_OF_MONTH) + "." + (calendar.get(Calendar.MONTH)+1) + "." + calendar.get(Calendar.YEAR);
    }

    public Date getDateDate(){
        return  calendar.getTime();
    }

    public Calendar getCalendar(){
        return calendar;
    }


    public static boolean sameDay(Calendar cal1, Calendar cal2){
        boolean sameDay = cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR);
        return sameDay;
    }


}
