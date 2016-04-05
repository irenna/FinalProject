package com.comp3617.finalproject.meggsage;

import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.telephony.PhoneNumberUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TextReminderActivity extends BaseActivity implements View.OnClickListener {

    private static final int TM_TYPE = 0; //Used indicate textreminder
    private static final int ACTIVE = 1;
    private static final int REQ_CODE_CONTACT= 99;
    private RemindersDBHelper db;
    private long id;
    private long originalDueDate;

    private EditText editTo;
    private EditText editDate;
    private EditText editTime;
    private EditText editTitle;
    private Spinner spStatuses;
    private EditText editMessage;
    private TextView txtToName;

    private String recipientNumber;
    private String recipientName;

    private Context context;

    private static SimpleDateFormat dateFormatter = new SimpleDateFormat("EEE. MMM d, yyyy");
    private static SimpleDateFormat timeFormatter = new SimpleDateFormat("h:mm a");
    private static SimpleDateFormat dateTimeFormatter = new SimpleDateFormat("EEE. MMM d, yyyy h:mm a");



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_reminder);

        context = this;

        id = getIntent().getLongExtra("id", 0);

        editTo = (EditText) findViewById(R.id.editTo);
        editTo.addTextChangedListener(new PhoneNumberFormattingTextWatcher());

        editDate = (EditText) findViewById(R.id.editDate);
        editTime = (EditText) findViewById(R.id.editTime);
        editTitle = (EditText) findViewById(R.id.editTitle);
        spStatuses = (Spinner) findViewById(R.id.spStatuses);
        editMessage = (EditText) findViewById(R.id.editMessage);
        txtToName = (TextView) findViewById(R.id.txtToName);

        if(id > 0) {
            db = RemindersDBHelper.getInstance(getApplicationContext());
            TextReminder tr = db.getTextReminder(id);
            db.close();
            if(tr == null) {
                Toast.makeText(this, getResources().getString(R.string.err_reminder_not_found), Toast.LENGTH_LONG).show();
                goToDisplayTextReminder(); //goes to DisplayTextRemindersActivity
            }

            editTo.setText(tr.getRecipientNumber());
            originalDueDate = tr.getDueDate();
            Date d = new Date(originalDueDate);
            editDate.setText(dateFormatter.format(d));
            editTime.setText(timeFormatter.format(d));
            editTitle.setText(tr.getTitle());
            spStatuses.setSelection(tr.getActive());
            editMessage.setText(tr.getMessage());
            if(tr.getRecipientName() != null) txtToName.setText(tr.getRecipientName());
        }

        if(id == 0) spStatuses.setSelection(ACTIVE);

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

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.action_save:
                selectSave();
                break;
            case R.id.action_cancel:
                selectCancel(DisplayTextRemindersActivity.class);
                break;
            case R.id.action_delete:
                if(id > 0) {
                    selectDelete(id, TM_TYPE);
                }
                break;
            case R.id.action_fav:
                selectFavourites();
                break;
            case R.id.action_nm:
                selectNotificationReminder();
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

        String inputTo = editTo.getText().toString();
        String inputTitle = editTitle.getText().toString();
        String inputMsg = editMessage.getText().toString();
        String inputDate = editDate.getText().toString();
        String inputTime = editTime.getText().toString();

        String[] inputs = {inputTo,inputTitle,inputDate,inputTime,inputMsg};
        int[] errMsgIds = {R.string.err_phone_number_blank,R.string.err_title_blank,R.string.err_due_date_blank,
                R.string.err_due_time_blank,R.string.err_message_blank};
        //Yay a loop to check them all!
        for(int i = 0; i < inputs.length; i++) {
            if(inputs[i].isEmpty()) {
                Toast.makeText(context, getResources().getString(errMsgIds[i]), Toast.LENGTH_LONG).show();
                return;
            }
        }
        if(!PhoneNumberUtils.isGlobalPhoneNumber(inputTo)) {
            Toast.makeText(context, getResources().getString(R.string.err_phone_number_invalid), Toast.LENGTH_LONG).show();
            return;
        }

        Date dueDate;

        try {
            dueDate = dateTimeFormatter.parse(inputDate + " " + inputTime);
            Date today = new Date();
            if(dueDate.before(today)) {
                Toast.makeText(context, getResources().getString(R.string.err_due_date), Toast.LENGTH_LONG).show();
                return;
            }

            TextReminder tr = new TextReminder();
            tr.setRecipientNumber(inputTo);
            tr.setActive(spStatuses.getSelectedItemPosition());
            tr.setTitle(inputTitle);
            tr.setDueDate(dueDate.getTime());
            tr.setMessage(inputMsg);

            db = RemindersDBHelper.getInstance(getApplicationContext());
            if(id > 0) {
                Alarm.cancelAlarm(this.getApplicationContext(), TM_TYPE, id);
                tr.setId(id);
                int rows = db.updateTextReminder(tr);
                if(rows > 0)Toast.makeText(context, getResources().getString(R.string.conf_update), Toast.LENGTH_LONG).show();
            } else {
                long newId = db.createTextReminder(tr);
                tr.setId(newId);
                if(newId > 0) Toast.makeText(context, getResources().getString(R.string.conf_create), Toast.LENGTH_LONG).show();
            }
            Alarm.setAlarm(this.getApplicationContext(), TM_TYPE, tr.getId(), tr.getDueDate());
            goToDisplayTextReminder();

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
        } else if(view.getId() == R.id.imgContact) {
            Intent i = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
            i.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
            startActivityForResult(i, REQ_CODE_CONTACT);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == REQ_CODE_CONTACT) {
            if (resultCode == RESULT_OK) {
                Uri contactUri = data.getData();
                Cursor cursor = getContentResolver().query(contactUri, null, null, null, null);
                if(cursor.moveToFirst()) {
                    recipientNumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    recipientName = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                    editTo.setText(recipientNumber);
                    txtToName.setText(recipientName);
                }
                cursor.close();
            }
        }
    }




}
