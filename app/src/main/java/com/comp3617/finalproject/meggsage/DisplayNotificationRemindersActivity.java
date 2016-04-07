package com.comp3617.finalproject.meggsage;

import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

public class DisplayNotificationRemindersActivity extends BaseActivity {
    

    private static final int NR_TYPE = 1;
    private static final String ALARMS_FAILED = "alarms_failed";
    private RemindersDBHelper db;
    private NotificationReminderListAdapter adapter;
    private static final String CONF_SENT = "sent";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_notification_reminders);

        db = RemindersDBHelper.getInstance(getApplicationContext());

        ArrayList<NotificationReminder> tmList = db.getAllNotificationReminders();
        ListView listViewNotificationReminders = (ListView) findViewById(R.id.listViewNotificationReminders);

        if(tmList.size() > 0) {

            adapter = new NotificationReminderListAdapter(this, tmList);
            listViewNotificationReminders.setAdapter(adapter);

            listViewNotificationReminders.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent i = new Intent(DisplayNotificationRemindersActivity.this, NotificationReminderActivity.class);
                    Long itemId = ((NotificationReminder)adapter.getItem(position)).getId();
                    i.putExtra("id", itemId);
                    startActivity(i);
                }
            });

            listViewNotificationReminders.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                    Long itemId = ((NotificationReminder)adapter.getItem(position)).getId();
                    DialogFragment frag = DeleteConfirmationFragment.newInstance(itemId, NR_TYPE);
                    frag.show(getFragmentManager(), "delete");
                    return true;
                }
            });
        }


        listViewNotificationReminders.setEmptyView(findViewById(R.id.txtEmpty));


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_bar, menu);

        menu.removeItem(R.id.action_cancel);
        menu.removeItem(R.id.action_delete);
        menu.removeItem(R.id.action_save);
        menu.removeItem(R.id.action_nm);
        menu.removeItem(R.id.action_fav);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.action_new:
                selectNew(NotificationReminderActivity.class);
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
}
