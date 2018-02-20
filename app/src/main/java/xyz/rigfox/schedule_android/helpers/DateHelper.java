package xyz.rigfox.schedule_android.helpers;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DateHelper {
    private static String getDayName(int dayOfWeek) {
        switch (dayOfWeek) {
            case 0:
                return "Понедельник";
            case 1:
                return "Вторник";
            case 2:
                return "Среда";
            case 3:
                return "Четверг";
            case 4:
                return "Пятница";
            case 5:
                return "Суббота";
            default:
                return "Unknown day";
        }
    }

    public static GregorianCalendar getCalendarByTimestamp(Long timestamp) {
        Date date = new Date(timestamp);
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(date);

        return calendar;
    }

    public static String getDayNameByTimestamp(Long timestamp) {
        GregorianCalendar calendar = getCalendarByTimestamp(timestamp);

        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 2;

        return getDayName(dayOfWeek);
    }

    public static int getWeekOfYearByTimestamp(Long timestamp) {
        GregorianCalendar calendar = getCalendarByTimestamp(timestamp);

        int currentWeek = calendar.get(Calendar.WEEK_OF_YEAR);
        int currentYear = calendar.get(Calendar.YEAR);

        if (currentWeek < 35) {
            currentWeek += 17 + 35;
        }

        if (currentYear == 2018) {
            currentWeek++;
        }

        return currentWeek - 35;
    }
}
