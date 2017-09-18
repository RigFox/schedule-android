package xyz.rigfox.schedule_android.models;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.ToMany;
import org.greenrobot.greendao.annotation.ToOne;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

@Entity
public class Group {
    @Id
    private Long id;

    private String name;
    private Long specialty_id;

    @ToOne(joinProperty = "specialty_id")
    private Specialty specialty;

    @ToMany(referencedJoinProperty = "group_id")
    private List<Schedule> schedules;

    @ToMany(referencedJoinProperty = "group_id")
    private List<Subject> subjects;

    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    @Generated(hash = 1591306109)
    private transient GroupDao myDao;

    @Generated(hash = 2048110654)
    private transient Long specialty__resolvedKey;

    public Group(JSONObject jsonGroup) {
        try {
            this.id = jsonGroup.getLong("id");
            this.name = jsonGroup.getString("name");
            this.specialty_id = jsonGroup.getLong("specialty_id");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Generated(hash = 1308674278)
    public Group(Long id, String name, Long specialty_id) {
        this.id = id;
        this.name = name;
        this.specialty_id = specialty_id;
    }

    @Generated(hash = 117982048)
    public Group() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getSpecialty_id() {
        return this.specialty_id;
    }

    public void setSpecialty_id(Long specialty_id) {
        this.specialty_id = specialty_id;
    }

    /** To-one relationship, resolved on first access. */
    @Generated(hash = 298324185)
    public Specialty getSpecialty() {
        Long __key = this.specialty_id;
        if (specialty__resolvedKey == null
                || !specialty__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            SpecialtyDao targetDao = daoSession.getSpecialtyDao();
            Specialty specialtyNew = targetDao.load(__key);
            synchronized (this) {
                specialty = specialtyNew;
                specialty__resolvedKey = __key;
            }
        }
        return specialty;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 78599167)
    public void setSpecialty(Specialty specialty) {
        synchronized (this) {
            this.specialty = specialty;
            specialty_id = specialty == null ? null : specialty.getId();
            specialty__resolvedKey = specialty_id;
        }
    }

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 1128702582)
    public List<Schedule> getSchedules() {
        if (schedules == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            ScheduleDao targetDao = daoSession.getScheduleDao();
            List<Schedule> schedulesNew = targetDao._queryGroup_Schedules(id);
            synchronized (this) {
                if (schedules == null) {
                    schedules = schedulesNew;
                }
            }
        }
        return schedules;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    @Generated(hash = 283382071)
    public synchronized void resetSchedules() {
        schedules = null;
    }

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 782663324)
    public List<Subject> getSubjects() {
        if (subjects == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            SubjectDao targetDao = daoSession.getSubjectDao();
            List<Subject> subjectsNew = targetDao._queryGroup_Subjects(id);
            synchronized (this) {
                if (subjects == null) {
                    subjects = subjectsNew;
                }
            }
        }
        return subjects;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    @Generated(hash = 1744012163)
    public synchronized void resetSubjects() {
        subjects = null;
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

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1333602095)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getGroupDao() : null;
    }
}
