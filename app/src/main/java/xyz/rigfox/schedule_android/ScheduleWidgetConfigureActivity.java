package xyz.rigfox.schedule_android;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

public class ScheduleWidgetConfigureActivity extends AppCompatActivity {

    public static final String PREFS_NAME = "xyz.rigfox.schedule_android.ScheduleWidget_";
    public static final String PREF_PREFIX_GROUP = "appwidget_group";
    public static final String PREF_PREFIX_TEACHER = "appwidget_teacher";
    public static final String PREF_PREFIX_DAY = "appwidget_day";

    int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;

    private ScheduleSingleton scheduleSingleton;

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;

    public ScheduleWidgetConfigureActivity() {
        super();
    }

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        setResult(RESULT_CANCELED);

        scheduleSingleton = ScheduleSingleton.getInstance();

        if (!scheduleSingleton.checkDB()) {
            Toast.makeText(this, "Расписание не загружено! Сейчас мы попробуем загрузить его.", Toast.LENGTH_LONG).show();
            scheduleSingleton.checkOrDownload();
            finish();
        }

        setContentView(R.layout.schedule_widget_configure);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

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

    public ListView.OnItemClickListener GroupListClickListener = new ListView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            // i + 1 (id in DB since 1)
            saveGroupPref(ScheduleWidgetConfigureActivity.this, mAppWidgetId, i + 1);

            Intent resultValue = new Intent();
            resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
            setResult(RESULT_OK, resultValue);
            finish();
        }
    };

    public ListView.OnItemClickListener TeacherListClickListener = new ListView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            // i + 1 (id in DB since 1)
            saveTeacherPref(ScheduleWidgetConfigureActivity.this, mAppWidgetId, i + 1);

            Intent resultValue = new Intent();
            resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
            setResult(RESULT_OK, resultValue);
            finish();
        }
    };

    private class SectionsPagerAdapter extends FragmentPagerAdapter {

        SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new GroupListFragment();
                case 1:
                    return new TeacherListFragment();
            }
            return null;
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Группы";
                case 1:
                    return "Преподаватели";
            }
            return null;
        }
    }

    public static class GroupListFragment extends Fragment {
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.list_fragment, container, false);

            ListView listView = rootView.findViewById(R.id.list);
            listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
            listView.setAdapter(new GroupAdapter(getContext(), ScheduleSingleton.getInstance().getGroups()));
            listView.setOnItemClickListener(((ScheduleWidgetConfigureActivity) getActivity()).GroupListClickListener);

            return rootView;
        }
    }

    public static class TeacherListFragment extends Fragment {
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.list_fragment, container, false);

            ListView listView = rootView.findViewById(R.id.list);
            listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
            listView.setAdapter(new TeacherAdapter(getContext(), ScheduleSingleton.getInstance().getTeachers()));
            listView.setOnItemClickListener(((ScheduleWidgetConfigureActivity) getActivity()).TeacherListClickListener);

            return rootView;
        }
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
}

