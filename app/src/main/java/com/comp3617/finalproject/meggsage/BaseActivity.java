package com.comp3617.finalproject.meggsage;

import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
    }



    //Overridden by separate classes.  Here so method name is the same for all.
    protected void selectSave(){

    }

    //These are the same across all activities.
    protected void selectNew(Class newActivity) {
        Intent i = new Intent(this, newActivity);
        startActivity(i);
    }

    protected void selectDelete(long id, int type) {
        DialogFragment frag = DeleteConfirmationFragment.newInstance(id, type);
        frag.show(getFragmentManager(), "delete");
    }

    protected void selectCancel(Class prevActivity) {
        Intent i = new Intent(this, prevActivity);
        startActivity(i);
    }

    //Used through out code to jump back to DisplayTextRemindersActivity
    protected void goToDisplayTextReminder() {
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
