package com.comp3617.finalproject.meggsage;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_bar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    //These are the same across all activities.
    protected void selectTextReminder() {
        Intent i = new Intent(this, DisplayTextRemindersActivity.class);
        startActivity(i);
    }

    protected void selectNotificationReminder() {
        //do stuff
    }

    protected void selectSettings() {
        //do stuff
    }

    protected void selectFavourites() {
        //do more stuff
    }

}
