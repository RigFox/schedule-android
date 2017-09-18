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
public class Specialty {
    @Id
    private Long id;

    private String name;
    private Long faculty_id;

    @ToOne(joinProperty = "faculty_id")
    private Faculty faculty;

    @ToMany(referencedJoinProperty = "specialty_id")
    private List<Group> groups;

    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    @Generated(hash = 1486779319)
    private transient SpecialtyDao myDao;

    @Generated(hash = 2040065736)
    private transient Long faculty__resolvedKey;

    public Specialty(JSONObject jsonSpecialty) {
        try {
            this.id = jsonSpecialty.getLong("id");
            this.name = jsonSpecialty.getString("name");
            this.faculty_id = jsonSpecialty.getLong("faculty_id");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Generated(hash = 405872159)
    public Specialty(Long id, String name, Long faculty_id) {
        this.id = id;
        this.name = name;
        this.faculty_id = faculty_id;
    }

    @Generated(hash = 933658010)
    public Specialty() {
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

    public Long getFaculty_id() {
        return this.faculty_id;
    }

    public void setFaculty_id(Long faculty_id) {
        this.faculty_id = faculty_id;
    }

    /** To-one relationship, resolved on first access. */
    @Generated(hash = 53056432)
    public Faculty getFaculty() {
        Long __key = this.faculty_id;
        if (faculty__resolvedKey == null || !faculty__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            FacultyDao targetDao = daoSession.getFacultyDao();
            Faculty facultyNew = targetDao.load(__key);
            synchronized (this) {
                faculty = facultyNew;
                faculty__resolvedKey = __key;
            }
        }
        return faculty;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 112012786)
    public void setFaculty(Faculty faculty) {
        synchronized (this) {
            this.faculty = faculty;
            faculty_id = faculty == null ? null : faculty.getId();
            faculty__resolvedKey = faculty_id;
        }
    }

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 549800679)
    public List<Group> getGroups() {
        if (groups == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            GroupDao targetDao = daoSession.getGroupDao();
            List<Group> groupsNew = targetDao._querySpecialty_Groups(id);
            synchronized (this) {
                if (groups == null) {
                    groups = groupsNew;
                }
            }
        }
        return groups;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    @Generated(hash = 464128061)
    public synchronized void resetGroups() {
        groups = null;
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
    @Generated(hash = 1659971169)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getSpecialtyDao() : null;
    }
}
