package xyz.rigfox.schedule_android;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.github.pwittchen.swipe.library.rx2.SimpleSwipeListener;
import com.github.pwittchen.swipe.library.rx2.Swipe;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import xyz.rigfox.schedule_android.helpers.DateHelper;
import xyz.rigfox.schedule_android.models.Group;
import xyz.rigfox.schedule_android.models.GroupDao;
import xyz.rigfox.schedule_android.models.Schedule;
import xyz.rigfox.schedule_android.models.Subject;
import xyz.rigfox.schedule_android.models.Teacher;
import xyz.rigfox.schedule_android.models.TeacherDao;

import static xyz.rigfox.schedule_android.helpers.ScheduleHelper.generateSubjectText;
import static xyz.rigfox.schedule_android.helpers.ScheduleHelper.generateTeacherText;
import static xyz.rigfox.schedule_android.helpers.ScheduleHelper.generateTimeText;
import static xyz.rigfox.schedule_android.helpers.ScheduleHelper.generateWeekText;

public class ScheduleActivity extends AppCompatActivity implements View.OnClickListener {

    final static Long MILLISECOND_OF_DAY = (long) 86400000;

    public static final String GROUP_ID = "GROUP_ID";
    public static final String TEACHER_ID = "TEACHER_ID";

    private Swipe swipe;

    private int group_id;
    private int teacher_id;
    private Long timestamp = System.currentTimeMillis();

