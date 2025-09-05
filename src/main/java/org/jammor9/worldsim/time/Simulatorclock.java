package org.jammor9.worldsim.time;
import java.util.ArrayList;
import java.util.HashMap;

public class Simulatorclock {
    private static Simulatorclock singleton = null;

    private static final int MONTHS_IN_YEAR = 12;
    private static final int DAYS_IN_MONTH = 30;
    private static final String CALENDAR_SUFFIX = " AC";

    private int currentYear;
    private int currentMonth;
    private int currentDay;

    private HashMap<TimeDuration, ArrayList<SimListener>> listeners = new HashMap<>() {};

    private Simulatorclock(int currentDay, int currentMonth, int currentYear) {
        this.currentDay = currentDay;
        this.currentMonth = currentMonth;
        this.currentYear = currentYear;

        listeners.put(TimeDuration.DAY, new ArrayList<>());
        listeners.put(TimeDuration.MONTH, new ArrayList<>());
        listeners.put(TimeDuration.YEAR, new ArrayList<>());
    }

    public static Simulatorclock getInstance() {
        if (singleton == null) singleton = new Simulatorclock(0, 0, 0);
        return singleton;
    }

    public void passDay() {
        currentDay++;
        update(TimeDuration.DAY);
        if (currentDay % DAYS_IN_MONTH == 0) newMonth();
    }

    private void newMonth() {
        currentMonth++;
        update(TimeDuration.MONTH);
        if (currentMonth % MONTHS_IN_YEAR == 0) newYear();
    }

    private void newYear() {
        currentYear++;
        update(TimeDuration.YEAR);
    }

    public int getDay() {
        return this.currentDay;
    }

    public void addListener(TimeDuration type, SimListener listener) {
        listeners.get(type).add(listener);
    }

    public void removeListener(TimeDuration type, SimListener listener) {
        listeners.get(type).remove(listener);
    }

    public void update(TimeDuration type) {
        ArrayList<SimListener> toUpdate = listeners.get(type);
        for (SimListener l : toUpdate) {
            l.timePassed(type);
        }
    }

    @Override
    public String toString() {
        return ((currentDay % DAYS_IN_MONTH) + 1) + "-" + ((currentMonth % MONTHS_IN_YEAR) + 1) + "-" + (currentYear + 1) + CALENDAR_SUFFIX;
    }
}
