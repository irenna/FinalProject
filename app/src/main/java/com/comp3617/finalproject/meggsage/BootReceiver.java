package com.comp3617.finalproject.meggsage;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;

import java.util.ArrayList;
import java.util.Date;

public class BootReceiver extends BroadcastReceiver {

    private static final int TM_TYPE = 0; //Used indicate textreminder
    private static final int NOTIFICATION_ID = 1001;
    private static final String ALARMS_FAILED = "alarms_failed";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            RemindersDBHelper db = RemindersDBHelper.getInstance(context);
            ArrayList<TextReminder> trs = db.getActiveTextReminders();
            if(trs.size() > 0) {
                Date now = new Date();
                boolean alarmsFailed = false;
                for (TextReminder tr: trs) {
                    if(tr.getDueDate() > now.getTime()) {
                        Alarm.setAlarm(context, TM_TYPE, tr.getId(), tr.getDueDate());
                    } else {
                        db.failTextReminder(tr.getId());
                        alarmsFailed = true;
                    }
                }
                if(alarmsFailed) {
                    NotificationManager manager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);

                    Notification.Builder builder = new Notification.Builder(context);
                    builder.setContentTitle(context.getResources().getString(R.string.conf_tr_title));
                    builder.setContentText(context.getResources().getString(R.string.msg_tr_failed));
                    builder.setSmallIcon(R.drawable.ic_mail_outline_white_24dp);
                    builder.setAutoCancel(true);
                    builder.setLights(Color.RED, 500, 500);

                    Intent i = new Intent(context, DisplayTextRemindersActivity.class);
                    intent.putExtra(ALARMS_FAILED, true);

                    PendingIntent pi = PendingIntent.getActivity(context, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);
                    builder.setContentIntent(pi);

                    manager.notify(NOTIFICATION_ID, builder.build());
                }
            }
            db.close();
        }
    }

}
