package com.comp3617.finalproject.meggsage;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

/**
 * Created by meggz on 4/5/16.
 */
public class Alarm{

    private Alarm() {

    }

    public static void setAlarm(Context context, int type, long id, long time) {

        Intent i = new Intent(context, TextAlarmReceiver.class);
        i.putExtra("id", id);
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, i, 0);
        AlarmManager manager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        manager.setExact(AlarmManager.RTC_WAKEUP, time, pi);
    }

    public static void cancelAlarm(Context context, int type, long id) {

        Intent i = new Intent(context, TextAlarmReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, i, 0);
        AlarmManager manager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        pi.cancel();
        manager.cancel(pi);
    }

}
