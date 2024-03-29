package xyz.rigfox.schedule_android.models;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.ToOne;
import org.json.JSONException;
import org.json.JSONObject;

@Entity
public class Schedule {
    @Id
    private Long id;

    private Long subject_id;
    private Integer day;
    private Integer num;
    private Integer startWeek;
    private Integer endWeek;

    private Long subject_id_2;
    private Integer startWeek_2;
    private Integer endWeek_2;

    private Long group_id;

    @ToOne(joinProperty = "subject_id")
    private Subject subject;

    @ToOne(joinProperty = "subject_id_2")
    private Subject subject_2;

    @ToOne(joinProperty = "group_id")
    private Group group;

    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    @Generated(hash = 1493574644)
    private transient ScheduleDao myDao;

    @Generated(hash = 711858396)
    private transient Long subject__resolvedKey;

    @Generated(hash = 201187923)
    private transient Long group__resolvedKey;

    @Generated(hash = 896007349)
    private transient Long subject_2__resolvedKey;

    public Schedule(JSONObject jsonSchedule) {
        try {
            this.id = jsonSchedule.getLong("id");
            this.subject_id = jsonSchedule.getLong("subject_id");
            this.day = jsonSchedule.getInt("day");
            this.num = jsonSchedule.getInt("num");
            this.startWeek = jsonSchedule.getInt("startWeek");
            this.endWeek = jsonSchedule.getInt("endWeek");

            this.group_id = jsonSchedule.getLong("group_id");

            try {
                this.subject_id_2 = jsonSchedule.getLong("subject_id_2");
            } catch (JSONException e) {
                this.subject_id_2 = null;
            }

            try {
                this.startWeek_2 = jsonSchedule.getInt("startWeek_2");
            } catch (JSONException e) {
                this.startWeek_2 = null;
            }

            try {
                this.endWeek_2 = jsonSchedule.getInt("endWeek_2");
            } catch (JSONException e) {
                this.endWeek_2 = null;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Generated(hash = 1145598737)
    public Schedule(Long id, Long subject_id, Integer day, Integer num, Integer startWeek,
            Integer endWeek, Long subject_id_2, Integer startWeek_2, Integer endWeek_2,
            Long group_id) {
        this.id = id;
        this.subject_id = subject_id;
        this.day = day;
        this.num = num;
        this.startWeek = startWeek;
        this.endWeek = endWeek;
        this.subject_id_2 = subject_id_2;
        this.startWeek_2 = startWeek_2;
        this.endWeek_2 = endWeek_2;
        this.group_id = group_id;
    }

    @Generated(hash = 729319394)
    public Schedule() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSubject_id() {
        return this.subject_id;
    }

    public void setSubject_id(Long subject_id) {
        this.subject_id = subject_id;
    }

    public Integer getDay() {
        return this.day;
    }

    public void setDay(Integer day) {
        this.day = day;
    }

    public Integer getNum() {
        return this.num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public Integer getStartWeek() {
        return this.startWeek;
    }

    public void setStartWeek(Integer startWeek) {
        this.startWeek = startWeek;
    }

    public Integer getEndWeek() {
        return this.endWeek;
    }

    public void setEndWeek(Integer endWeek) {
        this.endWeek = endWeek;
    }

    public Long getGroup_id() {
        return this.group_id;
    }

    public void setGroup_id(Long group_id) {
        this.group_id = group_id;
    }

    /** To-one relationship, resolved on first access. */
    @Generated(hash = 536031480)
    public Subject getSubject() {
        Long __key = this.subject_id;
        if (subject__resolvedKey == null || !subject__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            SubjectDao targetDao = daoSession.getSubjectDao();
            Subject subjectNew = targetDao.load(__key);
            synchronized (this) {
                subject = subjectNew;
                subject__resolvedKey = __key;
            }
        }
        return subject;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1938871270)
    public void setSubject(Subject subject) {
        synchronized (this) {
            this.subject = subject;
            subject_id = subject == null ? null : subject.getId();
            subject__resolvedKey = subject_id;
        }
    }

    /** To-one relationship, resolved on first access. */
    @Generated(hash = 1869940772)
    public Group getGroup() {
        Long __key = this.group_id;
        if (group__resolvedKey == null || !group__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            GroupDao targetDao = daoSession.getGroupDao();
            Group groupNew = targetDao.load(__key);
            synchronized (this) {
                group = groupNew;
                group__resolvedKey = __key;
            }
        }
        return group;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1283475317)
    public void setGroup(Group group) {
        synchronized (this) {
            this.group = group;
            group_id = group == null ? null : group.getId();
            group__resolvedKey = group_id;
        }
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#delete(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 128553479)
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.delete(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#refresh(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 1942392019)
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.refresh(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#update(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 713229351)
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.update(this);
    }

    public Long getSubject_id_2() {
        return this.subject_id_2;
    }

    public void setSubject_id_2(Long subject_id_2) {
        this.subject_id_2 = subject_id_2;
    }

    public Integer getStartWeek_2() {
        return this.startWeek_2;
    }

    public void setStartWeek_2(Integer startWeek_2) {
        this.startWeek_2 = startWeek_2;
    }

    public Integer getEndWeek_2() {
        return this.endWeek_2;
    }

    public void setEndWeek_2(Integer endWeek_2) {
        this.endWeek_2 = endWeek_2;
    }

    /** To-one relationship, resolved on first access. */
    @Generated(hash = 1764836738)
    public Subject getSubject_2() {
        Long __key = this.subject_id_2;
        if (subject_2__resolvedKey == null || !subject_2__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            SubjectDao targetDao = daoSession.getSubjectDao();
            Subject subject_2New = targetDao.load(__key);
            synchronized (this) {
                subject_2 = subject_2New;
                subject_2__resolvedKey = __key;
            }
        }
        return subject_2;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 26944271)
    public void setSubject_2(Subject subject_2) {
        synchronized (this) {
            this.subject_2 = subject_2;
            subject_id_2 = subject_2 == null ? null : subject_2.getId();
            subject_2__resolvedKey = subject_id_2;
        }
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 502317300)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getScheduleDao() : null;
    }
}
