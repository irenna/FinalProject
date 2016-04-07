package com.comp3617.finalproject.meggsage;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

/**
 * Created by meggz on 4/5/16.
 */
public class Alarm{

    private static final int TM_TYPE = 0;
    private static final int NR_TYPE = 1;

    private Alarm() {

    }

    public static void setAlarm(Context context, int type, long id, long time) {

        Intent i = new Intent(context, TextAlarmReceiver.class);
        i.putExtra("id", id);
        i.putExtra("type", type);
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, i, 0);
        AlarmManager manager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        manager.setExact(AlarmManager.RTC_WAKEUP, time, pi);
    }

    public static void setNotificationAlarm(Context context, int type, long id, long time, int repeat) {

        Intent i = new Intent(context, TextAlarmReceiver.class);
        i.putExtra("id", id);
        i.putExtra("type", type);
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, i, 0);
        AlarmManager manager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        if(repeat == 0) {
            manager.setExact(AlarmManager.RTC_WAKEUP, time, pi);
        } else {
            manager.setRepeating(AlarmManager.RTC_WAKEUP, time, repeat*3600000, pi);
        }

    }

    public static void cancelAlarm(Context context, int type, long id) {

        Intent i = new Intent(context, TextAlarmReceiver.class);
        i.putExtra("id", id);
        i.putExtra("type", type);
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, i, 0);
        AlarmManager manager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        pi.cancel();
        manager.cancel(pi);
    }

}
