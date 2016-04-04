package com.comp3617.finalproject.meggsage;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.widget.DatePicker;
import android.widget.EditText;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 */
public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    private static SimpleDateFormat dateFormatter = new SimpleDateFormat("EEE. MMM d, yyyy");
    private static int viewId; //Id of the view to be updated.

    public static DatePickerFragment newInstance(String inputDate, int viewId)  {

        DatePickerFragment frag = new DatePickerFragment();
        DatePickerFragment.viewId = viewId;

        Date dueDate = new Date();
        if(inputDate.length() > 0) {
            try {
                dueDate = dateFormatter.parse(inputDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        Calendar currDueDate = Calendar.getInstance();
        currDueDate.setTime(dueDate);
        Bundle args = new Bundle();
        args.putInt("year", currDueDate.get(Calendar.YEAR));
        args.putInt("month", currDueDate.get(Calendar.MONTH));
        args.putInt("day", currDueDate.get(Calendar.DAY_OF_MONTH));
        frag.setArguments(args);
        return frag;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {


        Bundle bun = getArguments();
        final Calendar cal = Calendar.getInstance();
        int year = bun.getInt("year", cal.get(Calendar.YEAR));
        int month = bun.getInt("month", cal.get(Calendar.MONTH));
        int day = bun.getInt("day", cal.get(Calendar.DAY_OF_MONTH));

        return new DatePickerDialog(getActivity(), this, year, month, day);

    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        Calendar newDate = Calendar.getInstance();
        newDate.set(year, monthOfYear, dayOfMonth);

        EditText editDate = (EditText) getActivity().findViewById(viewId);
        editDate.setText(dateFormatter.format(newDate.getTime()));

    }

}
