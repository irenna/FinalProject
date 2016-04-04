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

public class DisplayTextRemindersActivity extends BaseActivity {

    private static final int TYPE = 1; //Used indicate textreminder

    private RemindersDBHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_text_reminders);

        db = RemindersDBHelper.getInstance(getApplicationContext());
        ArrayList<TextReminder> tmList = db.getAllTextReminders();

        ListView listViewTextReminders = (ListView) findViewById(R.id.listViewTextReminders);
        TextReminderListAdapter adapter = new TextReminderListAdapter(this, tmList);
        listViewTextReminders.setAdapter(adapter);
        listViewTextReminders.setEmptyView(findViewById(R.id.txtEmpty));

        listViewTextReminders.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(DisplayTextRemindersActivity.this, TextReminderActivity.class);
                i.putExtra("id", id);
                startActivity(i);
            }
        });

        listViewTextReminders.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                DialogFragment frag = DeleteConfirmationFragment.newInstance(id, 1);
                frag.show(getFragmentManager(), "delete");
                return true;
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_bar, menu);

        menu.removeItem(R.id.action_cancel);
        menu.removeItem(R.id.action_delete);
        menu.removeItem(R.id.action_done);
        menu.removeItem(R.id.action_tm);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.action_new:
                newTextReminder();
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

    protected void newTextReminder() {
        Intent i = new Intent(this, TextReminderActivity.class);
        startActivity(i);
    }

}
