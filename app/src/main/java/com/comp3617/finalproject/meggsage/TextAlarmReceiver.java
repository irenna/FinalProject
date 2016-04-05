package com.comp3617.finalproject.meggsage;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;

public class TextAlarmReceiver extends BroadcastReceiver {

    private RemindersDBHelper db;

    public TextAlarmReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {



        long id = intent.getLongExtra("id", 0);
        db = RemindersDBHelper.getInstance(context);
        TextReminder tr = db.getTextReminder(id);

        Intent i = new Intent(context, TextAlarmReceiver.class);
        i.putExtra("id", id);
        PendingIntent pi = PendingIntent.getBroadcast(context, (int) id, i, PendingIntent.FLAG_UPDATE_CURRENT);


        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(tr.getRecipientNumber(), null, tr.getMessage(), );

        throw new UnsupportedOperationException("Not yet implemented");
    }



    private void showNotification(String downloadedLocation) {
        NotificationManager manager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);

        Notification.Builder builder = new Notification.Builder(this);
        builder.setContentTitle("Image Downloader");
        builder.setContentText("Finished downloading...");
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setAutoCancel(true);

        Intent intent = new Intent(this, DisplayImageActivity.class);
        intent.setAction(MainActivity.SHOW_IMG_ACTION);
        intent.putExtra(MainActivity.DOWNLOAD_IMG_LOC, downloadedLocation);

        PendingIntent pi = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pi);


        manager.notify(NOTIFICATION_ID, builder.build());
    }

}
