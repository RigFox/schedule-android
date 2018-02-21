package xyz.rigfox.schedule_android;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import xyz.rigfox.schedule_android.fragments.SelectGroupOrTeacherFragment;
import xyz.rigfox.schedule_android.models.Group;
import xyz.rigfox.schedule_android.models.Teacher;

public class ScheduleWidgetConfigureActivity extends AppCompatActivity {

    public static final String PREFS_NAME = "xyz.rigfox.schedule_android.ScheduleWidget_";
    public static final String PREF_PREFIX_GROUP = "appwidget_group";
    public static final String PREF_PREFIX_TEACHER = "appwidget_teacher";
    public static final String PREF_PREFIX_DAY = "appwidget_day";

    int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        setResult(RESULT_CANCELED);

        ScheduleSingleton scheduleSingleton = ScheduleSingleton.getInstance();

        if (!scheduleSingleton.checkDB()) {
            Toast.makeText(this, "Расписание не загружено! Сейчас мы попробуем загрузить его.", Toast.LENGTH_LONG).show();
            scheduleSingleton.checkOrDownload();
            finish();
        }

        setContentView(R.layout.content_main);

        SelectGroupOrTeacherFragment fragment = new SelectGroupOrTeacherFragment();
        fragment.setClickListeners(groupListClickListener, teacherListClickListener);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.container, fragment).commit();

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            mAppWidgetId = extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
        }
    }

    private ListView.OnItemClickListener groupListClickListener = new ListView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            Group group = (Group) adapterView.getAdapter().getItem(i);

            saveGroupPref(ScheduleWidgetConfigureActivity.this, mAppWidgetId, Integer.valueOf(Long.toString(group.getId())));

            Intent resultValue = new Intent();
            resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
            setResult(RESULT_OK, resultValue);
            finish();
        }
    };

    private ListView.OnItemClickListener teacherListClickListener = new ListView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            Teacher teacher = (Teacher) adapterView.getAdapter().getItem(i);

            saveTeacherPref(ScheduleWidgetConfigureActivity.this, mAppWidgetId, Integer.valueOf(Long.toString(teacher.getId())));

            Intent resultValue = new Intent();
            resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
            setResult(RESULT_OK, resultValue);
            finish();
        }
    };

    static void saveGroupPref(Context context, int appWidgetId, int group_id) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME + appWidgetId, 0).edit();
        prefs.putInt(PREF_PREFIX_GROUP, group_id);
        prefs.apply();
    }

    static Integer loadGroupPref(Context context, int appWidgetId) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME + appWidgetId, 0);
        int titleValue = prefs.getInt(PREF_PREFIX_GROUP, -1);
        if (titleValue != -1) {
            return titleValue;
        } else {
            return 1;
        }
    }

    static void saveTeacherPref(Context context, int appWidgetId, int teacher_id) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME + appWidgetId, 0).edit();
        prefs.putInt(PREF_PREFIX_TEACHER, teacher_id);
        prefs.apply();
    }

    static Integer loadTeacherPref(Context context, int appWidgetId) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME + appWidgetId, 0);
        return prefs.getInt(PREF_PREFIX_TEACHER, -1);
    }
}

