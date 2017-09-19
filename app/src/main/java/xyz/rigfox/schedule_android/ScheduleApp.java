package xyz.rigfox.schedule_android;

import android.app.Application;

public class ScheduleApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        ScheduleSingleton.initInstance(this);
        UpdateReceiver updateReceiver = new UpdateReceiver();
        updateReceiver.setUpdateAlarm(this);
    }
}
