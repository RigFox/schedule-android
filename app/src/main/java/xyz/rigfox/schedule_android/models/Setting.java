package xyz.rigfox.schedule_android.models;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

@Entity
public class Setting {
    @Id
    private Long id;

    @NotNull
    private Integer version;
    @NotNull
    private Integer revision;

    @NotNull
    private String updated_at;

    public Setting(JSONObject jsonSetting) {
        try {
            this.id = 1L;
            this.version = jsonSetting.getInt("version");
            this.revision = jsonSetting.getInt("revision");
            this.updated_at = jsonSetting.getString("updated_at");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Generated(hash = 2067123515)
    public Setting(Long id, @NotNull Integer version, @NotNull Integer revision,
            @NotNull String updated_at) {
        this.id = id;
        this.version = version;
        this.revision = revision;
        this.updated_at = updated_at;
    }

    @Generated(hash = 909716735)
    public Setting() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getVersion() {
        return this.version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public Integer getRevision() {
        return this.revision;
    }

    public void setRevision(Integer revision) {
        this.revision = revision;
    }

    public String getUpdated_at() {
        return this.updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }
}
