package xyz.rigfox.schedule_android;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;

import xyz.rigfox.schedule_android.models.Schedule;
import xyz.rigfox.schedule_android.models.ScheduleDao;
import xyz.rigfox.schedule_android.models.Subject;
import xyz.rigfox.schedule_android.models.SubjectDao;

class ScheduleFactory implements RemoteViewsService.RemoteViewsFactory {
    private Context context;
    private int appwidget_id;
    private int group_id;
    private int teacher_id;

    private ScheduleDao scheduleDao;
    private ArrayList<Schedule> schedules = new ArrayList<>();

    ScheduleFactory(Context ctx, Intent intent) {
        context = ctx;
        appwidget_id = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);
    }

    @Override
    public void onCreate() {
        group_id = ScheduleWidgetConfigureActivity.loadGroupPref(context, appwidget_id);
        teacher_id = ScheduleWidgetConfigureActivity.loadTeacherPref(context, appwidget_id);

        scheduleDao = ScheduleSingleton.getInstance().getScheduleDao();
    }

    @Override
    public int getCount() {
        return schedules.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews rView = new RemoteViews(context.getPackageName(),
                R.layout.schedule_item);

        Schedule currentItem;

        try {
            currentItem = schedules.get(position);
        } catch (IndexOutOfBoundsException e) {
            currentItem = new Schedule();
            currentItem.setSubject_id(-1L);
        }

        if (currentItem.getSubject_id() == -1) {
            rView.setTextViewText(R.id.subject, "");
            rView.setTextViewText(R.id.teacher, "");
            rView.setTextViewText(R.id.week, "");
            rView.setTextViewText(R.id.timeText, "");
            if (position == 0) {
                rView.setTextViewText(R.id.subject, "Ура! Первой пары нет");
            }
        } else {
            if (currentItem.getSubject_id_2() == null || teacher_id != -1) {
                Subject subjectItem = currentItem.getSubject();

                if (currentItem.getSubject_id_2() != null && currentItem.getSubject_2().getTeacher_id() == teacher_id) {
                    subjectItem = currentItem.getSubject_2();
                }

                rView.setTextViewText(R.id.timeText, generateTimeText(currentItem));

                rView.setTextViewText(R.id.subject, generateSubjectText(subjectItem));
                rView.setTextViewText(R.id.teacher, generateTeacherText(subjectItem));
                rView.setTextViewText(R.id.week, generateWeekText(subjectItem, currentItem.getStartWeek(), currentItem.getEndWeek()));

            } else {
                rView = new RemoteViews(context.getPackageName(),
                        R.layout.schedule_item_subgrouped);

                Subject subjectItem = currentItem.getSubject();
                Subject subjectItem_2 = currentItem.getSubject_2();

                rView.setTextViewText(R.id.timeText, generateTimeText(currentItem));

                rView.setTextViewText(R.id.subject, generateSubjectText(subjectItem));
                rView.setTextViewText(R.id.teacher, generateTeacherText(subjectItem));
                rView.setTextViewText(R.id.week, generateWeekText(subjectItem, currentItem.getStartWeek(), currentItem.getEndWeek()));

                rView.setTextViewText(R.id.subject_2, generateSubjectText(subjectItem_2));
                rView.setTextViewText(R.id.teacher_2, generateTeacherText(subjectItem_2));
                rView.setTextViewText(R.id.week_2, generateWeekText(subjectItem_2, currentItem.getStartWeek_2(), currentItem.getEndWeek_2()));
            }
        }

        return rView;
    }

    private String generateSubjectText(Subject subject) {
        return subject.getName();
    }

    private String generateTeacherText(Subject subject) {
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

    private String generateWeekText(Subject subject, int startWeek, int endWeek) {
        String startWeekText = String.valueOf(startWeek);
        String endWeekText = String.valueOf(endWeek);

        String week = "(" + startWeekText + "-" + endWeekText + ")\n";

        if (startWeekText == endWeekText) {
            week = "(" + startWeekText + ")\n";
        }

        week += subject.getClassroom();

        return week;
    }

    private String generateTimeText(Schedule schedule) {
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

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public void onDataSetChanged() {
        SharedPreferences sp = context.getSharedPreferences(ScheduleWidgetConfigureActivity.PREFS_NAME + appwidget_id, 0);
        Long timestamp = sp.getLong(ScheduleWidgetConfigureActivity.PREF_PREFIX_DAY, System.currentTimeMillis());

        Date date = new Date(timestamp);
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(date);

        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 2;

        int currentWeek = calendar.get(Calendar.WEEK_OF_YEAR);

        if (currentWeek < 35) {
            currentWeek += 17 + 35;
        }

        int numWeek = currentWeek - 35;

        QueryBuilder<Schedule> qb = scheduleDao.queryBuilder();
        QueryBuilder<Schedule> qb_2 = scheduleDao.queryBuilder();

        if (teacher_id != -1) {
            qb.join(ScheduleDao.Properties.Subject_id, Subject.class)
                    .where(SubjectDao.Properties.Teacher_id.eq(teacher_id));

            qb_2.join(ScheduleDao.Properties.Subject_id_2, Subject.class)
                    .where(SubjectDao.Properties.Teacher_id.eq(teacher_id));
        } else {
            qb.where(
                    qb.and(ScheduleDao.Properties.Group_id.eq(group_id),
                            ScheduleDao.Properties.Day.eq(dayOfWeek),
                            ScheduleDao.Properties.StartWeek.le(numWeek),
                            ScheduleDao.Properties.EndWeek.ge(numWeek)))
                    .orderAsc(ScheduleDao.Properties.Num);
        }

        ArrayList<Schedule> prepSchedules = (ArrayList<Schedule>) qb.list();

        if (teacher_id != -1) {
            prepSchedules.addAll(qb_2.list());

            ArrayList<Schedule> temp = new ArrayList<>();

            for (Schedule schedule : prepSchedules) {
                if (schedule.getDay() == dayOfWeek && schedule.getStartWeek() <= numWeek && schedule.getEndWeek() >= numWeek) {
                    temp.add(schedule);
                }
            }

            Collections.sort(temp, new Comparator<Schedule>() {
                @Override
                public int compare(Schedule schedule1, Schedule schedule2) {
                    return (schedule2.getNum() < schedule1.getNum() ? 1 :
                            (schedule2.getNum() == schedule1.getNum() ? 0 : -1));
                }
            });

            prepSchedules = temp;
        }

        schedules.clear();

        Schedule emptyItem = new Schedule();
        emptyItem.setSubject_id(-1L);

        int countSubject = prepSchedules.size();

        int numLastSubject = 0;
        if (countSubject != 0) {
            numLastSubject = prepSchedules.get(prepSchedules.size() - 1).getNum();
        }

        for (int i = 0; i < numLastSubject; i++) {
            schedules.add(emptyItem);
        }

        for (Schedule schedule : prepSchedules) {
            schedules.set(schedule.getNum() - 1, schedule);
        }
    }

    @Override
    public void onDestroy() {

    }

}
