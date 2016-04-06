package com.comp3617.finalproject.meggsage;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;

public class TextAlarmReceiver extends BroadcastReceiver {

    private RemindersDBHelper db;
    private static final int NOTIFICATION_ID = 1001;
    private static final String CONF_SENT = "sent";

    @Override
    public void onReceive(Context context, Intent intent) {

        long id = intent.getLongExtra("id", 0);
        db = RemindersDBHelper.getInstance(context);
        TextReminder tr = db.getTextReminder(id);

/*
        Intent i = new Intent(context, TextAlarmReceiver.class);
        i.putExtra("id", id);
        PendingIntent pi = PendingIntent.getBroadcast(context, (int) id, i, PendingIntent.FLAG_UPDATE_CURRENT);
*/
        //TODO send a pending intent to check if the text message was sent successfully or not.
        //SmsManager smsManager = SmsManager.getDefault();
        //smsManager.sendTextMessage(tr.getRecipientNumber(), null, tr.getMessage(), null, null);
        NotificationManager manager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);

        Notification.Builder builder = new Notification.Builder(context);
        builder.setContentTitle(context.getResources().getString(R.string.conf_tr_title));
        builder.setContentText(context.getResources().getString(R.string.conf_tr_sent) + tr.getRecipientNumber());
        builder.setSmallIcon(R.drawable.ic_mail_outline_white_24dp);
        builder.setAutoCancel(true);
        builder.setLights(Color.RED, 500, 500);

        Notification.InboxStyle inboxStyle = new Notification.InboxStyle();
        inboxStyle.setBigContentTitle(context.getResources().getString(R.string.conf_tr_sent) + tr.getRecipientNumber());
        inboxStyle.addLine(tr.getTitle());
        inboxStyle.addLine(tr.getMessage());
        builder.setStyle(inboxStyle);

        Intent i = new Intent(context, DisplayTextRemindersActivity.class);
        intent.putExtra(CONF_SENT, true);

        PendingIntent pi = PendingIntent.getActivity(context, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pi);

        db.sendTextReminder(tr.getId());
        db.close();
        manager.notify(NOTIFICATION_ID, builder.build());

    }

}
