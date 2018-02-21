package xyz.rigfox.schedule_android.helpers;

import xyz.rigfox.schedule_android.models.Schedule;
import xyz.rigfox.schedule_android.models.Subject;

public class ScheduleHelper {
    public static String generateSubjectText(Subject subject) {
        return subject.getName();
    }

    public static String generateTeacherText(Subject subject, int teacher_id) {
        String teacher;
        if (teacher_id != -1) {
            teacher = subject.getGroup().getName();
        } else {
            teacher = subject.getTeacher().getName();

            if (teacher.equals("????")) {
                teacher = "";
            }
        }

        return teacher;
    }

    public static String generateWeekText(Subject subject, int startWeek, int endWeek) {
        String startWeekText = String.valueOf(startWeek);
        String endWeekText = String.valueOf(endWeek);

        String week = "(" + startWeekText + "-" + endWeekText + ")\n";

        if (startWeekText == endWeekText) {
            week = "(" + startWeekText + ")\n";
        }

        week += subject.getClassroom();

        return week;
    }

    public static String generateTimeText(Schedule schedule) {
        String timeText = "";
        switch (schedule.getNum()) {
            case 1:
                timeText = "8:00-\n9:35";
                break;
            case 2:
                timeText = "9:45-\n11:20";
                break;
            case 3:
                timeText = "11:30-\n13:05";
                break;
            case 4:
                timeText = "13:30-\n15:05";
                break;
            case 5:
                timeText = "15:15-\n16:50";
                break;
            case 6:
                timeText = "17:00-\n18:35";
                break;
        }

        return timeText;
    }
}
