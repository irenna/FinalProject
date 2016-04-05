package com.comp3617.finalproject.meggsage;


import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.format.DateFormat;
import android.widget.EditText;
import android.widget.TimePicker;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 */
public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {


    private static SimpleDateFormat timeFormatter = new SimpleDateFormat("h:mm a");
    private static int viewId; //Id of the view to be updated.

    public static TimePickerFragment newInstance(String inputDate, int viewId)  {

        TimePickerFragment frag = new TimePickerFragment();
        TimePickerFragment.viewId = viewId;

        Date dueDate = new Date();
        if(inputDate.length() > 0) {
            try {
                dueDate = timeFormatter.parse(inputDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        Calendar currDueDate = Calendar.getInstance();
        currDueDate.setTime(dueDate);
        Bundle args = new Bundle();
        args.putInt("hour", currDueDate.get(Calendar.HOUR_OF_DAY));
        args.putInt("min", currDueDate.get(Calendar.MINUTE));
        frag.setArguments(args);
        return frag;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Bundle bun = getArguments();
        final Calendar cal = Calendar.getInstance();
        int hour = bun.getInt("hour", cal.get(Calendar.HOUR_OF_DAY));
        int minute = bun.getInt("min", cal.get(Calendar.MINUTE));

        return new TimePickerDialog(getActivity(), this, hour, minute, DateFormat.is24HourFormat(getActivity()));

    }
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        Calendar newDate = Calendar.getInstance();
        newDate.set(Calendar.HOUR_OF_DAY, hourOfDay);
        newDate.set(Calendar.MINUTE, minute);

        EditText editDate = (EditText) getActivity().findViewById(viewId);
        editDate.setText(timeFormatter.format(newDate.getTime()));

    }


}
