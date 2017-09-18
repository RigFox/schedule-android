package xyz.rigfox.schedule_android;

import android.content.Intent;
import android.widget.RemoteViewsService;

public class ScheduleFactoryService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new ScheduleFactory(getApplicationContext(), intent);
    }
}
