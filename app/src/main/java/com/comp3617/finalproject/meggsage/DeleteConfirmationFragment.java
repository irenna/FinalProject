package com.comp3617.finalproject.meggsage;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.widget.Toast;

/**
 * A simple {@link Fragment} subclass.
 */
public class DeleteConfirmationFragment extends DialogFragment {

    private static final int TM_TYPE = 0;
    private static final int NR_TYPE = 1;
    private static final int FAV_TYPE = 2;



    public DeleteConfirmationFragment() {
        // Required empty public constructor
    }

    public static DeleteConfirmationFragment newInstance(long id, int type) {
        DeleteConfirmationFragment frag = new DeleteConfirmationFragment();
        Bundle args = new Bundle();
        args.putLong("id", id);
        args.putInt("type", type);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final long id = getArguments().getLong("id");
        final int type = getArguments().getInt("type");

        AlertDialog.Builder conf = new AlertDialog.Builder(getActivity());
        conf.setTitle(R.string.conf_del_title);
        conf.setMessage(R.string.conf_del_text);
        conf.setPositiveButton(R.string.conf_del_yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                RemindersDBHelper db = RemindersDBHelper.getInstance(getActivity());
                Class action = null;
                boolean success = false;

                if (type == TM_TYPE) {
                    success = db.deleteTextReminder(id);
                    action = DisplayTextRemindersActivity.class;
                    Alarm.cancelAlarm(getActivity(), type, id);
                } else if (type == NR_TYPE) {
                    success = db.deleteNotificationReminder(id);
                    action = DisplayTextRemindersActivity.class;
                    Alarm.cancelAlarm(getActivity(), type, id);
                }
                db.close();
                if (action != null && success) {
                    Toast.makeText(getActivity(), getResources().getString(R.string.conf_del_success), Toast.LENGTH_LONG).show();
                    Intent i = new Intent(getActivity(), action);
                    startActivity(i);
                } else {
                    dialog.dismiss(); //shouldn't happen, but lets not get stuck...
                }


            }
        });
        conf.setNegativeButton(R.string.conf_del_no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        return conf.create();


    }

}
