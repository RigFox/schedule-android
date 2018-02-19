package xyz.rigfox.schedule_android;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.widget.RemoteViews;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class ScheduleWidget extends AppWidgetProvider {

    final static String ACTION_NEXT = "xyz.rigfox.schedule_androidNext";
    final static String ACTION_BACK = "xyz.rigfox.schedule_androidBack";
    final static String ACTION_TODAY = "xyz.rigfox.schedule_androidToday";

    final static Long MILLISECOND_OF_DAY = (long) 86400000;

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                         int appWidgetId) {

        RemoteViews rv = new RemoteViews(context.getPackageName(),
                R.layout.schedule_widget);

        setUpdateTV(rv, context, appWidgetId);
        setList(rv, context, appWidgetId);

        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId,
                R.id.listView);
        appWidgetManager.updateAppWidget(appWidgetId, rv);
    }

    void setUpdateTV(RemoteViews rv, Context context, int appWidgetId) {
        SharedPreferences sp = context.getSharedPreferences(ScheduleWidgetConfigureActivity.PREFS_NAME + appWidgetId, 0);
        Long timestamp = sp.getLong(ScheduleWidgetConfigureActivity.PREF_PREFIX_DAY, System.currentTimeMillis());

        Date date = new Date(timestamp);
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(date);

        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 2;

        String dayOfWeekString = "";

        switch (dayOfWeek) {
            case 0:
                dayOfWeekString = "Понедельник";
                break;
            case 1:
                dayOfWeekString = "Вторник";
                break;
            case 2:
                dayOfWeekString = "Среда";
                break;
            case 3:
                dayOfWeekString = "Четверг";
                break;
            case 4:
                dayOfWeekString = "Пятница";
                break;
            case 5:
                dayOfWeekString = "Суббота";
                break;
        }

        int currentWeek = calendar.get(Calendar.WEEK_OF_YEAR);
        int currentYear = calendar.get(Calendar.YEAR);

        if (currentWeek < 35) {
            currentWeek += 17 + 35;
        }

        if (currentYear == 2018) {
            currentWeek++;
        }

        int weekOfYear = currentWeek - 35;

        String numWeek = String.valueOf(weekOfYear) + " неделя";

        rv.setTextViewText(R.id.date, calendar.get(Calendar.DAY_OF_MONTH) + "." + (calendar.get(Calendar.MONTH) + 1));
        rv.setTextViewText(R.id.dayOfWeek, dayOfWeekString);
        rv.setTextViewText(R.id.numWeek, numWeek);

        setPendingIntent(ACTION_NEXT, R.id.nextButton, context, appWidgetId, rv);
        setPendingIntent(ACTION_BACK, R.id.backButton, context, appWidgetId, rv);
        setPendingIntent(ACTION_TODAY, R.id.todayButton, context, appWidgetId, rv);
    }

    void setList(RemoteViews rv, Context context, int appWidgetId) {
        Intent adapter = new Intent(context, ScheduleFactoryService.class);
        adapter.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        Uri data = Uri.parse(adapter.toUri(Intent.URI_INTENT_SCHEME));
        adapter.setData(data);
        rv.setRemoteAdapter(R.id.listView, adapter);
    }

    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

        int mAppWidgetId;

        if (intent == null) {
            return;
        }

        Bundle extras = intent.getExtras();
        if (extras == null) {
            return;
        }

        mAppWidgetId = extras.getInt(
                AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);

        SharedPreferences sp = context.getSharedPreferences(ScheduleWidgetConfigureActivity.PREFS_NAME + mAppWidgetId, 0);
        SharedPreferences.Editor editor = sp.edit();

        Long timestamp = sp.getLong(ScheduleWidgetConfigureActivity.PREF_PREFIX_DAY, System.currentTimeMillis());

        Date date = new Date(timestamp);
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(date);

        Long addMS;

        switch (intent.getAction()) {
            case ACTION_NEXT:
                addMS = MILLISECOND_OF_DAY;

                if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
                    addMS *= 2;
                }

                editor.putLong(ScheduleWidgetConfigureActivity.PREF_PREFIX_DAY, timestamp + addMS);
                editor.apply();
                break;
            case ACTION_BACK:
                addMS = -MILLISECOND_OF_DAY;

                if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY) {
                    addMS *= 2;
                }

                editor.putLong(ScheduleWidgetConfigureActivity.PREF_PREFIX_DAY, timestamp + addMS);
                editor.apply();
                break;
            case ACTION_TODAY:
                timestamp = System.currentTimeMillis();

                if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
                    timestamp += MILLISECOND_OF_DAY;
                }

                editor.putLong(ScheduleWidgetConfigureActivity.PREF_PREFIX_DAY, timestamp);
                editor.apply();
                break;
        }

        updateAppWidget(context, AppWidgetManager.getInstance(context),
                mAppWidgetId);
    }

    private void setPendingIntent(String Action, int id, Context context, int appWidgetId, RemoteViews rv) {
        Intent intent = new Intent(context, ScheduleWidget.class);
        intent.setAction(Action);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        PendingIntent pIntent = PendingIntent.getBroadcast(context, appWidgetId, intent, 0);

        rv.setOnClickPendingIntent(id, pIntent);
    }
}