    private TextView date;
    private TextView dayOfWeek;
    private TextView numWeek;
    private ListView listView;
    private ScheduleController scheduleController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        swipe = new Swipe();
        swipe.setListener(new SimpleSwipeListener() {
            @Override
            public boolean onSwipedLeft(MotionEvent event) {
                Date date = new Date(timestamp);
                GregorianCalendar calendar = new GregorianCalendar();
                calendar.setTime(date);

                Long addMS;

                addMS = MILLISECOND_OF_DAY;

                if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
                    addMS *= 2;
                }

                timestamp += addMS;

                updateTV();
                updateSchedule();

                return true;
            }

            @Override
            public boolean onSwipedRight(MotionEvent event) {
                Date date = new Date(timestamp);
                GregorianCalendar calendar = new GregorianCalendar();
                calendar.setTime(date);

                Long addMS;

                addMS = -MILLISECOND_OF_DAY;

                if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY) {
                    addMS *= 2;
                }

                timestamp += addMS;

                updateTV();
                updateSchedule();

                return true;
            }
        });

        date = findViewById(R.id.date);
        dayOfWeek = findViewById(R.id.dayOfWeek);
        numWeek = findViewById(R.id.numWeek);

        findViewById(R.id.backButton).setOnClickListener(this);
        findViewById(R.id.todayButton).setOnClickListener(this);
        findViewById(R.id.nextButton).setOnClickListener(this);

        listView = findViewById(R.id.listView);

        Intent intent = getIntent();

        if (intent == null) {
            finish();
            return;
        }

        int group_id = intent.getIntExtra(GROUP_ID, 1);
        int teacher_id = intent.getIntExtra(TEACHER_ID, -1);

        String groupName;

        if (teacher_id != -1) {
            QueryBuilder<Teacher> qb = ScheduleSingleton.getInstance().getTeacherDao().queryBuilder();

            Teacher teacher = qb.where(TeacherDao.Properties.Id.eq(teacher_id)).unique();
            groupName = teacher.getName();
        } else {
            QueryBuilder<Group> qb = ScheduleSingleton.getInstance().getGroupDao().queryBuilder();

            Group group = qb.where(GroupDao.Properties.Id.eq(group_id)).unique();
            groupName = group.getName();
        }

        scheduleController = new ScheduleController(timestamp, group_id, teacher_id);
        listView.setAdapter(new ScheduleAdapter(this, scheduleController.getSchedule()));

        setTitle(groupName);
        updateTV();
        updateSchedule();
    }

    private void updateTV() {
        String dayOfWeekString = DateHelper.getDayNameByTimestamp(timestamp);
        int weekOfYear = DateHelper.getWeekOfYearByTimestamp(timestamp);

        String numWeekString = String.valueOf(weekOfYear) + " неделя";

        GregorianCalendar calendar = DateHelper.getCalendarByTimestamp(timestamp);

        date.setText(String.format("%d.%d", calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.MONTH) + 1));
        dayOfWeek.setText(dayOfWeekString);
        numWeek.setText(numWeekString);
    }

    private void updateSchedule() {
        scheduleController.setTimestamp(timestamp);
        ArrayList<Schedule> schedules = scheduleController.getSchedule();

        ((ScheduleAdapter) listView.getAdapter()).clear();
        ((ScheduleAdapter) listView.getAdapter()).addAll(schedules);
        ((ScheduleAdapter) listView.getAdapter()).notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        Date date = new Date(timestamp);
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(date);

        Long addMS;

        switch (v.getId()) {
            case R.id.nextButton:
                addMS = MILLISECOND_OF_DAY;

                if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
                    addMS *= 2;
                }

                timestamp += addMS;
                break;
            case R.id.backButton:
                addMS = -MILLISECOND_OF_DAY;

                if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY) {
                    addMS *= 2;
                }

                timestamp += addMS;
                break;
            case R.id.todayButton:
                timestamp = System.currentTimeMillis();

                if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
                    timestamp += MILLISECOND_OF_DAY;
                }
                break;
        }

        updateTV();
        updateSchedule();
    }



    private class ScheduleAdapter extends ArrayAdapter<Schedule> {

        private final LayoutInflater lInflater;

        ScheduleAdapter(Context context, ArrayList<Schedule> schedules) {
            super(context, R.layout.schedule_item, schedules);

            lInflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View view = lInflater.inflate(R.layout.schedule_item, null);

            Schedule currentItem;

            try {
                currentItem = getItem(position);
            } catch (IndexOutOfBoundsException e) {
                currentItem = new Schedule();
                currentItem.setSubject_id(-1L);
            }

            if (currentItem.getSubject_id() == -1) {
                ((TextView) view.findViewById(R.id.subject)).setText("");
                ((TextView) view.findViewById(R.id.teacher)).setText("");
                ((TextView) view.findViewById(R.id.week)).setText("");
                ((TextView) view.findViewById(R.id.timeText)).setText("");
                if (position == 0) {
                    ((TextView) view.findViewById(R.id.subject)).setText("Ура! Первой пары нет");
                }
            } else {
                if (currentItem.getSubject_id_2() == null || teacher_id != -1) {
                    Subject subjectItem = currentItem.getSubject();

                    if (currentItem.getSubject_id_2() != null && currentItem.getSubject_2().getTeacher_id() == teacher_id) {
                        subjectItem = currentItem.getSubject_2();
                    }

                    ((TextView) view.findViewById(R.id.timeText)).setText(generateTimeText(currentItem));

                    ((TextView) view.findViewById(R.id.subject)).setText(generateSubjectText(subjectItem));
                    ((TextView) view.findViewById(R.id.teacher)).setText(generateTeacherText(subjectItem, teacher_id));
                    ((TextView) view.findViewById(R.id.week)).setText(generateWeekText(subjectItem, currentItem.getStartWeek(), currentItem.getEndWeek()));
                } else {
                    view = lInflater.inflate(R.layout.schedule_item_subgrouped, null);

                    Subject subjectItem = currentItem.getSubject();
                    Subject subjectItem_2 = currentItem.getSubject_2();

                    ((TextView) view.findViewById(R.id.timeText)).setText(generateTimeText(currentItem));

                    ((TextView) view.findViewById(R.id.subject)).setText(generateSubjectText(subjectItem));
                    ((TextView) view.findViewById(R.id.teacher)).setText(generateTeacherText(subjectItem, teacher_id));
                    ((TextView) view.findViewById(R.id.week)).setText(generateWeekText(subjectItem, currentItem.getStartWeek(), currentItem.getEndWeek()));

                    ((TextView) view.findViewById(R.id.subject_2)).setText(generateSubjectText(subjectItem_2));
                    ((TextView) view.findViewById(R.id.teacher_2)).setText(generateTeacherText(subjectItem_2, teacher_id));
                    ((TextView) view.findViewById(R.id.week_2)).setText(generateWeekText(subjectItem_2, currentItem.getStartWeek_2(), currentItem.getEndWeek_2()));
                }
            }

            return view;
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        swipe.dispatchTouchEvent(event);
        return super.dispatchTouchEvent(event);
    }
}
