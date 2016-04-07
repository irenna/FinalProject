package com.comp3617.finalproject.meggsage;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.telephony.SmsManager;
import android.util.Log;

public class TextAlarmReceiver extends BroadcastReceiver {

    private static final int TM_TYPE = 0;
    private static final int NR_TYPE = 1;
    private RemindersDBHelper db;
    private static final int NOTIFICATION_ID = 1001;
    private static final String CONF_SENT = "sent";
    private static final long[] VIBRATE_PATTERN = {0, 200, 200, 200 };
    private static TypedArray colours;

    @Override
    public void onReceive(Context context, Intent intent) {

        long id = intent.getLongExtra("id", 0);
        int type = intent.getIntExtra("type",-1);
        db = RemindersDBHelper.getInstance(context);





        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification.Builder builder = new Notification.Builder(context);
        colours = context.getResources().obtainTypedArray(R.array.colour_ids);

        if(type == TM_TYPE) {


/*        Intent i = new Intent(context, TextAlarmReceiver.class);
        i.putExtra("id", id);
        PendingIntent pi = PendingIntent.getBroadcast(context, (int) id, i, PendingIntent.FLAG_UPDATE_CURRENT);*/
            //TODO send a pending intent to check if the text message was sent successfully or not.

            TextReminder tr = db.getTextReminder(id);
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(tr.getRecipientNumber(), null, tr.getMessage(), null, null);


            builder.setContentTitle(context.getResources().getString(R.string.conf_tr_title));
            builder.setContentText(context.getResources().getString(R.string.conf_tr_sent2));
            builder.setSmallIcon(R.drawable.ic_mail_outline_white_24dp);
            builder.setAutoCancel(true);


            SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(context);
            boolean trNotificationsEnabled = SP.getBoolean("tr_enable_notifications", true);

            if(trNotificationsEnabled) {
                boolean vibrate = SP.getBoolean("tr_vibrate", false);
                if(vibrate) builder.setVibrate(VIBRATE_PATTERN);

                String ledColourIndex = SP.getString("tr_colour", "");
                int colour = 0;
                if(!ledColourIndex.isEmpty()) {
                    int index = -1;
                    try{
                        index = Integer.parseInt(ledColourIndex);
                    }catch(NumberFormatException e){
                        Log.d("Mine","The led colour setting isn't a parsable number");
                    }
                    if(index > -1) {
                        colour = ContextCompat.getColor(context, colours.getResourceId(index, 0));
                    }
                }

                int ledColour = (colour > 0) ? colour : Color.CYAN;
                builder.setLights(ledColour, 500, 1500);

                String ringtone = SP.getString("tr_ringtone", "");
                if(!ringtone.isEmpty()) {
                    builder.setSound(Uri.parse(ringtone));
                }
            }

            Notification.InboxStyle inboxStyle = new Notification.InboxStyle();
            inboxStyle.setBigContentTitle(context.getResources().getString(R.string.conf_tr_sent2));
            inboxStyle.addLine(tr.getRecipientNumber());
            inboxStyle.addLine(tr.getTitle());
            inboxStyle.addLine(tr.getMessage());
            builder.setStyle(inboxStyle);

            db.sendTextReminder(tr.getId());
            Intent i = new Intent(context, DisplayTextRemindersActivity.class);
            intent.putExtra(CONF_SENT, true);

            PendingIntent pi = PendingIntent.getActivity(context, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);
            builder.setContentIntent(pi);

            db.close();
            manager.notify(NOTIFICATION_ID, builder.build());

        } else if(type == NR_TYPE) {

            NotificationReminder nr = db.getNotificationReminder(id);
            builder.setContentTitle(nr.getTitle());
            builder.setContentText(nr.getMessage());
            builder.setSmallIcon(R.drawable.ic_notifications_white_24dp);

            SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(context);
            boolean nrNotificationsEnabled = SP.getBoolean("nr_enable_notifications", true);

            if(nrNotificationsEnabled) {
                boolean vibrate = SP.getBoolean("nr_vibrate", false);
                if(vibrate) builder.setVibrate(VIBRATE_PATTERN);

                int colour = 0;
                if(nr.getColour() != null) {
                    int res = colours.getResourceId(nr.getColour(), 0);
                    colour = ContextCompat.getColor(context, res);
                }

                int ledColour = (colour != 0) ? colour : Color.CYAN;
                builder.setLights(ledColour, 500, 500);

                String ringtone = SP.getString("nr_ringtone", "");
                if(!ringtone.isEmpty()) {
                    builder.setSound(Uri.parse(ringtone));
                }
            }

            Notification.InboxStyle inboxStyle = new Notification.InboxStyle();
            inboxStyle.setBigContentTitle(nr.getTitle());
            inboxStyle.addLine(nr.getMessage());
            builder.setStyle(inboxStyle);

            db.sendNotificationReminder(nr.getId());
            Intent i = new Intent(context, DisplayNotificationRemindersActivity.class);
            intent.putExtra(CONF_SENT, true);

            PendingIntent pi = PendingIntent.getActivity(context, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);
            builder.setContentIntent(pi);

            db.close();
            manager.notify(NOTIFICATION_ID, builder.build());
        }


    }

}
