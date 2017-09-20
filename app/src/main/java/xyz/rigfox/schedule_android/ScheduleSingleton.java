package xyz.rigfox.schedule_android;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
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
import xyz.rigfox.schedule_android.models.TeacherDao;

class ScheduleSingleton {
    private static ScheduleSingleton mInstance;

    private final DaoSession daoSession;
    private final Context ctx;

    static void initInstance(Context context) {
        if (mInstance == null) {
            mInstance = new ScheduleSingleton(context);
        }
    }

    static ScheduleSingleton getInstance() {
        return mInstance;
    }

    private ScheduleSingleton(Context context) {
        ctx = context;

        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(context, "schedules-db", null);
        SQLiteDatabase db = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();

//        DaoMaster.dropAllTables(daoSession.getDatabase(), true);
//        DaoMaster.createAllTables(daoSession.getDatabase(), true);
    }

    boolean checkDB() {
        return daoSession.getSettingDao().queryBuilder().where(SettingDao.Properties.Id.eq(1)).count() != 0;
    }

    void checkOrDownload() {
        if (!checkDB()) {
            DownloadTask downloadTask = new DownloadTask();
            downloadTask.execute();
        }
    }

    void resetDB() {
        DaoMaster.dropAllTables(daoSession.getDatabase(), true);
        DaoMaster.createAllTables(daoSession.getDatabase(), true);

        checkOrDownload();
    }

    void checkAndDownloadUpdate() {
        UpdateTask updateTask = new UpdateTask();
        updateTask.execute();
    }

    Setting getSetting() {
        return daoSession.getSettingDao().queryBuilder().where(SettingDao.Properties.Id.eq(1)).unique();
    }

    List<Group> getGroups() {
        return daoSession.getGroupDao().loadAll();
    }

    List<Teacher> getTeachers() {
        return daoSession.getTeacherDao().queryBuilder()
                .where(TeacherDao.Properties.Name.notEq("????"))
                .orderAsc(TeacherDao.Properties.Name).list();
    }

    ScheduleDao getScheduleDao() {
        return daoSession.getScheduleDao();
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
                Toast.makeText(ctx, "Не удалось загрузить расписание!", Toast.LENGTH_LONG).show();
                return;
            }

            try {
                JSONObject jsonSetting = jsonObject.getJSONObject("setting");

                Setting setting = new Setting(jsonSetting);
                if (setting.getVersion() != ctx.getResources().getInteger(R.integer.version)) {
                    Toast.makeText(ctx, "Не удалось загрузить расписание. Обновите приложение!", Toast.LENGTH_LONG).show();
                    return;
                }

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

                Toast.makeText(ctx, "Расписание успешно загружено!", Toast.LENGTH_LONG).show();
            } catch (JSONException e) {
                Toast.makeText(ctx, "Ошибка загрузки! Возможно неконсистентное состояние!", Toast.LENGTH_LONG).show();
            }
        }
    }

    private class UpdateTask extends AsyncTask<Void, Void, JSONObject> {
        @Override
        protected JSONObject doInBackground(Void... voids) {
            try {
                String JSON = run("http://rasp.sibsu.tk/setting.json");

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
                return;
            }

            Setting setting = new Setting(jsonObject);
            if (setting.getVersion() != ctx.getResources().getInteger(R.integer.version)) {
                Toast.makeText(ctx, "Не удалось обновить расписание. Обновите приложение!", Toast.LENGTH_LONG).show();
                return;
            }

            if (setting.getRevision() > getSetting().getRevision()) {
                Toast.makeText(ctx, "Вышло новое обновление!", Toast.LENGTH_LONG).show();
                resetDB();
            }
        }
    }

    private OkHttpClient client = new OkHttpClient();

    private String run(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();

        Response response = client.newCall(request).execute();
        return response.body().string();
    }
}
