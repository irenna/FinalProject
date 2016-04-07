package com.comp3617.finalproject.meggsage;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.PorterDuff;
import android.support.v4.content.ContextCompat;
import android.telephony.PhoneNumberUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by meggz on 4/3/16.
 */
public class TextReminderListAdapter extends ArrayAdapter {

    private Context context;
    private List<TextReminder> tms;

    private static String[] textStatuses;
    private static String[] reminderStatuses;
    private static TypedArray textStatusIcons;


    public TextReminderListAdapter(Context context, List<TextReminder> data) {
        super(context, R.layout.text_reminder_item, data);
        tms = data;
        this.context = context;

        textStatuses = context.getResources().getStringArray(R.array.text_statuses);
        reminderStatuses = context.getResources().getStringArray(R.array.reminder_statuses);
        textStatusIcons = context.getResources().obtainTypedArray(R.array.reminder_status_icons);
        
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        TextReminder tm = tms.get(position);

        View v;
        LayoutInflater lf = LayoutInflater.from(context);

        if(convertView != null) {
            v = convertView; // reuse view
        } else {
            v = lf.inflate(R.layout.text_reminder_item, parent, false);  //new view
        }

        TextView txtTitle = (TextView) v.findViewById(R.id.txtTitle);
        txtTitle.setText(tm.getTitle());


        if(tm.getActive() == 0) {
            TextView txtActive = (TextView) v.findViewById(R.id.txtActive);
            txtActive.setText(reminderStatuses[tm.getActive()]);
        }

        TextView txtRecipient = (TextView) v.findViewById(R.id.txtRecipient);
        String recNum = PhoneNumberUtils.formatNumber(tm.getRecipientNumber());
        if(tm.getRecipientName() != null) {
            txtRecipient.setText(tm.getRecipientName() + " " + recNum);
        } else {
            txtRecipient.setText(recNum);
        }

        TextView txtMsg = (TextView) v.findViewById(R.id.txtMessage);
        txtMsg.setText(tm.getMessage());

        TextView txtDate = (TextView) v.findViewById(R.id.txtDueDate);
        txtDate.setText(tm.getDueDateString());

        TextView txtStatus = (TextView) v.findViewById(R.id.txtTextStatus);
        ImageView imgTextStatus = (ImageView) v.findViewById(R.id.imgTextStatus);
        int status = tm.getSentStatus();
        txtStatus.setText(textStatuses[status]);
        imgTextStatus.setImageResource(textStatusIcons.getResourceId(status, 0));
        if(status == 1) {
            txtStatus.setTextColor(ContextCompat.getColor(context, R.color.tealgreen));
            imgTextStatus.getDrawable().setColorFilter(ContextCompat.getColor(context, R.color.tealgreen), PorterDuff.Mode.SRC_ATOP);
        } else if(status == 2) {
            txtStatus.setTextColor(ContextCompat.getColor(context, R.color.redpink));
            imgTextStatus.getDrawable().setColorFilter(ContextCompat.getColor(context, R.color.redpink), PorterDuff.Mode.SRC_ATOP);
        } else if(status == 0) {
            txtStatus.setTextColor(ContextCompat.getColor(context, R.color.grey));
            imgTextStatus.getDrawable().setColorFilter(ContextCompat.getColor(context, R.color.grey), PorterDuff.Mode.SRC_ATOP);
        }

        return v;
    }
}
