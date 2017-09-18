package xyz.rigfox.schedule_android;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import xyz.rigfox.schedule_android.models.Group;
import xyz.rigfox.schedule_android.models.Teacher;

public class ScheduleWidgetConfigureActivity extends Activity {

    public static final String PREFS_NAME = "xyz.rigfox.schedule_android.ScheduleWidget_";
    public static final String PREF_PREFIX_GROUP = "appwidget_group";
    public static final String PREF_PREFIX_TEACHER = "appwidget_teacher";
    public static final String PREF_PREFIX_DAY = "appwidget_day";

    int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;

    ListView.OnItemClickListener GroupListClickListener = new ListView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            final Context context = ScheduleWidgetConfigureActivity.this;

            // i + 1 (id in DB since 1)
            saveGroupPref(context, mAppWidgetId, i + 1);

            Intent resultValue = new Intent();
            resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
            setResult(RESULT_OK, resultValue);
            finish();
        }
    };

    ListView.OnItemClickListener TeacherListClickListener = new ListView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            final Context context = ScheduleWidgetConfigureActivity.this;

            // i + 1 (id in DB since 1)
            saveTeacherPref(context, mAppWidgetId, i + 1);

            Intent resultValue = new Intent();
            resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
            setResult(RESULT_OK, resultValue);
            finish();
        }
    };

    ServiceConnection sConn;
    ScheduleService scheduleService;

    public ScheduleWidgetConfigureActivity() {
        super();
    }

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

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        setResult(RESULT_CANCELED);

        setContentView(R.layout.schedule_widget_configure);
        ((ListView) findViewById(R.id.group_list)).setOnItemClickListener(GroupListClickListener);
        ((ListView) findViewById(R.id.teacher_list)).setOnItemClickListener(TeacherListClickListener);

        final Intent conIntent = new Intent(this, ScheduleService.class);
        sConn = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                scheduleService = ((ScheduleService.ScheduleBinder) iBinder).getService();

                if (!scheduleService.checkDB()) {
                    Toast.makeText(scheduleService, "Расписание не загружено! Запустите приложение, чтобы загрузить расписание.", Toast.LENGTH_LONG).show();
                    finish();
                }

                connectList();
            }

            @Override
            public void onServiceDisconnected(ComponentName componentName) {
            }
        };

        bindService(conIntent, sConn, 0);

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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(sConn);
    }

    void connectList() {
        List<Group> groups = scheduleService.getGroups();
        GroupAdapter groupAdapter = new GroupAdapter(this, groups);

        ListView lvGroup = findViewById(R.id.group_list);
        lvGroup.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        lvGroup.setAdapter(groupAdapter);

        List<Teacher> teachers = scheduleService.getTeachers();
        TeacherAdapter teacherAdapter = new TeacherAdapter(this, teachers);

        ListView lvTeacher = findViewById(R.id.teacher_list);
        lvTeacher.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        lvTeacher.setAdapter(teacherAdapter);
    }
}

