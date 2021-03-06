package com.comp3617.finalproject.meggsage;

import android.app.DialogFragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class NotificationReminderActivity extends BaseActivity implements View.OnClickListener {



    private static final int NR_TYPE = 1; //Used indicate textreminder
    private static final int ACTIVE = 1;
    private static final int REQ_CODE_CONTACT= 99;
    private RemindersDBHelper db;
    private long id;
    private long originalDueDate;


    private EditText editDate;
    private EditText editTime;
    private EditText editTitle;
    private Spinner spStatuses;
    private Spinner spImportance;
    private Spinner spColour;
    private Spinner spRecurrence;
    private EditText editMessage;


    private Context context;

    private static SimpleDateFormat dateFormatter = new SimpleDateFormat("EEE. MMM d, yyyy");
    private static SimpleDateFormat timeFormatter = new SimpleDateFormat("h:mm a");
    private static SimpleDateFormat dateTimeFormatter = new SimpleDateFormat("EEE. MMM d, yyyy h:mm a");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_reminder);

        context = this;

        id = getIntent().getLongExtra("id", 0);

        editDate = (EditText) findViewById(R.id.editDate);
        editTime = (EditText) findViewById(R.id.editTime);
        editTitle = (EditText) findViewById(R.id.editTitle);
        spStatuses = (Spinner) findViewById(R.id.spStatuses);
        spImportance = (Spinner) findViewById(R.id.spImportance);
        spColour = (Spinner) findViewById(R.id.spColour);
        spRecurrence = (Spinner) findViewById(R.id.spRecurrence);
        editMessage = (EditText) findViewById(R.id.editMessage);


        if(id > 0) {
            db = RemindersDBHelper.getInstance(getApplicationContext());
            NotificationReminder tr = db.getNotificationReminder(id);
            db.close();
            if(tr == null) {
                Toast.makeText(this, getResources().getString(R.string.err_reminder_not_found), Toast.LENGTH_LONG).show();
                goToDisplayNotificationReminder(); //goes to DisplayNotificationsActivity
            }

            originalDueDate = tr.getDueDate();
            Date d = new Date(originalDueDate);
            editDate.setText(dateFormatter.format(d));
            editTime.setText(timeFormatter.format(d));
            editTitle.setText(tr.getTitle());
            spStatuses.setSelection(tr.getActive());
            spImportance.setSelection(tr.getImportance());
            spRecurrence.setSelection(tr.getRecurrence());
            if(tr.getColour() != null) spColour.setSelection(tr.getColour());
            editMessage.setText(tr.getMessage());

        }

        if(id == 0) {
            spStatuses.setSelection(ACTIVE);
            SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(this);
            String ledColourIndex = SP.getString("nr_colour", "");
            int index = 0;
            if(!ledColourIndex.isEmpty()) {
                try{
                    index = Integer.parseInt(ledColourIndex);
                }catch(NumberFormatException e){
                    Log.d("Mine", "The led colour setting isn't a parsable number");
                }
            }
            spColour.setSelection(index);

        }

        editDate.setOnClickListener(this);
        editTime.setOnClickListener(this);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.clear();
        getMenuInflater().inflate(R.menu.action_bar, menu);

        if(id == 0) menu.removeItem(R.id.action_delete);
        menu.removeItem(R.id.action_new);
        menu.removeItem(R.id.action_tm);
        menu.removeItem(R.id.action_fav);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.action_save:
                selectSave();
                break;
            case R.id.action_cancel:
                selectCancel(DisplayNotificationRemindersActivity.class);
                break;
            case R.id.action_delete:
                if(id > 0) {
                    selectDelete(id, NR_TYPE);
                }
                break;
            case R.id.action_fav:
                selectFavourites();
                break;
            case R.id.action_tm:
                goToDisplayTextReminder();
                break;
            case R.id.action_settings:
                selectSettings();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }


    @Override
    protected void selectSave() {

        String inputTitle = editTitle.getText().toString();
        String inputMsg = editMessage.getText().toString();
        String inputDate = editDate.getText().toString();
        String inputTime = editTime.getText().toString();

        String[] inputs = {inputTitle,inputDate,inputTime,inputMsg};
        int[] errMsgIds = {R.string.err_title_blank,R.string.err_due_date_blank,
                R.string.err_due_time_blank,R.string.err_message_blank};
        //Yay a loop to check them all!
        for(int i = 0; i < inputs.length; i++) {
            if(inputs[i].isEmpty()) {
                Toast.makeText(context, getResources().getString(errMsgIds[i]), Toast.LENGTH_LONG).show();
                return;
            }
        }

        Date dueDate;

        try {
            dueDate = dateTimeFormatter.parse(inputDate + " " + inputTime);
            Date today = new Date();
            if (dueDate.before(today)) {
                Toast.makeText(context, getResources().getString(R.string.err_due_date), Toast.LENGTH_LONG).show();
                return;
            }

            NotificationReminder tr = new NotificationReminder();
            tr.setActive(spStatuses.getSelectedItemPosition());
            tr.setTitle(inputTitle);
            tr.setDueDate(dueDate.getTime());
            tr.setMessage(inputMsg);
            tr.setColour(spColour.getSelectedItemPosition());
            tr.setImportance(spImportance.getSelectedItemPosition());
            tr.setRecurrence(spRecurrence.getSelectedItemPosition());

            db = RemindersDBHelper.getInstance(getApplicationContext());
            if(id > 0) {
                Alarm.cancelAlarm(this.getApplicationContext(), NR_TYPE, id);
                tr.setId(id);
                int rows = db.updateNotificationReminder(tr);
                if(rows > 0)Toast.makeText(context, getResources().getString(R.string.conf_update), Toast.LENGTH_LONG).show();
            } else {
                long newId = db.createNotificationReminder(tr);
                tr.setId(newId);
                if(newId > 0) Toast.makeText(context, getResources().getString(R.string.conf_create), Toast.LENGTH_LONG).show();
            }
            if(tr.getActive() == ACTIVE) Alarm.setNotificationAlarm(this.getApplicationContext(), NR_TYPE, tr.getId(), tr.getDueDate(), tr.getRecurrence());
            goToDisplayNotificationReminder();

        } catch (ParseException e) {
            e.printStackTrace();
        }

    }


    @Override
    public void onClick(View view) {
        if(view == editDate) {
            String inputDate = editDate.getText().toString();
            DialogFragment newFragment = DatePickerFragment.newInstance(inputDate, view.getId());
            newFragment.show(getFragmentManager(), "datePicker");
        } else if(view == editTime) {
            String inputTime = editTime.getText().toString();
            DialogFragment newFragment = TimePickerFragment.newInstance(inputTime, view.getId());
            newFragment.show(getFragmentManager(), "timePicker");
        }
    }
}
