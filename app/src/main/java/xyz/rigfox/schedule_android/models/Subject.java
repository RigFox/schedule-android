package xyz.rigfox.schedule_android.models;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.ToOne;
import org.json.JSONException;
import org.json.JSONObject;

@Entity
public class Subject {
    @Id
    private Long id;

    private String name;
    private Long teacher_id;
    private String classroom;
    private Long group_id;

    @ToOne(joinProperty = "group_id")
    private Group group;

    @ToOne(joinProperty = "teacher_id")
    private Teacher teacher;

    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    @Generated(hash = 1644932788)
    private transient SubjectDao myDao;

    @Generated(hash = 201187923)
    private transient Long group__resolvedKey;

    @Generated(hash = 155140967)
    private transient Long teacher__resolvedKey;

    public Subject(JSONObject jsonSubject) {
        try {
            this.id = jsonSubject.getLong("id");
            this.name = jsonSubject.getString("name");
            this.teacher_id = jsonSubject.getLong("teacher_id");
            this.classroom = jsonSubject.getString("classroom");
            this.group_id = jsonSubject.getLong("group_id");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Generated(hash = 778539568)
    public Subject(Long id, String name, Long teacher_id, String classroom,
            Long group_id) {
        this.id = id;
        this.name = name;
        this.teacher_id = teacher_id;
        this.classroom = classroom;
        this.group_id = group_id;
    }

    @Generated(hash = 1617906264)
    public Subject() {
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

    public Long getTeacher_id() {
        return this.teacher_id;
    }

    public void setTeacher_id(Long teacher_id) {
        this.teacher_id = teacher_id;
    }

    public String getClassroom() {
        return this.classroom;
    }

    public void setClassroom(String classroom) {
        this.classroom = classroom;
    }

    public Long getGroup_id() {
        return this.group_id;
    }

    public void setGroup_id(Long group_id) {
        this.group_id = group_id;
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

    /** To-one relationship, resolved on first access. */
    @Generated(hash = 1629490509)
    public Teacher getTeacher() {
        Long __key = this.teacher_id;
        if (teacher__resolvedKey == null || !teacher__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            TeacherDao targetDao = daoSession.getTeacherDao();
            Teacher teacherNew = targetDao.load(__key);
            synchronized (this) {
                teacher = teacherNew;
                teacher__resolvedKey = __key;
            }
        }
        return teacher;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1122484375)
    public void setTeacher(Teacher teacher) {
        synchronized (this) {
            this.teacher = teacher;
            teacher_id = teacher == null ? null : teacher.getId();
            teacher__resolvedKey = teacher_id;
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

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 937984622)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getSubjectDao() : null;
    }
}
