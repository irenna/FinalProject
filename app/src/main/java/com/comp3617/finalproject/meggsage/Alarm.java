package com.comp3617.finalproject.meggsage;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * Created by meggz on 4/5/16.
 */
public class Alarm{

    private Alarm() {

    }

    public static void setAlarm(Context context, int type, long id, long time) {

        Intent i = new Intent(context, TextAlarmReceiver.class);
        i.putExtra("id", id);
        PendingIntent pi = PendingIntent.getBroadcast(context, (int)id, i, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager manager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);

        manager.setExact(AlarmManager.RTC_WAKEUP, time, pi);
        Toast.makeText(context, "Alarm Set", Toast.LENGTH_SHORT).show();
    }

    public static void cancelAlarm(Context context, int type, long id) {

        Intent i = new Intent(context, TextAlarmReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(context, (int)id, i, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager manager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        pi.cancel();
        manager.cancel(pi);
    }

}
