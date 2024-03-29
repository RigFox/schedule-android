package xyz.rigfox.schedule_android.models;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.ToMany;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

@Entity
public class Faculty {
    @Id
    private Long id;

    private String name;

    @ToMany(referencedJoinProperty = "faculty_id")
    private List<Specialty> specialties;

    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    @Generated(hash = 1268625480)
    private transient FacultyDao myDao;

    public Faculty(JSONObject jsonFaculty) {
        try {
            this.id = jsonFaculty.getLong("id");
            this.name = jsonFaculty.getString("name");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Generated(hash = 355737088)
    public Faculty(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    @Generated(hash = 2112390923)
    public Faculty() {
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

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 510488386)
    public List<Specialty> getSpecialties() {
        if (specialties == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            SpecialtyDao targetDao = daoSession.getSpecialtyDao();
            List<Specialty> specialtiesNew = targetDao
                    ._queryFaculty_Specialties(id);
            synchronized (this) {
                if (specialties == null) {
                    specialties = specialtiesNew;
                }
            }
        }
        return specialties;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    @Generated(hash = 1836688477)
    public synchronized void resetSpecialties() {
        specialties = null;
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
    @Generated(hash = 1121940746)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getFacultyDao() : null;
    }
}
