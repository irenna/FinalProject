package com.comp3617.finalproject.meggsage;

import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;

public class DisplayTextRemindersActivity extends BaseActivity {

    private static final int TM_TYPE = 0;
    private static final String ALARMS_FAILED = "alarms_failed";
    private RemindersDBHelper db;
    private TextReminderListAdapter adapter;
    private static final String CONF_SENT = "sent";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_text_reminders);
        
        if(getIntent() != null) {
            if(getIntent().getBooleanExtra(CONF_SENT, false)) {
                Toast.makeText(this, getResources().getString(R.string.conf_tr_sent2), Toast.LENGTH_LONG).show();
            }
        }

        db = RemindersDBHelper.getInstance(getApplicationContext());
        
        //Check if any reminders should have sent but haven't (ex: device was off)
        Date now = new Date();
        ArrayList<TextReminder> tmCheckList = db.getActiveTextReminders();
        boolean alarmsFailed = false;
        for (TextReminder t: tmCheckList) {
            if(t.getDueDate() < now.getTime()) {
                db.failTextReminder(t.getId());
                alarmsFailed = true;
            }
        }

        if(getIntent() != null) {
            if(getIntent().getBooleanExtra(ALARMS_FAILED, false)) alarmsFailed = true;
        }


        if(alarmsFailed) Toast.makeText(this, getResources().getString(R.string.msg_tr_failed), Toast.LENGTH_LONG).show();
        
        ArrayList<TextReminder> tmList = db.getAllTextReminders();
        ListView listViewTextReminders = (ListView) findViewById(R.id.listViewTextReminders);

        if(tmList.size() > 0) {

            adapter = new TextReminderListAdapter(this, tmList);
            listViewTextReminders.setAdapter(adapter);

            listViewTextReminders.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent i = new Intent(DisplayTextRemindersActivity.this, TextReminderActivity.class);
                    Long itemId = ((TextReminder)adapter.getItem(position)).getId();
                    i.putExtra("id", itemId);
                    startActivity(i);
                }
            });

            listViewTextReminders.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                    Long itemId = ((TextReminder)adapter.getItem(position)).getId();
                    DialogFragment frag = DeleteConfirmationFragment.newInstance(itemId, TM_TYPE);
                    frag.show(getFragmentManager(), "delete");
                    return true;
                }
            });
        }


        listViewTextReminders.setEmptyView(findViewById(R.id.txtEmpty));


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_bar, menu);

        menu.removeItem(R.id.action_cancel);
        menu.removeItem(R.id.action_delete);
        menu.removeItem(R.id.action_save);
        menu.removeItem(R.id.action_tm);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.action_new:
                selectNew(TextReminderActivity.class);
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

}
