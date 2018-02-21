package xyz.rigfox.schedule_android;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.util.ArrayList;

import xyz.rigfox.schedule_android.models.Schedule;
import xyz.rigfox.schedule_android.models.Subject;

import static xyz.rigfox.schedule_android.helpers.ScheduleHelper.generateSubjectText;
import static xyz.rigfox.schedule_android.helpers.ScheduleHelper.generateTeacherText;
import static xyz.rigfox.schedule_android.helpers.ScheduleHelper.generateTimeText;
import static xyz.rigfox.schedule_android.helpers.ScheduleHelper.generateWeekText;

class ScheduleFactory implements RemoteViewsService.RemoteViewsFactory {
    private Context context;
    private int appwidget_id;
    private int teacher_id;

    private ScheduleController scheduleController;

    private ArrayList<Schedule> schedules = new ArrayList<>();

    ScheduleFactory(Context ctx, Intent intent) {
        context = ctx;
        appwidget_id = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);
    }

    @Override
    public void onCreate() {
        int group_id = ScheduleWidgetConfigureActivity.loadGroupPref(context, appwidget_id);
        teacher_id = ScheduleWidgetConfigureActivity.loadTeacherPref(context, appwidget_id);

        scheduleController = new ScheduleController(System.currentTimeMillis(), group_id, teacher_id);
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
                rView.setTextViewText(R.id.teacher, generateTeacherText(subjectItem, teacher_id));
                rView.setTextViewText(R.id.week, generateWeekText(subjectItem, currentItem.getStartWeek(), currentItem.getEndWeek()));

            } else {
                rView = new RemoteViews(context.getPackageName(),
                        R.layout.schedule_item_subgrouped);

                Subject subjectItem = currentItem.getSubject();
                Subject subjectItem_2 = currentItem.getSubject_2();

                rView.setTextViewText(R.id.timeText, generateTimeText(currentItem));

                rView.setTextViewText(R.id.subject, generateSubjectText(subjectItem));
                rView.setTextViewText(R.id.teacher, generateTeacherText(subjectItem, teacher_id));
                rView.setTextViewText(R.id.week, generateWeekText(subjectItem, currentItem.getStartWeek(), currentItem.getEndWeek()));

                rView.setTextViewText(R.id.subject_2, generateSubjectText(subjectItem_2));
                rView.setTextViewText(R.id.teacher_2, generateTeacherText(subjectItem_2, teacher_id));
                rView.setTextViewText(R.id.week_2, generateWeekText(subjectItem_2, currentItem.getStartWeek_2(), currentItem.getEndWeek_2()));
            }
        }

        return rView;
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

        scheduleController.setTimestamp(timestamp);
        schedules = scheduleController.getSchedule();
    }

    @Override
    public void onDestroy() {

    }

}
