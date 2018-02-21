package xyz.rigfox.schedule_android;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;

import xyz.rigfox.schedule_android.helpers.DateHelper;
import xyz.rigfox.schedule_android.models.Schedule;
import xyz.rigfox.schedule_android.models.ScheduleDao;
import xyz.rigfox.schedule_android.models.Subject;
import xyz.rigfox.schedule_android.models.SubjectDao;

class ScheduleController {
    private Long timestamp;
    private int group_id;
    private int teacher_id;

    private ScheduleDao scheduleDao;

    ScheduleController(Long timestamp, int group_id, int teacher_id) {
        this.timestamp = timestamp;
        this.group_id = group_id;
        this.teacher_id = teacher_id;

        this.scheduleDao = ScheduleSingleton.getInstance().getScheduleDao();
    }

    void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    ArrayList<Schedule> getSchedule() {
        int dayOfWeek = DateHelper.getCalendarByTimestamp(timestamp).get(Calendar.DAY_OF_WEEK) - 2;
        int weekOfYear = DateHelper.getWeekOfYearByTimestamp(timestamp);

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
                            ScheduleDao.Properties.StartWeek.le(weekOfYear),
                            ScheduleDao.Properties.EndWeek.ge(weekOfYear)))
                    .orderAsc(ScheduleDao.Properties.Num);
        }

        ArrayList<Schedule> prepSchedules = (ArrayList<Schedule>) qb.list();

        if (teacher_id != -1) {
            prepSchedules.addAll(qb_2.list());

            ArrayList<Schedule> temp = new ArrayList<>();

            for (Schedule schedule : prepSchedules) {
                if (schedule.getDay() == dayOfWeek && schedule.getStartWeek() <= weekOfYear && schedule.getEndWeek() >= weekOfYear) {
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

        ArrayList<Schedule> schedules = new ArrayList<>();

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

        return schedules;
    }
}
