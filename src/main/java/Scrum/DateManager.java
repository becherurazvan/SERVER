package Scrum;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by rbech on 2/27/2016.
 */
public class DateManager implements Runnable {

    int currentDay;
    int currentMonth;
    int currentYear;

    ArrayList<DayChangeListener> listeners;


    Calendar cal;

    boolean automatic = false;

    private static DateManager instance;

    static public DateManager getInstance(){
        if(instance==null){
            instance = new DateManager();
        }

        return instance;
    }

    public DateManager() {
        this.currentDay = getDay();
        this.currentMonth = getMonth();
        this.currentYear = getYear();
        listeners = new ArrayList<>();


        if(automatic) {
            Thread t = new Thread(this);
            t.start();
        }else{
            cal = Calendar.getInstance();
            cal.setTime(new Date());
        }
    }



    @Override
    public void run() {
        while(true){
            try {
                Thread.sleep(3600000); // sleep hour
                int day = getDay();
                int month = getMonth();
                int year = getYear();

                if(day!=currentDay||month!=currentMonth||year!=currentYear){
                    currentDay=day;
                    currentYear=year;
                    currentMonth=month;

                    for(DayChangeListener listener:listeners){
                        listener.onDateChanged(currentDay,currentMonth,currentYear);
                    }
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    public void registerListener(DayChangeListener listener){
        listeners.add(listener);
    }


    public interface DayChangeListener{
        public void onDateChanged(int day,int month,int year);
    }


    public void incrementDay(){
        if(automatic)
            return;
        cal.add(Calendar.DATE,1);
        for(DayChangeListener listener:listeners){
            listener.onDateChanged(cal.get(Calendar.DAY_OF_MONTH),cal.get(Calendar.MONTH),cal.get(Calendar.YEAR));
        }
    }



    public int getDay(){
        Date date = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.DAY_OF_MONTH);
    }


    public int getMonth(){
        Date date = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.MONTH);
    }

    public int getYear(){
        Date date = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.YEAR);
    }

    public String getDate(){
        return cal.get(Calendar.DAY_OF_MONTH)+"."+cal.get(Calendar.MONTH)+"."+cal.get(Calendar.YEAR);
    }



}
