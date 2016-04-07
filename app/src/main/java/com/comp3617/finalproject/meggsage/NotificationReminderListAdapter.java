package com.comp3617.finalproject.meggsage;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.PorterDuff;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by meggz on 4/6/16.
 */
public class NotificationReminderListAdapter extends ArrayAdapter {

    private Context context;
    private List<NotificationReminder> tms;

    private static String[] importanceStatuses;
    private static String[] reminderStatuses;
    private static TypedArray statusIcons;
    private static TypedArray colours;
    private static TypedArray importanceIcons;
    private static String[] timeIntervals;

    private static final int HIGH_PRIORITY = 1;
    private static final int ACTIVE = 1;


    public NotificationReminderListAdapter(Context context, List<NotificationReminder> data) {
        super(context, R.layout.notification_reminder_item, data);
        tms = data;
        this.context = context;

        importanceStatuses = context.getResources().getStringArray(R.array.importance);
        reminderStatuses = context.getResources().getStringArray(R.array.reminder_statuses);
        timeIntervals = context.getResources().getStringArray(R.array.time_intervals);

        statusIcons = context.getResources().obtainTypedArray(R.array.notif_status_icons);
        colours = context.getResources().obtainTypedArray(R.array.colour_ids);
        importanceIcons = context.getResources().obtainTypedArray(R.array.importance_status_icons);

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        NotificationReminder tm = tms.get(position);

        View v;
        LayoutInflater lf = LayoutInflater.from(context);

        if(convertView != null) {
            v = convertView; // reuse view
        } else {
            v = lf.inflate(R.layout.notification_reminder_item, parent, false);  //new view
        }

        TextView txtTitle = (TextView) v.findViewById(R.id.txtTitle);
        txtTitle.setText(tm.getTitle());
        TextView txtActive = (TextView) v.findViewById(R.id.txtImportance);
        txtActive.setText(reminderStatuses[tm.getActive()]);
        ImageView imgActive = (ImageView) v.findViewById(R.id.imgActive);
        imgActive.setImageResource(statusIcons.getResourceId(tm.getActive(), 0));
        if(tm.getColour() != null) {
            imgActive.getDrawable().setColorFilter(ContextCompat.getColor(context, colours.getResourceId(tm.getColour(), 0)), PorterDuff.Mode.SRC_ATOP);
        }

        TextView txtImportance = (TextView) v.findViewById(R.id.txtImportance);
        ImageView imgImportance = (ImageView) v.findViewById(R.id.imgImportance);
        if(tm.getImportance() == HIGH_PRIORITY) {
            txtImportance.setText(importanceStatuses[HIGH_PRIORITY]);
            imgImportance.setImageResource(importanceIcons.getResourceId(HIGH_PRIORITY, 0));
            imgImportance.getDrawable().setColorFilter(ContextCompat.getColor(context, R.color.redpink), PorterDuff.Mode.SRC_ATOP);
        }

        TextView txtMsg = (TextView) v.findViewById(R.id.txtMessage);
        txtMsg.setText(tm.getMessage());

        TextView txtDate = (TextView) v.findViewById(R.id.txtDueDate);
        txtDate.setText(tm.getDueDateString());

        if(tm.getRecurrence() > 0) {
            TextView txtRecurrence = (TextView) v.findViewById(R.id.txtRecurrence);
            txtRecurrence.setText(timeIntervals[tm.getRecurrence()]);
        }
        return v;
    }
}
