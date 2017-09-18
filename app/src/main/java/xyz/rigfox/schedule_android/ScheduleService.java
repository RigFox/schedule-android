package xyz.rigfox.schedule_android;

import android.app.Service;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.IBinder;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import xyz.rigfox.schedule_android.models.DaoMaster;
import xyz.rigfox.schedule_android.models.DaoSession;
import xyz.rigfox.schedule_android.models.Faculty;
import xyz.rigfox.schedule_android.models.Group;
import xyz.rigfox.schedule_android.models.Schedule;
import xyz.rigfox.schedule_android.models.ScheduleDao;
import xyz.rigfox.schedule_android.models.Setting;
import xyz.rigfox.schedule_android.models.SettingDao;
import xyz.rigfox.schedule_android.models.Subject;
import xyz.rigfox.schedule_android.models.Teacher;

public class ScheduleService extends Service {
    public static final String CHECK_OR_DOWNLOAD = "CHECK_OR_DOWNLOAD";
    public static final String CHECK_UPDATE = "CHECK_UPDATE";
    public static final String RESET = "RESET";

    private DaoSession daoSession;

    private ScheduleBinder binder;

    OkHttpClient client = new OkHttpClient();

    String run(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();

        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    @Override
    public void onCreate() {
        super.onCreate();

        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "schedule-db", null);
        SQLiteDatabase db = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();

//        DaoMaster.dropAllTables(daoSession.getDatabase(), true);
//        DaoMaster.createAllTables(daoSession.getDatabase(), true);

        binder = new ScheduleBinder();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String action = intent.getAction();

        switch (action) {
            case CHECK_OR_DOWNLOAD:
                checkOrDownload();
                break;
            case CHECK_UPDATE:

                break;
            case RESET:
                DaoMaster.dropAllTables(daoSession.getDatabase(), true);
                DaoMaster.createAllTables(daoSession.getDatabase(), true);

                checkOrDownload();
                break;
            default:
                throw new UnsupportedOperationException("Action unsupported");
        }

        return Service.START_STICKY;
    }

    private class DownloadTask extends AsyncTask<Void, Void, JSONObject> {
        @Override
        protected JSONObject doInBackground(Void... voids) {
            try {
                String JSON = run("http://rasp.sibsu.tk/schedule.json");

                return new JSONObject(JSON);
            } catch (IOException | JSONException e) {
                return null;
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            super.onPostExecute(jsonObject);

            if (jsonObject == null) {
                Toast.makeText(ScheduleService.this, "Не удалось загрузить расписание!", Toast.LENGTH_LONG).show();
                return;
            }

            try {
                JSONObject jsonSetting = jsonObject.getJSONObject("setting");
                daoSession.getSettingDao().insert(new Setting(jsonSetting));

                JSONArray jsonTeachers = jsonObject.getJSONArray("teachers");
                ArrayList<Teacher> teachers = new ArrayList<>();
                for (int i = 0; i < jsonTeachers.length(); i++) {
                    teachers.add(new Teacher(jsonTeachers.getJSONObject(i)));
                }
                daoSession.getTeacherDao().insertInTx(teachers);

                JSONArray jsonSubjects = jsonObject.getJSONArray("subjects");
                ArrayList<Subject> subjects = new ArrayList<>();
                for (int i = 0; i < jsonSubjects.length(); i++) {
                    subjects.add(new Subject(jsonSubjects.getJSONObject(i)));
                }
                daoSession.getSubjectDao().insertInTx(subjects);

                JSONArray jsonFaculties = jsonObject.getJSONArray("faculties");
                ArrayList<Faculty> faculties = new ArrayList<>();
                for (int i = 0; i < jsonFaculties.length(); i++) {
                    faculties.add(new Faculty(jsonFaculties.getJSONObject(i)));
                }
                daoSession.getFacultyDao().insertInTx(faculties);

                JSONArray jsonGroups = jsonObject.getJSONArray("groups");
                ArrayList<Group> groups = new ArrayList<>();
                for (int i = 0; i < jsonGroups.length(); i++) {
                    groups.add(new Group(jsonGroups.getJSONObject(i)));
                }
                daoSession.getGroupDao().insertInTx(groups);

                JSONArray jsonSchedules = jsonObject.getJSONArray("schedules");
                ArrayList<Schedule> schedules = new ArrayList<>();
                for (int i = 0; i < jsonSchedules.length(); i++) {
                    schedules.add(new Schedule(jsonSchedules.getJSONObject(i)));
                }
                daoSession.getScheduleDao().insertInTx(schedules);

                Toast.makeText(ScheduleService.this, "Расписание успешно загружено!", Toast.LENGTH_LONG).show();
            } catch (JSONException e) {
                Toast.makeText(ScheduleService.this, "Ошибка загрузки! Возможно неконсистентное состояние!", Toast.LENGTH_LONG).show();
            }
        }
    }

    void checkOrDownload() {
        if (!checkDB()) {
            DownloadTask downloadTask = new DownloadTask();
            downloadTask.execute();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    class ScheduleBinder extends Binder {
        ScheduleService getService() {
            return ScheduleService.this;
        }
    }

    boolean checkDB() {
        return daoSession.getSettingDao().queryBuilder().where(SettingDao.Properties.Id.eq(1)).count() != 0;
    }

    Setting getSetting() {
        return daoSession.getSettingDao().queryBuilder().where(SettingDao.Properties.Id.eq(1)).unique();
    }

    List<Group> getGroups() {
        return daoSession.getGroupDao().loadAll();
    }

    List<Teacher> getTeachers() {
        return daoSession.getTeacherDao().loadAll();
    }

    ScheduleDao getScheduleDao() {
        return daoSession.getScheduleDao();
    }
}
