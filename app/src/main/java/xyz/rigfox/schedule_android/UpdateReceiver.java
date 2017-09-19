package xyz.rigfox.schedule_android;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class UpdateReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction() == null) {
            ScheduleSingleton.getInstance().checkAndDownloadUpdate();
        } else {
            if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
                setUpdateAlarm(context);
            }
        }
    }

    public void setUpdateAlarm(Context context) {
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent(context, UpdateReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, i, 0);
        am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 1000 * 60 * 60, pendingIntent); // Millisec * Second * Minute
    }

    public void cancelUpdateAlarm(Context context) {
        Intent intent = new Intent(context, UpdateReceiver.class);
        PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(sender);
    }
}
